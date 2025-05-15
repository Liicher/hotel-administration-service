package senla.education.hotel.services.dataServices.impl;

import hotel.services.dataServices.impl.RoomImportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.entities.Room;
import hotel.exceptions.FileNotFoundException;
import hotel.services.dataServices.DataUtil;
import hotel.services.hotelServices.RoomService;
import senla.education.hotel.util.TestObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomImportServiceTest {
	@Mock
	private DataUtil dataUtil;
	@Mock
	private RoomService roomService;
	@InjectMocks
	private RoomImportService roomImportService;

	@Test
	void shouldImportDataTest() {
		// Given
		String filepath = "rooms.csv";
		List<String> mockLines = new ArrayList<>(Arrays.asList("header", "1;1001;STANDARD;OPEN"));
		Room room = TestObjectUtils.createTestRoom(1L, 1001L);
		Map<Long, Room> expectedMap = Map.of(1L, room);

		// When
		when(dataUtil.readAllLines(filepath)).thenReturn(mockLines);

		roomImportService.importData(filepath);

		// Then
		verify(dataUtil, times(1)).checkFileExists(filepath);
		verify(dataUtil, times(1)).readAllLines(filepath);
		verify(roomService, times(1)).addAll(expectedMap);
	}

	@Test
	void shouldNotImportDataWhenFileNotExistsTest() {
		// Given
		String invalidPath = "rooms.csv";

		// When
		doThrow(new FileNotFoundException("File not found")).when(dataUtil).checkFileExists(invalidPath);

		// Then
		assertThrows(FileNotFoundException.class, () -> roomImportService.importData(invalidPath));
		verify(dataUtil, times(1)).checkFileExists(invalidPath);
		verifyNoMoreInteractions(dataUtil, roomService);
	}
}