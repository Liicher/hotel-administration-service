package hotel.dto.guest.response;

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
public class GuestListResponse {
	private Long totalCount;

	@Builder.Default
	private List<GuestSimpleResponse> guests = new ArrayList<>();
}
