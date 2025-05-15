package ui.actions.roomActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Room;
import hotel.services.hotelServices.RoomService;
import ui.actions.ActionUtils;

@Slf4j
@Service("Show info about specific room")
@AllArgsConstructor
public class ShowInfoAboutRoomAction implements RoomAction {
    private ActionUtils actionUtils;
    private RoomService roomService;

    @Override
    public void execute() {
        try {
            Long roomNumber = actionUtils.roomNumberInput();
            Room room = roomService.getRoomByNumber(roomNumber);
            System.out.println("--- Info ---");
            System.out.println(room);
            room.getReservationList().forEach(v -> System.out.println(v.toString()));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
