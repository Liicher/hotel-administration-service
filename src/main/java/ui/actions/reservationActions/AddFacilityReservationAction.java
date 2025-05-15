package ui.actions.reservationActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Facility;
import hotel.entities.Reservation;
import hotel.services.hotelServices.FacilityService;
import hotel.services.hotelServices.ReservationService;
import ui.actions.ActionUtils;

import java.util.List;

@Slf4j
@Service("Add facility to the reservation")
@AllArgsConstructor
public class AddFacilityReservationAction implements ReservationAction {
	private ActionUtils actionUtils;
	private FacilityService facilityService;
	private ReservationService reservationService;

	@Override
	public void execute() {
		try {
			Reservation reservation = actionUtils.getReservation();
			System.out.println("List of facilities: ");
			List<Facility> facilities = facilityService.getFacilitiesListSortedByPrice();
			facilities.forEach((v) -> System.out.println(v.toString()));
			System.out.println("Enter facility's id: ");
			Long facilityId = actionUtils.idInput();
			Facility facility = facilities.stream().filter(v -> v.getId().equals(facilityId)).findAny().get();
			reservation.addFacility(facility);
			reservationService.updateReservation(reservation);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}
}
