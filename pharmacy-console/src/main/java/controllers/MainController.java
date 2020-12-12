package controllers;

import dao.hibernate.DiseaseDao;
import dao.hibernate.MedicineDao;
import dao.hibernate.MedicineOrderDao;
import javafx.fxml.FXML;
import services.impl.DiseaseServiceImpl;
import services.impl.MedicineOrderServiceImpl;
import services.impl.MedicineServiceImpl;

public class MainController {
    @FXML MedicineController medicineController;
    @FXML DiseasesController diseasesController;
    @FXML MedicineOrderController medicineOrderController;

    MedicineServiceImpl medicineService;
    DiseaseServiceImpl diseaseService;
    MedicineOrderServiceImpl medicineOrderService;

    public void initialize() {
        // TODO: inject
        medicineService = new MedicineServiceImpl(new MedicineDao(), new DiseaseDao());
        diseaseService = new DiseaseServiceImpl(new DiseaseDao());
        medicineOrderService = new MedicineOrderServiceImpl(new MedicineOrderDao());

        medicineController.setMedicineService(medicineService);
        medicineController.setDiseaseService(diseaseService);
    }
}
