package ui.actions.reservationActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Reservation;
import hotel.services.hotelServices.ReservationService;

import java.util.List;

@Service("Show current reservations")
@AllArgsConstructor
public class ShowCurrentReservationsAction implements ReservationAction {
    private ReservationService reservationService;

    @Override
    public void execute() {
        List<Reservation> reservations = reservationService.getListOfCurrentReservations();
        System.out.println("List of current reservations:");
        reservations.forEach((v) -> System.out.println(v.toString()));
    }
}
