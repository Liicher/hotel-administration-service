package hotel.dto.room.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import hotel.enums.RoomStatus;
import hotel.enums.RoomType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomSimpleResponse {
	private Long id;
	private Long roomNumber;
	private RoomType roomType;
	private RoomStatus roomStatus;
}
