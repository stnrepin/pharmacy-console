package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import models.Medicine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Контроллер для окна "Создание заказа"
 */
public class AddMedicineOrderController extends ModalControllerBase {
    private static final Logger logger = LogManager.getLogger(AddMedicineOrderController.class);

    private final Medicine medicine;

    @FXML public Label nameLabel;
    @FXML public Label priceLabel;
    @FXML public Label quantityLabel;
    @FXML public Spinner<Integer> quantitySpinner;
    @FXML public SpinnerValueFactory.IntegerSpinnerValueFactory quantitySpinnerValueFactory;

    public AddMedicineOrderController(Medicine medicineToOrder) {
        this.medicine = medicineToOrder;
    }

    /**
     * Инициализует контроллер, автоматически вызывается JavaFX
     */
    public void initialize() {
        quantitySpinnerValueFactory.setMax(medicine.getCount());

        nameLabel.setText(medicine.getName());
        priceLabel.setText(String.valueOf(medicine.getPrice()));
        quantityLabel.setText(String.valueOf(medicine.getCount()));

        logger.info("Initialized");
    }

    /**
     * Возвращает количество единиц лекарства, которые требуется заказать
     * @return Количество единиц лекарства для заказа
     */
    public int getQuantityToOrder() {
        return quantitySpinner.getValue();
    }
}
