package hotel.dto.facility.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityRequest {
	@NotNull(message = "Facility name cannot be null")
	private String facilityName;

	@DecimalMin(value = "0.00", message = "Price must be greater than 0.00")
	@NotNull(message = "Facility price cannot be null")
	private BigDecimal facilityPrice;
}
