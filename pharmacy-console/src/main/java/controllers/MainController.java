package controllers;

import dao.hibernate.MedicineDao;
import dao.hibernate.MedicineOrderDao;
import javafx.fxml.FXML;
import services.impl.MedicineOrderServiceImpl;
import services.impl.MedicineServiceImpl;

public class MainController {
    @FXML MedicineController medicineController;
    @FXML DiseasesController diseasesController;
    @FXML MedicineOrderController medicineOrderController;

    MedicineServiceImpl medicineService;
    MedicineOrderServiceImpl medicineOrderService;

    public void initialize() {
        // TODO: inject
        medicineService = new MedicineServiceImpl(new MedicineDao());
        medicineOrderService = new MedicineOrderServiceImpl(new MedicineOrderDao());

        medicineController.setMedicineService(medicineService);
    }
}
