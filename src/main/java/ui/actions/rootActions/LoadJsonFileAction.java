package ui.actions.rootActions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.services.fileServices.LoadJsonFileService;
import ui.interfaces.Action;

@Service
@AllArgsConstructor
public class LoadJsonFileAction implements Action {
    private LoadJsonFileService loadJsonFileService;

    @Override
    public void execute() {
        System.out.println("Loading ...");
        loadJsonFileService.loadJsonData();
        System.out.println("Done!");
    }
}
