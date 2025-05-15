package ui.actions.guestActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.hotelServices.GuestService;

@Service("Show amount of settled guests")
@AllArgsConstructor
public class ShowAmountOfSettledGuestsAction implements GuestAction {
    private GuestService guestService;

    @Override
    public void execute() {
        System.out.println("Amount of settled guests: " + guestService.getAmountOfSettledGuests());
    }
}
