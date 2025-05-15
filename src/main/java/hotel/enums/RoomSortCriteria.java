package hotel.enums;

import lombok.Getter;

@Getter
public enum RoomSortCriteria {
	ID("r.id"),
	ROOM_NUMBER("r.roomNumber"),
	ROOM_TYPE("r.roomType"),
	ROOM_STATUS("r.roomStatus"),
	ROOM_CAPACITY("r.roomType.roomTypeMaxCapacity"),
	PRICE("(SELECT p.roomTypePrice FROM Price p WHERE p.roomType = r.roomType)");

	private final String hqlField;

	RoomSortCriteria(String hqlField) {
		this.hqlField = hqlField;
	}
}
