package hotel.services.dataServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Facility;
import hotel.services.dataServices.DataUtil;
import hotel.services.dataServices.ExportService;
import hotel.services.hotelServices.FacilityService;

import java.io.File;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityExportService implements ExportService {
    private final DataUtil dataUtil;
    private final FacilityService facilityService;

    @Override
    public void exportData(String filepath) {
        File file = new File(filepath);
        List<Facility> facilities = facilityService.getAllSimple();
        String data = getDataLines(facilities);
        dataUtil.writeAllLine(file, data);
    }

    private String getDataLines(List<Facility> facilities) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID;Name;Price\n");
        for (Facility facility : facilities) {
            stringBuilder.append(facility.getId()).append(";");
            stringBuilder.append(facility.getFacilityName()).append(";");
            stringBuilder.append(facility.getFacilityPrice().setScale(2, RoundingMode.CEILING)).append("\n");
        }
        return String.valueOf(stringBuilder);
    }
}
