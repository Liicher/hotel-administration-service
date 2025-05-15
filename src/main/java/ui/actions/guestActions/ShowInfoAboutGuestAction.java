package ui.actions.guestActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Guest;
import hotel.services.hotelServices.GuestService;
import ui.actions.ActionUtils;

import java.util.List;

@Slf4j
@Service("Show info about specific guest")
@AllArgsConstructor
public class ShowInfoAboutGuestAction implements GuestAction {
    private ActionUtils actionUtils;
    private GuestService guestService;

    @Override
    public void execute() {
        try {
            List<Guest> guests = guestService.getAllSimple();
            guests.forEach(v -> System.out.println(v.toString()));
            System.out.println("Enter guest's id: ");
            Long id = actionUtils.idInput();
            Guest guest = guestService.getGuestById(id);
            System.out.println(guest);
            guest.getReservationList().forEach(System.out::println);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
