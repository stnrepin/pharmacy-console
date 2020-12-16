package services.impl;

import dao.MedicineOrderDao;
import models.MedicineOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.List;

public class MedicineOrderService implements services.MedicineOrderService {
    private static final Logger logger = LogManager.getLogger(MedicineOrderService.class);

    private final MedicineOrderDao medicineOrderDao;

    public MedicineOrderService(MedicineOrderDao medicineOrderDao) {
        this.medicineOrderDao = medicineOrderDao;
    }

    @Override
    public void orderMedicine(int medId, int count) {
        logger.debug("Order medicine: #" + medId + " (count " + count + ")");
        medicineOrderDao.orderMedicine(medId, count);
    }

    @Override
    public List<MedicineOrder> findAll() {
        logger.debug("Find all orders");
        return medicineOrderDao.findAll();
    }

    @Override
    public List<MedicineOrder> findAllInPeriod(Instant begin, Instant end) {
        logger.debug("Find all orders in period " + begin + "-" + end);
        return medicineOrderDao.findAllInPeriod(begin, end);
    }

    @Override
    public int calcTotalCost() {
        logger.debug("Calc total cost");
        return medicineOrderDao.calcTotalCost();
    }

    @Override
    public int calcTotalCostInPeriod(Instant from, Instant to) {
        logger.debug("Calc total cost in period " + from + "-" + to);
        return medicineOrderDao.calcTotalCost(from, to);
    }
}
