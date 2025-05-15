package hotel.enums;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum RoomType {
    STANDARD("Стандартный", 2),
    SUPERIOR("Расширенный", 3),
    BEDROOM("Спальня", 4),
    LUXURY("Люкс", 5);

    private String title;
    @JsonIgnore
    private int roomTypeMaxCapacity;

    RoomType(String title, int roomTypeMaxCapacity) {
        this.title = title;
        this.roomTypeMaxCapacity = roomTypeMaxCapacity;
    }

    public String getTitle() {
        return title;
    }

    public int getRoomTypeMaxCapacity() {
        return roomTypeMaxCapacity;
    }
}
