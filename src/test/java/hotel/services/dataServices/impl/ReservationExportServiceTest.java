package senla.education.hotel.services.dataServices.impl;

import hotel.services.dataServices.impl.ReservationExportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.services.dataServices.DataUtil;
import hotel.services.hotelServices.ReservationService;
import senla.education.hotel.util.TestObjectUtils;

import java.io.File;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationExportServiceTest {
	@Mock
	private ReservationService reservationService;
	@Mock
	private DataUtil dataUtil;
	@InjectMocks
	private ReservationExportService reservationExportService;

	@Test
	void shouldExportDataTest() {
		// Given
		Room room = TestObjectUtils.createTestRoom(1L, 1001L);
		Guest guest = TestObjectUtils.createTestGuest(1L);
		Reservation reservation = TestObjectUtils.createFilledReservation(1L);
		reservation.setRoom(room);
		reservation.setGuest(guest);

		String filePath = "test_export.csv";
		List<Reservation> testReservations = List.of(reservation);
		String expectedData = "Id;RoomNumber;CheckInDate;CheckOutDate;Price;GuestId;Facilities\n" +
				"1;1;2025-01-01;2025-02-02;100.00;1";

		// When
		when(reservationService.getAll(any())).thenReturn(testReservations);

		reservationExportService.exportData(filePath);

		// Then
		verify(reservationService, times(1)).getAll(any());
		verify(dataUtil, times(1)).writeAllLine(eq(new File(filePath)), eq(expectedData));
	}

	@Test
	void shouldExportDataWhenEmptyTest() {
		// Given
		String filePath = "empty_export.csv";
		String expectedData = "Id;RoomNumber;CheckInDate;CheckOutDate;Price;GuestId;Facilities\n";

		// When
		when(reservationService.getAll(any())).thenReturn(List.of());

		reservationExportService.exportData(filePath);

		// Then
		verify(dataUtil, times(1)).writeAllLine(eq(new File(filePath)), eq(expectedData));
	}
}