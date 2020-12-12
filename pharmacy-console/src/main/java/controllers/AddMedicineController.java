package controllers;

import com.jfoenix.controls.JFXTextField;
import dao.MedicineDao;
import javafx.fxml.FXML;
import models.Medicine;
import services.impl.DiseaseServiceImpl;

import java.util.ArrayList;

public class AddMedicineController extends ModalControllerBase {
    private Medicine medicine;
    private DiseaseServiceImpl diseaseService;

    @FXML private JFXTextField nameField;
    @FXML private JFXTextField priceField;
    @FXML private JFXTextField quantityField;
    @FXML private JFXTextField diseasesField;

    public AddMedicineController() {
        this(new Medicine());
    }

    public AddMedicineController(Medicine initial) {
        medicine = initial;
    }

    public void initialize() {
        nameField.setText(medicine.getName());
        priceField.setText(String.valueOf(medicine.getPrice()));
        quantityField.setText(String.valueOf(medicine.getCount()));

        var ds = medicine.getTargetDiseases();
        var sb = new StringBuilder();
        for (var i = 0; i < ds.size(); i++) {
            if (i != 0) {
                sb.append("; ");
            }
            sb.append(ds.get(i).getName());
        }
        diseasesField.setText(sb.toString());
    }

    public Medicine getResultMedicine() {
        // TODO: validate hasResult
        // TODO: validate fields

        medicine.setName(nameField.getText());
        medicine.setPrice(Integer.parseInt(priceField.getText()));
        medicine.setCount(Integer.parseInt(quantityField.getText()));

        medicine.setTargetDiseases(new ArrayList<>());
        for (var diseaseName : diseasesField.getText().trim().split(";++")) {
            medicine.addTargetDisease(diseaseService.findByNameOrCreate(diseaseName));
        }

        return medicine;
    }

    public void setDiseaseService(DiseaseServiceImpl diseaseService) {
        this.diseaseService = diseaseService;
    }
}
