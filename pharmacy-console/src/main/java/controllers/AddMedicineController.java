package controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Disease;
import models.Medicine;

public class AddMedicineController {
    private boolean hasResult = false;

    @FXML private JFXTextField nameField;
    @FXML private JFXTextField priceField;
    @FXML private JFXTextField quantityField;
    @FXML private JFXTextField diseasesField;

    public void ok() {
        hasResult = true;
        close();
    }

    public void cancel() {
        hasResult = false;
        close();
    }

    public boolean hasResult() {
        return hasResult;
    }

    public Medicine getResultMedicine() {
        // TODO: validate hasResult
        // TODO: validate fields

        var med = new Medicine(nameField.getText(),
                               Integer.parseInt(priceField.getText()),
                               Integer.parseInt(quantityField.getText()));

        for (var diseaseName : diseasesField.getText().trim().split(";++")) {
            med.addTargetDisease(new Disease(diseaseName));
        }

        return med;
    }

    private void close() {
        Stage stage = (Stage)nameField.getScene().getWindow();
        stage.close();
    }
}
