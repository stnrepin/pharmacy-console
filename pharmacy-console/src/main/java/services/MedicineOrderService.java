package services;

import models.MedicineOrder;

import java.time.Instant;
import java.util.List;

public interface MedicineOrderService {
    void orderMedicine(int medId, int count);

    List<MedicineOrder> findAll();
    List<MedicineOrder> findAllInPeriod(Instant begin, Instant end);

    int calcTotalCost();
    int calcTotalCostInPeriod(Instant from, Instant to);
}
