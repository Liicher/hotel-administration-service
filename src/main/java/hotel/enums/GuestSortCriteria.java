package hotel.enums;

import lombok.Getter;

@Getter
public enum GuestSortCriteria {
	ID("g.id"),
	NAME("g.name"),
	LAST_NAME("g.lastName"),
	IS_SETTLED("g.isSettled");

	private final String hqlField;

	GuestSortCriteria(String hqlField) {
		this.hqlField = hqlField;
	}
}
