package controllers;

import dao.DiseaseDao;
import dao.MedicineDao;
import dao.MedicineOrderDao;
import javafx.fxml.FXML;
import services.impl.DiseaseServiceImpl;
import services.impl.MedicineOrderServiceImpl;
import services.impl.MedicineServiceImpl;

public class MainController {
    @FXML MedicineController medicineController;
    @FXML DiseaseController diseasesController;
    @FXML MedicineOrderController medicineOrderController;

    private final MedicineDao medicineDao = new dao.hibernate.MedicineDao();
    private final DiseaseDao diseaseDao = new dao.hibernate.DiseaseDao();
    private final MedicineOrderDao medicineOrderDao = new dao.hibernate.MedicineOrderDao();

    private final MedicineServiceImpl medicineService =
            new MedicineServiceImpl(medicineDao, diseaseDao);
    private final DiseaseServiceImpl diseaseService =
            new DiseaseServiceImpl(diseaseDao);
    private final MedicineOrderServiceImpl medicineOrderService =
            new MedicineOrderServiceImpl(medicineDao, medicineOrderDao);

    public void initialize() {
        // TODO: inject
        medicineController.setMedicineService(medicineService);
        medicineController.setDiseaseService(diseaseService);
        medicineController.setMedicineOrderService(medicineOrderService);

        diseasesController.setDiseaseService(diseaseService);
    }
}
