package hotel.dto.room.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import hotel.dto.reservation.response.ReservationSimpleResponse;
import hotel.enums.RoomStatus;
import hotel.enums.RoomType;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
	private Long id;
	private Long roomNumber;
	private RoomType roomType;
	private RoomStatus roomStatus;

	@Builder.Default
	private List<ReservationSimpleResponse> reservationList = new ArrayList<>();
}
