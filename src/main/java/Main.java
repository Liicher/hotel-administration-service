import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ui.menu.MenuController;

@Configuration
@ComponentScan({"hotel", "ui"})
@PropertySource("classpath:application.properties")
public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
		MenuController controller = context.getBean(MenuController.class);
		controller.run();
	}
}
