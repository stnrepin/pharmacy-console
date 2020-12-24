package controllers.exceptions;

/**
 * Исключение, использующееся при попытки создания объекта с недопустимым именем
 */
public class IncorrectNameException extends Exception {
    public IncorrectNameException(String o) {
        super(o + " has incorrect name");
    }

    /**
     * Возвращает сообщение об ошибки, удобное для отображения пользователю
     * @return Сообщение об ошибки
     */
    public String getPrintableMessage() {
        var mes = getMessage();
        if (mes.length() < 1) {
            return mes;
        }
        return Character.toUpperCase(mes.charAt(0)) +
                mes.substring(1);
    }
}
