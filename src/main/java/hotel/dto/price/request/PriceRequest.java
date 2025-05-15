package hotel.dto.price.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import hotel.enums.RoomType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceRequest {
	@NotNull
	private RoomType roomType;

	@NotNull
	@Positive(message = "Price must be greater than 0.00")
	private BigDecimal roomTypePrice;
}
