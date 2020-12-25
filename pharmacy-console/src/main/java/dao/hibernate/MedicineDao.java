package dao.hibernate;

import java.util.List;

import models.Disease;
import models.Medicine;
import utils.PersistenceEntityManagerUtils;

/**
 * {@inheritDoc}
 */
public class MedicineDao extends DaoCrudOperations<Integer, Medicine> implements dao.MedicineDao  {
    @Override
    public List<Medicine> findAll() {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            return em.createQuery("from models.Medicine order by id asc", Medicine.class).getResultList();
        });
    }

    @Override
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
