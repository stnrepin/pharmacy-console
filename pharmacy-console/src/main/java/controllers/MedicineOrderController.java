package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.events.MedicineOrderCreatedEvent;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import models.MedicineOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.MedicineOrderService;
import utils.DateUtils;
import utils.ViewManager;
import utils.event.EventListener;

import java.io.File;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

public class MedicineOrderController extends WindowContainingControllerBase
                                     implements EventListener<MedicineOrderCreatedEvent> {
    private static final Logger logger = LogManager.getLogger(MedicineOrderController.class);

    private MedicineOrderService medicineOrderService;
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

        logger.info("Initialized");
    }

    public void createReport(ActionEvent event) {
        var win = getWindowFromEvent(event);
        var out_file = ViewManager.showSaveDialog(win,"PDF files (*.pdf)", "*.pdf");
        if (out_file == null) {
            return;
        }

        var spinnerController = ViewManager.showSpinner(win);

        var isFiltered = filterByDateToggle.isSelected();
        var from = isFiltered ? DateUtils.toInstant(dateFromPicker.getValue())
                                     : Instant.ofEpochMilli(0);
        var to = isFiltered ? DateUtils.toInstant(dateToPicker.getValue())
                                   : Instant.now();

        URI templateUri;
        try {
            templateUri = MedicineOrderController.class.getResource(
                    "/Report_MedicineOrder.jrxml").toURI();
        } catch (Exception e) {
            logger.error("Template uri construction error", e);
            ViewManager.showException(e);
            return;
        }

        var thread = new Thread(() ->
                medicineOrderService.printReportToFile(
                    out_file.getAbsolutePath(), templateUri, from, to,
                    (e) -> {
                        Platform.runLater(() ->{
                            spinnerController.close();
                            rootPane.requestFocus();
                            if (e != null) {
                                ViewManager.showException(e);
                            }
                        });
                    }
                )
        );
        thread.start();
    }

    public void filterByDateStateChanged() {
        if (filterByDateToggle.isSelected()) {
            var from = DateUtils.toInstant(dateFromPicker.getValue());
            var to = DateUtils.toInstant(dateToPicker.getValue());
            var newOrders = medicineOrderService.findAllInPeriod(from, to);
            var totalCost = medicineOrderService.calcTotalCostInPeriod(from, to);
            setOrdersTotalCost(totalCost);
            setWrappersWith(newOrders);
        } else {
            setOrdersTotalCost(allOrdersTotalCost);
            setWrappersWith(allMedicineOrders);
        }
    }

    public void setMedicineOrderService(MedicineOrderService medicineOrderService) {
        this.medicineOrderService = medicineOrderService;
        loadMedicineOrders();
        logger.info("Medicine orders loaded");
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
