package dao;

import java.time.Instant;
import java.util.List;

import models.MedicineOrder;

/**
 * Представляет операции над полями таблицы лекарств
 */
public interface MedicineOrderDao extends DaoCrudOperations<Integer, MedicineOrder> {
    List<MedicineOrder> findAll();
    List<MedicineOrder> findAllInPeriod(Instant begin, Instant end);
    int calcTotalCost();
    int calcTotalCost(Instant today, Instant monthAgo);
    void orderMedicine(int medId, int count);
}
