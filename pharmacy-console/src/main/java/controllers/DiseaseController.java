package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.events.MedicinesUpdatedEvent;
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
import models.Disease;
import services.DiseaseService;
import utils.event.EventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DiseaseController implements EventListener<MedicinesUpdatedEvent> {
    private DiseaseService diseaseService;
    private List<Disease> allDiseases;
    private final ObservableList<DiseaseWrapper> diseaseWrappers;

    @FXML public AnchorPane rootPane;
    @FXML public JFXTreeTableView<DiseaseController.DiseaseWrapper> diseaseTable;
    @FXML public JFXTreeTableColumn<DiseaseController.DiseaseWrapper, Integer> diseaseIdColumn;
    @FXML public JFXTreeTableColumn<DiseaseController.DiseaseWrapper, String>  diseaseNameColumn;

    public DiseaseController() {
        allDiseases = new ArrayList<>();
        diseaseWrappers = FXCollections.observableArrayList();
    }

    public void initialize() {
        diseaseIdColumn.setCellValueFactory(
                x -> x.getValue().getValue().getIdProperty().asObject()
        );
        diseaseNameColumn.setCellValueFactory(
                x -> x.getValue().getValue().getNameProperty()
        );

        TreeItem<DiseaseWrapper> root =
                new RecursiveTreeItem<>(diseaseWrappers, RecursiveTreeObject::getChildren);
        diseaseTable.setRoot(root);
        diseaseTable.setShowRoot(false);

        // Disable horizontal scrolling
        diseaseTable.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });
    }

    public void removeAction() {
        if (diseaseTable.getSelectionModel().isEmpty()) {
            return;
        }
        var selected = diseaseTable.getSelectionModel().getSelectedIndices();
        for (var s : selected) {
            var disease =
                    diseaseService.findById(diseaseWrappers.get(s).getIdProperty().getValue());
            if (disease == null) {
                continue;
            }
            if (disease.getTargetMedicines().size() != 0) {
                return;
            }
            diseaseService.remove(disease);
            allDiseases.removeIf(x -> x.getId() == disease.getId());
            diseaseWrappers.removeIf(x -> x.getIdProperty().getValue() == disease.getId());
        }
    }

    public void setDiseaseService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
        loadDiseases();
    }

    private void loadDiseases() {
        allDiseases = diseaseService.findAll();
        setWrappersWith(allDiseases);
    }

    private void setWrappersWith(Collection<Disease> ms) {
        diseaseWrappers.clear();
        diseaseWrappers.setAll(ms.stream()
                .map(DiseaseController.DiseaseWrapper::new)
                .collect(Collectors.toList()));
    }

    public void handle(MedicinesUpdatedEvent event) {
        loadDiseases();
    }

    private static class DiseaseWrapper extends RecursiveTreeObject<DiseaseController.DiseaseWrapper> {
        private Disease wrapped;
        private final IntegerProperty id;
        private final StringProperty name;

        public DiseaseWrapper(Disease m) {
            wrapped = m;
            id = new SimpleIntegerProperty(m.getId());
            name = new SimpleStringProperty(m.getName());
        }

        public void update() {
            id.setValue(wrapped.getId());
            name.setValue(wrapped.getName());
        }

        public void setWrappedDisease(Disease m) {
            wrapped = m;
            update();
        }

        public Disease getWrappedDisease() { return wrapped; }
        public IntegerProperty getIdProperty() { return id; }
        public StringProperty getNameProperty() { return name; }
    }
}
