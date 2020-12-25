package controllers;

import com.jfoenix.controls.JFXTextField;
import controllers.exceptions.IncorrectNameException;
import javafx.fxml.FXML;
import models.Disease;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Контроллер для окна "Создание/Редактирование заболевания"
 */
public class AddDiseaseController extends ModalControllerBase {
    private static final Logger logger = LogManager.getLogger(AddDiseaseController.class);

    private final Disease disease;

    @FXML
    public JFXTextField nameField;

    public AddDiseaseController() {
        this(new Disease());
    }

    public AddDiseaseController(Disease initial) {
        disease = initial;
    }

    /**
     * Инициализует контроллер, автоматически вызывается JavaFX
     */
    public void initialize() {
        nameField.setText(disease.getName());

        logger.info("Initialized");
    }

    /**
     * Возвращает новое (или отредактированное) заболевание
     * @return Заболевание
     * @throws IncorrectNameException Если имя пустое или содержит ';'
     */
    public Disease getResult() throws IncorrectNameException {
        var name = nameField.getText();
        if (name.isEmpty() || name.contains(";")) {
            throw new IncorrectNameException("disease");
        }
        disease.setName(nameField.getText());
        return disease;
    }
}
