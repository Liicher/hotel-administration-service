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
@Service("Change guest in the reservation")
@AllArgsConstructor
public class ChangeGuestInReservationAction implements ReservationAction {
    private ActionUtils actionUtils;
    private ReservationService reservationService;
    private GuestService guestService;

    @Override
    public void execute() {
        try {
            Reservation reservation = actionUtils.getReservation();
            List<Guest> guestList = guestService.getAllSimple();
            System.out.println("Current reservation owner - " + reservation.getGuest());
            System.out.println("Enter new guest id: ");
            guestList.forEach(v -> System.out.println(v.toString()));
            Long guestId = actionUtils.idInput();
            reservationService.changeGuestInReservation(reservation, guestId);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
