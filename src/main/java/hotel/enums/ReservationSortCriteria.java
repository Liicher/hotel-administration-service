package hotel.enums;

import lombok.Getter;

@Getter
public enum ReservationSortCriteria {
	ID("r.id"),
	PRICE("r.price"),
	CHECK_IN("r.checkIn"),
	CHECK_OUT("r.checkOut"),
	ROOM_NUMBER("r.room.roomNumber"),
	GUEST_LAST_NAME("r.guest.lastName"),
	FACILITY_COUNT("(SELECT COUNT(rf) FROM r.facilityList rf)");

	private final String hqlField;

	ReservationSortCriteria(String hqlField) {
		this.hqlField = hqlField;
	}
}
