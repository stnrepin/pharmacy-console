package dao;

import java.time.Instant;
import java.util.List;

import models.MedicineOrder;

public interface MedicineOrderDao extends DaoCrudOperations<Integer, MedicineOrder> {
    List<MedicineOrder> findAll();
    List<MedicineOrder> findAllMedicineOrdersInPeriod(Instant begin, Instant end);
    int getMedicineOrderCountInPeriod(Instant begin, Instant end);
    int getOrderTotalCost(Instant today, Instant monthAgo);
    void orderMedicine(int medId, int count);
}
