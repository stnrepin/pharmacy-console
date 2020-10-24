package dao.hibernate;

import java.util.List;

import models.Disease;
import models.Medicine;

public class MedicineDao extends DaoCrudOperations<Integer, Medicine>  {
    public List<Medicine> findAllMedicinesFor(Disease disease) {
        return null;
    }

    public boolean containsMedicine(Medicine medicine) {
        return false;
    }

    public int getMedicineCount(Medicine medicine) {
        return 0;
    }
}
