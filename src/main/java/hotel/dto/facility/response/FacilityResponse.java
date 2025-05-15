package hotel.dto.facility.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import hotel.dto.reservation.response.ReservationSimpleResponse;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityResponse {
	private Long id;
	private String facilityName;
	private BigDecimal facilityPrice;
	private List<ReservationSimpleResponse> reservations;
}
