package ui.builders;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ui.actions.entitiesActions.FacilityMenuAction;
import ui.actions.entitiesActions.GuestMenuAction;
import ui.actions.entitiesActions.ReservationMenuAction;
import ui.actions.entitiesActions.RoomMenuAction;
import ui.actions.rootActions.LoadJsonFileAction;
import ui.actions.rootActions.SaveJsonFileAction;
import ui.interfaces.Action;
import ui.menu.Builder;
import ui.menu.ChildBuilder;
import ui.menu.Menu;
import ui.menu.MenuItem;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RootMenuBuilder implements Builder {
    private Menu menu;
    private final Map<String, ChildBuilder> builders;

    // Actions
    private final RoomMenuAction roomMenuAction;
    private final GuestMenuAction guestMenuAction;
    private final FacilityMenuAction facilityMenuAction;
    private final ReservationMenuAction reservationMenuAction;
    private final SaveJsonFileAction saveJsonFileAction;
    private final LoadJsonFileAction loadJsonFileAction;

    @Override
    public void buildMenu() {
        menu = new Menu("--- ROOT MENU ---");
        builders.values().forEach(ChildBuilder::buildMenu);
        List<MenuItem> menuItems = createMenuItems();
        menu.setMenuItems(menuItems);
    }

    @Override
    public Menu getRootMenu() {
        return menu;
    }

    private List<MenuItem> createMenuItems() {
        MenuItem room = createMenuItem("Room", roomMenuAction);
        MenuItem guest = createMenuItem("Guest", guestMenuAction);
        MenuItem facility = createMenuItem("Facility", facilityMenuAction);
        MenuItem reservation = createMenuItem("Reservation", reservationMenuAction);
        MenuItem saveJson = createMenuItem("Save data", saveJsonFileAction);
        MenuItem loadJson = createMenuItem("Load data", loadJsonFileAction);

        return List.of(room, guest, facility, reservation, saveJson, loadJson);
    }

    private MenuItem createMenuItem(String name, Action action) {
        if (builders.get(name) == null) {
            return new MenuItem(name, action, menu);
        }
        return new MenuItem(name, action, builders.get(name).getRootMenu());
    }
}
