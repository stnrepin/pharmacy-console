package dao;

import models.Disease;

import java.util.List;

/**
 * Представляет операции над полями таблицы заболеваний
 */
public interface DiseaseDao extends DaoCrudOperations<Integer, Disease> {
    /**
     * Возвращает все заболевания, находящиеся в таблице
     * @return Список всех заболеваний
     */
    List<Disease> findAll();

    /**
     * Находит заболевание в таблицу по имени
     * @param name Имя заболевания
     * @return Заболевание, соответствующее имени
     */
    Disease findByName(String name);
}
