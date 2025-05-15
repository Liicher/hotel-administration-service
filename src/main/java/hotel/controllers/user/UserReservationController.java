package hotel.controllers.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hotel.dto.reservation.response.ReservationListResponse;
import hotel.dto.reservation.response.ReservationResponse;
import hotel.entities.Reservation;
import hotel.mappers.ReservationMapper;
import hotel.services.hotelServices.ReservationService;
import hotel.sort.ReservationSortParams;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/reservations")
@AllArgsConstructor
public class UserReservationController {
	private ReservationService reservationService;
	private ReservationMapper reservationMapper;

	@GetMapping
	public ResponseEntity<ReservationListResponse> getAllReservations(@ModelAttribute ReservationSortParams params) {
		List<Reservation> reservations = reservationService.getAll(params);
		return ResponseEntity.ok(reservationMapper.toReservationListResponse(reservations, reservationService.getActiveCount()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
		return ResponseEntity.ok(reservationMapper.toFullResponse(reservationService.getReservationById(id)));
	}

	@GetMapping("/{id}/price/total")
	public ResponseEntity<BigDecimal> getTotalPriceForReservation(@PathVariable Long id) {
		return ResponseEntity.ok(reservationService.getTotalPrice(id));
	}
}