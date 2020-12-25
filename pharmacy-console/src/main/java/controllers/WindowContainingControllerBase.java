package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Базовый класс для контроллеров, обращающихся к своему окну
 */
public class WindowContainingControllerBase {
    private Stage window;

    public Stage getWindow() {
        return window;
    }

    public void setWindow(Stage window) {
        this.window = window;
    }

    /**
     * Возвращает текущее окно на основании информации,
     * хранящейся в событии
     * @param event Событие
     * @return Окно, соответствующее событию
     */
    public Window getWindowFromEvent(ActionEvent event) {
        return ((Node)event.getSource()).getScene().getWindow();
    }
}
