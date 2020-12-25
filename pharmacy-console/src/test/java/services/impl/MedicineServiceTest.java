package services.impl;

import dao.hibernate.DiseaseDao;
import dao.hibernate.MedicineDao;
import junit.framework.TestCase;
import models.Disease;
import models.Medicine;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.HSQLDialect;
import org.hsqldb.jdbcDriver;
import services.MedicineService;
import utils.PersistenceEntityManagerUtils;

public class MedicineServiceTest extends TestCase {
    private MedicineService medicineService;

    public void setUp() throws Exception {
        super.setUp();

        var dialectClassName = HSQLDialect.class.getName();
        var config = new Configuration()
                .addAnnotatedClass(Medicine.class)
                .addAnnotatedClass(Disease.class);

        config.setProperty(Environment.DIALECT, dialectClassName);
        config.setProperty(Environment.DRIVER, jdbcDriver.class.getName());
        config.setProperty(Environment.URL, "jdbc:hsqldb:mem:pharmacy");
        config.setProperty(Environment.HBM2DDL_AUTO, "create");
        config.setProperty(Environment.USER, "sa");
        config.setProperty(Environment.PASS, "");

        var sessionFactory = config.buildSessionFactory();
        PersistenceEntityManagerUtils.setEntityManager(sessionFactory.createEntityManager());

        var medicineDao = new MedicineDao();
        var diseaseDao = new DiseaseDao();
        medicineService = new services.impl.MedicineService(medicineDao, diseaseDao);
    }

    public void testAdd_InsertsNewEntriesIntoDb() {
        var m = new Medicine("test", 10, 20);
        medicineService.add(m);

        var em = PersistenceEntityManagerUtils.getEntityManager();
        em.getTransaction().begin();
        var dbMedicine = em.createQuery("from Medicine", Medicine.class).getSingleResult();
        em.getTransaction().commit();

        assertTrue(em.contains(m));
        assertEquals(dbMedicine.getCount(), m.getCount());
    }

    public void testAdd_GeneratesId() {
        var m = new Medicine("test", 10, 20);
        medicineService.add(m);
        assertEquals(1, m.getId());
    }

    public void testUpdate_ChangesFields() {
        final var price = 10;
        final var newPrice = price*3;
        var m = new Medicine("test", price, 20);
        medicineService.add(m);
        assertEquals(price, m.getPrice());

        m.setPrice(newPrice);
        medicineService.update(m);
        var mDb = medicineService.findById(m.getId());

        assertEquals(newPrice, mDb.getPrice());
    }

    public void testFindAll_ReturnsAllMedicinesInDb() {
        var m1 = new Medicine("test1", 10, 20);
        var m2 = new Medicine("test2", 300, 400);
        medicineService.add(m1);
        medicineService.add(m2);

        var ms = medicineService.findAll();

        assertEquals(2, ms.size());
        assertEquals(m1.getName(), ms.get(0).getName());
        assertEquals(m2.getName(), ms.get(1).getName());
    }

    public void testFindById_FindsMedicineByItsUniqueId() {
        final var price = 10;
        var m = new Medicine("test", price, 20);
        medicineService.add(m);
        assertEquals(m.getPrice(), price);

        var mDb = medicineService.findById(m.getId());

        assertEquals(m.getId(), mDb.getId());
        assertEquals(price, mDb.getPrice());
    }
}