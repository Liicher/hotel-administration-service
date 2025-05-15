package hotel.sort;

import lombok.Getter;
import lombok.Setter;
import hotel.enums.ReservationSortCriteria;
import hotel.enums.SortDirection;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class ReservationSortParams {
	private Integer page;
	private Integer size;
	private Map<ReservationSortCriteria, SortDirection> sort = new LinkedHashMap<>();

	public Map<ReservationSortCriteria, SortDirection> getDefaultSort() {
		return Map.of(ReservationSortCriteria.ID, SortDirection.ASC);
	}
}
