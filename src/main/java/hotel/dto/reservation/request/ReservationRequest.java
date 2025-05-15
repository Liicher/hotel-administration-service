package hotel.dto.reservation.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {
	@NotNull
	private Long roomId;

	@NotNull
	private Long guestId;

	@DecimalMin(value = "0.01", message = "Price must be greater than 0.00")
	private BigDecimal price;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
	@FutureOrPresent(message = "Check in date must be in the future")
	private LocalDate checkIn;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
	@Future(message = "Check out date must be in the future")
	private LocalDate checkOut;
}
