import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PersistenceEntityManagerUtils;
import utils.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Точка входа в приложение
 */
public class Main extends javafx.application.Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    /**
     * Инициализирует приложение и запускает главное окно
     * @param stage Корневой контейнер для содержимого окна
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Pharmacy Console");
        stage.setMinHeight(640);
        stage.setMinWidth(stage.getMinHeight() * 1.6);

        if (!PersistenceEntityManagerUtils.tryInitializeEntityManager()) {
            logger.fatal("Init failed: Database is not connected");
            ViewManager.showError("Database is not available", null);
            return;
        }

        try {
            ViewManager.showMainView(stage);
        } catch (Exception e) {
            logger.fatal(e);
            ViewManager.showException(e);
        }
    }

    /**
     * Точка входа в приложение
     * @param args Аргументы командной строки
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
