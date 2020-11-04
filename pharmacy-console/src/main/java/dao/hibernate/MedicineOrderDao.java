package dao.hibernate;

import java.time.LocalDateTime;
import java.util.List;

import models.MedicineOrder;

public class MedicineOrderDao extends DaoCrudOperations<Integer, MedicineOrder> {
    public List<MedicineOrder> findAllMedicineOrdersInPeriod(LocalDateTime begin, LocalDateTime end) {
        return null;
    }

    public int getMedicineOrderCountInPeriod(LocalDateTime begin, LocalDateTime end) {
        return 0;
    }

    public int getOrdersTotalCost(LocalDateTime begin, LocalDateTime end) {
        return 0;
    }
}
