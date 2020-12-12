package services.impl;

import dao.DiseaseDao;
import models.Disease;

import java.util.List;

public class DiseaseServiceImpl {
    private final DiseaseDao diseaseDao;

    public DiseaseServiceImpl(DiseaseDao diseaseDao) {
        this.diseaseDao = diseaseDao;
    }

    public List<Disease> findAll() {
        return diseaseDao.findAll();
    }

    public Disease findByNameOrCreate(String name) {
        var d = diseaseDao.findByName(name);
        if (d != null) {
            return d;
        }
        return new Disease(name);
    }
}
