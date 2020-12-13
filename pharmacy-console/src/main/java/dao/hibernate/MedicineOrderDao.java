package dao.hibernate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import models.Medicine;
import models.MedicineOrder;
import utils.PersistenceEntityManagerUtils;

public class MedicineOrderDao extends DaoCrudOperations<Integer, MedicineOrder>
                              implements dao.MedicineOrderDao {

    public List<MedicineOrder> findAll() {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            var query =
                    em.createQuery("""
                        from models.MedicineOrder
                        order by orderDate desc
                        """, MedicineOrder.class);
            return query.getResultList();
        });
    }

    public List<MedicineOrder> findAllMedicineOrdersInPeriod(Instant begin, Instant end) {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            var query = em.createQuery("""
                from models.MedicineOrder
                where orderDate between :beginDate and :endDate
                """,
                MedicineOrder.class
            );
            return query.setParameter("beginDate", begin)
                        .setParameter("endDate", end)
                        .getResultList();
        });
    }

    public int getMedicineOrderCountInPeriod(Instant begin, Instant end) {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            var query = em.createQuery("""
                select count(*)
                from models.MedicineOrder
                where orderDate between :beginDate and :endDate
                """,
                int.class
            );
            return query.setParameter("beginDate", begin)
                        .setParameter("endDate", end)
                        .getSingleResult();
        });
    }

    public int calcTotalCost() {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            var query = em.createQuery("""
                select sum(totalPrice)
                from models.MedicineOrder
                """,
                Long.class
            );
            var res = query.getSingleResult();
            return res == null ? 0 : res.intValue();
        });
    }

    public int calcTotalCost(Instant begin, Instant end) {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            var query = em.createQuery("""
                select sum(totalPrice)
                from models.MedicineOrder
                where orderDate between :beginDate and :endDate
                """,
                Long.class
            );
            var res = query.setParameter("beginDate", begin)
                               .setParameter("endDate", end)
                               .getSingleResult();
            return res == null ? 0 : res.intValue();
        });
    }

    public void orderMedicine(int medId, int count) {
        PersistenceEntityManagerUtils.doTransaction(em -> {
            var medicine = em.find(Medicine.class, medId);
            medicine.setCount(medicine.getCount() - count);
            em.merge(medicine);
            em.persist(new MedicineOrder(medicine, count));
        });
    }
}
