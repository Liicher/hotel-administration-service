package ui.actions.reservationActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.services.hotelServices.RoomService;
import ui.actions.ActionUtils;

import java.util.List;

@Slf4j
@Service("Show reservations in specific room")
@AllArgsConstructor
public class ShowRoomReservationAction implements ReservationAction {
    private ActionUtils actionUtils;
    private RoomService roomService;

    @Override
    public void execute() {
        try {
            Long roomNumber = actionUtils.roomNumberInput();
            Room room = roomService.getRoomByNumber(roomNumber);
            List<Reservation> roomReservations = room.getReservationList();
            System.out.println("Reservations in room №" + roomNumber + ":");
            roomReservations.forEach(System.out::println);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
