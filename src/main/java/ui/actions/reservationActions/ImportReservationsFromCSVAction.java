package ui.actions.reservationActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.dataServices.impl.ReservationImportService;
import ui.actions.ActionUtils;

@Service
@AllArgsConstructor
public class ImportReservationsFromCSVAction implements ReservationAction {
    private ActionUtils actionUtils;
    private ReservationImportService reservationImportService;

    @Override
    public void execute() {
        System.out.println("Enter filepath:");
        String filepath = actionUtils.stringInput();
        System.out.println("Importing reservations...");
        reservationImportService.importData(filepath);
    }
}
