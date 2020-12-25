package dao;

import java.util.List;

import models.Medicine;
import models.Disease;

/**
 * Представляет операции над полями таблицы лекарств
 */
public interface MedicineDao extends DaoCrudOperations<Integer, Medicine> {
    /**
     * Возвращает все лекарства, находящиеся в таблице
     * @return Список всех лекарств
     */
    List<Medicine> findAll();

    /**
     * Возвращает список всех лекарств, применяемых для
     * лечения указанной болезни
     * @param disease Заболевание, для которого надо найти лекарства
     * @return Список лекарств для заболевания
     */
    List<Medicine> findAllFor(Disease disease);
}
