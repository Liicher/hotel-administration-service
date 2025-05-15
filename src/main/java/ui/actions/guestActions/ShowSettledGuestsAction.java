package ui.actions.guestActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.services.hotelServices.GuestService;
import hotel.services.hotelServices.ReservationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("Show list of settled guests")
@AllArgsConstructor
public class ShowSettledGuestsAction implements GuestAction {
    private GuestService guestService;
    private ReservationService reservationService;

    @Override
    public void execute() {
        List<Guest> guests = guestService.getSortedListOfSettledGuests();
        List<Reservation> reservations = reservationService.getListOfCurrentReservations();
        Map<Guest, List<Reservation>> guestToReservationMap = new HashMap<>();
        for (Guest guest : guests) {
            List<Reservation> guestsReservation = reservations.stream().filter(v -> v.getGuest().getId().equals(guest.getId())).collect(Collectors.toList());
            guestToReservationMap.put(guest, guestsReservation);
        }
        System.out.println("List of settled guests: ");
        guestToReservationMap.forEach((guest, reservationsList) -> {
            System.out.println(guest + ":");
            reservationsList.forEach(reservation -> System.out.println(" - " + reservation));
        });
    }
}
