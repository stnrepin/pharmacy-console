package dao.hibernate;

import java.util.List;

import models.Disease;
import models.Medicine;
import utils.PersistenceEntityManagerUtils;

public class MedicineDao extends DaoCrudOperations<Integer, Medicine> implements dao.MedicineDao  {
    public List<Medicine> findAll() {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            return em.createQuery("from models.Medicine order by id asc", Medicine.class).getResultList();
        });
    }

    public Medicine findByName(String name) {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            var query= em.createQuery("""
                                from Medicine m
                                where m.name = :name
                                """,
                    Medicine.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        });
    }

    public List<Medicine> findAllFor(Disease disease) {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            var query= em.createQuery("""
                                select m
                                from Disease d
                                join d.targetMedicines m
                                where d.id = :id
                                order by m.id asc
                                """,
                                Medicine.class);
            query.setParameter("id", disease.getId());
            return query.getResultList();
        });
    }
}
