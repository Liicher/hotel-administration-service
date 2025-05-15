package ui.actions.reservationActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.services.hotelServices.GuestService;
import hotel.services.hotelServices.ReservationService;
import ui.actions.ActionUtils;

import java.util.List;

@Slf4j
@Service("Add guest to the reservation")
@AllArgsConstructor
public class AddGuestToReservationAction implements ReservationAction {
    private ActionUtils actionUtils;
    private GuestService guestService;
    private ReservationService reservationService;

    @Override
    public void execute() {
        try {
            Reservation reservation = actionUtils.getReservation();
            List<Guest> guests = guestService.getSortedListOfAllGuests();
            System.out.println("List of all guests: ");
            guests.forEach((v) -> System.out.println(v.toString()));
            System.out.println("Enter guest's id: ");
            Long guestId = actionUtils.idInput();
            Guest guest = guests.stream().filter(v -> v.getId().equals(guestId)).findAny().get();
            guestService.addReservation(guest, reservation);
            reservationService.updateReservation(reservation);
        }catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
