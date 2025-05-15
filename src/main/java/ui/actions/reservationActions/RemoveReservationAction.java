package ui.actions.reservationActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Reservation;
import hotel.services.hotelServices.ReservationService;
import ui.actions.ActionUtils;

import java.util.List;

@Slf4j
@Service("Remove reservation")
@AllArgsConstructor
public class RemoveReservationAction implements ReservationAction {
	private ActionUtils actionUtils;
	private ReservationService reservationService;

	@Override
	public void execute() {
		try {
			List<Reservation> activeReservations = reservationService.getListOfActiveReservations();
			activeReservations.forEach(System.out::println);
			Long reservationId = actionUtils.idInput();
			Reservation reservation = activeReservations.stream().filter(v -> v.getId().equals(reservationId)).findFirst().get();
			reservationService.removeReservation(reservation);
			System.out.println("Reservation in room №" + reservation.getRoom().getRoomNumber() + " removed!");
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}
}
