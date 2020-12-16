package services.impl;

import dao.DiseaseDao;
import models.Disease;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DiseaseService implements services.DiseaseService {
    private static final Logger logger = LogManager.getLogger(DiseaseService.class);

    private final DiseaseDao diseaseDao;

    public DiseaseService(DiseaseDao diseaseDao) {
        this.diseaseDao = diseaseDao;
    }

    @Override
    public void remove(Disease d) {
        logger.debug("Remove disease '" + d.getName() + "'");
        diseaseDao.delete(d);
    }

    @Override
    public Disease findById(int id) {
        logger.debug("Find disease #" + id);
        return diseaseDao.read(id);
    }

    @Override
    public List<Disease> findAll() {
        logger.debug("Find all diseases");
        return diseaseDao.findAll();
    }

    @Override
    public Disease findByNameOrCreate(String name) {
        var d = diseaseDao.findByName(name);
        if (d != null) {
            logger.debug("Disease '" + name + "' found");
            return d;
        }
        logger.debug("Disease '" + name + "' created");
        return new Disease(name);
    }
}
