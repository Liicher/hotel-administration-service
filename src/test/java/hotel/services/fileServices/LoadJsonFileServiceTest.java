package senla.education.hotel.services.fileServices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.services.fileServices.LoadJsonFileService;
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
import hotel.exceptions.FileNotFoundException;
import hotel.exceptions.NotFoundException;
import hotel.services.hotelServices.FacilityService;
import hotel.services.hotelServices.GuestService;
import hotel.services.hotelServices.ReservationService;
import hotel.services.hotelServices.RoomService;
import senla.education.hotel.util.TestObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoadJsonFileServiceTest {
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
	@InjectMocks
	private LoadJsonFileService loadJsonFileService;

	private String testFilePath = "test.json";
	private Room room1;
	private Guest guest1;
	private Facility facility1;
	private Reservation reservation1;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(loadJsonFileService, "jsonFilePath", testFilePath);
		ReflectionTestUtils.setField(loadJsonFileService, "objectMapper", objectMapper);
		room1 = TestObjectUtils.createTestRoom(1L, 1001L);
		guest1 = TestObjectUtils.createTestGuest(1L);
		facility1 = TestObjectUtils.createTestFacility(1L);
		reservation1 = TestObjectUtils.createTestReservation(1L);
	}

	@Test
	void shouldLoadJsonDataTest() throws IOException {
		// Given
		reservation1.setRoom(room1);
		reservation1.setGuest(guest1);
		reservation1.setFacilityList(Collections.singleton(facility1));

		Map<String, List<Object>> testData = Map.of(
				"rooms", Collections.singletonList(room1),
				"guests", Collections.singletonList(guest1),
				"facilities", Collections.singletonList(facility1),
				"reservations", Collections.singletonList(reservation1));

		// When
		when(objectMapper.readValue(any(File.class), any(TypeReference.class))).thenReturn(testData);
		when(objectMapper.convertValue(eq(testData.get("rooms")), any(TypeReference.class))).thenReturn(Collections.singletonList(room1));
		when(objectMapper.convertValue(eq(testData.get("guests")), any(TypeReference.class))).thenReturn(Collections.singletonList(guest1));
		when(objectMapper.convertValue(eq(testData.get("facilities")), any(TypeReference.class))).thenReturn(Collections.singletonList(facility1));
		when(objectMapper.convertValue(eq(testData.get("reservations")), any(TypeReference.class))).thenReturn(Collections.singletonList(reservation1));

		when(roomService.getRoomById(1L)).thenReturn(room1);
		when(guestService.getGuestById(1L)).thenReturn(guest1);
		when(facilityService.getFacilityById(1L)).thenReturn(facility1);

		loadJsonFileService.loadJsonData();

		// Then
		verify(roomService, times(1)).createRoom(room1);
		verify(guestService, times(1)).createGuest(guest1);
		verify(facilityService, times(1)).createFacility(facility1);
		verify(reservationService, times(1)).addReservation(reservation1);
	}

	@Test
	void shouldNotLoadJsonDataWhenFileNotExistsTest() {
		// Given
		// When and Then
		assertThrows(FileNotFoundException.class, () -> loadJsonFileService.loadJsonData());
	}

	@Test
	void shouldNotLoadJsonDataWhenRoomOrGuestInReservationNotFoundTest() throws IOException {
		// Given
		Map<String, List<Object>> testData = Map.of(
				"rooms", Collections.singletonList(room1),
				"guests", Collections.singletonList(guest1),
				"facilities", Collections.singletonList(facility1),
				"reservations", Collections.singletonList(reservation1));


		// When
		when(objectMapper.readValue(any(File.class), any(TypeReference.class))).thenReturn(testData);
		when(objectMapper.convertValue(eq(testData.get("rooms")), any(TypeReference.class))).thenReturn(Collections.singletonList(room1));
		when(objectMapper.convertValue(eq(testData.get("guests")), any(TypeReference.class))).thenReturn(Collections.singletonList(guest1));
		when(objectMapper.convertValue(eq(testData.get("facilities")), any(TypeReference.class))).thenReturn(Collections.singletonList(facility1));
		when(objectMapper.convertValue(eq(testData.get("reservations")), any(TypeReference.class))).thenReturn(Collections.singletonList(reservation1));

		// Then
		assertThrows(NotFoundException.class, () -> loadJsonFileService.loadJsonData());
	}
}