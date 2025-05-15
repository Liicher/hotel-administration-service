package ui.actions.reservationActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Facility;
import hotel.entities.Reservation;
import hotel.services.hotelServices.FacilityService;
import hotel.services.hotelServices.ReservationService;
import ui.actions.ActionUtils;

import java.util.Set;

@Slf4j
@Service("Remove facility from the reservation")
@AllArgsConstructor
public class RemoveFacilityReservationAction implements ReservationAction {
    private ActionUtils actionUtils;
    private FacilityService facilityService;
    private ReservationService reservationService;

    @Override
    public void execute() {
        try {
            Reservation reservation = actionUtils.getReservation();

            System.out.println("List of room facilities: ");
            Set<Facility> facilities = reservation.getFacilityList();
            if (facilities.isEmpty()) {
                System.out.println("There is no facilities in the room!");
                return;
            }
            facilities.forEach((v) -> System.out.println(v.toString()));

            System.out.println("Enter facility's id: ");
            Long id = actionUtils.idInput();

            Facility facility = facilityService.getFacilityById(id);
            reservation.removeFacility(facility);
            reservationService.updateReservation(reservation);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
