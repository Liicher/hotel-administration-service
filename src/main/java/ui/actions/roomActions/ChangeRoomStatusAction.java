package ui.actions.roomActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import hotel.enums.RoomStatus;
import hotel.services.hotelServices.RoomService;
import ui.actions.ActionUtils;

@Slf4j
@Service("Change room status")
@Order(2)
@AllArgsConstructor
public class ChangeRoomStatusAction implements RoomAction {
    private ActionUtils actionUtils;
    private RoomService roomService;

    @Override
    public void execute() {
        try {
            Long roomNumber = actionUtils.roomNumberInput();
            RoomStatus roomStatus = actionUtils.roomStatusInput();
            roomService.changeRoomStatus(roomNumber, roomStatus);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
