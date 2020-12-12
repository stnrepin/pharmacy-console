package controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import models.Medicine;
import services.impl.DiseaseServiceImpl;

public class AddMedicineController extends ModalControllerBase {
    private DiseaseServiceImpl diseaseService;

    @FXML private JFXTextField nameField;
    @FXML private JFXTextField priceField;
    @FXML private JFXTextField quantityField;
    @FXML private JFXTextField diseasesField;

    public Medicine getResultMedicine() {
        // TODO: validate hasResult
        // TODO: validate fields

        var med = new Medicine(nameField.getText(),
                               Integer.parseInt(priceField.getText()),
                               Integer.parseInt(quantityField.getText()));

        for (var diseaseName : diseasesField.getText().trim().split(";++")) {
            med.addTargetDisease(diseaseService.findByNameOrCreate(diseaseName));
        }

        return med;
    }

    public void setDiseaseService(DiseaseServiceImpl diseaseService) {
        this.diseaseService = diseaseService;
    }
}
