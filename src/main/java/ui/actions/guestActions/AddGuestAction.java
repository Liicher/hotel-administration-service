package ui.actions.guestActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.hotelServices.GuestService;
import ui.actions.ActionUtils;

@Service("Add new guest")
@AllArgsConstructor
public class AddGuestAction implements GuestAction {
    private ActionUtils actionUtils;
    private GuestService guestService;

    @Override
    public void execute() {
        System.out.println("Enter the name with a capital letter: ");
        String name = actionUtils.nameInput();
        System.out.println("Enter the last name with a capital letter: ");
        String lastName = actionUtils.nameInput();
        guestService.createGuest(name, lastName);
    }
}
