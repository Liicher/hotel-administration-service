package ui.actions.roomActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import hotel.entities.Room;
import hotel.enums.RoomStatus;
import hotel.enums.RoomType;
import hotel.services.hotelServices.RoomService;
import ui.actions.ActionUtils;

@Slf4j
@Service("Create new room")
@Order(1)
@AllArgsConstructor
public class AddRoomAction implements RoomAction {
    private ActionUtils actionUtils;
    private RoomService roomService;

    @Override
    public void execute() {
        try {
            Long roomNumber = actionUtils.newRoomNumberInput();
            RoomType roomType = actionUtils.roomTypeInput();
            Room room = new Room();
            room.setRoomNumber(roomNumber);
            room.setRoomType(roomType);
            room.setRoomStatus(RoomStatus.OPEN);
            roomService.createRoom(room);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
