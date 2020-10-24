package dao.hibernate;

import java.util.Date;
import java.util.List;

import models.MedicineOrder;

public class MedicineOrderDao extends DaoCrudOperations<Integer, MedicineOrder> {
    public List<MedicineOrder> findAllMedicineOrdersInPeriod(Date begin, Date end) {
        return null;
    }

    public int getMedicineOrderCountInPeriod(Date begin, Date end) {
        return 0;
    }
}
