package hotel.services.hotelServices;

import hotel.dto.guest.request.GuestRequest;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.sort.GuestSortParams;

import java.util.List;
import java.util.Map;

public interface GuestService {
    void addAll(Map<Long, Guest> guests);

    List<Guest> getAll(GuestSortParams params);

    List<Guest> getAllSimple();

    void createGuest(String name, String lastName);

    Guest createGuest(Guest guest);

    Guest update(Long id, GuestRequest request);

    void deleteGuest(Long id);

    void addReservation(Guest guest, Reservation reservation);

    Guest getGuestById(Long id);

    List<Guest> getSortedListOfAllGuests();

    List<Guest> getSortedListOfSettledGuests();

    Integer getAmountOfSettledGuests();

    Reservation getCurrentReservation(Guest guest);

    void update(Guest guest);

    Long getActiveCount();
}
