package services;

import models.Disease;

import java.util.List;

public interface DiseaseService {
    void remove(Disease d);
    Disease findById(int id);
    List<Disease> findAll();
    Disease findByNameOrCreate(String name);
}
