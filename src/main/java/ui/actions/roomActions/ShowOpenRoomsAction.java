package ui.actions.roomActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Room;
import hotel.services.hotelServices.RoomService;

import java.util.List;

@Service("Show available rooms")
@AllArgsConstructor
public class ShowOpenRoomsAction implements RoomAction {
    private RoomService roomService;

    @Override
    public void execute() {
        List<Room> rooms = roomService.getListOfOpenRooms();
        if (rooms.isEmpty()) {
            System.out.println("There is no open rooms in the hotel!");
            return;
        }
        System.out.println("List of open rooms:");
        rooms.forEach((v) -> System.out.println(v.toString()));
    }
}
