package ui.actions.roomActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.entities.Room;
import hotel.services.hotelServices.RoomService;
import ui.actions.ActionUtils;

import java.time.LocalDate;
import java.util.List;

@Service("Show available rooms on date")
@AllArgsConstructor
public class ShowRoomsOnDateAction implements RoomAction {
    private ActionUtils actionUtils;
    private RoomService roomService;

    @Override
    public void execute() {
        LocalDate date = actionUtils.dateInput();
        List<Room> rooms = roomService.getAllSimple();
        System.out.println("On date " + date + " available rooms:");
        rooms.forEach((v) -> System.out.println(v.toString()));
    }
}
