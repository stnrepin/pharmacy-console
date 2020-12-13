package services;

import models.Medicine;

import java.util.List;

public interface MedicineService {
    void add(Medicine medicine);
    void update(Medicine medicine);
    void remove(Medicine m);

    List<Medicine> findAll();
    Medicine findById(int name);
    List<Medicine> findAllFor(String diseaseName);
}
