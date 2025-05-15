package hotel.controllers.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hotel.dto.guest.response.GuestListResponse;
import hotel.dto.guest.response.GuestResponse;
import hotel.entities.Guest;
import hotel.mappers.GuestMapper;
import hotel.services.hotelServices.GuestService;
import hotel.sort.GuestSortParams;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/guests")
@AllArgsConstructor
public class UserGuestController {
	private GuestService guestService;
	private GuestMapper guestMapper;

	@GetMapping
	public ResponseEntity<GuestListResponse> getAllGuests(@ModelAttribute GuestSortParams params) {
		List<Guest> guests = guestService.getAll(params);
		return ResponseEntity.ok(guestMapper.toGuestListResponse(guests, guestService.getActiveCount()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GuestResponse> getGuestById(@PathVariable Long id) {
		return ResponseEntity.ok(guestMapper.toFullResponse(guestService.getGuestById(id)));
	}

	@GetMapping("/settled/count")
	public ResponseEntity<Integer> getAmountOfSettledGuests() {
		return ResponseEntity.ok(guestService.getAmountOfSettledGuests());
	}

	@GetMapping("/settled")
	public ResponseEntity<GuestListResponse> getListOfSettledGuests() {
		return ResponseEntity.ok().body(guestMapper.toGuestListResponse(guestService.getSortedListOfSettledGuests(), guestService.getActiveCount()));
	}
}
