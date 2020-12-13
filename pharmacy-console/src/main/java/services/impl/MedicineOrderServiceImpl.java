package services.impl;

import dao.MedicineDao;
import dao.MedicineOrderDao;
import models.Medicine;
import models.MedicineOrder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MedicineOrderServiceImpl {
    private final MedicineDao medicineDao;
    private final MedicineOrderDao medicineOrderDao;

    public MedicineOrderServiceImpl(MedicineDao medicineDao, MedicineOrderDao medicineOrderDao) {
        this.medicineDao = medicineDao;
        this.medicineOrderDao = medicineOrderDao;
    }

    public void orderMedicine(int medId, int count) {
        medicineOrderDao.orderMedicine(medId, count);
    }

    public List<MedicineOrder> findAll() {
        return medicineOrderDao.findAll();
    }

    public List<MedicineOrder> findAllMedicineOrdersInPeriod(Instant begin, Instant end) {
        return medicineOrderDao.findAllMedicineOrdersInPeriod(begin, end);
    }

    public int getMedicineOrderCountInPeriod(Instant begin, Instant end) {
        return medicineOrderDao.getMedicineOrderCountInPeriod(begin, end);
    }

    public int getOrderTotalCostMonth() {
        var now = Instant.now();
        var monthAgo = now.minus(30, ChronoUnit.DAYS);
        return medicineOrderDao.getOrderTotalCost(monthAgo, now);
    }
}
