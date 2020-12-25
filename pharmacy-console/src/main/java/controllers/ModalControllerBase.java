package controllers;

/**
 * Базовый класс для контроллеров модальных представлений
 * (основная особенность -- имеет кнопки OK и Cancel,
 * которыми может быть закрыт)
 */
public class ModalControllerBase extends WindowContainingControllerBase {
    private boolean hasResult = false;

    public boolean hasResult() {
        return hasResult;
    }

    public void ok() {
        hasResult = true;
        close();
    }

    public void cancel() {
        hasResult = false;
        close();
    }

    public void close() {
        getWindow().close();
    }
}
