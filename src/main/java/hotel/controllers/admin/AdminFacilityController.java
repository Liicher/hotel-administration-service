package hotel.controllers.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hotel.dto.facility.request.FacilityRequest;
import hotel.dto.facility.response.FacilityResponse;
import hotel.entities.Facility;
import hotel.mappers.FacilityMapper;
import hotel.services.hotelServices.FacilityService;

@RestController
@RequestMapping("/api/v1/admin/facilities")
@AllArgsConstructor
public class AdminFacilityController {
	private FacilityService facilityService;
	private FacilityMapper facilityMapper;

	@PostMapping
	public ResponseEntity<FacilityResponse> createFacility(@Valid @RequestBody FacilityRequest request) {
		Facility createdFacility = facilityService.createFacility(facilityMapper.toEntity(request));
		return new ResponseEntity<>(facilityMapper.toFullResponse(createdFacility), HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<FacilityResponse> updateFacility(@PathVariable Long id, @Valid @RequestBody FacilityRequest request) {
		Facility updatedFacility = facilityService.updateFacility(id, request);
		return ResponseEntity.ok(facilityMapper.toFullResponse(updatedFacility));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
		facilityService.deleteFacility(facilityService.getFacilityById(id));
		return ResponseEntity.noContent().build();
	}
}
