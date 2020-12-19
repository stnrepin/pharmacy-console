package services.impl;

import dao.MedicineOrderDao;
import models.MedicineOrder;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import utils.DateUtils;
import utils.PersistenceEntityManagerUtils;

import java.io.File;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
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

    @Override
    public void printReportToFile(String out_path, URI templateUri, Instant from, Instant to,
                                  PrintReportFinishedCallback callback) {
        logger.debug("Print orders to file in period " + from + " - " + to);

        var entityManager = PersistenceEntityManagerUtils.getEntityManager();
        var session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            try {
                var parameters = new HashMap<String, Object>();
                parameters.put("begin", DateUtils.toString(from));
                parameters.put("end", DateUtils.toString(to));

                var template = new File(templateUri);
                var design = JRXmlLoader.load(template);
                var report  = JasperCompileManager.compileReport(design);
                var reportPrint   = JasperFillManager.fillReport(report, parameters, connection);
                JasperExportManager.exportReportToPdfFile(reportPrint, out_path);
                logger.debug("report created");
                callback.call(null);
            } catch (JRException e) {
                logger.error("Report creation error", e);
                callback.call(e);
            }
        });
    }
}
