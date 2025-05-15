package ui.actions.facilityActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Facility;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.services.hotelServices.GuestService;
import ui.actions.ActionUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("Show facilities of specific guest")
@AllArgsConstructor
public class ShowFacilitiesOfGuestAction implements FacilityAction {
    private ActionUtils actionUtils;
    private GuestService guestService;

    @Override
    public void execute() {
        try {
            List<Guest> guestList = guestService.getSortedListOfSettledGuests();
            guestList.forEach(v -> System.out.println(v.toString()));
            System.out.println("Enter guest's id: ");
            Long guestId = actionUtils.idInput();
            Guest guest = guestService.getGuestById(guestId);

            List<Reservation> currentReservations = guest.getReservationList().stream()
                    .filter(v -> v.getCheckIn().isBefore(LocalDate.now()) && v.getCheckOut().isAfter(LocalDate.now()))
                    .collect(Collectors.toList());

            Set<Facility> facilities = new HashSet<>();
            currentReservations.forEach(v -> facilities.addAll(v.getFacilityList()));

            System.out.println(guest + "\nList of facilities: ");
            facilities.forEach(v -> System.out.println(v.toString()));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
