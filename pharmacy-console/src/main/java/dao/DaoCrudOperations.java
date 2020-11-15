package dao;

import java.io.Serializable;

public interface DaoCrudOperations<K extends Serializable, E> {
    void create(E entity);
    E read(K id);
    void update(E entity);
    void delete(E entity);
}
