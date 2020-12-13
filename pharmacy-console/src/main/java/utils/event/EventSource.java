package utils.event;

import java.util.ArrayList;
import java.util.List;

public class EventSource<T> {
    private List<EventListener<T>> listeners = new ArrayList<>();

    public void notifyAll(T event) {
        for (var listener : listeners) {
            listener.handle(event);
        }
    }

    public void addListener(EventListener<T> listener) {
        listeners.add(listener);
    }
}
