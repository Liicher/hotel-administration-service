package hotel.services.dataServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Guest;
import hotel.services.dataServices.DataUtil;
import hotel.services.dataServices.ImportService;
import hotel.services.hotelServices.GuestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GuestImportService implements ImportService {
    private final DataUtil dataUtil;
    private final GuestService guestService;

    @Override
    public void importData(String filepath) {
        dataUtil.checkFileExists(filepath);
        List<String> lines = dataUtil.readAllLines(filepath);
        guestService.addAll(getImportedMap(lines));
    }

    private Map<Long, Guest> getImportedMap(List<String> lines) {
        Map<Long, Guest> importMap = new HashMap<>();
        lines.removeFirst();
        for (String line : lines) {
            Guest guest = parseLine(line);
            importMap.put(guest.getId(), guest);
        }
        return importMap;
    }

    private Guest parseLine(String line) {
        String[] data = line.split(";");
        Guest guest = new Guest();
        guest.setId(Long.parseLong(data[0].trim()));
        guest.setName(data[1].trim());
        guest.setLastName(data[2].trim());
        return guest;
    }
}
