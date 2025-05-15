package ui.actions.roomActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Reservation;
import hotel.services.hotelServices.RoomService;
import ui.actions.ActionUtils;

import java.util.List;

@Slf4j
@Service("Show three last reservations on room")
@AllArgsConstructor
public class ShowThreeLastReservationsAction implements RoomAction {
    private ActionUtils actionUtils;
    private RoomService roomService;

    @Override
    public void execute() {
        try {
            Long roomNumber = actionUtils.roomNumberInput();
            List<Reservation> reservations = roomService.getThreeLastReservations(roomNumber);
            System.out.println("Last three reservations on room №" + roomNumber);
            reservations.forEach((v) -> System.out.println(v.toString()));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
