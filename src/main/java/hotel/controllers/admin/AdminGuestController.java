package hotel.controllers.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hotel.dto.guest.request.GuestRequest;
import hotel.dto.guest.response.GuestResponse;
import hotel.entities.Guest;
import hotel.mappers.GuestMapper;
import hotel.services.hotelServices.GuestService;

@RestController
@RequestMapping("/api/v1/admin/guests")
@AllArgsConstructor
public class AdminGuestController {
	private GuestService guestService;
	private GuestMapper guestMapper;

	@PostMapping
	public ResponseEntity<GuestResponse> createGuest(@Valid @RequestBody GuestRequest request) {
		Guest createdGuest = guestService.createGuest(guestMapper.toEntity(request));
		return new ResponseEntity<>(guestMapper.toFullResponse(createdGuest), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<GuestResponse> updateGuest(@PathVariable Long id, @Valid @RequestBody GuestRequest request) {
		return ResponseEntity.ok(guestMapper.toFullResponse(guestService.update(id, request)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
		guestService.deleteGuest(id);
		return ResponseEntity.noContent().build();
	}
}
