package hotel.dto.room.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class RoomRequest {
	@NotNull(message = "Room number cannot be null")
	@Min(value = 1, message = "Room number must be greater than 0")
	private Long roomNumber;

	@NotNull(message = "Room type cannot be null")
	private RoomType roomType;

	@NotNull(message = "Room status cannot be null")
	private RoomStatus roomStatus;
}
