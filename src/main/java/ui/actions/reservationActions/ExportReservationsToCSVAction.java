package ui.actions.reservationActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.dataServices.impl.ReservationExportService;
import ui.actions.ActionUtils;
import ui.interfaces.Action;

@Service
@AllArgsConstructor
public class ExportReservationsToCSVAction implements Action {
    private ActionUtils actionUtils;
    private ReservationExportService reservationExportService;

    @Override
    public void execute() {
        System.out.println("Enter filepath:");
        String filepath = actionUtils.stringInput();
        System.out.println("Exporting reservations...");
        reservationExportService.exportData(filepath);
        System.out.println("Reservations exported!");
    }
}
