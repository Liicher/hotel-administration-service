package hotel.services.hotelServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotel.entities.Price;
import hotel.enums.RoomType;
import hotel.repository.interfaces.PriceRepository;
import hotel.services.hotelServices.PriceService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {
	private final PriceRepository priceRepository;

	@Override
	@Transactional
	public List<Price> getAll() {
		return priceRepository.findAll();
	}

	@Override
	@Transactional
	public BigDecimal getPriceByRoomType(RoomType roomType) {
		return priceRepository.findByRoomType(roomType).getRoomTypePrice();
	}
}
