package hotel.services.dataServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Reservation;
import hotel.services.dataServices.DataUtil;
import hotel.services.dataServices.ImportService;
import hotel.services.hotelServices.GuestService;
import hotel.services.hotelServices.ReservationService;
import hotel.services.hotelServices.RoomService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationImportService implements ImportService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DataUtil dataUtil;
    private final ReservationService reservationService;
    private final GuestService guestService;
    private final RoomService roomService;

    @Override
    public void importData(String filepath) {
        dataUtil.checkFileExists(filepath);
        List<String> lines = dataUtil.readAllLines(filepath);
        Map<Long, Reservation> reservationMap = getImportedMap(lines);
        reservationService.addAll(reservationMap);
    }

    private Map<Long, Reservation> getImportedMap(List<String> lines) {
        Map<Long, Reservation> importMap = new HashMap<>();
        lines.removeFirst();
        for (String line : lines) {
            Reservation reservation = parseLine(line);
            importMap.put(reservation.getId(), reservation);
        }
        return importMap;
    }

    private Reservation parseLine(String line) {
        String[] data = line.split(";");
        Reservation reservation = new Reservation();
        reservation.setId(Long.parseLong(data[0].trim()));
        reservation.setRoom(roomService.getRoomById(Long.parseLong(data[1].trim())));
        reservation.setGuest(guestService.getGuestById(Long.parseLong(data[2].trim())));
        reservation.setCheckIn(LocalDate.parse(data[3].trim(), formatter));
        reservation.setCheckOut(LocalDate.parse(data[4].trim(), formatter));
        reservation.setPrice(BigDecimal.valueOf(Double.parseDouble(data[5].trim())));
        return reservation;
    }
}
