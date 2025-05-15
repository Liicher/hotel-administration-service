package hotel.services.dataServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Room;
import hotel.services.dataServices.DataUtil;
import hotel.services.dataServices.ExportService;
import hotel.services.hotelServices.RoomService;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomExportService implements ExportService {
    private final DataUtil dataUtil;
    private final RoomService roomService;

    @Override
    public void exportData(String filepath) {
        File file = new File(filepath);
        List<Room> rooms = roomService.getAllSimple();
        String data = getDataLines(rooms);
        dataUtil.writeAllLine(file, data);
    }

    private String getDataLines(List<Room> rooms) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Id;RoomNumber;RoomType;RoomStatus\n");
        for (Room room : rooms) {
            stringBuilder.append(room.getId()).append(";");
            stringBuilder.append(room.getRoomNumber()).append(";");
            stringBuilder.append(room.getRoomType()).append(";");
            stringBuilder.append(room.getRoomStatus()).append("\n");
        }
        return String.valueOf(stringBuilder);
    }
}
