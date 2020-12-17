package utils;

import controllers.AddMedicineController;
import controllers.AddMedicineOrderController;
import controllers.WindowContainingControllerBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ViewManager {
    private static final Logger logger = LogManager.getLogger(ViewManager.class);

    private static final String mainCustomStyle =
            ViewManager.class.getResource("/styles/style.css").toExternalForm();

    public static void showMainView(Stage stage) {
        showView("/views/MainView.fxml", new MainWindowOpeningStrategy(stage));
    }

    public static void showAddMedicineView(Window parent, AddMedicineController c) {
        showView("/views/AddMedicineView.fxml", c, new ModalWindowOpeningStrategy(parent));
    }

    public static void showAddOrderView(Window parent, AddMedicineOrderController c) {
        showView("/views/AddMedicineOrderView.fxml", c, new ModalWindowOpeningStrategy(parent));
    }

    public static void showException(Exception e) {
        showError(e.toString());
    }

    public static void showError(String errorMessage) {
        var message = "An error occurred:\n" + errorMessage;
        var alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void showInfoDialog(String message, Window parent) {
        var dialog = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initOwner(parent);
        dialog.showAndWait();
    }

    public static boolean showConfirmationDialog(String message, Window parent) {
        var dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initOwner(parent);
        var res = dialog.showAndWait();
        return res.map(x -> x == ButtonType.OK)
                  .orElse(false);
    }

    private static <T> void showView(String url, WindowOpeningStrategy openingStrategy) {
        showView(url, null, openingStrategy);
    }

    private static <T> void showView(String url, T ctl, WindowOpeningStrategy openingStrategy) {
        logger.debug("Showing " + url);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ViewManager.class.getResource(url));

        if (ctl != null) {
            fxmlLoader.setController(ctl);
        }

        Parent win;
        try {
            win = fxmlLoader.load();
        } catch (IOException e) {
            logger.error("FXML loading error", e);
            showException(e);
            return;
        }

        Scene scene = new Scene(win);
        scene.getStylesheets().add(mainCustomStyle);


        // Check if ctl extends WindowContainingControllerBase
        //
        ctl = fxmlLoader.getController();
        var ctlClass = ctl.getClass();
        if (ctlClass != WindowContainingControllerBase.class &&
                WindowContainingControllerBase.class.isAssignableFrom(ctlClass))
        {
            ((WindowContainingControllerBase)ctl).setWindow(openingStrategy.getStage());
        }

        logger.info("Show view " + url);
        openingStrategy.open(win, scene);
    }
}

interface WindowOpeningStrategy {
    Stage getStage();
    void open(Parent win, Scene scene);
}

class MainWindowOpeningStrategy implements WindowOpeningStrategy {
    private final Stage stage;

    public MainWindowOpeningStrategy(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void open(Parent win, Scene scene) {
        stage.setScene(scene);
        stage.show();
    }
}

class ModalWindowOpeningStrategy implements WindowOpeningStrategy {
    private final Stage stage = new Stage();

    public ModalWindowOpeningStrategy(Window parent) {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(parent);
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void open(Parent win, Scene scene) {
        stage.setScene(scene);
        stage.showAndWait();
    }
}
