package hotel.services.hotelServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotel.dto.reservation.request.ReservationRequest;
import hotel.entities.Facility;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.exceptions.NotFoundException;
import hotel.mappers.ReservationMapper;
import hotel.repository.interfaces.ReservationRepository;
import hotel.services.hotelServices.GuestService;
import hotel.services.hotelServices.PriceService;
import hotel.services.hotelServices.ReservationService;
import hotel.services.hotelServices.RoomService;
import hotel.sort.ReservationSortParams;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final PriceService priceService;
    private final RoomService roomService;
    private final GuestService guestService;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
    @Transactional
    public void addAll(Map<Long, Reservation> reservations) {
        reservationRepository.saveAll(reservations.values());
    }

    @Override
    @Transactional
    public List<Reservation> getAll(ReservationSortParams params) {
        return reservationRepository.findAllSorted(params);
    }

    @Override
    @Transactional
    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        if (reservation.getPrice() == null) {
            Room room = roomService.getRoomById(reservation.getRoom().getId());
            reservation.setPrice(priceService.getPriceByRoomType(room.getRoomType()));
        }
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void updateReservation(Reservation reservation) {
        reservationRepository.update(reservation);
    }

    @Override
    @Transactional
    public Reservation updateReservation(Long id, ReservationRequest request) {
        Reservation existingReservation = getReservationById(id);
        reservationMapper.updateEntity(existingReservation, request);
        return reservationRepository.update(existingReservation);
    }

    @Override
    @Transactional
    public void removeReservation(Reservation reservation) {
        Room room = roomService.getRoomById(reservation.getRoom().getId());
        Guest guest = guestService.getGuestById(reservation.getGuest().getId());

        room.removeReservation(reservation);
        guest.removeReservation(reservation);

        reservationRepository.delete(reservation.getId());
        guestService.update(guest);
        roomService.update(room);
    }

    @Override
    @Transactional
    public void createNewReservation(Reservation reservation, Room room, Guest guest) {
        roomService.addReservation(room, reservation);
        guestService.addReservation(guest, reservation);
        addReservation(reservation);
    }

    @Override
    @Transactional
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("%s\t-\tReservation with id - %d doesn't exist", getClass().getName(), id)));
    }

    @Override
    @Transactional
    public void changeGuestInReservation(Reservation reservation, Long newGuestId) {
        Guest prevGuest = guestService.getGuestById(reservation.getGuest().getId());
        prevGuest.removeReservation(reservation);

        Guest newGuest = guestService.getGuestById(newGuestId);
        newGuest.addReservation(reservation);

        reservationRepository.update(reservation);
        List<Reservation> currentReservations = getListOfCurrentReservations();
        for (Reservation r : currentReservations) {
            if (reservation.getId().equals(r.getId())) {
                continue;
            }

            if (r.getGuest().getId().equals(prevGuest.getId())) {
                prevGuest.setIsSettled(true);
            }

            if (r.getGuest().getId().equals(newGuestId)) {
                newGuest.setIsSettled(true);
            }
        }

        guestService.update(newGuest);
        guestService.update(prevGuest);
    }

    @Override
    @Transactional
    public Reservation getReservationByRoomNumber(Long roomNumber) {
        return reservationRepository.getReservationByRoomNumber(roomNumber)
                .orElseThrow(() -> new NotFoundException(
                        String.format("%s\t-\tReservation with room - %d doesn't exist", getClass().getName(), roomNumber)));
    }

    @Override
    @Transactional
    public List<Reservation> getListOfCurrentReservations() {
        LocalDate today = LocalDate.now();
        ReservationSortParams params = new ReservationSortParams();
        List<Reservation> reservations = getAll(params);
        return reservations.stream()
                .filter(v -> v.getCheckIn().isBefore(today) && v.getCheckOut().isAfter(today))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getListOfActiveReservations() {
        LocalDate today = LocalDate.now();
        ReservationSortParams params = new ReservationSortParams();
        List<Reservation> reservations = getAll(params);
        return reservations.stream()
                .filter(v -> !v.getCheckOut().isBefore(today))
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalPrice(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        if (reservation == null) {
            throw new NotFoundException(String.format("%s\t-\tReservation with id - %d doesn't exist", getClass().getName(), reservationId));
        }
        return getTotalRoomPrice(reservation).add(getTotalServicePrice(reservation));
    }

    private BigDecimal getTotalRoomPrice(Reservation reservation) {
        BigDecimal days = BigDecimal.valueOf(ChronoUnit.DAYS.between(reservation.getCheckIn(), reservation.getCheckOut()));
        BigDecimal price = reservation.getPrice();
        return price.multiply(days);
    }

    private BigDecimal getTotalServicePrice(Reservation reservation) {
        BigDecimal totalServicePrice = BigDecimal.ZERO;
        Set<Facility> facilityList = reservation.getFacilityList();
        for (Facility facility : facilityList) {
            totalServicePrice = totalServicePrice.add(facility.getFacilityPrice());
        }
        return totalServicePrice;
    }

    @Override
    @Transactional
    public Long getActiveCount() {
        return reservationRepository.countALl();
    }
}
