package dao.hibernate;

import utils.PersistenceEntityManagerUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * {@inheritDoc}
 */
public class DaoCrudOperations<K extends Serializable, E> implements dao.DaoCrudOperations<K, E> {
    private final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public DaoCrudOperations() {
        this.entityClass =
                (Class<E>)((ParameterizedType)this.getClass().getGenericSuperclass())
                                .getActualTypeArguments()[1];
    }

    @Override
    public void create(E entity) {
        PersistenceEntityManagerUtils.doTransaction(em -> {
            em.persist(entity);
        });
    }

    @Override
    public E read(K id) {
        return PersistenceEntityManagerUtils.doTransaction(em -> {
            return em.find(this.entityClass, id);
        });
    }

    @Override
    public void update(E entity) {
        PersistenceEntityManagerUtils.doTransaction(em -> {
            em.merge(entity);
        });
    }

    @Override
    public void delete(E entity) {
        PersistenceEntityManagerUtils.doTransaction(em -> {
            em.remove(entity);
        });
    }
}
