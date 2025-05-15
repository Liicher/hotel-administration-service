package hotel.repository.interfaces;

import hotel.entities.Reservation;
import hotel.repository.GenericDao;
import hotel.sort.ReservationSortParams;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends GenericDao<Reservation, Long> {
    Optional<Reservation> getReservationByRoomNumber(Long roomNumber);
    List<Reservation> findAllSorted(ReservationSortParams params);
}