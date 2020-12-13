package services;

import models.Medicine;

import java.util.List;

public interface MedicineService {
    void addMedicine(Medicine medicine);
    void updateMedicine(Medicine medicine);
    void removeMedicine(Medicine m);

    List<Medicine> findAllMedicines();
    Medicine findMedicineById(int name);
    List<Medicine> findAllMedicinesFor(String diseaseName);
}
