import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Pharmacy Console");
        stage.setMinHeight(640);
        stage.setMinWidth(stage.getMinHeight() * 1.6);

        var mainFxml = getClass().getResource("/views/MainView.fxml");
        var styleCss = getClass().getResource("/styles/style.css").toExternalForm();

        var loader = new FXMLLoader();
        loader.setLocation(mainFxml);

        Parent root = loader.load();
        var scene = new Scene(root);
        scene.getStylesheets().add(styleCss);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
