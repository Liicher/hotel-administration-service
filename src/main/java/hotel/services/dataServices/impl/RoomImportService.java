package hotel.services.dataServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Room;
import hotel.enums.RoomStatus;
import hotel.enums.RoomType;
import hotel.services.dataServices.DataUtil;
import hotel.services.dataServices.ImportService;
import hotel.services.hotelServices.RoomService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomImportService implements ImportService {
    private final DataUtil dataUtil;
    private final RoomService roomService;

    @Override
    public void importData(String filepath) {
        dataUtil.checkFileExists(filepath);
        List<String> lines = dataUtil.readAllLines(filepath);
        lines.removeFirst();
        roomService.addAll(getImportedMap(lines));
    }

    private Map<Long, Room> getImportedMap(List<String> lines) {
        Map<Long, Room> importMap = new HashMap<>();
        for (String line : lines) {
            Room room = parseLine(line);
            importMap.put(room.getId(), room);
        }
        return importMap;
    }

    private Room parseLine(String line) {
        String[] data = line.split(";");
        Room room = new Room();
        room.setId(Long.parseLong(data[0].trim()));
        room.setRoomNumber(Long.parseLong(data[1].trim()));
        room.setRoomType(RoomType.valueOf(data[2].trim()));
        room.setRoomStatus(RoomStatus.valueOf(data[3].trim()));
        return room;
    }
}
