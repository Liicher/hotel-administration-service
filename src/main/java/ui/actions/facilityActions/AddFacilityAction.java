package ui.actions.facilityActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Facility;
import hotel.services.hotelServices.FacilityService;
import ui.actions.ActionUtils;

import java.math.BigDecimal;

@Service("Add new facility")
@AllArgsConstructor
public class AddFacilityAction implements FacilityAction {
    private ActionUtils actionUtils;
    private FacilityService facilityService;

    @Override
    public void execute() {
        System.out.println("FACILITY NAME");
        String name = actionUtils.stringInput();
        BigDecimal price = actionUtils.priceInput();
        Facility facility = new Facility();
        facility.setFacilityName(name);
        facility.setFacilityPrice(price);
        facilityService.createFacility(facility);
    }
}

