package senla.education.hotel.services.dataServices.impl;

import hotel.services.dataServices.impl.RoomExportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.entities.Room;
import hotel.services.dataServices.DataUtil;
import hotel.services.hotelServices.RoomService;
import senla.education.hotel.util.TestObjectUtils;

import java.io.File;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomExportServiceTest {
	@Mock
	private RoomService roomService;
	@Mock
	private DataUtil dataUtil;
	@InjectMocks
	private RoomExportService roomExportService;

	@Test
	void shouldExportDataTest() {
		// Given
		String filePath = "test_export.csv";
		Room room = TestObjectUtils.createTestRoom(1L, 1001L);
		List<Room> testRooms = List.of(room);
		String expectedData = "Id;RoomNumber;RoomType;RoomStatus\n" + "1;1001;STANDARD;OPEN\n";

		// When
		when(roomService.getAllSimple()).thenReturn(testRooms);

		roomExportService.exportData(filePath);

		// Then
		verify(roomService, times(1)).getAllSimple();
		verify(dataUtil, times(1)).writeAllLine(eq(new File(filePath)), eq(expectedData));
	}

	@Test
	void shouldExportDataWhenEmptyTest() {
		// Given
		String filePath = "empty_export.csv";
		String expectedData = "Id;RoomNumber;RoomType;RoomStatus\n";

		// When
		when(roomService.getAllSimple()).thenReturn(List.of());

		roomExportService.exportData(filePath);

		// Then
		verify(dataUtil, times(1)).writeAllLine(eq(new File(filePath)), eq(expectedData));
	}
}