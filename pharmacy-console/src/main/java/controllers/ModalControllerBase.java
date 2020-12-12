package controllers;

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
