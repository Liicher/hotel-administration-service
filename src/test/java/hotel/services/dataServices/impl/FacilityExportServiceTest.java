package senla.education.hotel.services.dataServices.impl;

import hotel.services.dataServices.impl.FacilityExportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.entities.Facility;
import hotel.services.dataServices.DataUtil;
import hotel.services.hotelServices.FacilityService;
import senla.education.hotel.util.TestObjectUtils;

import java.io.File;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityExportServiceTest {
	@Mock
	private FacilityService facilityService;
	@Mock
	private DataUtil dataUtil;
	@InjectMocks
	private FacilityExportService facilityExportService;

	@Test
	void shouldExportDataTest() {
		// Given
		Facility facility1 = TestObjectUtils.createTestFacility(1L);
		facility1.setFacilityName("Pool");

		String filePath = "test_export.csv";
		List<Facility> testFacilities = List.of(facility1);
		String expectedData = "ID;Name;Price\n" + "1;Pool;100.00\n";

		// When
		when(facilityService.getAllSimple()).thenReturn(testFacilities);

		facilityExportService.exportData(filePath);

		// Then
		verify(facilityService, times(1)).getAllSimple();
		verify(dataUtil, times(1)).writeAllLine(eq(new File(filePath)), eq(expectedData));
	}

	@Test
	void shouldExportDataWhenEmptyTest() {
		// Given
		String filePath = "empty_export.csv";
		String expectedData = "ID;Name;Price\n";

		// When
		when(facilityService.getAllSimple()).thenReturn(List.of());

		facilityExportService.exportData(filePath);

		// Then
		verify(dataUtil, times(1)).writeAllLine(eq(new File(filePath)), eq(expectedData));
	}
}