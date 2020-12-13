package services;

import models.Disease;

import java.util.List;

public interface DiseaseService {
    List<Disease> findAll();
    Disease findByNameOrCreate(String name);
}
