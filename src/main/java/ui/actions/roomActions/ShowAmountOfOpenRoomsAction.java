package ui.actions.roomActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.hotelServices.RoomService;

@Service("Show amount of OPEN rooms")
@AllArgsConstructor
public class ShowAmountOfOpenRoomsAction implements RoomAction {
    private RoomService roomService;

    @Override
    public void execute() {
        System.out.println("Amount of open rooms: " + roomService.getAmountOfOpenRooms());
    }
}
