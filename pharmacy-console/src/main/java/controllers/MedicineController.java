package controllers;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ScrollEvent;
import models.Medicine;
import services.impl.MedicineServiceImpl;

import java.util.stream.Collectors;

public class MedicineController {
    MedicineServiceImpl medicineService;
    private ObservableList<MedicineWrapper> medicines;

    @FXML public JFXTreeTableView<MedicineWrapper> medicineTable;
    @FXML public JFXTreeTableColumn<MedicineWrapper, Integer> medicineIdColumn;
    @FXML public JFXTreeTableColumn<MedicineWrapper, String>  medicineNameColumn;
    @FXML public JFXTreeTableColumn<MedicineWrapper, Integer> medicineQuantityColumn;
    @FXML public JFXTreeTableColumn<MedicineWrapper, Integer> medicinePriceColumn;

    public MedicineController() {
        medicines = FXCollections.observableArrayList();
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
                new RecursiveTreeItem<>(medicines, RecursiveTreeObject::getChildren);
        medicineTable.setRoot(root);
        medicineTable.setShowRoot(false);

        // Disable horizontal scrolling
        medicineTable.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });
    }

    public void addMedicineAction() {

    }

    public void removeMedicineAction() {

    }

    public void createOrderAction() {

    }

    public void filterByDisease() {

    }

    public void resetFilterByDisease() {

    }

    public void setMedicineService(MedicineServiceImpl medicineService) {
        this.medicineService = medicineService;
        loadMedicines();
    }

    private void loadMedicines() {
        medicines.clear();
        var newMedicines = medicineService.findAllMedicines();
        medicines.setAll(newMedicines.stream()
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
