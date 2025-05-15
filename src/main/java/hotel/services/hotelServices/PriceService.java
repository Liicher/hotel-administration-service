package hotel.services.hotelServices;

import hotel.entities.Price;
import hotel.enums.RoomType;

import java.math.BigDecimal;
import java.util.List;

public interface PriceService {
    List<Price> getAll();
    BigDecimal getPriceByRoomType(RoomType roomType);
}
