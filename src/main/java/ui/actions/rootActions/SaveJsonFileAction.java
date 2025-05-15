package ui.actions.rootActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.fileServices.SaveJsonFileService;
import ui.interfaces.Action;

@Service
@AllArgsConstructor
public class SaveJsonFileAction implements Action {
    private SaveJsonFileService saveJsonFileService;

    @Override
    public void execute() {
        System.out.println("Saving data ... ");
        saveJsonFileService.saveJsonData();
        System.out.println("Done!");
    }
}
