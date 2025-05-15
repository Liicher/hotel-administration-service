package ui.actions.guestActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Guest;
import hotel.services.hotelServices.GuestService;
import ui.actions.ActionUtils;

import java.util.List;

@Slf4j
@Service("Remove guest")
@AllArgsConstructor
public class DeleteGuestAction implements GuestAction {
    private ActionUtils actionUtils;
    private GuestService guestService;

    @Override
    public void execute() {
        try {
            List<Guest> guests = guestService.getAllSimple();
            guests.forEach(v -> System.out.println(v.toString()));
            System.out.println("Enter guest's id: ");
            guestService.deleteGuest(actionUtils.idInput());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
