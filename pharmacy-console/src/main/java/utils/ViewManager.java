package utils;

import controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ViewManager {
    private static final String mainCustomStyle =
            ViewManager.class.getResource("/styles/style.css").toExternalForm();

    public static MainController showMainView(Stage stage) {
        return showView("/views/MainView.fxml", stage);
    }

    private static <T> T showView(String url) {
        return showView(url, null);
    }

    private static <T> T showView(String url, Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ViewManager.class.getResource(url));

        Parent win;
        try {
            win = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Error: "  + e.getMessage());
            return null;
        }

        Scene scene = new Scene(win);
        scene.getStylesheets().add(mainCustomStyle);

        if (stage == null) {
            stage = new Stage();
        }

        stage.setScene(scene);
        stage.show();

        return fxmlLoader.getController();
    }
}
