package ui.actions.guestActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Guest;
import hotel.services.hotelServices.GuestService;

import java.util.List;

@Service("Show list of all guests")
@AllArgsConstructor
public class ShowAllGuestsAction implements GuestAction {
    private GuestService guestService;

    @Override
    public void execute() {
        List<Guest> guests = guestService.getSortedListOfAllGuests();
        System.out.println("List of all guests: ");
        guests.forEach((v) -> System.out.println(v.toString()));
    }
}
