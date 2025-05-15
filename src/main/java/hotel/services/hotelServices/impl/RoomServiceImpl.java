package hotel.services.hotelServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotel.config.ConfigValue;
import hotel.dto.room.request.RoomRequest;
import hotel.entities.Price;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.enums.RoomStatus;
import hotel.enums.RoomType;
import hotel.exceptions.EntityAlreadyExistsException;
import hotel.exceptions.NotFoundException;
import hotel.exceptions.RoomCapacityOverflowException;
import hotel.mappers.RoomMapper;
import hotel.repository.interfaces.RoomRepository;
import hotel.services.hotelServices.PriceService;
import hotel.services.hotelServices.RoomService;
import hotel.sort.RoomSortParams;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final PriceService priceService;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final Map<RoomType, BigDecimal> roomPrices;

    @ConfigValue
    private Integer maxAmountOfRooms;
    @ConfigValue
    private Boolean roomStatusChangeEnabled;
    @ConfigValue
    private Integer reservationHistoryLimit;

    @PostConstruct
    public void init() {
        for (Price price : priceService.getAll()) {
            roomPrices.put(price.getRoomType(), price.getRoomTypePrice());
        }
    }

    @Override
    @Transactional
    public void addAll(Map<Long, Room> rooms) {
        roomRepository.saveAll(rooms.values());
    }

    @Override
    @Transactional
    public List<Room> getAll(RoomSortParams params) {
        return roomRepository.findAllSorted(params);
    }

    @Override
    @Transactional
    public List<Room> getAllSimple() {
        return roomRepository.findAllSimpleInfo();
    }


    @Override
    public BigDecimal getRoomTypePrice(RoomType roomType) {
        return roomPrices.get(roomType);
    }

    @Override
    public void setNewRoomTypePrice(RoomType roomType, BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("%s\t-\t\t-\tPrice couldn't be lower than 0!", getClass().getName()));
        }
        roomPrices.replace(roomType, price);
    }

    @Override
    @Transactional
    public Room createRoom(Room room) {
        if (isRoomOverflow()) {
            throw new RoomCapacityOverflowException(String.format("%s\t-\tThe maximum amount of rooms has been reached - %d", getClass().getName(), maxAmountOfRooms));
        }
        if (room.getRoomNumber() <= 0) {
            throw new IllegalArgumentException(String.format("%s\t-\tRoom number must be greater than 0", getClass().getName()));
        }
        if (roomRepository.getRoomByNumber(room.getRoomNumber()).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("%s\t-\tRoom №%d is already exists!", getClass().getName(), room.getRoomNumber()));
        }
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public void update(Room room) {
        roomRepository.update(room);
    }

    @Override
    @Transactional
    public Room update(Long id, RoomRequest request) {
        Room existingRoom = getRoomById(id);
        roomMapper.updateEntity(existingRoom, request);
        return roomRepository.update(existingRoom);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("%s\t-\tReservation with id - %d doesn't exist", getClass().getName(), id)));;
        room.setIsActive(false);
        roomRepository.update(room);
    }

    @Override
    @Transactional
    public void addReservation(Room room, Reservation reservation) {
        if (room.getReservationList().size() >= reservationHistoryLimit) {
            List<Reservation> roomReservations = room.getReservationList();
            roomReservations.removeFirst();
            room.setReservationList(roomReservations);
        }
        room.addReservation(reservation);
        roomRepository.update(room);
    }


    @Override
    @Transactional
    public void changeRoomStatus(Long roomNumber, RoomStatus status) {
        if (!roomStatusChangeEnabled) {
            throw new IllegalStateException(String.format("%s\t-\tConfiguration - Disabled to change room status!", getClass().getName()));
        }
        Room room = getRoomByNumber(roomNumber);
        room.setRoomStatus(status);
        roomRepository.update(room);
    }

    @Override
    public List<Room> getSortedListOfOpenRooms() {
        List<Room> rooms = getListOfOpenRooms();
        Comparator<Room> roomComparator = Comparator
                .comparing((Room room) -> this.getRoomTypePrice(room.getRoomType()))
                .thenComparing(Room::getRoomCapacity)
                .thenComparing(Room::getRoomType);
        rooms.sort(roomComparator);
        return rooms;
    }

    @Override
    public List<Room> getListOfOpenRooms() {
        List<Room> rooms = getAllSimple();
        return rooms.stream()
                .filter(room -> room.getRoomStatus() == RoomStatus.OPEN)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> getListOfOccupiedRooms() {
        List<Room> rooms = getAllSimple();
        return rooms.stream()
                .filter(room -> room.getRoomStatus() == RoomStatus.OCCUPIED)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getAmountOfOpenRooms() {
        return getListOfOpenRooms().size();
    }

    @Override
    @Transactional
    public List<Room> getAvailableRoomsOnDate(LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAllAvailableRoomsOnDate(checkIn, checkOut);
    }

    @Override
    public List<Reservation> getThreeLastReservations(Long roomNumber) {
        Room room = getRoomByNumber(roomNumber);
        List<Reservation> allReservations = room.getReservationList();
        if (allReservations.size() < 3) {
            return allReservations;
        }
        return allReservations.subList(allReservations.size() - 3, allReservations.size());
    }

    @Override
    public Reservation getCurrentReservation(Room room) {
        LocalDate currentDate = LocalDate.now();
	    return room.getReservationList().stream()
	            .filter(v -> v.getCheckIn().isBefore(currentDate) && v.getCheckOut().isAfter(currentDate))
	            .findFirst()
	            .orElse(null);
    }

    @Override
    @Transactional
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("%s\t-\tRoom with id - %d doesn't exist", getClass().getName(), id)));
    }

    @Override
    @Transactional
    public Room getRoomByNumber(Long roomNumber) {
        return roomRepository.getRoomByNumber(roomNumber)
                .orElseThrow(() -> new NotFoundException(
                        String.format("%s\t-\tRoom with room number - %d doesn't exist", getClass().getName(), roomNumber)));
    }

    @Override
    @Transactional
    public Long getActiveCount() {
        return roomRepository.countALl();
    }

    private Boolean isRoomOverflow() {
        return roomRepository.findAllSimpleInfo().size() >= maxAmountOfRooms;
    }
}
