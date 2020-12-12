package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import models.Medicine;
import services.impl.MedicineServiceImpl;
import utils.ViewManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MedicineController {
    MedicineServiceImpl medicineService;
    private List<Medicine> allMedicines;
    private final ObservableList<MedicineWrapper> medicineWrappers;

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
        var parent = ((Node)event.getSource()).getScene().getWindow();
        var addMedController = ViewManager.showAddMedicineView(parent);
        rootPane.requestFocus();
        if (addMedController == null) {
            System.out.println("addMedicineAction [Error]: addMedController is null");
            return;
        }
        if (!addMedController.hasResult()) {
            return;
        }
        Medicine m = addMedController.getResultMedicine();
        medicineService.addMedicine(m);
        allMedicines.add(m);
        medicineWrappers.add(new MedicineWrapper(m));
    }

    public void removeMedicineAction() {

    }

    public void createOrderAction() {

    }

    public void filterByDiseaseStateChanged() {
        if (filterByDiseaseToggle.isSelected()) {
            var name = filterField.getText();
            var newMedicines = medicineService.findAllMedicinesFor(name);
            setWrappersWith(newMedicines);
        } else {
            setWrappersWith(allMedicines);
        }
    }

    public void setMedicineService(MedicineServiceImpl medicineService) {
        this.medicineService = medicineService;
        loadMedicines();
    }

    private void loadMedicines() {
        allMedicines = medicineService.findAllMedicines();
        setWrappersWith(allMedicines);
    }

    private void setWrappersWith(Collection<Medicine> ms) {
        medicineWrappers.clear();
        medicineWrappers.setAll(ms.stream()
                                  .map(MedicineWrapper::new)
                                  .collect(Collectors.toList()));
    }

    private static class MedicineWrapper extends RecursiveTreeObject<MedicineWrapper> {
        private final IntegerProperty id;
        private final StringProperty name;
        private final IntegerProperty quantity;
        private final IntegerProperty price;

        public MedicineWrapper(Medicine m) {
            id = new SimpleIntegerProperty(m.getId());
            name = new SimpleStringProperty(m.getName());
            quantity = new SimpleIntegerProperty(m.getCount());
            price = new SimpleIntegerProperty(m.getPrice());
        }

        public IntegerProperty getIdProperty() { return id; }
        public StringProperty getNameProperty() { return name; }
        public IntegerProperty getQuantityProperty() { return quantity; }
        public IntegerProperty getPriceProperty() { return price; }
    }
}
