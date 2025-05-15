package ui.actions.roomActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Room;
import hotel.services.hotelServices.RoomService;

import java.util.List;

@Service("Show list of all rooms")
@AllArgsConstructor
public class ShowAllRoomsAction implements RoomAction {
    private RoomService roomService;

    @Override
    public void execute() {
        List<Room> rooms = roomService.getAllSimple();
        if (rooms.isEmpty()) {
            System.out.println("There is no rooms in the hotel!");
            return;
        }
        System.out.println("List of rooms:");
        rooms.forEach((v) -> System.out.println(v.toString()));
    }
}
