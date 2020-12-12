package dao.hibernate;

import models.Disease;
import models.Medicine;
import utils.PersistenceEntityManagerUtils;

public class DiseaseDao extends DaoCrudOperations<Integer, Disease> implements dao.DiseaseDao {
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
