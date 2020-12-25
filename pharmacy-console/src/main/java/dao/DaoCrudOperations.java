package dao;

import java.io.Serializable;

/**
 * Представляет обобщенные CRUD-операции
 * @param <K> Первичный ключ (ID)
 * @param <E> Модель (entity)
 */
public interface DaoCrudOperations<K extends Serializable, E> {
    void create(E entity);
    E read(K id);
    void update(E entity);
    void delete(E entity);
}
