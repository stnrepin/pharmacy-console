package utils.event;

public interface EventListener<T> {
    void handle(T event);
}
