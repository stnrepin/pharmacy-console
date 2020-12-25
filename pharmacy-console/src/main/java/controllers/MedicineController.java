package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.events.MedicineOrderCreatedEvent;
import controllers.events.MedicinesUpdatedEvent;
import controllers.exceptions.IncorrectNameException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import models.Medicine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.DiseaseService;
import services.MedicineOrderService;
import services.MedicineService;
import utils.ViewManager;
import utils.event.EventSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер, представляющий логику работы пользователя с лекарствами
 */
public class MedicineController extends WindowContainingControllerBase {
    private static final Logger logger = LogManager.getLogger(MedicineController.class);

    private MedicineService medicineService;
    private DiseaseService diseaseService;
    private MedicineOrderService medicineOrderService;
    private List<Medicine> allMedicines;
    private final ObservableList<MedicineWrapper> medicineWrappers;
    private final EventSource<MedicinesUpdatedEvent> medicinesUpdatedEventSource;
    private final EventSource<MedicineOrderCreatedEvent> orderCreatedEventSource;

    @FXML public AnchorPane rootPane;
    @FXML public JFXToggleButton filterByDiseaseToggle;
    @FXML public JFXTextField filterField;
    @FXML public JFXTreeTableView<MedicineWrapper> medicineTable;
    @FXML public JFXTreeTableColumn<MedicineWrapper, Integer> medicineIdColumn;
    @FXML public JFXTreeTableColumn<MedicineWrapper, String>  medicineNameColumn;
    @FXML public JFXTreeTableColumn<MedicineWrapper, Integer> medicineQuantityColumn;
    @FXML public JFXTreeTableColumn<MedicineWrapper, Integer> medicinePriceColumn;

    public MedicineController() {
        allMedicines = new ArrayList<>();
        medicineWrappers = FXCollections.observableArrayList();
        medicinesUpdatedEventSource = new EventSource<>();
        orderCreatedEventSource = new EventSource<>();
    }

    /**
     * Инициализует контроллер, автоматически вызывается JavaFX
     */
    public void initialize() {
        medicineIdColumn.setCellValueFactory(
                x -> x.getValue().getValue().getIdProperty().asObject()
        );
        medicineNameColumn.setCellValueFactory(
                x -> x.getValue().getValue().getNameProperty()
        );
        medicineQuantityColumn.setCellValueFactory(
                x -> x.getValue().getValue().getQuantityProperty().asObject()
        );
        medicinePriceColumn.setCellValueFactory(
                x -> x.getValue().getValue().getPriceProperty().asObject()
        );

        TreeItem<MedicineWrapper> root =
                new RecursiveTreeItem<>(medicineWrappers, RecursiveTreeObject::getChildren);
        medicineTable.setRoot(root);
        medicineTable.setShowRoot(false);

        // Disable horizontal scrolling
        medicineTable.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });

        logger.info("Initialized");
    }

    /**
     * Обрабатывает событие для действия "Добавить лекарство"
     * @param event Информация о событии
     */
    public void addMedicineAction(ActionEvent event) {
        var win = getWindowFromEvent(event);
        var addMedController = new AddMedicineController();
        addMedController.setDiseaseService(diseaseService);

        ViewManager.showAddMedicineView(win, addMedController);
        rootPane.requestFocus();
        if (!addMedController.hasResult()) {
            return;
        }
        Medicine m;
        try {
            m = addMedController.getResultMedicine();
        } catch (IncorrectNameException e) {
            ViewManager.showError(e.getPrintableMessage(), win);
            logger.error(e);
            return;
        }
        medicineService.add(m);
        allMedicines.add(m);
        medicineWrappers.add(new MedicineWrapper(m));
        medicinesUpdatedEventSource.notifyAll(new MedicinesUpdatedEvent());

        logger.debug("Medicine '" + m.getName() + "' added");
    }

    /**
     * Обрабатывает событие для действия "Удалить лекарство"
     * @param event Информация о событии
     */
    public void removeMedicineAction(ActionEvent event) {
        if (medicineTable.getSelectionModel().isEmpty()) {
            return;
        }
        var selected = medicineTable.getSelectionModel().getSelectedIndices();
        for (var s : selected) {
            var medicine =
                    medicineService.findById(medicineWrappers.get(s).getIdProperty().getValue());
            if (medicine == null) {
                logger.debug("Can't fine medicine");
                continue;
            }
            var res = ViewManager.showConfirmationDialog(
                    "Are you sure you want to delete medicine '" + medicine.getName() + "'?",
                    getWindowFromEvent(event));
            rootPane.requestFocus();
            if (!res) {
                logger.debug("User canceled deleting");
                continue;
            }
            medicineService.remove(medicine);
            allMedicines.removeIf(x -> x.getId() == medicine.getId());
            medicineWrappers.removeIf(x -> x.getIdProperty().getValue() == medicine.getId());
            medicinesUpdatedEventSource.notifyAll(new MedicinesUpdatedEvent());

            logger.debug("Medicine '" + medicine.getName() + "' removed");
        }
    }

    /**
     * Обрабатывает событие для действия "Редактировать лекарство"
     * @param event Информация о событии
     */
    public void editMedicineAction(ActionEvent event) {
        var selected = getSelectedIndexOrNull();
        if (selected == null) {
            return;
        }

        var win = getWindowFromEvent(event);

        var mWrapped = medicineWrappers.get(selected);
        var addMedController = new AddMedicineController(mWrapped.getWrappedMedicine());
        addMedController.setDiseaseService(diseaseService);

        ViewManager.showAddMedicineView(win, addMedController);
        rootPane.requestFocus();
        if (!addMedController.hasResult()) {
            return;
        }
        Medicine m;
        try {
            m = addMedController.getResultMedicine();
        } catch (IncorrectNameException e) {
            ViewManager.showError(e.getPrintableMessage(), win);
            logger.error(e);
            return;
        }
        medicineService.update(m);
        mWrapped.setWrappedMedicine(m);
        medicinesUpdatedEventSource.notifyAll(new MedicinesUpdatedEvent());

        logger.debug("Medicine '" + m.getName() + "' edited");
    }

    /**
     * Обрабатывает событие действия "Создание заказа"
     * @param event Информация о событии
     */
    public void createOrderAction(ActionEvent event) {
        var selected = getSelectedIndexOrNull();
        if (selected == null) {
            return;
        }

        var mWrapped = medicineWrappers.get(selected);
        var m = mWrapped.getWrappedMedicine();
        var addOrderController = new AddMedicineOrderController(m);
        ViewManager.showAddOrderView(getWindowFromEvent(event), addOrderController);
        rootPane.requestFocus();
        if (!addOrderController.hasResult()) {
            return;
        }
        var quantity = addOrderController.getQuantityToOrder();
        medicineOrderService.orderMedicine(m.getId(), quantity);
        mWrapped.update();
        orderCreatedEventSource.notifyAll(new MedicineOrderCreatedEvent());

        logger.debug("Order for '" + m.getName() + "' created");
    }

    /**
     * Обрабатывает событие для действия "Включение/выключение фильтрации"
     */
    public void filterByDiseaseStateChanged() {
        if (filterByDiseaseToggle.isSelected()) {
            var name = filterField.getText();
            var newMedicines = medicineService.findAllFor(name);
            setWrappersWith(newMedicines);
        } else {
            setWrappersWith(allMedicines);
        }
    }

    /**
     * Инъектирует {@link services.MedicineService} в контроллер
     * @param medicineService Объект сервиса
     */
    public void setMedicineService(MedicineService medicineService) {
        this.medicineService = medicineService;
        loadMedicines();
        logger.info("Medicines loaded");
    }

    /**
     * Инъектирует {@link services.DiseaseService} в контроллер
     * @param diseaseService Объект сервиса
     */
    public void setDiseaseService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    /**
     * Инъектирует {@link services.MedicineOrderService} в контроллер
     * @param medicineOrderService Объект сервиса
     */
    public void setMedicineOrderService(MedicineOrderService medicineOrderService) {
        this.medicineOrderService = medicineOrderService;
    }

    /**
     * Возвращает {@link EventSource}, уведомляющий об изменениях лекарств
     * @return Объект EventSource
     */
    public EventSource<MedicinesUpdatedEvent> getMedicinesUpdatedEventSource() {
        return medicinesUpdatedEventSource;
    }

    /**
     * Возвращает {@link EventSource}, уведомляющий о создании нового заказа
     * @return Объект EventSource
     */
    public EventSource<MedicineOrderCreatedEvent> getOrderCreatedEventSource() {
        return orderCreatedEventSource;
    }

    private void loadMedicines() {
        allMedicines = medicineService.findAll();
        setWrappersWith(allMedicines);
    }

    private void setWrappersWith(Collection<Medicine> ms) {
        medicineWrappers.clear();
        medicineWrappers.setAll(ms.stream()
                                  .map(MedicineWrapper::new)
                                  .collect(Collectors.toList()));
    }

    private Integer getSelectedIndexOrNull() {
        if (medicineTable.getSelectionModel().isEmpty()) {
            return null;
        }
        var selectedList = medicineTable.getSelectionModel().getSelectedIndices();
        if (selectedList.size() != 1) {
            return null;
        }
        return selectedList.get(0);
    }

    /**
     * Обертка вокруг {@link models.Medicine} для работы из JavaFX
     * <p>
     * Можно считать Model из MVC
     */
    private static class MedicineWrapper extends RecursiveTreeObject<MedicineWrapper> {
        private Medicine wrapped;
        private final IntegerProperty id;
        private final StringProperty name;
        private final IntegerProperty quantity;
        private final IntegerProperty price;

        public MedicineWrapper(Medicine m) {
            wrapped = m;
            id = new SimpleIntegerProperty(m.getId());
            name = new SimpleStringProperty(m.getName());
            quantity = new SimpleIntegerProperty(m.getCount());
            price = new SimpleIntegerProperty(m.getPrice());
        }

        public void update() {
            id.setValue(wrapped.getId());
            name.setValue(wrapped.getName());
            quantity.setValue(wrapped.getCount());
            price.setValue(wrapped.getPrice());
        }

        public void setWrappedMedicine(Medicine m) {
            wrapped = m;
            update();
        }

        public Medicine getWrappedMedicine() { return wrapped; }
        public IntegerProperty getIdProperty() { return id; }
        public StringProperty getNameProperty() { return name; }
        public IntegerProperty getQuantityProperty() { return quantity; }
        public IntegerProperty getPriceProperty() { return price; }
    }
}
