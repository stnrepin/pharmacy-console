package controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import models.Disease;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public void initialize() {
        nameField.setText(disease.getName());

        logger.info("Initialized");
    }

    public Disease getResult() {
        disease.setName(nameField.getText());
        return disease;
    }
}
