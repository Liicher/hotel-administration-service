package ui.actions.roomActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.dataServices.impl.RoomExportService;
import ui.actions.ActionUtils;
import ui.interfaces.Action;

@Service
@AllArgsConstructor
public class ExportRoomsToCSVAction implements Action {
    private ActionUtils actionUtils;
    private RoomExportService roomExportService;

    @Override
    public void execute() {
        System.out.println("Enter filepath:");
        String filepath = actionUtils.stringInput();
        System.out.println("Exporting rooms!");
        roomExportService.exportData(filepath);
        System.out.println("DONE!");
    }
}
