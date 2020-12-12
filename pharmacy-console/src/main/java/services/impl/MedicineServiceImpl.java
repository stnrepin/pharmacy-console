package services.impl;

import dao.DiseaseDao;
import dao.MedicineDao;
import models.Medicine;

import java.util.ArrayList;
import java.util.List;

public class MedicineServiceImpl {
    private final MedicineDao medicineDao;
    private final DiseaseDao diseaseDao;

    public MedicineServiceImpl(MedicineDao medicineDao, DiseaseDao diseaseDao) {
        this.medicineDao = medicineDao;
        this.diseaseDao = diseaseDao;
    }

    public void addMedicine(Medicine medicine) {
        medicineDao.create(medicine);
    }

    public List<Medicine> findAllMedicines() {
        return medicineDao.findAll();
    }

    public Medicine findMedicineById(int name) {
        return medicineDao.read(name);
    }

    public List<Medicine> findAllMedicinesFor(String diseaseName) {
        var disease = diseaseDao.findByName(diseaseName);
        if (disease == null) {
            return new ArrayList<>();
        }
        return medicineDao.findAllFor(disease);
    }

    public void removeMedicine(Medicine m) {
        medicineDao.delete(m);
    }

    public boolean containsMedicine(Medicine medicine) {
        return medicine.getCount() > 0;
    }
}
