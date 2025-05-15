package senla.education.hotel.services.dataServices.impl;

import hotel.services.dataServices.impl.GuestImportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.entities.Guest;
import hotel.exceptions.FileNotFoundException;
import hotel.services.dataServices.DataUtil;
import hotel.services.hotelServices.GuestService;
import senla.education.hotel.util.TestObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestImportServiceTest {
	@Mock
	private DataUtil dataUtil;
	@Mock
	private GuestService guestService;
	@InjectMocks
	private GuestImportService guestImportService;

	@Test
	void shouldImportDataTest() {
		// Given
		String filepath = "guests.csv";
		List<String> mockLines = new ArrayList<>(Arrays.asList("header", "1;John;Doe"));

		Guest guest = TestObjectUtils.createTestGuest(1L);
		Map<Long, Guest> expectedMap = Map.of(1L, guest);

		// When
		when(dataUtil.readAllLines(filepath)).thenReturn(mockLines);
		
		guestImportService.importData(filepath);

		// Then
		verify(dataUtil, times(1)).checkFileExists(filepath);
		verify(dataUtil, times(1)).readAllLines(filepath);
		verify(guestService, times(1)).addAll(expectedMap);
	}

	@Test
	void shouldNotImportDataWhenFileNotExistsTest() {
		// Given
		String invalidPath = "guests.csv";

		// When
		doThrow(new FileNotFoundException("File not found")).when(dataUtil).checkFileExists(invalidPath);

		// Then
		assertThrows(FileNotFoundException.class, () -> guestImportService.importData(invalidPath));
		verify(dataUtil, times(1)).checkFileExists(invalidPath);
		verifyNoMoreInteractions(dataUtil, guestService);
	}
}