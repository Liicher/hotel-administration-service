package hotel.dto.guest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import hotel.dto.reservation.response.ReservationSimpleResponse;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestResponse {
	private Long id;
	private String name;
	private String lastName;
	private Boolean isSettled;

	@Builder.Default
	private List<ReservationSimpleResponse> reservationIds = new ArrayList<>();
}
