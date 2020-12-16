package services.impl;

import dao.DiseaseDao;
import dao.MedicineDao;
import models.Medicine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MedicineService implements services.MedicineService {
    private static final Logger logger = LogManager.getLogger(MedicineService.class);

    private final MedicineDao medicineDao;
    private final DiseaseDao diseaseDao;

    public MedicineService(MedicineDao medicineDao, DiseaseDao diseaseDao) {
        this.medicineDao = medicineDao;
        this.diseaseDao = diseaseDao;
    }

    @Override
    public void add(Medicine medicine) {
        logger.debug("Add '" + medicine.getName() + "'");
        medicineDao.create(medicine);
    }

    @Override
    public void update(Medicine medicine) {
        logger.debug("Update '" + medicine.getName() + "'");
        medicineDao.update(medicine);
    }

    @Override
    public void remove(Medicine m) {
        logger.debug("Remove '" + m.getName() + "'");
        medicineDao.delete(m);
    }

    @Override
    public List<Medicine> findAll() {
        logger.debug("Find all medicines");
        return medicineDao.findAll();
    }

    @Override
    public Medicine findById(int id) {
        logger.debug("Add #" + id);
        return medicineDao.read(id);
    }

    @Override
    public List<Medicine> findAllFor(String diseaseName) {
        var disease = diseaseDao.findByName(diseaseName);
        if (disease == null) {
            logger.debug("No medicine for '" + diseaseName + "'");
            return new ArrayList<>();
        }
        logger.debug("Find all medicines for '" +diseaseName + "'");
        return medicineDao.findAllFor(disease);
    }
}
