import utils.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Pharmacy Console");
        stage.setMinHeight(640);
        stage.setMinWidth(stage.getMinHeight() * 1.6);

        ViewManager.showMainView(stage);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
