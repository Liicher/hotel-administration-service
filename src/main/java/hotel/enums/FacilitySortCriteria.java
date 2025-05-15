package hotel.enums;

import lombok.Getter;

@Getter
public enum FacilitySortCriteria {
	ID("f.id"),
	NAME("f.facilityName"),
	PRICE("f.facilityPrice");

	private final String hqlField;

	FacilitySortCriteria(String hqlField) {
		this.hqlField = hqlField;
	}
}