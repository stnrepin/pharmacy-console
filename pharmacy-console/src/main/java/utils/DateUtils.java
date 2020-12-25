package utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Набор утилитных методов для работы с датами
 */
public class DateUtils {
    /**
     * Преобразует {@link Instant} в строку
     * @param date Дата
     * @return Строковое представление {@code date}
     */
    public static String toString(Instant date) {
        var formatter =
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC));

        return formatter.format(date);
    }

    /**
     * Преобразует объект типа {@code LocalDate} в
     * объект типа {@code Instant}
     * @param localDate Дата для преобразования
     * @return Преобразованная дата
     */
    public static Instant toInstant(LocalDate localDate) {
        return localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
    }
}
