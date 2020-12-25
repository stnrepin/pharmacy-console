package services;

import models.Medicine;

import java.util.List;

/**
 * Сервисный класс для операций над лекарствами
 */
public interface MedicineService {
    /**
     * Добавляет новое лекарство в БД
     * @param medicine Новое лекарство
     */
    void add(Medicine medicine);

    /**
     * Обновляет лекарство в БД
     * @param medicine Существующее в БД лекарство
     */
    void update(Medicine medicine);

    /**
     * Удаляет лекарство из БД
     * @param m Существующее в БД лекарство
     */
    void remove(Medicine m);

    /**
     * Возвращает список всех лекарств в БД
     * @return Список всех лекарств
     */
    List<Medicine> findAll();

    /**
     * Находит лекарство по ID
     * @param name ID
     * @return Лекарство, соответствующее указанному ID
     */
    Medicine findById(int name);

    /**
     * Находит все лекарства, применяемые для лечения болезни
     * @param diseaseName Имя болезни
     * @return Список применяемых лекарств
     */
    List<Medicine> findAllFor(String diseaseName);
}
