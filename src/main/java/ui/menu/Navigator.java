package ui.menu;

import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class Navigator {
    private Menu currentMenu;

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void printMenu() {
        System.out.println(currentMenu.getName());
        IntStream.range(0, currentMenu.getMenuItems().size())
                .forEach(i -> System.out.println("- " + (i + 1) + ". " + currentMenu.getMenuItems().get(i).getTitle()));
    }

    public void navigate(Integer index) {
        currentMenu.getMenuItems().get(index).doAction();
        currentMenu = currentMenu.getMenuItems().get(index).getNextMenu();
    }
}
