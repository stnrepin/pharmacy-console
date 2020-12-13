import utils.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Pharmacy Console");
        stage.setMinHeight(640);
        stage.setMinWidth(stage.getMinHeight() * 1.6);

        try {
            ViewManager.showMainView(stage);
        } catch (Exception e) {
            System.out.println("Unhandled exception: " + e.toString());
            e.printStackTrace();
            ViewManager.showException(e);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
