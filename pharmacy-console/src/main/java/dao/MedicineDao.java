package dao;

import java.util.List;

import models.Medicine;
import models.Disease;

public interface MedicineDao extends DaoCrudOperations<Integer, Medicine> {
    List<Medicine> findAll();
    Medicine findByName(String name);
    List<Medicine> findAllFor(Disease disease);
}
