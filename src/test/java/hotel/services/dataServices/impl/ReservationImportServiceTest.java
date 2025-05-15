package senla.education.hotel.services.dataServices.impl;

import hotel.services.dataServices.impl.ReservationImportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.entities.Reservation;
import hotel.exceptions.FileNotFoundException;
import hotel.services.dataServices.DataUtil;
import hotel.services.hotelServices.GuestService;
import hotel.services.hotelServices.ReservationService;
import hotel.services.hotelServices.RoomService;
import senla.education.hotel.util.TestObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationImportServiceTest {
	@Mock
	private DataUtil dataUtil;
	@Mock
	private ReservationService reservationService;
	@Mock
	private GuestService guestService;
	@Mock
	private RoomService roomService;
	@InjectMocks
	private ReservationImportService reservationImportService;

	@Test
	void shouldImportDataTest() {
		// Given
		String filepath = "reservations.csv";
		List<String> mockLines = new ArrayList<>(Arrays.asList("header", "1;1;1;01.01.2025;02.02.2025;100.00;"));

		Reservation reservation = TestObjectUtils.createFilledReservation(1L);
		Map<Long, Reservation> expectedMap = Map.of(1L, reservation);

		// When
		when(dataUtil.readAllLines(filepath)).thenReturn(mockLines);
		when(roomService.getRoomById(anyLong())).thenReturn(TestObjectUtils.createTestRoom(1L, 1001L));
		when(guestService.getGuestById(anyLong())).thenReturn(TestObjectUtils.createTestGuest(1L));

		reservationImportService.importData(filepath);

		// Then
		verify(dataUtil, times(1)).checkFileExists(filepath);
		verify(dataUtil, times(1)).readAllLines(filepath);
		verify(reservationService, times(1)).addAll(expectedMap);
		verify(roomService, times(1)).getRoomById(anyLong());
		verify(guestService, times(1)).getGuestById(anyLong());
	}

	@Test
	void shouldNotImportDataWhenFileNotExistsTest() {
		// Given
		String invalidPath = "reservations.csv";

		// When
		doThrow(new FileNotFoundException("File not found")).when(dataUtil).checkFileExists(invalidPath);

		// Then
		assertThrows(FileNotFoundException.class, () -> reservationImportService.importData(invalidPath));
		verify(dataUtil, times(1)).checkFileExists(invalidPath);
		verifyNoInteractions(reservationService, guestService, roomService);
	}
}