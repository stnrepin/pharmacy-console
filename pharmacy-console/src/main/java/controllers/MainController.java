package controllers;

import javafx.fxml.FXML;
import dao.DiseaseDao;
import dao.MedicineDao;
import dao.MedicineOrderDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.DiseaseService;
import services.MedicineOrderService;
import services.MedicineService;

public class MainController {
    private static final Logger logger = LogManager.getLogger(MainController.class);

    @FXML MedicineController medicineController;
    @FXML DiseaseController diseasesController;
    @FXML MedicineOrderController medicineOrderController;

    private final MedicineDao medicineDao = new dao.hibernate.MedicineDao();
    private final DiseaseDao diseaseDao = new dao.hibernate.DiseaseDao();
    private final MedicineOrderDao medicineOrderDao = new dao.hibernate.MedicineOrderDao();

    private final MedicineService medicineService =
            new services.impl.MedicineService(medicineDao, diseaseDao);
    private final DiseaseService diseaseService =
            new services.impl.DiseaseService(diseaseDao);
    private final MedicineOrderService medicineOrderService =
            new services.impl.MedicineOrderService(medicineOrderDao);

    public void initialize() {
        medicineController.setMedicineService(medicineService);
        medicineController.setDiseaseService(diseaseService);
        medicineController.setMedicineOrderService(medicineOrderService);

        diseasesController.setDiseaseService(diseaseService);

        medicineOrderController.setMedicineOrderService(medicineOrderService);

        medicineController.getMedicinesUpdatedEventSource().addListener(diseasesController);
        medicineController.getOrderCreatedEventSource().addListener(medicineOrderController);

        logger.info("Initialized");
    }
}
