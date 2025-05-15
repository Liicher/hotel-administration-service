package hotel.sort;

import lombok.Getter;
import lombok.Setter;
import hotel.enums.GuestSortCriteria;
import hotel.enums.SortDirection;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class GuestSortParams {
	private Integer page;
	private Integer size;
	private Map<GuestSortCriteria, SortDirection> sort = new LinkedHashMap<>();

	public Map<GuestSortCriteria, SortDirection> getDefaultSort() {
		return Map.of(GuestSortCriteria.ID, SortDirection.ASC);
	}
}
