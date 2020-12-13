package services.impl;

import dao.MedicineOrderDao;
import models.MedicineOrder;

import java.time.Instant;
import java.util.List;

public class MedicineOrderService implements services.MedicineOrderService {
    private final MedicineOrderDao medicineOrderDao;

    public MedicineOrderService(MedicineOrderDao medicineOrderDao) {
        this.medicineOrderDao = medicineOrderDao;
    }

    @Override
    public void orderMedicine(int medId, int count) {
        medicineOrderDao.orderMedicine(medId, count);
    }

    @Override
    public List<MedicineOrder> findAll() {
        return medicineOrderDao.findAll();
    }

    @Override
    public List<MedicineOrder> findAllInPeriod(Instant begin, Instant end) {
        return medicineOrderDao.findAllMedicineOrdersInPeriod(begin, end);
    }

    @Override
    public int calcTotalCost() {
        return medicineOrderDao.calcTotalCost();
    }

    @Override
    public int calcTotalCostInPeriod(Instant from, Instant to) {
        return medicineOrderDao.calcTotalCost(from, to);
    }
}
