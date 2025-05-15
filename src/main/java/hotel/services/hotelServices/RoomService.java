package hotel.services.hotelServices;

import hotel.dto.room.request.RoomRequest;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.enums.RoomStatus;
import hotel.enums.RoomType;
import hotel.sort.RoomSortParams;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RoomService {
    void addAll(Map<Long, Room> rooms);

    List<Room> getAll(RoomSortParams params);

    List<Room> getAllSimple();

    BigDecimal getRoomTypePrice(RoomType roomType);

    void setNewRoomTypePrice(RoomType roomType, BigDecimal price);

    Room createRoom(Room room);

    void update(Room room);

    Room update(Long id, RoomRequest request);

    void deleteRoom(Long id);

    void addReservation(Room room, Reservation reservation);

    void changeRoomStatus(Long roomNumber, RoomStatus status);

    List<Room> getSortedListOfOpenRooms();

    List<Room> getListOfOpenRooms();

    List<Room> getListOfOccupiedRooms();

    Integer getAmountOfOpenRooms();

    List<Room> getAvailableRoomsOnDate(LocalDate checkInDate, LocalDate checkOutDate);

    List<Reservation> getThreeLastReservations(Long roomNumber);

    Reservation getCurrentReservation(Room room);

    Room getRoomById(Long id);

    Room getRoomByNumber(Long roomNumber);

    Long getActiveCount();
}
