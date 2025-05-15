package hotel.dto.room.response;

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
public class RoomListResponse {
	private Long totalCount;

	@Builder.Default
	private List<RoomSimpleResponse> rooms = new ArrayList<>();
}
