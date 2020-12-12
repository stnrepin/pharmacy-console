package dao;

import models.Disease;

import java.util.List;

public interface DiseaseDao extends DaoCrudOperations<Integer, Disease> {
    List<Disease> findAll();
    Disease findByName(String name);
}
