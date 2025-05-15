package senla.education.hotel.services.hotelServices.impl;

import hotel.services.hotelServices.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.dto.reservation.request.ReservationRequest;
import hotel.entities.Facility;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.exceptions.NotFoundException;
import hotel.mappers.ReservationMapper;
import hotel.repository.interfaces.ReservationRepository;
import hotel.services.hotelServices.GuestService;
import hotel.services.hotelServices.PriceService;
import hotel.services.hotelServices.RoomService;
import hotel.sort.ReservationSortParams;
import senla.education.hotel.util.TestObjectUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {
	@Mock
	private PriceService priceService;
	@Mock
	private RoomService roomService;
	@Mock
	private GuestService guestService;
	@Mock
	private ReservationRepository reservationRepository;
	@Mock
	private ReservationMapper reservationMapper;
	@InjectMocks
	private ReservationServiceImpl reservationService;

	private Room room1;
	private Guest guest1;
	private Guest guest2;
	private Reservation reservation1;
	private Reservation reservation2;
	private Reservation reservation3;

	@BeforeEach
	void setUp() {
		room1 = TestObjectUtils.createTestRoom(1L, 1001L);
		guest1 = TestObjectUtils.createTestGuest(1L);
		guest2 = TestObjectUtils.createTestGuest(2L);
		reservation1 = TestObjectUtils.createPastReservation(1L);
		reservation2 = TestObjectUtils.createCurrentReservation(2L);
		reservation3 = TestObjectUtils.createFutureReservation(3L);
	}

	@Test
	void shouldAddAllTest() {
		// Given
		Map<Long, Reservation> reservations = Map.of(1L, reservation1, 2L, reservation2);

		// When
		reservationService.addAll(reservations);

		// Then
		verify(reservationRepository, times(1)).saveAll(any(Collection.class));
	}

	@Test
	void shouldGetAllTest() {
		// Given
		ReservationSortParams params = new ReservationSortParams();
		List<Reservation> expectedReservations = Arrays.asList(reservation1, reservation2);

		// When
		when(reservationRepository.findAllSorted(params)).thenReturn(expectedReservations);

		List<Reservation> actual = reservationService.getAll(params);

		// Then
		assertEquals(expectedReservations, actual);
		verify(reservationRepository, times(1)).findAllSorted(params);
	}

	@Test
	void shouldAddReservationTest() {
		// Given
		Reservation reservation = TestObjectUtils.createTestReservation(1L);

		// When
		reservationService.addReservation(reservation);

		// Then
		verify(reservationRepository, times(1)).save(reservation);
	}

	@Test
	void shouldCreateReservationTest() {
		// Given
		reservation1.setPrice(BigDecimal.valueOf(100));

		// When
		reservationService.createReservation(reservation1);

		// Then
		verify(reservationRepository, times(1)).save(reservation1);
		verifyNoInteractions(roomService, priceService);
	}

	@Test
	void shouldUpdateReservationTest() {
		// Given
		// When
		reservationService.updateReservation(reservation1);

		// Then
		verify(reservationRepository, times(1)).update(reservation1);
	}

	@Test
	void shouldUpdateReservationByParamsTest() {
		// Given
		Long id = 1L;
		ReservationRequest request = new ReservationRequest();

		// When
		when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation1));
		when(reservationRepository.update(reservation1)).thenReturn(reservation1);

		Reservation actual = reservationService.updateReservation(id, request);

		// Then
		verify(reservationMapper, times(1)).updateEntity(reservation1, request);
		verify(reservationRepository, times(1)).update(reservation1);
		assertEquals(reservation1, actual);
	}

	@Test
	void shouldNotUpdateReservationWhenReservationNotExistsTest() {
		// Given
		Long id = 1L;
		ReservationRequest request = new ReservationRequest();

		// When
		when(reservationRepository.findById(id)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> reservationService.updateReservation(id, request));
	}

	@Test
	void shouldRemoveReservationTest() {
		// Given
		reservation1.setRoom(room1);
		reservation1.setGuest(guest1);

		// When
		when(roomService.getRoomById(anyLong())).thenReturn(room1);
		when(guestService.getGuestById(anyLong())).thenReturn(guest1);

		reservationService.removeReservation(reservation1);

		// Then
		verify(reservationRepository, times(1)).delete(reservation1.getId());
		verify(guestService, times(1)).update(guest1);
		verify(roomService, times(1)).update(room1);
	}

	@Test
	void shouldCreateNewReservationTest() {
		// When
		reservationService.createNewReservation(reservation1, room1, guest1);

		// Then
		verify(roomService, times(1)).addReservation(room1, reservation1);
		verify(guestService, times(1)).addReservation(guest1, reservation1);
		verify(reservationRepository, times(1)).save(reservation1);
	}

	@Test
	void shouldGetReservationByIdTest() {
		// Given
		Long id = 1L;

		// When
		when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation1));

		Reservation actual = reservationService.getReservationById(id);

		// Then
		assertEquals(reservation1, actual);
	}

	@Test
	void shouldNotGetReservationByIdWhenReservationNotExistsTest() {
		// Given
		Long id = 1L;

		// When
		when(reservationRepository.findById(id)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> reservationService.getReservationById(id));
	}

	@Test
	void shouldChangeGuestInReservationTest() {
		// Given
		Long prevGuestId = 1L;
		Long newGuestId = 2L;
		reservation2.setGuest(guest1);

		// When
		when(guestService.getGuestById(prevGuestId)).thenReturn(guest1);
		when(guestService.getGuestById(newGuestId)).thenReturn(guest2);
		when(reservationRepository.findAllSorted(any())).thenReturn(List.of(reservation2));

		reservationService.changeGuestInReservation(reservation2, newGuestId);

		// Then
		verify(reservationRepository, times(1)).update(reservation2);
		verify(guestService, times(1)).update(guest2);
		verify(guestService, times(1)).update(guest1);
		assertTrue(guest2.getIsSettled());
	}

	@Test
	void shouldGetReservationByRoomNumberTest() {
		// Given
		Long roomNumber = 1001L;

		// When
		when(reservationRepository.getReservationByRoomNumber(roomNumber)).thenReturn(Optional.of(reservation1));

		Reservation actual = reservationService.getReservationByRoomNumber(roomNumber);

		// Then
		assertEquals(reservation1, actual);
	}

	@Test
	void shouldNotGetReservationByRoomNumberWhenReservationNotExistsTest() {
		// Given
		Long roomNumber = 1001L;

		// When
		when(reservationRepository.getReservationByRoomNumber(roomNumber)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> reservationService.getReservationByRoomNumber(roomNumber));
	}

	@Test
	void shouldGetListOfCurrentReservationsTest() {
		// Given
		List<Reservation> allReservations = Arrays.asList(reservation1, reservation2, reservation3);

		// When
		when(reservationRepository.findAllSorted(any())).thenReturn(allReservations);

		List<Reservation> actual = reservationService.getListOfCurrentReservations();

		// Then
		assertEquals(1, actual.size());
		assertEquals(reservation2, actual.get(0));
	}

	@Test
	void shouldGetListOfActiveReservationsTest() {
		// Given
		List<Reservation> allReservations = Arrays.asList(reservation1, reservation2, reservation3);

		// When
		when(reservationRepository.findAllSorted(any())).thenReturn(allReservations);

		List<Reservation> actual = reservationService.getListOfActiveReservations();

		// Then
		assertEquals(2, actual.size());
		assertTrue(actual.contains(reservation2));
		assertTrue(actual.contains(reservation3));
	}

	@Test
	void shouldGetTotalPriceTest() {
		// Given
		Long reservationId = 1L;
		reservation1.setPrice(BigDecimal.valueOf(100));
		Facility facility = Facility.builder().facilityName("Test").facilityPrice(BigDecimal.valueOf(25)).build();
		reservation1.setFacilityList(Set.of(facility));

		// When
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation1));

		BigDecimal actual = reservationService.getTotalPrice(reservationId);

		// Then
		BigDecimal expectedRoomPrice = BigDecimal.valueOf(100 * 2);
		BigDecimal expectedServicePrice = BigDecimal.valueOf(25);
		assertEquals(expectedRoomPrice.add(expectedServicePrice), actual);
	}

	@Test
	void shouldNotGetTotalPriceWhenReservationNotExistsTest() {
		// Given
		Long reservationId = 1L;

		// When
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> reservationService.getTotalPrice(reservationId));
	}

	@Test
	void shouldGetActiveCountTest() {
		// Given
		Long expectedCount = 5L;

		// When
		when(reservationRepository.countALl()).thenReturn(expectedCount);

		Long actual = reservationService.getActiveCount();

		// Then
		assertEquals(expectedCount, actual);
	}
}