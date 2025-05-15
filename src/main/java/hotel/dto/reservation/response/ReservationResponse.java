package hotel.dto.reservation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import hotel.dto.facility.response.FacilitySimpleResponse;
import hotel.dto.guest.response.GuestSimpleResponse;
import hotel.dto.room.response.RoomSimpleResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
	private Long id;
	private RoomSimpleResponse room;
	private GuestSimpleResponse guest;
	private BigDecimal price;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
	@FutureOrPresent(message = "Check in date must be in the future")
	private LocalDate checkIn;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
	@FutureOrPresent(message = "Check out date must be in the future")
	private LocalDate checkOut;

	@Builder.Default
	private List<FacilitySimpleResponse> facilities = new ArrayList<>();
}
