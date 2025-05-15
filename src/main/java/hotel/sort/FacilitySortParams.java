package hotel.sort;

import lombok.Getter;
import lombok.Setter;
import hotel.enums.FacilitySortCriteria;
import hotel.enums.SortDirection;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class FacilitySortParams {
	private Integer page;
	private Integer size;
	private Map<FacilitySortCriteria, SortDirection> sort = new LinkedHashMap<>();

	public Map<FacilitySortCriteria, SortDirection> getDefaultSort() {
		return Map.of(FacilitySortCriteria.ID, SortDirection.ASC);
	}
}
