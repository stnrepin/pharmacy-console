package services;

import models.MedicineOrder;

import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * Сервисный класс для операций над заказами
 */
public interface MedicineOrderService {
    /**
     * Создает новый заказ для данного количества единиц лекарства
     * @param medId ID лекарства для заказа
     * @param count Количество единиц выбранного лекарства
     */
    void orderMedicine(int medId, int count);

    /**
     * Возвращает список всех заказов в БД
     * @return Список всех заказов
     */
    List<MedicineOrder> findAll();

    /**
     * Возвращает список всех заказов, совершенных в указанный период
     * @param begin Начало периода (не включая)
     * @param end Конец периода (не включая)
     * @return Список заказов за период
     */
    List<MedicineOrder> findAllInPeriod(Instant begin, Instant end);

    /**
     * Возвращает общую сумму заказов в БД
     * @return Сумма заказов
     */
    int calcTotalCost();

    /**
     * Возвращает общую сумму заказов в БД за период
     * @param from Начало периода (не включая)
     * @param to Конец периода (не включая)
     * @return Сумма заказов за период
     */
    int calcTotalCostInPeriod(Instant from, Instant to);

    /**
     * Коллбэк, вызывающейся при окончании печати отчета в файл
     */
    interface PrintReportFinishedCallback {
        void call(Exception e);
    }

    /**
     * Совершает печать отчета обо всех заказах, совершенных в
     * указанный период, в PDF-файл
     * @param out_path Путь до выходного файла
     * @param templateUri Путь до шаблона отчета
     * @param from Начало периода (не включая)
     * @param to Конец периода (не включая)
     * @param callback Функция, которая будет вызвана при завершении печати
     */
    void printReportToFile(String out_path, URI templateUri, Instant from, Instant to,
                           PrintReportFinishedCallback callback);
}
