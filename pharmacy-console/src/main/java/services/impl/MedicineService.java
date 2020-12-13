package services.impl;

import dao.DiseaseDao;
import dao.MedicineDao;
import models.Medicine;

import java.util.ArrayList;
import java.util.List;

public class MedicineService implements services.MedicineService {
    private final MedicineDao medicineDao;
    private final DiseaseDao diseaseDao;

    public MedicineService(MedicineDao medicineDao, DiseaseDao diseaseDao) {
        this.medicineDao = medicineDao;
        this.diseaseDao = diseaseDao;
    }

    @Override
    public void add(Medicine medicine) {
        medicineDao.create(medicine);
    }

    @Override
    public void update(Medicine medicine) {
        medicineDao.update(medicine);
    }

    @Override
    public void remove(Medicine m) {
        medicineDao.delete(m);
    }

    @Override
    public List<Medicine> findAll() {
        return medicineDao.findAll();
    }

    @Override
    public Medicine findById(int name) {
        return medicineDao.read(name);
    }

    @Override
    public List<Medicine> findAllFor(String diseaseName) {
        var disease = diseaseDao.findByName(diseaseName);
        if (disease == null) {
            return new ArrayList<>();
        }
        return medicineDao.findAllFor(disease);
    }
}
