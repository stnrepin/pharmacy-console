package services.impl;

import java.util.Date;
import java.util.List;

import models.*;
import dao.hibernate.*;

public class PharmacyServiceImpl {
    private MedicineDao medicineDao;
    private DiseaseDao diseaseDao;
    private MedicineOrder medicineOrderDao;

    public List<Medicine> getAllMedicines() {
        return null;
    }

    public List<MedicineOrder> getAllOrders() {
        return null;
    }

    public void createOrder(Medicine medicine, int count) {

    }

    public List<Medicine> findAllMedicinesFor(Disease disease) {
        return null;
    }

    public boolean containsMedicine(Medicine medicine) {
        return false;
    }

    public int getMedicineAvailableCount(Medicine medicine) {
        return 0;
    }

    public List<MedicineOrder> findAllMedicineOrdersInPeriod(Date begin, Date end) {
        return null;
    }

    public int getMedicineOrderCountInPeriod(Date begin, Date end) {
        return 0;
    }

    public int getTotalMonthOrdersCost() {
        return 0;
    }
}
