package services.impl;

import dao.DiseaseDao;
import models.Disease;

import java.util.List;

public class DiseaseService implements services.DiseaseService {
    private final DiseaseDao diseaseDao;

    public DiseaseService(DiseaseDao diseaseDao) {
        this.diseaseDao = diseaseDao;
    }

    @Override
    public List<Disease> findAll() {
        return diseaseDao.findAll();
    }

    @Override
    public Disease findByNameOrCreate(String name) {
        var d = diseaseDao.findByName(name);
        if (d != null) {
            return d;
        }
        return new Disease(name);
    }
}
