package services;

import models.Disease;

import java.util.List;

/**
 * Сервисный класс для операций над заболеваниями
 */
public interface DiseaseService {
    /**
     * Добавляет новое заболевание в БД
     * @param d Новое заболевание
     */
    void add(Disease d);

    /**
     * Обновляется данные о заболевании в БД
     * @param d Существующее в БД заболевание
     */
    void update(Disease d);

    /**
     * Удаляет заболевание из БД
     * @param d Существующее в БД заболевание
     */
    void remove(Disease d);

    /**
     * Выполняет поиск заболевания в БД по ID
     * @param id Существующее в БД заболевание
     * @return Соответствующее ID заболевание
     */
    Disease findById(int id);

    /**
     * Возвращает список всех заболеваний в БД
     * @return Все заболевания в БД
     */
    List<Disease> findAll();

    /**
     * Находит заболевание в БД по имени. В случае
     * отсутствия соответствующего заболевания создает
     * его.
     * @param name Имя заболевания
     * @return Найденное или созданное заболевание
     */
    Disease findByNameOrCreate(String name);
}
