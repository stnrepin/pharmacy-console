package utils;

import controllers.AddMedicineController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class ViewManager {
    private static final String mainCustomStyle =
            ViewManager.class.getResource("/styles/style.css").toExternalForm();

    public static void showMainView(Stage stage) {
        showView("/views/MainView.fxml", new MainWindowOpeningStrategy(stage));
    }

    public static void showAddMedicineView(Window parent, AddMedicineController c) {
        showView("/views/AddMedicineView.fxml", c, new ModalWindowOpeningStrategy(parent));
    }

    private static <T> void showView(String url, WindowOpeningStrategy openingStrategy) {
        showView(url, null, openingStrategy);
    }

    private static <T> void showView(String url, T ctl, WindowOpeningStrategy openingStrategy) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ViewManager.class.getResource(url));

        if (ctl != null) {
            fxmlLoader.setController(ctl);
        }

        Parent win;
        try {
            win = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("showView [Error] "  + e.getMessage());
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(win);
        scene.getStylesheets().add(mainCustomStyle);

        openingStrategy.open(win, scene);
    }
}

interface WindowOpeningStrategy {
    void open(Parent win, Scene scene);
}

class MainWindowOpeningStrategy implements WindowOpeningStrategy {
    private final Stage stage;

    public MainWindowOpeningStrategy(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void open(Parent win, Scene scene) {
        stage.setScene(scene);
        stage.show();
    }
}

class ModalWindowOpeningStrategy implements WindowOpeningStrategy {
    private final Window parent;

    public ModalWindowOpeningStrategy(Window parent) {
        this.parent = parent;
    }

    @Override
    public void open(Parent win, Scene scene) {
        var stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(parent);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}