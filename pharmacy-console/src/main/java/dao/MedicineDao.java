package dao;

import java.util.List;

import models.Medicine;
import models.Disease;

public interface MedicineDao extends DaoCrudOperations<Integer, Medicine> {
    List<Medicine> findAll();
    List<Medicine> findAllFor(Disease disease);
}
