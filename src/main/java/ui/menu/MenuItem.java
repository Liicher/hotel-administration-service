package ui.menu;

import ui.interfaces.Action;

public class MenuItem {
    private final String title;
    private final Action action;
    private final Menu nextMenu;

    public MenuItem() {
        this.title = "title";
        this.action = null;
        this.nextMenu = null;
    }

    public MenuItem(String title, Action action, Menu nextMenu) {
        this.title = title;
        this.action = action;
        this.nextMenu = nextMenu;
    }

    public String getTitle() {
        return title;
    }

    public Action getAction() {
        return action;
    }

    public Menu getNextMenu() {
        return nextMenu;
    }

    public void doAction() {
        action.execute();
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "title='" + title + '\'' +
                '}';
    }
}
