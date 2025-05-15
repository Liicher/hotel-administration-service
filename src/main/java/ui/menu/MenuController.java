package ui.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ui.builders.RootMenuBuilder;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class MenuController {
    private final RootMenuBuilder builder;
    private final Navigator navigator;

    public void run() {
        this.builder.buildMenu();
        this.navigator.setCurrentMenu(builder.getRootMenu());
        Scanner scanner = new Scanner(System.in);
        String index = "";

        while (!index.equals("0")) {
            int amountOfMenu = navigator.getCurrentMenu().getMenuItems().size();
            this.navigator.printMenu();
            System.out.println("Input action number:");
            index = scanner.next().trim();
            if (!index.matches("\\d+") || Integer.parseInt(index) < 0 || Integer.parseInt(index) > amountOfMenu) {
                continue;
            }
            this.navigator.navigate(Integer.parseInt(index) - 1);
        }
    }
}
