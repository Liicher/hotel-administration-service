package senla.education.hotel.services.hotelServices.impl;

import hotel.services.hotelServices.impl.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import hotel.dto.room.request.RoomRequest;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.enums.RoomStatus;
import hotel.enums.RoomType;
import hotel.exceptions.EntityAlreadyExistsException;
import hotel.exceptions.NotFoundException;
import hotel.exceptions.RoomCapacityOverflowException;
import hotel.mappers.RoomMapper;
import hotel.repository.interfaces.RoomRepository;
import hotel.sort.RoomSortParams;
import senla.education.hotel.util.TestObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static hotel.enums.RoomType.STANDARD;
import static hotel.enums.RoomType.SUPERIOR;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {
	@Mock
	private RoomRepository roomRepository;
	@Mock
	private RoomMapper roomMapper;
	@InjectMocks
	private RoomServiceImpl roomService;

	private Map<RoomType, BigDecimal> roomPrices;
	private Room room1;
	private Room room2;
	private Room room3;
	private RoomRequest testRoomRequest;

	@BeforeEach
	void setUp() {
		roomPrices = TestObjectUtils.setRoomPricesMap();
		ReflectionTestUtils.setField(roomService, "roomPrices", roomPrices);
		ReflectionTestUtils.setField(roomService, "maxAmountOfRooms", 2);
		ReflectionTestUtils.setField(roomService, "roomStatusChangeEnabled", true);
		ReflectionTestUtils.setField(roomService, "reservationHistoryLimit", 2);

		room1 = TestObjectUtils.createTestRoom(1L, 1001L);
		room2 = TestObjectUtils.createTestRoom(2L, 1002L);
		room3 = TestObjectUtils.createTestRoom(3L, 1003L);
		testRoomRequest = RoomRequest.builder().roomNumber(1001L).roomType(SUPERIOR).roomStatus(RoomStatus.OPEN).build();
	}

	@Test
	void shouldAddAllTest() {
		// Given
		Map<Long, Room> rooms = Map.of(1L, room1, 2L, room2);

		// When
		roomService.addAll(rooms);

		// Then
		verify(roomRepository, times(1)).saveAll(any(Collection.class));
	}

	@Test
	void shouldGetAllTest() {
		// Given
		RoomSortParams params = new RoomSortParams();
		List<Room> expectedRooms = Arrays.asList(room1, room2);

		// When
		when(roomRepository.findAllSorted(params)).thenReturn(expectedRooms);

		List<Room> actual = roomService.getAll(params);

		// Then
		assertEquals(expectedRooms, actual);
		verify(roomRepository, times(1)).findAllSorted(params);
	}

	@Test
	void shouldGetAllSimpleTest() {
		// Given
		List<Room> expectedRooms = Arrays.asList(room1, room2);

		// When
		when(roomRepository.findAllSimpleInfo()).thenReturn(expectedRooms);

		List<Room> actual = roomService.getAllSimple();

		// Then
		assertEquals(expectedRooms, actual);
		verify(roomRepository, times(1)).findAllSimpleInfo();
	}

	@Test
	void shouldGetRoomTypePriceTest() {
		// Given
		roomPrices.put(STANDARD, BigDecimal.valueOf(100));

		// When
		BigDecimal actual = roomService.getRoomTypePrice(STANDARD);

		// Then
		assertEquals(BigDecimal.valueOf(100), actual);
	}

	@Test
	void shouldSetNewRoomTypePriceTest() {
		// Given
		roomPrices.put(STANDARD, BigDecimal.valueOf(100));

		// When
		roomService.setNewRoomTypePrice(STANDARD, BigDecimal.valueOf(150));

		// Then
		assertEquals(BigDecimal.valueOf(150), roomPrices.get(STANDARD));
	}

	@Test
	void shouldNotSetNewRoomTypePriceWhenPriceIsNegative() {
		// Given
		roomPrices.put(STANDARD, BigDecimal.valueOf(100));

		// When and Then
		assertThrows(IllegalArgumentException.class, () -> roomService.setNewRoomTypePrice(STANDARD, BigDecimal.valueOf(-1)));
	}

	@Test
	void shouldCreateRoomTest() {
		// Given
		// When
		when(roomRepository.findAllSimpleInfo()).thenReturn(Collections.emptyList());
		when(roomRepository.getRoomByNumber(1001L)).thenReturn(Optional.empty());
		when(roomRepository.save(room1)).thenReturn(room1);

		Room actual = roomService.createRoom(room1);

		// Then
		assertEquals(room1, actual);
		verify(roomRepository, times(1)).save(room1);
	}

	@Test
	void shouldNotCreateRoomWhenRoomCapacityIsOverflowTest() {
		// Given
		List<Room> rooms = List.of(room1, room2);

		// When
		when(roomRepository.findAllSimpleInfo()).thenReturn(rooms);

		// Then
		assertThrows(RoomCapacityOverflowException.class, () -> roomService.createRoom(room1));
	}

	@Test
	void shouldNotCreateRoomWhenRoomNumberIsInvalidTest() {
		// Given
		Room invalidRoom = Room.builder().roomNumber(0L).build();

		// When and Then
		assertThrows(IllegalArgumentException.class, () -> roomService.createRoom(invalidRoom));
	}

	@Test
	void shouldNotCreateRoomWhenRoomAlreadyExistsTest() {
		// Given
		// When
		when(roomRepository.findAllSimpleInfo()).thenReturn(Collections.emptyList());
		when(roomRepository.getRoomByNumber(1001L)).thenReturn(Optional.of(room1));

		// Then
		assertThrows(EntityAlreadyExistsException.class, () -> roomService.createRoom(room1));
	}

	@Test
	void shouldUpdateTest() {
		// When
		roomService.update(room1);

		// Then
		verify(roomRepository, times(1)).update(room1);
	}

	@Test
	void shouldUpdateRoomByRequestTest() {
		// Given
		// When
		when(roomRepository.findById(1L)).thenReturn(Optional.of(room1));
		when(roomRepository.update(room1)).thenReturn(room1);

		Room actual = roomService.update(1L, testRoomRequest);

		// Then
		verify(roomMapper, times(1)).updateEntity(room1, testRoomRequest);
		assertEquals(room1, actual);
	}

	@Test
	void shouldDeleteRoomBySettingInactiveTest() {
		// Given
		// When
		when(roomRepository.findById(1L)).thenReturn(Optional.of(room1));

		roomService.deleteRoom(1L);

		// Then
		assertFalse(room1.getIsActive());
		verify(roomRepository, times(1)).update(room1);
	}

	@Test
	void shouldAddReservationTest() {
		// Given
		Reservation reservation = TestObjectUtils.createCurrentReservation(1L);
		room1.setReservationList(new ArrayList<>());

		// When
		roomService.addReservation(room1, reservation);

		// Then
		assertTrue(room1.getReservationList().contains(reservation));
		verify(roomRepository, times(1)).update(room1);
	}

	@Test
	void shouldChangeRoomStatusTest() {
		// Given
		// When
		when(roomRepository.getRoomByNumber(1001L)).thenReturn(Optional.of(room1));

		roomService.changeRoomStatus(1001L, RoomStatus.REPAIRING);

		// Then
		assertEquals(RoomStatus.REPAIRING, room1.getRoomStatus());
		verify(roomRepository, times(1)).update(room1);
	}

	@Test
	void shouldGetSortedListOfOpenRoomsTest() {
		// Given
		room1.setRoomStatus(RoomStatus.OPEN);
		room2.setRoomStatus(RoomStatus.OCCUPIED);
		room3.setRoomStatus(RoomStatus.OPEN);

		// When
		when(roomRepository.findAllSimpleInfo()).thenReturn(Arrays.asList(room1, room2, room3));

		List<Room> actual = roomService.getSortedListOfOpenRooms();

		// Then
		assertEquals(2, actual.size());
		assertEquals(room1, actual.get(0));
		assertEquals(room3, actual.get(1));
	}

	@Test
	void shouldGetListOfOccupiedRoomsTest() {
		// Given
		room1.setRoomStatus(RoomStatus.OPEN);
		room2.setRoomStatus(RoomStatus.OCCUPIED);
		room3.setRoomStatus(RoomStatus.OCCUPIED);

		// When
		when(roomRepository.findAllSimpleInfo()).thenReturn(Arrays.asList(room1, room2, room3));

		List<Room> actual = roomService.getListOfOccupiedRooms();

		// Then
		assertEquals(2, actual.size());
		assertEquals(RoomStatus.OCCUPIED, actual.get(0).getRoomStatus());
		assertEquals(RoomStatus.OCCUPIED, actual.get(1).getRoomStatus());
	}

	@Test
	void shouldGetAmountOfOpenRoomsTest() {
		// Given
		room1.setRoomStatus(RoomStatus.OPEN);
		room2.setRoomStatus(RoomStatus.OPEN);
		room3.setRoomStatus(RoomStatus.OCCUPIED);

		// When
		when(roomRepository.findAllSimpleInfo()).thenReturn(Arrays.asList(room1, room2, room3));

		Integer actual = roomService.getAmountOfOpenRooms();

		// Then
		assertEquals(2, actual);
	}

	@Test
	void shouldGetAvailableRoomsOnDateTest() {
		// Given
		LocalDate checkIn = LocalDate.now();
		LocalDate checkOut = LocalDate.now().plusDays(1);
		List<Room> expectedRooms = Arrays.asList(room1);

		// When
		when(roomRepository.findAllAvailableRoomsOnDate(checkIn, checkOut)).thenReturn(expectedRooms);

		List<Room> actual = roomService.getAvailableRoomsOnDate(checkIn, checkOut);

		// Then
		assertEquals(expectedRooms, actual);
	}

	@Test
	void shouldGetThreeLastReservationsTest() {
		// Given
		List<Reservation> reservations = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			reservations.add(new Reservation());
		}
		room1.setReservationList(reservations);

		// When
		when(roomRepository.getRoomByNumber(1001L)).thenReturn(Optional.of(room1));

		List<Reservation> actual = roomService.getThreeLastReservations(1001L);

		// Then
		assertEquals(3, actual.size());
	}

	@Test
	void shouldGetCurrentReservationTest() {
		// Given
		Reservation pastReservation = TestObjectUtils.createPastReservation(1L);
		Reservation currentReservation = TestObjectUtils.createCurrentReservation(1L);
		room1.setReservationList(Arrays.asList(pastReservation, currentReservation));

		// When
		Reservation actual = roomService.getCurrentReservation(room1);

		// Then
		assertEquals(currentReservation, actual);
	}

	@Test
	void shouldGetRoomByIdTest() {
		// Given
		when(roomRepository.findById(1L)).thenReturn(Optional.of(room1));

		// When
		Room actual = roomService.getRoomById(1L);

		// Then
		assertEquals(room1, actual);
	}

	@Test
	void shouldNotGetRoomByIdWhenRoomNotExistsTest() {
		// Given
		// When
		when(roomRepository.findById(1L)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> roomService.getRoomById(1L));
	}

	@Test
	void shouldGetRoomByNumberTest() {
		// Given
		// When
		when(roomRepository.getRoomByNumber(1001L)).thenReturn(Optional.of(room1));

		Room actual = roomService.getRoomByNumber(1001L);

		// Then
		assertEquals(room1, actual);
	}

	@Test
	void shouldNotGetRoomByNumberWhenRoomNumberIsInvalidTest() {
		// Given
		// When
		when(roomRepository.getRoomByNumber(1001L)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> roomService.getRoomByNumber(1001L));
	}

	@Test
	void getActiveCount_ShouldReturnCount() {
		// Given
		// When
		when(roomRepository.countALl()).thenReturn(5L);

		Long actual = roomService.getActiveCount();

		// Then
		assertEquals(5L, actual);
	}
}