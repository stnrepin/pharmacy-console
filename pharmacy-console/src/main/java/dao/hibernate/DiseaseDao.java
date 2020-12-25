package dao.hibernate;

import models.Disease;
import utils.PersistenceEntityManagerUtils;

import java.util.List;

/**
 * {@inheritDoc}
 */
public class DiseaseDao extends DaoCrudOperations<Integer, Disease> implements dao.DiseaseDao {
    @Override
    public List<Disease> findAll() {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            return em.createQuery("from Disease order by id asc", Disease.class).getResultList();
        });
    }

    @Override
    public Disease findByName(String name) {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            var query= em.createQuery("""
                                from Disease d
                                where d.name = :name
                                """,
                                Disease.class);
            query.setParameter("name", name);
            return query.getResultList().stream().findFirst().orElse(null);
        });
    }
}
