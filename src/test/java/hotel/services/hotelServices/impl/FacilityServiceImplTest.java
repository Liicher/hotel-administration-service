package senla.education.hotel.services.hotelServices.impl;

import hotel.services.hotelServices.impl.FacilityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.dto.facility.request.FacilityRequest;
import hotel.entities.Facility;
import hotel.exceptions.NotFoundException;
import hotel.mappers.FacilityMapper;
import hotel.repository.interfaces.FacilityRepository;
import hotel.sort.FacilitySortParams;
import senla.education.hotel.util.TestObjectUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityServiceImplTest {
	@Mock
	private FacilityRepository facilityRepository;
	@Mock
	private FacilityMapper facilityMapper;
	@InjectMocks
	private FacilityServiceImpl facilityService;

	private Facility facility1;
	private Facility facility2;

	@BeforeEach
	void setUp() {
		facility1 = TestObjectUtils.createTestFacility(1L);
		facility2 = TestObjectUtils.createTestFacility(2L);
	}

	@Test
	void shouldAddAllFacilitiesTest() {
		// Given
		Map<Long, Facility> facilities = Map.of(facility1.getId(), facility1, facility2.getId(), facility2);

		// When
		facilityService.addAll(facilities);

		// Then
		verify(facilityRepository, times(1)).saveAll(any(Collection.class));
	}

	@Test
	void shouldGetAllFacilitiesTest() {
		// Given
		FacilitySortParams params = new FacilitySortParams();
		List<Facility> expectedFacilities = List.of(facility1, facility2);

		// When
		when(facilityRepository.findAllSorted(params)).thenReturn(expectedFacilities);

		List<Facility> actual = facilityService.getAll(params);

		// Then
		assertEquals(expectedFacilities, actual);
		verify(facilityRepository, times(1)).findAllSorted(params);
	}

	@Test
	void shouldGetAllSimpleFacilitiesTest() {
		// Given
		List<Facility> expectedFacilities = List.of(facility1, facility2);

		// When
		when(facilityRepository.findAllSimpleInfo()).thenReturn(expectedFacilities);

		List<Facility> actual = facilityService.getAllSimple();

		// Then
		assertEquals(expectedFacilities, actual);
		verify(facilityRepository, times(1)).findAllSimpleInfo();
	}

	@Test
	void shouldReturnCreatedFacilityTest() {
		// Given
		// When
		when(facilityRepository.save(facility1)).thenReturn(facility1);

		Facility actual = facilityService.createFacility(facility1);

		// Then
		assertEquals(facility1, actual);
		verify(facilityRepository, times(1)).save(facility1);
	}

	@Test
	void shouldUpdateFacilityTest() {
		// Given
		// When
		facilityService.updateFacility(facility1);

		// Then
		verify(facilityRepository, times(1)).update(facility1);
	}

	@Test
	void shouldUpdateAndReturnFacilityTest() {
		// Given
		FacilityRequest request = new FacilityRequest("New Updated Facility Name", BigDecimal.valueOf(10));

		// When
		when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility1));
		when(facilityRepository.update(facility1)).thenReturn(facility1);

		Facility actual = facilityService.updateFacility(1L, request);

		// Then
		verify(facilityMapper, times(1)).updateEntity(facility1, request);
		verify(facilityRepository, times(1)).update(facility1);
		assertEquals(facility1, actual);
	}

	@Test
	void shouldNotUpdateFacilityWhenUserNotFoundTest() {
		// Given
		Long id = 3L;
		FacilityRequest request = new FacilityRequest("Exception Name", BigDecimal.ONE);

		// When
		when(facilityRepository.findById(id)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> facilityService.updateFacility(id, request));
	}

	@Test
	void shouldDeleteFacilityBySettingInactiveTest() {
		// Given
		// When
		facilityService.deleteFacility(facility1);

		// Then
		assertFalse(facility1.getIsActive());
		verify(facilityRepository, times(1)).update(facility1);
	}

	@Test
	void shouldSetFacilityPriceTest() {
		// Given
		BigDecimal newPrice = BigDecimal.valueOf(111);

		// When
		facilityService.setFacilityPrice(facility1, newPrice);

		// Then
		assertEquals(newPrice, facility1.getFacilityPrice());
	}

	@Test
	void shouldNotSetFacilityPriceWhenPriceIsNegativeTest() {
		// Given
		BigDecimal negativePrice = BigDecimal.valueOf(-1);

		// When and Then
		assertThrows(IllegalArgumentException.class, () -> facilityService.setFacilityPrice(facility1, negativePrice));
	}

	@Test
	void shouldGetFacilityByIdTest() {
		// Given
		// When
		when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility1));

		Facility actual = facilityService.getFacilityById(1L);

		// Then
		assertEquals(facility1, actual);
	}

	@Test
	void shouldNotGetFacilityByIdWhenFacilityNotExistsTest() {
		// Given
		// When
		when(facilityRepository.findById(1L)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> facilityService.getFacilityById(1L));
	}

	@Test
	void shouldGetFacilitiesSortedByPriceTest() {
		// Given
		facility2.setFacilityPrice(BigDecimal.valueOf(200));
		List<Facility> unsorted = Arrays.asList(facility2, facility1);

		// When
		when(facilityRepository.findAllSimpleInfo()).thenReturn(unsorted);

		List<Facility> actual = facilityService.getFacilitiesListSortedByPrice();

		// Then
		assertEquals(facility1, actual.get(0));
		assertEquals(facility2, actual.get(1));
	}

	@Test
	void shouldGetActiveCountFacilitiesTest() {
		// Given
		Long expectedCount = 5L;

		// When
		when(facilityRepository.countALl()).thenReturn(expectedCount);

		Long actual = facilityService.getActiveCount();

		// Then
		assertEquals(expectedCount, actual);
		verify(facilityRepository, times(1)).countALl();
	}
}