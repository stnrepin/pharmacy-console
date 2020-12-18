package services;

import models.Disease;

import java.util.List;

public interface DiseaseService {
    void add(Disease d);
    void update(Disease d);
    void remove(Disease d);
    Disease findById(int id);
    List<Disease> findAll();
    Disease findByNameOrCreate(String name);
}
