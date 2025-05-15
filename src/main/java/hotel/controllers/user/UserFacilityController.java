package hotel.controllers.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hotel.dto.facility.response.FacilityListResponse;
import hotel.dto.facility.response.FacilityResponse;
import hotel.entities.Facility;
import hotel.mappers.FacilityMapper;
import hotel.services.hotelServices.FacilityService;
import hotel.sort.FacilitySortParams;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/facilities")
@AllArgsConstructor
public class UserFacilityController {
	private FacilityService facilityService;
	private FacilityMapper facilityMapper;

	@GetMapping
	public ResponseEntity<FacilityListResponse> getAllFacilities(@ModelAttribute FacilitySortParams params) {
		List<Facility> facilities = facilityService.getAll(params);
		return ResponseEntity.ok(facilityMapper.toFacilityListResponse(facilities, facilityService.getActiveCount()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<FacilityResponse> getFacilityById(@PathVariable Long id) {
		return ResponseEntity.ok(facilityMapper.toFullResponse(facilityService.getFacilityById(id)));
	}
}
