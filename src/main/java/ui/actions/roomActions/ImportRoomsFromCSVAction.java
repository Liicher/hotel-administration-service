package ui.actions.roomActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.dataServices.impl.RoomImportService;
import hotel.services.hotelServices.RoomService;
import ui.actions.ActionUtils;

@Service
@AllArgsConstructor
public class ImportRoomsFromCSVAction implements RoomAction {
    private ActionUtils actionUtils;
    private RoomImportService roomImportService;
    private RoomService roomService;

    @Override
    public void execute() {
        System.out.println("Enter filepath:");
        String filepath = actionUtils.stringInput();
        System.out.println("Importing rooms");
        roomImportService.importData(filepath);
        System.out.println("List of rooms:");
        roomService.getAllSimple().forEach((v) -> System.out.println(v.toString()));
    }
}
