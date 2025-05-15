package hotel.services.dataServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Reservation;
import hotel.services.dataServices.DataUtil;
import hotel.services.dataServices.ExportService;
import hotel.services.hotelServices.ReservationService;
import hotel.sort.ReservationSortParams;

import java.io.File;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationExportService implements ExportService {
    private final DataUtil dataUtil;
    private final ReservationService reservationService;

    @Override
    public void exportData(String filepath) {
        File file = new File(filepath);
        List<Reservation> reservations = reservationService.getAll(new ReservationSortParams());
        String data = getDataLines(reservations);
        dataUtil.writeAllLine(file, data);
    }

    private String getDataLines(List<Reservation> reservations) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Id;RoomNumber;CheckInDate;CheckOutDate;Price;GuestId;Facilities\n");
        for (Reservation reservation : reservations) {
            stringBuilder.append(reservation.getId()).append(";");
            stringBuilder.append(reservation.getRoom().getId()).append(";");
            stringBuilder.append(reservation.getCheckIn()).append(";");
            stringBuilder.append(reservation.getCheckOut()).append(";");
            stringBuilder.append(reservation.getPrice().setScale(2, RoundingMode.CEILING)).append(";");
            stringBuilder.append(reservation.getGuest().getId());
        }
        return String.valueOf(stringBuilder);
    }
}
