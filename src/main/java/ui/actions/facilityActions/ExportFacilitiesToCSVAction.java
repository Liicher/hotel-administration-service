package ui.actions.facilityActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.dataServices.impl.FacilityExportService;
import ui.actions.ActionUtils;
import ui.interfaces.Action;

@Service
@AllArgsConstructor
public class ExportFacilitiesToCSVAction implements Action {
    private ActionUtils actionUtils;
    private FacilityExportService facilityExportService;

    @Override
    public void execute() {
        System.out.println("Enter filepath:");
        String filepath = actionUtils.stringInput();
        System.out.println("Exporting facilities!");
        facilityExportService.exportData(filepath);
        System.out.println("DONE!");
    }
}
