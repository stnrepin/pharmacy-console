package utils.event;

/**
 * Слушатель событий определенного типа
 * @param <T> Тип событий
 */
public interface EventListener<T> {
    /**
     * Вызывается при возникновении события типа {@code T}
     * @param event Объект события
     */
    void handle(T event);
}
