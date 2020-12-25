package utils;

import controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Содержит методы для открытия новых окон различных форм и типов
 */
public class ViewManager {
    private static final Logger logger = LogManager.getLogger(ViewManager.class);

    private static final String mainCustomStyle =
            ViewManager.class.getResource("/styles/style.css").toExternalForm();

    /**
     * Открывает "Главное окно"
     * @param stage Корневой контейнер содержимого окна
     */
    public static void showMainView(Stage stage) {
        showView("/views/MainView.fxml", new MainWindowOpeningStrategy(stage));
    }

    /**
     * Открывает окно "Создание/редактирование лекарства лекарства"
     * @param parent Родительское окно
     * @param c Контроллер
     */
    public static void showAddMedicineView(Window parent, AddMedicineController c) {
        showView("/views/AddMedicineView.fxml", c, new ModalWindowOpeningStrategy(parent));
    }

    /**
     * Открывает окно "Создание/редактирование заболевания"
     * @param parent Родительское окно
     * @param c Контроллер
     * @return Контроллер
     */
    public static AddDiseaseController showAddDiseaseView(Window parent, AddDiseaseController c) {
        return showView("/views/AddDiseaseView.fxml", c, new ModalWindowOpeningStrategy(parent));
    }

    /**
     * Открывает окно "Создание заказа"
     * @param parent Родительское окно
     * @param c Контроллер
     */
    public static void showAddOrderView(Window parent, AddMedicineOrderController c) {
        showView("/views/AddMedicineOrderView.fxml", c, new ModalWindowOpeningStrategy(parent));
    }

    /**
     * Открывает окно, отображающее вечный прогресс
     * @param parent Родительское окно
     * @return Контроллер
     */
    public static SpinnerController showSpinner(Window parent) {
        return showView("/views/SpinnerView.fxml",
                new SpinnerController(),
                new TopTransparentModalWindowOpeningStrategy(parent));
    }

    /**
     * Открывает диалоговое окно с информацией об исключении
     * @param e Объект исключения
     */
    public static void showException(Exception e) {
        showError(e.toString(), null);
    }

    /**
     * Открывает диалоговое окно с информацией об ошибки
     * @param errorMessage Текстовое сообщение об ошибки
     * @param parent Родительское окно
     */
    public static void showError(String errorMessage, Window parent) {
        var message = "An error occurred:\n" + errorMessage;
        var alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        if (parent != null) {
            alert.initOwner(parent);
        }
        alert.showAndWait();
    }

    /**
     * Открывает диалоговое окно с некоторой информацией.
     * Окно содержит только кнопку OK
     * @param message Информационное сообщение
     * @param parent Родительское окно
     */
    public static void showInfoDialog(String message, Window parent) {
        var dialog = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initOwner(parent);
        dialog.showAndWait();
    }

    /**
     * Открывает диалоговое окно с требованием подтверждения действия.
     * Содержит кнопки OK и Cancel
     * @param message Отображаемое сообщение
     * @param parent Родительское сообщение
     * @return {@code true}, если нажато OK, иначе -- {@code false}
     */
    public static boolean showConfirmationDialog(String message, Window parent) {
        var dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initOwner(parent);
        var res = dialog.showAndWait();
        return res.map(x -> x == ButtonType.OK)
                  .orElse(false);
    }

    /**
     * Открывает окно для выбора места сохранения файла
     * @param parent Родительское окно
     * @param ext_long Длинное описание расширения файла
     * @param ext Короткое описание расширения файла
     * @return Объект типа {@code File}, соответствующий выбранному месту
     */
    public static File showSaveDialog(Window parent, String ext_long, String ext) {
        var fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(ext_long, ext);
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showSaveDialog(parent);
    }

    /**
     * Общий метод для открытия окон
     * @param url Путь до fxml-файла представления
     * @param openingStrategy Стратегия открытия окна
     * @param <T> Тип контроллера
     * @return Контроллер
     */
    private static <T> T showView(String url, WindowOpeningStrategy openingStrategy) {
        return showView(url, null, openingStrategy);
    }

    /**
     * Общий метод для открытия окок
     * @param url Путь до fxml-файла представления
     * @param ctl Контроллер
     * @param openingStrategy Стратегия открытия окна
     * @param <T> Тип контроллера
     * @return Контроллер
     */
    private static <T> T showView(String url, T ctl, WindowOpeningStrategy openingStrategy) {
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
            return null;
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
        return ctl;
    }
}

/**
 * Интерфейс для стратегий открытия окна
 */
interface WindowOpeningStrategy {
    /**
     * Возвращает сцену, соответствующую окну
     * @return Корневой контейнер окна
     */
    Stage getStage();

    /**
     * Открывает окно
     * @param win Родительское окно
     * @param scene Сцена, в которой необходимо отобразить содержимое окна
     */
    void open(Parent win, Scene scene);
}

/**
 * Стратегия, открывающая представление как главное окно приложения
 */
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

/**
 * Стратегия, открывающая окно как модальное
 */
class ModalWindowOpeningStrategy implements WindowOpeningStrategy {
    protected final Stage stage = new Stage();

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

/**
 * Стратегия, открывающая окно как прозрачное и модальное
 */
class TopTransparentModalWindowOpeningStrategy extends ModalWindowOpeningStrategy {

    public TopTransparentModalWindowOpeningStrategy(Window parent) {
        super(parent);
    }

    @Override
    public void open(Parent win, Scene scene) {
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
}