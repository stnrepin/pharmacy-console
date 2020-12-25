package utils.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Источник (генератор) событий
 * @param <T> Тип события
 */
public class EventSource<T> {
    private static final Logger logger = LogManager.getLogger(EventSource.class);
    private final List<EventListener<T>> listeners = new ArrayList<>();

    /**
     * Уведомляет всех слушателей о возникновении события
     * @param event Объект события
     */
    public void notifyAll(T event) {
        for (var listener : listeners) {
            listener.handle(event);
            logger.debug("Notify " + listener.toString());
        }
    }

    /**
     * Добавляет нового слушателя
     * @param listener Слушатель
     */
    public void addListener(EventListener<T> listener) {
        listeners.add(listener);
        logger.debug("Add listener " + listener.toString());
    }
}
