package ui.actions.facilityActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Facility;
import hotel.services.hotelServices.FacilityService;
import ui.actions.ActionUtils;

import java.util.List;

@Slf4j
@Service("Remove facility")
@AllArgsConstructor
public class DeleteFacilityAction implements FacilityAction {
    private ActionUtils actionUtils;
    private FacilityService facilityService;

    @Override
    public void execute() {
        try {
            List<Facility> facilities = facilityService.getAllSimple();
            facilities.forEach(System.out::println);
            System.out.println("Enter facility id: ");
            Long id = actionUtils.idInput();
            Facility facility = facilities.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
            facilityService.deleteFacility(facility);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
