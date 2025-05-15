package ui.actions.facilityActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Facility;
import hotel.services.hotelServices.FacilityService;

import java.util.List;

@Service("Show list of all facilities")
@AllArgsConstructor
public class ShowAllFacilitiesAction implements FacilityAction {
    private FacilityService facilityService;

    @Override
    public void execute() {
        List<Facility> facilities = facilityService.getFacilitiesListSortedByPrice();
        System.out.println("List of facilities: ");
        facilities.forEach((v) -> System.out.println(v.toString()));
    }
}
