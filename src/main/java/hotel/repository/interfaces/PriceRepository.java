package hotel.repository.interfaces;

import hotel.entities.Price;
import hotel.enums.RoomType;

import java.util.List;

public interface PriceRepository {
	List<Price> findAll();
	Price findByRoomType(RoomType roomType);
}
