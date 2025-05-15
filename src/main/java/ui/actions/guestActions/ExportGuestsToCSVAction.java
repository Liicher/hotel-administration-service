package ui.actions.guestActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.dataServices.impl.GuestExportService;
import ui.actions.ActionUtils;
import ui.interfaces.Action;

@Service
@AllArgsConstructor
public class ExportGuestsToCSVAction implements Action {
    private ActionUtils actionUtils;
    private GuestExportService guestExportService;

    @Override
    public void execute() {
        System.out.println("Enter filepath:");
        String filepath = actionUtils.stringInput();
        System.out.println("Exporting guests!");
        guestExportService.exportData(filepath);
        System.out.println("DONE!");
    }
}
