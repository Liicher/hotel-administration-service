package senla.education.hotel.services.dataServices.impl;

import hotel.services.dataServices.impl.FacilityImportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.entities.Facility;
import hotel.exceptions.FileNotFoundException;
import hotel.services.dataServices.DataUtil;
import hotel.services.hotelServices.FacilityService;
import senla.education.hotel.util.TestObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityImportServiceTest {
	@Mock
	private DataUtil dataUtil;
	@Mock
	private FacilityService facilityService;
	@InjectMocks
	private FacilityImportService facilityImportService;

	@Test
	void shouldImportDataTest() {
		// Given
		String filepath = "facilities.csv";
		List<String> mockLines = new ArrayList<>(Arrays.asList("header", "1;TestFacilityName;100.00"));

		Facility facility1 = TestObjectUtils.createTestFacility(1L);
		Map<Long, Facility> expectedMap = Map.of(1L, facility1);

		// When
		when(dataUtil.readAllLines(filepath)).thenReturn(mockLines);

		facilityImportService.importData(filepath);

		// Then
		verify(dataUtil, times(1)).checkFileExists(filepath);
		verify(dataUtil, times(1)).readAllLines(filepath);
		verify(facilityService, times(1)).addAll(expectedMap);
	}

	@Test
	void shouldNotImportDataWhenFileNotExistsTest() {
		// Given
		String invalidPath = "facilities.csv";

		// When
		doThrow(new FileNotFoundException("File not found")).when(dataUtil).checkFileExists(invalidPath);

		// Then
		assertThrows(FileNotFoundException.class, () -> facilityImportService.importData(invalidPath));
		verify(dataUtil, times(1)).checkFileExists(invalidPath);
		verifyNoMoreInteractions(dataUtil, facilityService);
	}
}