package dao;

import models.Disease;

public interface DiseaseDao extends DaoCrudOperations<Integer, Disease> {
    Disease findByName(String name);
}
