package hotel.services.hotelServices;

import hotel.dto.reservation.request.ReservationRequest;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.sort.ReservationSortParams;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    void addAll(Map<Long, Reservation> reservationMap);

    List<Reservation> getAll(ReservationSortParams params);

    void addReservation(Reservation reservation);

    Reservation createReservation(Reservation reservation);

    void updateReservation(Reservation reservation);

    Reservation updateReservation(Long id, ReservationRequest request);

    void removeReservation(Reservation reservation);

    void createNewReservation(Reservation reservation, Room room, Guest guest);

    Reservation getReservationById(Long reservationId);

    void changeGuestInReservation(Reservation reservation, Long guestId);

    Reservation getReservationByRoomNumber(Long roomNumber);

    List<Reservation> getListOfCurrentReservations();

    List<Reservation> getListOfActiveReservations();

    BigDecimal getTotalPrice(Long reservationId);

    Long getActiveCount();
}
