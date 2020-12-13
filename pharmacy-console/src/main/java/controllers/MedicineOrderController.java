package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.events.MedicineOrderCreatedEvent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import models.MedicineOrder;
import services.impl.MedicineOrderServiceImpl;
import utils.event.EventListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MedicineOrderController implements EventListener<MedicineOrderCreatedEvent> {
    private MedicineOrderServiceImpl medicineOrderService;
    private List<MedicineOrder> allMedicineOrders;
    private int allOrdersTotalCost;
    private final ObservableList<MedicineOrderController.MedicineOrderWrapper> medicineOrderWrappers;

    public IntegerProperty ordersTotalCost;

    @FXML
    public AnchorPane rootPane;
    @FXML public JFXTreeTableView<MedicineOrderController.MedicineOrderWrapper> orderTable;
    @FXML public JFXTreeTableColumn<MedicineOrderController.MedicineOrderWrapper, Integer>
            orderIdColumn;
    @FXML public JFXTreeTableColumn<MedicineOrderController.MedicineOrderWrapper, String>
            medicineNameColumn;
    @FXML public JFXTreeTableColumn<MedicineOrderController.MedicineOrderWrapper, Integer>
            countColumn;
    @FXML public JFXTreeTableColumn<MedicineOrderController.MedicineOrderWrapper, Integer>
            priceColumn;
    @FXML public JFXTreeTableColumn<MedicineOrderController.MedicineOrderWrapper, String>
            orderDateColumn;

    @FXML
    public JFXToggleButton filterByDateToggle;
    public JFXDatePicker dateFromPicker;
    public JFXDatePicker dateToPicker;

    public MedicineOrderController() {
        allMedicineOrders = new ArrayList<>();
        medicineOrderWrappers = FXCollections.observableArrayList();
        ordersTotalCost = new SimpleIntegerProperty(0);
    }

    public void initialize() {
        orderIdColumn.setCellValueFactory(
                x -> x.getValue().getValue().getIdProperty().asObject()
        );
        medicineNameColumn.setCellValueFactory(
                x -> x.getValue().getValue().getNameProperty()
        );
        countColumn.setCellValueFactory(
                x -> x.getValue().getValue().getCountProperty().asObject()
        );
        priceColumn.setCellValueFactory(
                x -> x.getValue().getValue().getTotalPriceProperty().asObject()
        );
        orderDateColumn.setCellValueFactory(
                x -> x.getValue().getValue().getOrderDateProperty()
        );

        TreeItem<MedicineOrderController.MedicineOrderWrapper> root =
                new RecursiveTreeItem<>(medicineOrderWrappers, RecursiveTreeObject::getChildren);
        orderTable.setRoot(root);
        orderTable.setShowRoot(false);

        // Disable horizontal scrolling
        orderTable.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });

        dateFromPicker.setValue(LocalDate.now());
        dateToPicker.setValue(LocalDate.now());
    }

    public void filterByDateStateChanged() {
        if (filterByDateToggle.isSelected()) {
            var from = dateFromPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant();
            var to = dateToPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant();;
            var newOrders = medicineOrderService.findAllInPeriod(from, to);
            var totalCost = medicineOrderService.calcTotalCostInPeriod(from, to);
            setOrdersTotalCost(totalCost);
            setWrappersWith(newOrders);
        } else {
            setOrdersTotalCost(allOrdersTotalCost);
            setWrappersWith(allMedicineOrders);
        }
    }

    public void setMedicineOrderService(MedicineOrderServiceImpl medicineOrderService) {
        this.medicineOrderService = medicineOrderService;
        loadMedicineOrders();
    }

    public int getOrdersTotalCost() {
        return ordersTotalCost.getValue();
    }

    public void setOrdersTotalCost(int v) {
        ordersTotalCost.setValue(v);
    }

    public IntegerProperty ordersTotalCostProperty() {
        return ordersTotalCost;
    }

    public void handle(MedicineOrderCreatedEvent event) {
        filterByDateToggle.setSelected(false);
        loadMedicineOrders();
    }

    private void loadMedicineOrders() {
        allMedicineOrders = medicineOrderService.findAll();
        allOrdersTotalCost = medicineOrderService.calcTotalCost();
        setOrdersTotalCost(allOrdersTotalCost);
        setWrappersWith(allMedicineOrders);
    }

    private void setWrappersWith(Collection<MedicineOrder> ms) {
        medicineOrderWrappers.clear();
        medicineOrderWrappers.setAll(ms.stream()
                .map(MedicineOrderController.MedicineOrderWrapper::new)
                .collect(Collectors.toList()));
    }

    private static class MedicineOrderWrapper extends
            RecursiveTreeObject<MedicineOrderController.MedicineOrderWrapper> {
        private MedicineOrder wrapped;
        private final IntegerProperty id;
        private final StringProperty name;
        private final IntegerProperty count;
        private final IntegerProperty totalPrice;
        private final StringProperty orderDate;

        public MedicineOrderWrapper(MedicineOrder mo) {
            wrapped = mo;
            id = new SimpleIntegerProperty();
            name = new SimpleStringProperty();
            count = new SimpleIntegerProperty();
            totalPrice = new SimpleIntegerProperty();
            orderDate = new SimpleStringProperty();
            update();
        }

        public void update() {
            id.setValue(wrapped.getId());
            count.setValue(wrapped.getCount());
            totalPrice.setValue(wrapped.getTotalPrice());

            var medicine = wrapped.getItem();
            var medicineName = medicine == null ? "N/A" : medicine.getName();
            name.setValue(medicineName);

            var formatter =
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(Locale.GERMAN)
                            .withZone(ZoneId.systemDefault());
            orderDate.setValue(formatter.format(wrapped.getOrderDate()));
        }

        public void setWrappedMedicineOrder(MedicineOrder m) {
            wrapped = m;
            update();
        }

        public MedicineOrder getWrappedMedicineOrder() { return wrapped; }
        public IntegerProperty getIdProperty() { return id; }
        public StringProperty getNameProperty() { return name; }
        public IntegerProperty getCountProperty() { return count; }
        public IntegerProperty getTotalPriceProperty() { return totalPrice; }
        public StringProperty getOrderDateProperty() { return orderDate; }
    }
}
