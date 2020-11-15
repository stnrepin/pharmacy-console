package services.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import models.*;
import dao.*;

public class PharmacyServiceImpl {
    private final MedicineDao medicineDao;
    private final DiseaseDao diseaseDao;
    private final MedicineOrderDao medicineOrderDao;

    public PharmacyServiceImpl() {
        this(new dao.hibernate.MedicineDao(),
             new dao.hibernate.DiseaseDao(),
             new dao.hibernate.MedicineOrderDao());
    }

    PharmacyServiceImpl(MedicineDao medicineDao, DiseaseDao diseaseDao,
                        MedicineOrderDao medicineOrderDao) {
        this.medicineDao = medicineDao;
        this.diseaseDao = diseaseDao;
        this.medicineOrderDao = medicineOrderDao;
    }

    public void  addMedicine(Medicine medicine) {
        medicineDao.create(medicine);
    }

    public Medicine findMedicineByName(String name) {
        return medicineDao.findByName(name);
    }

    public List<Medicine> getAllMedicines() {
        return medicineDao.findAll();
    }

    public List<Medicine> findAllMedicinesFor(Disease disease) {
        return medicineDao.findAllFor(disease);
    }

    public boolean containsMedicine(Medicine medicine) {
        return medicine.getCount() > 0;
    }

    public void orderMedicine(int medId, int count) {
        medicineOrderDao.orderMedicine(medId, count);
    }

    public List<MedicineOrder> getAllOrders() {
        return medicineOrderDao.findAll();
    }

    public List<MedicineOrder> findAllMedicineOrdersInPeriod(Instant begin, Instant end) {
        return medicineOrderDao.findAllMedicineOrdersInPeriod(begin, end);
    }

    public int getMedicineOrderCountInPeriod(Instant begin, Instant end) {
        return medicineOrderDao.getMedicineOrderCountInPeriod(begin, end);
    }

    public int getOrderTotalCostMonth() {
        var now = Instant.now();
        var monthAgo = now.minus(30, ChronoUnit.DAYS);
        return medicineOrderDao.getOrderTotalCost(monthAgo, now);
    }
}
