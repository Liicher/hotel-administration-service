package senla.education.hotel.services.hotelServices.impl;

import hotel.services.hotelServices.impl.GuestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.dto.guest.request.GuestRequest;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.exceptions.NotFoundException;
import hotel.mappers.GuestMapper;
import hotel.repository.interfaces.GuestRepository;
import hotel.sort.GuestSortParams;
import senla.education.hotel.util.TestObjectUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {
	@Mock
	private GuestRepository guestRepository;
	@Mock
	private GuestMapper guestMapper;
	@InjectMocks
	private GuestServiceImpl guestService;

	private Guest guest1;
	private Guest guest2;
	private Guest guest3;

	@BeforeEach
	void setUp() {
		guest1 = TestObjectUtils.createTestGuest(1L);
		guest2 = TestObjectUtils.createTestGuest(2L);
		guest3 = TestObjectUtils.createTestGuest(3L);
	}

	@Test
	void shouldAddAllGuestsTest() {
		// Given
		Map<Long, Guest> guests = Map.of(1L, guest1, 2L, guest2);

		// When
		guestService.addAll(guests);

		// Then
		verify(guestRepository, times(1)).saveAll(any(Collection.class));
	}

	@Test
	void shouldGetAllGuestsTest() {
		// Given
		GuestSortParams params = new GuestSortParams();
		List<Guest> expectedGuests = Arrays.asList(guest1, guest2);

		// When
		when(guestRepository.findAllSorted(params)).thenReturn(expectedGuests);

		List<Guest> actual = guestService.getAll(params);

		// Then
		assertEquals(expectedGuests, actual);
		verify(guestRepository, times(1)).findAllSorted(params);
	}

	@Test
	void shouldGetAllSimpleGuestsTest() {
		// Given
		List<Guest> expectedGuests = Arrays.asList(guest1, guest2);

		// When
		when(guestRepository.findAllSimpleInfo()).thenReturn(expectedGuests);

		List<Guest> actual = guestService.getAllSimple();

		// Then
		assertEquals(expectedGuests, actual);
		verify(guestRepository, times(1)).findAllSimpleInfo();
	}

	@Test
	void shouldCreateGuestWithParamsTest() {
		// Given
		String name = "John";
		String lastName = "Doe";

		// When
		guestService.createGuest(name, lastName);

		// Then
		verify(guestRepository, times(1)).save(argThat(guest -> guest.getName().equals(name) && guest.getLastName().equals(lastName)));
	}

	@Test
	void shouldCreateGuestTest() {
		// Given
		guest1.setName("John");
		guest1.setLastName("Doe");

		// When
		when(guestRepository.save(guest1)).thenReturn(guest1);

		Guest actual = guestService.createGuest(guest1);

		// Then
		assertEquals(guest1, actual);
		verify(guestRepository, times(1)).save(guest1);
	}

	@Test
	void shouldUpdateGuestWithParamsTest() {
		// Given
		GuestRequest request = GuestRequest.builder().name("Name").lastName("LastName").build();

		// When
		when(guestRepository.findById(1L)).thenReturn(Optional.of(guest1));
		when(guestRepository.update(guest1)).thenReturn(guest1);

		Guest actual = guestService.update(1L, request);

		// Then
		verify(guestMapper, times(1)).updateEntity(guest1, request);
		verify(guestRepository, times(1)).update(guest1);
		assertEquals(guest1, actual);
	}

	@Test
	void shouldNotUpdateGuestWhenGuestNotExistsTest() {
		// Given
		Long id = 1L;
		GuestRequest request = new GuestRequest();

		// When
		when(guestRepository.findById(id)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> guestService.update(id, request));
		verify(guestRepository, times(1)).findById(id);
		verifyNoInteractions(guestMapper);
	}

	@Test
	void shouldDeleteGuestBySettingInactiveTest() {
		// Given
		Long id = 1L;

		// When
		when(guestRepository.findById(id)).thenReturn(Optional.of(guest1));

		guestService.deleteGuest(id);

		// Then
		assertFalse(guest1.getIsActive());
		verify(guestRepository, times(1)).update(guest1);
	}

	@Test
	void shouldAddReservationTest() {
		// Given
		guest1.setIsSettled(false);
		Reservation reservation = TestObjectUtils.createCurrentReservation(1L);

		// When
		guestService.addReservation(guest1, reservation);

		// Then
		assertTrue(guest1.getIsSettled());
		assertTrue(guest1.getReservationList().contains(reservation));
		verify(guestRepository, times(1)).update(guest1);
	}

	@Test
	void shouldGetGuestByIdTest() {
		// Given
		Long id = 1L;

		// When
		when(guestRepository.findById(id)).thenReturn(Optional.of(guest1));

		Guest actual = guestService.getGuestById(id);

		// Then
		assertEquals(guest1, actual);
	}

	@Test
	void shouldNotGetGuestByIdWhenGuestNotExistsTest() {
		// Given
		Long id = 1L;

		// When
		when(guestRepository.findById(id)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> guestService.getGuestById(id));
	}

	@Test
	void shouldGetSortedListOfGuestsTest() {
		// Given
		guest1.setName("AGuest");
		guest2.setName("BGuest");
		List<Guest> unsorted = Arrays.asList(guest2, guest1);
		List<Guest> expected = Arrays.asList(guest1, guest2);

		// When
		when(guestRepository.findAllSimpleInfo()).thenReturn(unsorted);

		List<Guest> actual = guestService.getSortedListOfAllGuests();

		// Then
		assertEquals(expected, actual);
	}

	@Test
	void shouldGetSortedListOfSettledGuestsTest() {
		// Given
		guest1.setName("BGuest");
		guest3.setName("AGuest");
		guest1.setIsSettled(true);
		guest2.setIsSettled(false);
		guest3.setIsSettled(true);

		List<Guest> allGuests = Arrays.asList(guest3, guest2, guest1);
		List<Guest> expected = Arrays.asList(guest3, guest1);

		// When
		when(guestRepository.findAllSimpleInfo()).thenReturn(allGuests);

		List<Guest> actual = guestService.getSortedListOfSettledGuests();

		// Then
		assertEquals(expected, actual);
	}

	@Test
	void shouldGetAmountOfSettledGuestsTest() {
		// Given
		guest1.setIsSettled(true);
		guest2.setIsSettled(false);
		guest3.setIsSettled(true);

		List<Guest> allGuests = Arrays.asList(guest1, guest2, guest3);

		// When
		when(guestRepository.findAllSimpleInfo()).thenReturn(allGuests);

		Integer actual = guestService.getAmountOfSettledGuests();

		// Then
		assertEquals(2, actual);
	}

	@Test
	void shouldGetCurrentReservationTest() {
		// Given
		guest1.setIsSettled(true);
		Reservation reservation1 = TestObjectUtils.createPastReservation(1L);
		Reservation reservation2 = TestObjectUtils.createCurrentReservation(2L);
		guest1.setReservationList(Arrays.asList(reservation1, reservation2));

		// When
		Reservation actual = guestService.getCurrentReservation(guest1);

		// Then
		assertEquals(reservation2, actual);
	}

	@Test
	void shouldNotGetCurrentReservationWhenGuestIsNotSettledTest() {
		// Given
		guest1.setIsSettled(false);

		// When and Then
		assertThrows(NotFoundException.class, () -> guestService.getCurrentReservation(guest1));
	}

	@Test
	void shouldUpdateGuestTest() {
		// Given
		// When
		guestService.update(guest1);

		// Then
		verify(guestRepository, times(1)).update(guest1);
	}

	@Test
	void shouldGetActiveCountGuestsTest() {
		// Given
		Long expectedCount = 5L;

		// When
		when(guestRepository.countALl()).thenReturn(expectedCount);

		Long actual = guestService.getActiveCount();

		// Then
		assertEquals(expectedCount, actual);
		verify(guestRepository, times(1)).countALl();
	}
}