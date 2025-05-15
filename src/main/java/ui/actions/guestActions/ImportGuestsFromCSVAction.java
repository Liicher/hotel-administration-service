package ui.actions.guestActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.dataServices.impl.GuestImportService;
import hotel.services.hotelServices.GuestService;
import ui.actions.ActionUtils;

@Service
@AllArgsConstructor
public class ImportGuestsFromCSVAction implements GuestAction {
    private ActionUtils actionUtils;
    private GuestImportService guestImportService;
    private GuestService guestService;

    @Override
    public void execute() {
        System.out.println("Enter filepath:");
        String filepath = actionUtils.stringInput();
        System.out.println("Importing guests!");
        guestImportService.importData(filepath);
        System.out.println("List of guests:");
        guestService.getSortedListOfAllGuests().forEach((v) -> System.out.println(v.toString()));
    }
}
