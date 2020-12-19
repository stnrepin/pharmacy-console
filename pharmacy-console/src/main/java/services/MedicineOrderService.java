package services;

import models.MedicineOrder;

import java.net.URI;
import java.time.Instant;
import java.util.List;

public interface MedicineOrderService {
    void orderMedicine(int medId, int count);

    List<MedicineOrder> findAll();
    List<MedicineOrder> findAllInPeriod(Instant begin, Instant end);

    int calcTotalCost();
    int calcTotalCostInPeriod(Instant from, Instant to);

    interface PrintReportFinishedCallback {
        void call(Exception e);
    }

    void printReportToFile(String out_path, URI templateUri, Instant from, Instant to,
                           PrintReportFinishedCallback callback);
}
