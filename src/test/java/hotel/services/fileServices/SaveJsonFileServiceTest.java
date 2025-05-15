package senla.education.hotel.services.fileServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import hotel.services.fileServices.SaveJsonFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import hotel.entities.Facility;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.services.hotelServices.FacilityService;
import hotel.services.hotelServices.GuestService;
import hotel.services.hotelServices.ReservationService;
import hotel.services.hotelServices.RoomService;
import senla.education.hotel.util.TestObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveJsonFileServiceTest {
	@Mock
	private RoomService roomService;
	@Mock
	private GuestService guestService;
	@Mock
	private FacilityService facilityService;
	@Mock
	private ReservationService reservationService;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private ObjectWriter prettyPrinterMapper;
	@InjectMocks
	private SaveJsonFileService saveJsonFileService;

	private Room room1;
	private Guest guest1;
	private Facility facility1;
	private Reservation reservation1;

	private final String testFilePath = "test_output.json";

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(saveJsonFileService, "jsonFilePath", testFilePath);
		ReflectionTestUtils.setField(saveJsonFileService, "objectMapper", objectMapper);
		room1 = TestObjectUtils.createTestRoom(1L, 1001L);
		guest1 = TestObjectUtils.createTestGuest(1L);
		facility1 = TestObjectUtils.createTestFacility(1L);
		reservation1 = TestObjectUtils.createTestReservation(1L);
		when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(prettyPrinterMapper);
	}

	@Test
	void shouldSaveJsonDataTest() throws IOException {
		// Given
		Map<String, Object> expectedData = Map.of(
				"rooms", Collections.singletonList(room1),
				"guests", Collections.singletonList(guest1),
				"facilities", Collections.singletonList(facility1),
				"reservations", Collections.singletonList(reservation1));

		// When
		when(roomService.getAllSimple()).thenReturn(Collections.singletonList(room1));
		when(guestService.getAllSimple()).thenReturn(Collections.singletonList(guest1));
		when(facilityService.getAllSimple()).thenReturn(Collections.singletonList(facility1));
		when(reservationService.getAll(any())).thenReturn(Collections.singletonList(reservation1));

		saveJsonFileService.saveJsonData();

		// Then
		verify(roomService, times(1)).getAllSimple();
		verify(guestService, times(1)).getAllSimple();
		verify(facilityService, times(1)).getAllSimple();
		verify(reservationService, times(1)).getAll(any());
		verify(prettyPrinterMapper, times(1)).writeValue(eq(new File(testFilePath)), eq(expectedData));
	}

	@Test
	void shouldSaveJsonDataEvenWhenEmptyTest() throws IOException {
		// Given
		Map<String, Object> expectedData = Map.of(
				"rooms", Collections.emptyList(),
				"guests", Collections.emptyList(),
				"facilities", Collections.emptyList(),
				"reservations", Collections.emptyList());

		// When
		when(roomService.getAllSimple()).thenReturn(Collections.emptyList());
		when(guestService.getAllSimple()).thenReturn(Collections.emptyList());
		when(facilityService.getAllSimple()).thenReturn(Collections.emptyList());
		when(reservationService.getAll(any())).thenReturn(Collections.emptyList());

		saveJsonFileService.saveJsonData();

		// Then
		verify(prettyPrinterMapper, times(1)).writeValue(eq(new File(testFilePath)), eq(expectedData));
	}
}