package senla.education.hotel.services.hotelServices.impl;

import hotel.services.hotelServices.impl.PriceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.entities.Price;
import hotel.enums.RoomType;
import hotel.repository.interfaces.PriceRepository;
import senla.education.hotel.util.TestObjectUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {
	@Mock
	private PriceRepository priceRepository;
	@InjectMocks
	private PriceServiceImpl priceService;

	private Price price1;
	private Price price2;

	@BeforeEach
	void setUp() {
		price1 = TestObjectUtils.createTestPrice();
		price2 = TestObjectUtils.createTestPrice();
	}

	@Test
	void shouldGetAllTest() {
		// Given
		List<Price> expectedPrices = List.of(price1, price2);

		// When
		when(priceRepository.findAll()).thenReturn(expectedPrices);

		List<Price> actualPrices = priceService.getAll();

		// Then
		assertEquals(expectedPrices, actualPrices);
		verify(priceRepository, times(1)).findAll();
		verifyNoMoreInteractions(priceRepository);
	}

	@Test
	void shouldGetPriceByRoomTypeTest() {
		// Given
		RoomType roomType = RoomType.STANDARD;

		// When
		when(priceRepository.findByRoomType(roomType)).thenReturn(price1);

		BigDecimal actualPrice = priceService.getPriceByRoomType(roomType);

		// Then
		assertEquals(BigDecimal.valueOf(100), actualPrice);
		verify(priceRepository, times(1)).findByRoomType(roomType);
		verifyNoMoreInteractions(priceRepository);
	}

	@Test
	void shouldNotGetPriceByRoomTypeWhenTest() {
		// Given
		RoomType roomType = RoomType.BEDROOM;

		// When
		when(priceRepository.findByRoomType(roomType)).thenThrow();

		// Then
		assertThrows(RuntimeException.class, () -> priceService.getPriceByRoomType(roomType));
		verify(priceRepository, times(1)).findByRoomType(roomType);
		verifyNoMoreInteractions(priceRepository);
	}
}