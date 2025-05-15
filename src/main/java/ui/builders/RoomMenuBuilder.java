package ui.builders;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ui.actions.roomActions.RoomAction;
import ui.interfaces.Action;
import ui.menu.ChildBuilder;
import ui.menu.Menu;
import ui.menu.MenuItem;

import java.util.List;
import java.util.Map;

@Component("Room")
@RequiredArgsConstructor
public class RoomMenuBuilder implements ChildBuilder {
    private Menu menu;
    private RootMenuBuilder rootMenuBuilder;
    private final Map<String, RoomAction> actions;

    @Autowired
    public void setRootMenuBuilder(@Lazy RootMenuBuilder rootMenuBuilder) {
        this.rootMenuBuilder = rootMenuBuilder;
    }

    @Override
    public void buildMenu() {
        menu = new Menu("--- ROOM MENU ---");
        List<MenuItem> menuItems = createMenuItems();
        menu.setMenuItems(menuItems);
    }

    @Override
    public Menu getRootMenu() {
        return menu;
    }

    private List<MenuItem> createMenuItems() {
        return actions.entrySet().stream()
                .map(v -> createMenuItem(v.getKey(), v.getValue()))
                .toList();
    }

    private MenuItem createMenuItem(String name, Action action) {
        return new MenuItem(name, action, rootMenuBuilder.getRootMenu());
    }
}
