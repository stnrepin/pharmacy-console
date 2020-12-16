package utils.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class EventSource<T> {
    private static final Logger logger = LogManager.getLogger(EventSource.class);
    private final List<EventListener<T>> listeners = new ArrayList<>();

    public void notifyAll(T event) {
        for (var listener : listeners) {
            listener.handle(event);
            logger.debug("Notify " + listener.toString());
        }
    }

    public void addListener(EventListener<T> listener) {
        listeners.add(listener);
        logger.debug("Add listener " + listener.toString());
    }
}
