package controllers;

import com.jfoenix.controls.JFXTextField;
import dao.MedicineDao;
import javafx.fxml.FXML;
import javafx.scene.control.SpinnerValueFactory;
import models.Medicine;
import services.impl.DiseaseServiceImpl;

import java.util.ArrayList;

public class AddMedicineController extends ModalControllerBase {
    private Medicine medicine;
    private DiseaseServiceImpl diseaseService;

    @FXML public JFXTextField nameField;
    @FXML public SpinnerValueFactory.IntegerSpinnerValueFactory priceSpinnerValueFactory;
    @FXML public SpinnerValueFactory.IntegerSpinnerValueFactory quantitySpinnerValueFactory;
    @FXML public JFXTextField diseasesField;

    public AddMedicineController() {
        this(new Medicine());
    }

    public AddMedicineController(Medicine initial) {
        medicine = initial;
    }

    public void initialize() {
        nameField.setText(medicine.getName());
        priceSpinnerValueFactory.setValue(medicine.getPrice());
        priceSpinnerValueFactory.setMax(Integer.MAX_VALUE);
        quantitySpinnerValueFactory.setValue(medicine.getCount());
        quantitySpinnerValueFactory.setMax(Integer.MAX_VALUE);

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
        medicine.setPrice(priceSpinnerValueFactory.getValue());
        medicine.setCount(quantitySpinnerValueFactory.getValue());

        medicine.setTargetDiseases(new ArrayList<>());
        for (var diseaseName : diseasesField.getText().trim().split(";++")) {
            diseaseName = diseaseName.trim();
            medicine.addTargetDisease(diseaseService.findByNameOrCreate(diseaseName));
        }

        return medicine;
    }

    public void setDiseaseService(DiseaseServiceImpl diseaseService) {
        this.diseaseService = diseaseService;
    }
}
