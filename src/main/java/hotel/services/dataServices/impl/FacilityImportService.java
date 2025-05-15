package hotel.services.dataServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Facility;
import hotel.services.dataServices.DataUtil;
import hotel.services.dataServices.ImportService;
import hotel.services.hotelServices.FacilityService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FacilityImportService implements ImportService {
    private final DataUtil dataUtil;
    private final FacilityService facilityService;

    @Override
    public void importData(String filepath) {
        dataUtil.checkFileExists(filepath);
        List<String> lines = dataUtil.readAllLines(filepath);
        facilityService.addAll(getImportedMap(lines));
    }

    private Map<Long, Facility> getImportedMap(List<String> lines) {
        Map<Long, Facility> importMap = new HashMap<>();
        lines.removeFirst();
        for (String line : lines) {
            Facility facility = parseLine(line);
            importMap.put(facility.getId(), facility);
        }
        return importMap;
    }

    private Facility parseLine(String line) {
        String[] data = line.split(";");
        Facility facility = new Facility();
        facility.setId(Long.parseLong(data[0].trim()));
        facility.setFacilityName(data[1].trim());
        facility.setFacilityPrice(BigDecimal.valueOf(Double.parseDouble(data[2].trim())));
        return facility;
    }
}
