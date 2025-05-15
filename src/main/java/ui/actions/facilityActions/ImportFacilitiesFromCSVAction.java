package ui.actions.facilityActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.dataServices.impl.FacilityImportService;
import hotel.services.hotelServices.FacilityService;
import ui.actions.ActionUtils;
import ui.interfaces.Action;

@Service
@AllArgsConstructor
public class ImportFacilitiesFromCSVAction implements Action {
    private ActionUtils actionUtils;
    private FacilityImportService facilityImportService;
    private FacilityService facilityService;

    @Override
    public void execute() {
        System.out.println("Enter filepath:");
        String filepath = actionUtils.stringInput();
        System.out.println("Importing facilities!");
        facilityImportService.importData(filepath);
        System.out.println("List of facilities:");
        facilityService.getFacilitiesListSortedByPrice().forEach((v) -> System.out.println(v.toString()));
    }
}
