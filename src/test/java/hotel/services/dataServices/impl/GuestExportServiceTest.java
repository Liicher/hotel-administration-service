package senla.education.hotel.services.dataServices.impl;

import hotel.services.dataServices.impl.GuestExportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.entities.Guest;
import hotel.services.dataServices.DataUtil;
import hotel.services.hotelServices.GuestService;
import senla.education.hotel.util.TestObjectUtils;

import java.io.File;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestExportServiceTest {
	@Mock
	private GuestService guestService;
	@Mock
	private DataUtil dataUtil;
	@InjectMocks
	private GuestExportService guestExportService;

	@Test
	void shouldExportDataTest() {
		// Given
		Guest guest = TestObjectUtils.createTestGuest(1L);
		guest.setName("John");
		guest.setLastName("Doe");

		String filePath = "test_export.csv";
		List<Guest> testGuests = List.of(guest);
		String expectedData = "ID;Name;Lastname\n" + "1;John;Doe\n";

		// When
		when(guestService.getAllSimple()).thenReturn(testGuests);

		guestExportService.exportData(filePath);

		// Then
		verify(guestService, times(1)).getAllSimple();
		verify(dataUtil, times(1)).writeAllLine(eq(new File(filePath)), eq(expectedData));
	}

	@Test
	void shouldExportDataWhenEmptyTest() {
		// Given
		String filePath = "empty_export.csv";
		String expectedData = "ID;Name;Lastname\n";

		// When
		when(guestService.getAllSimple()).thenReturn(List.of());

		guestExportService.exportData(filePath);

		// Then
		verify(dataUtil, times(1)).writeAllLine(eq(new File(filePath)), eq(expectedData));
	}
}