package controllers;

import javafx.stage.Stage;

public class WindowContainingControllerBase {
    private Stage window;

    public Stage getWindow() {
        return window;
    }

    public void setWindow(Stage window) {
        this.window = window;
    }
}
