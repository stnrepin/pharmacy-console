package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.events.MedicineOrderCreatedEvent;
import controllers.events.MedicinesUpdatedEvent;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import models.Medicine;
import services.DiseaseService;
import services.MedicineOrderService;
import services.MedicineService;
import utils.ViewManager;
import utils.event.EventSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MedicineController {
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
    }

    public void addMedicineAction(ActionEvent event) {
        var addMedController = new AddMedicineController();
        addMedController.setDiseaseService(diseaseService);

        ViewManager.showAddMedicineView(getWindowFromEvent(event), addMedController);
        rootPane.requestFocus();
        if (!addMedController.hasResult()) {
            return;
        }
        Medicine m = addMedController.getResultMedicine();
        medicineService.add(m);
        allMedicines.add(m);
        medicineWrappers.add(new MedicineWrapper(m));
        medicinesUpdatedEventSource.notifyAll(new MedicinesUpdatedEvent());
    }

    public void removeMedicineAction() {
        if (medicineTable.getSelectionModel().isEmpty()) {
            return;
        }
        var selected = medicineTable.getSelectionModel().getSelectedIndices();
        for (var s : selected) {
            var medicine =
                    medicineService.findById(medicineWrappers.get(s).getIdProperty().getValue());
            if (medicine == null) {
                continue;
            }
            medicineService.remove(medicine);
            allMedicines.removeIf(x -> x.getId() == medicine.getId());
            medicineWrappers.removeIf(x -> x.getIdProperty().getValue() == medicine.getId());
        }
    }

    public void editMedicineAction(ActionEvent event) {
        var selected = getSelectedIndexOrNull();
        if (selected == null) {
            return;
        }

        var mWrapped = medicineWrappers.get(selected);
        var addMedController = new AddMedicineController(mWrapped.getWrappedMedicine());
        addMedController.setDiseaseService(diseaseService);

        ViewManager.showAddMedicineView(getWindowFromEvent(event), addMedController);
        rootPane.requestFocus();
        if (!addMedController.hasResult()) {
            return;
        }
        var m = addMedController.getResultMedicine();
        medicineService.update(m);
        mWrapped.setWrappedMedicine(m);
        medicinesUpdatedEventSource.notifyAll(new MedicinesUpdatedEvent());
    }

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
    }

    public void filterByDiseaseStateChanged() {
        if (filterByDiseaseToggle.isSelected()) {
            var name = filterField.getText();
            var newMedicines = medicineService.findAllFor(name);
            setWrappersWith(newMedicines);
        } else {
            setWrappersWith(allMedicines);
        }
    }

    public void setMedicineService(MedicineService medicineService) {
        this.medicineService = medicineService;
        loadMedicines();
    }

    public void setDiseaseService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    public void setMedicineOrderService(MedicineOrderService medicineOrderService) {
        this.medicineOrderService = medicineOrderService;
    }

    public EventSource<MedicinesUpdatedEvent> getMedicinesUpdatedEventSource() {
        return medicinesUpdatedEventSource;
    }

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

    private Window getWindowFromEvent(ActionEvent event) {
        return ((Node)event.getSource()).getScene().getWindow();
    }

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
