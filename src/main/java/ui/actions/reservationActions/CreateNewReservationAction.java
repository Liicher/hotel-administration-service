package ui.actions.reservationActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.services.hotelServices.ReservationService;
import hotel.services.hotelServices.RoomService;
import ui.actions.ActionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service("Create new reservation")
@AllArgsConstructor
public class CreateNewReservationAction implements ReservationAction {
    private ActionUtils actionUtils;
    private RoomService roomService;
    private ReservationService reservationService;

    @Override
    public void execute() {
        try {
            System.out.println("Enter check in date (dd.mm.yyyy):");
            LocalDate checkInDate = actionUtils.dateInput();
            System.out.println("Enter check out date (dd.mm.yyyy):");
            LocalDate checkOutDate = actionUtils.checkOutDateInput(checkInDate);
            Room room = setRoom(checkInDate, checkOutDate);
            BigDecimal price = roomService.getRoomTypePrice(room.getRoomType());
            Guest guest = actionUtils.getGuestForReservation();
            Reservation reservation = new Reservation();
            reservation.setCheckIn(checkInDate);
            reservation.setCheckOut(checkOutDate);
            reservation.setPrice(price);
            reservationService.createNewReservation(reservation, room, guest);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private Room setRoom(LocalDate checkIn, LocalDate checkOut) {
        List<Room> rooms = roomService.getAvailableRoomsOnDate(checkIn, checkOut);
        Long roomNumber = actionUtils.roomNumberInput(rooms);
        return roomService.getRoomByNumber(roomNumber);
    }
}
