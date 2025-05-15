package hotel.services.dataServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Guest;
import hotel.services.dataServices.DataUtil;
import hotel.services.dataServices.ExportService;
import hotel.services.hotelServices.GuestService;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestExportService implements ExportService {
    private final DataUtil dataUtil;
    private final GuestService guestService;

    @Override
    public void exportData(String filepath) {
        File file = new File(filepath);
        List<Guest> guests = guestService.getAllSimple();
        String data = getDataLines(guests);
        dataUtil.writeAllLine(file, data);
    }

    private String getDataLines(List<Guest> guests) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID;Name;Lastname\n");
        for (Guest guest : guests) {
            stringBuilder.append(guest.getId()).append(";");
            stringBuilder.append(guest.getName()).append(";");
            stringBuilder.append(guest.getLastName()).append("\n");
        }
        return String.valueOf(stringBuilder);
    }
}
