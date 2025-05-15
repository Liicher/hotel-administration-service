package hotel.sort;

import lombok.Getter;
import lombok.Setter;
import hotel.enums.RoomSortCriteria;
import hotel.enums.SortDirection;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class RoomSortParams {
	private Integer page;
	private Integer size;
	private Map<RoomSortCriteria, SortDirection> sort = new LinkedHashMap<>();

	public Map<RoomSortCriteria, SortDirection> getDefaultSort() {
		return Map.of(RoomSortCriteria.ID, SortDirection.ASC);
	}
}
