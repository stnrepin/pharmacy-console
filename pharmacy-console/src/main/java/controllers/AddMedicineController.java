package controllers;

import com.jfoenix.controls.JFXTextField;
import controllers.exceptions.IncorrectNameException;
import javafx.fxml.FXML;
import javafx.scene.control.SpinnerValueFactory;
import models.Medicine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.DiseaseService;

import java.util.ArrayList;

public class AddMedicineController extends ModalControllerBase {
    private static final Logger logger = LogManager.getLogger(AddMedicineController.class);

    private final Medicine medicine;
    private DiseaseService diseaseService;

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
        quantitySpinnerValueFactory.setValue(medicine.getCount());

        var ds = medicine.getTargetDiseases();
        var sb = new StringBuilder();
        for (var i = 0; i < ds.size(); i++) {
            if (i != 0) {
                sb.append("; ");
            }
            sb.append(ds.get(i).getName());
        }
        diseasesField.setText(sb.toString());

        logger.info("Initialized");
    }

    public Medicine getResultMedicine() throws IncorrectNameException {
        medicine.setName(nameField.getText());
        medicine.setPrice(priceSpinnerValueFactory.getValue());
        medicine.setCount(quantitySpinnerValueFactory.getValue());

        if (medicine.getName().isEmpty()) {
            throw new IncorrectNameException("medicine");
        }

        medicine.getTargetDiseases().clear();
        for (var diseaseName : diseasesField.getText().trim().split(";++")) {
            diseaseName = diseaseName.trim();
            if (diseaseName.isEmpty()) {
                continue;
            }
            medicine.addTargetDisease(diseaseService.findByNameOrCreate(diseaseName));
        }

        return medicine;
    }

    public void setDiseaseService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }
}
