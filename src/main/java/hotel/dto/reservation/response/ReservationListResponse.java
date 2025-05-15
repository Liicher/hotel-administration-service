package hotel.dto.reservation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationListResponse {
	private Long totalCount;

	@Builder.Default
	private List<ReservationSimpleResponse> reservations = new ArrayList<>();
}
