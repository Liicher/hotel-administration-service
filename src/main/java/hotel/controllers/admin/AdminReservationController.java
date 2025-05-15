package hotel.controllers.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hotel.dto.reservation.request.ReservationRequest;
import hotel.dto.reservation.response.ReservationResponse;
import hotel.entities.Reservation;
import hotel.mappers.ReservationMapper;
import hotel.services.hotelServices.ReservationService;

@RestController
@RequestMapping("/api/v1/admin/reservations")
@AllArgsConstructor
public class AdminReservationController {
	private ReservationService reservationService;
	private ReservationMapper reservationMapper;

	@PostMapping
	public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
		Reservation createdReservation = reservationService.createReservation(reservationMapper.toEntity(request));
		return new ResponseEntity<>(reservationMapper.toFullResponse(createdReservation), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ReservationResponse> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationRequest request) {
		Reservation updatedReservation = reservationService.updateReservation(id, request);
		return ResponseEntity.ok(reservationMapper.toFullResponse(updatedReservation));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
		reservationService.removeReservation(reservationService.getReservationById(id));
		return ResponseEntity.noContent().build();
	}
}
