package hotel.dto.facility.response;

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
public class FacilityListResponse {
	private Long totalCount;

	@Builder.Default
	private List<FacilitySimpleResponse> facilities = new ArrayList<>();
}
