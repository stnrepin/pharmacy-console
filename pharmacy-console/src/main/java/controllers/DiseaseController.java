package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.events.MedicinesUpdatedEvent;
import controllers.exceptions.IncorrectNameException;
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
import models.Disease;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.DiseaseService;
import utils.ViewManager;
import utils.event.EventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DiseaseController extends WindowContainingControllerBase
                               implements EventListener<MedicinesUpdatedEvent> {
    private static final Logger logger = LogManager.getLogger(DiseaseController.class);

    private DiseaseService diseaseService;
    private List<Disease> allDiseases;
    private final ObservableList<DiseaseWrapper> diseaseWrappers;

    @FXML public AnchorPane rootPane;
    @FXML public JFXTreeTableView<DiseaseController.DiseaseWrapper> diseaseTable;
    @FXML public JFXTreeTableColumn<DiseaseController.DiseaseWrapper, Integer> diseaseIdColumn;
    @FXML public JFXTreeTableColumn<DiseaseController.DiseaseWrapper, String>  diseaseNameColumn;

    public DiseaseController(){
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

        logger.info("Initialized");
    }

    public void addAction(ActionEvent event) {
        var win = getWindowFromEvent(event);
        var ctl = ViewManager.showAddDiseaseView(win,
                                        new AddDiseaseController());
        rootPane.requestFocus();
        if (!ctl.hasResult()) {
            return;
        }
        Disease d;
        try {
            d = ctl.getResult();
        } catch (IncorrectNameException e) {
            ViewManager.showError(e.getPrintableMessage(), win);
            logger.error(e);
            return;
        }
        diseaseService.add(d);
        allDiseases.add(d);
        diseaseWrappers.add(new DiseaseController.DiseaseWrapper(d));

        logger.debug("Disease '" + d.getName() + "' added");
    }

    public void removeAction(ActionEvent event) {
        if (diseaseTable.getSelectionModel().isEmpty()) {
            return;
        }
        var selected = diseaseTable.getSelectionModel().getSelectedIndices();
        for (var s : selected) {
            var id = diseaseWrappers.get(s).getIdProperty().getValue();
            var disease = diseaseService.findById(id);
            if (disease == null) {
                logger.debug("Can't find disease '" + id + "'");
                continue;
            }
            if (disease.getTargetMedicines().size() != 0) {
                ViewManager.showInfoDialog(
                        "Disease '" + disease.getName() + "' has links with medicines",
                        getWindowFromEvent(event));
                rootPane.requestFocus();
                logger.debug("Disease '" + disease.getName() + "': has links");
                continue;
            }
            var res = ViewManager.showConfirmationDialog(
                    "Are you sure you want to delete disease '" + disease.getName() + "'?",
                    getWindowFromEvent(event));
            rootPane.requestFocus();
            if (!res) {
                logger.debug("User canceled deleting");
                continue;
            }
            diseaseService.remove(disease);
            allDiseases.removeIf(x -> x.getId() == disease.getId());
            diseaseWrappers.removeIf(x -> x.getIdProperty().getValue() == disease.getId());
            logger.debug("Removed '" + disease.getName() + "'");
        }
    }

    public void editAction(ActionEvent event) {
        var selected = getSelectedIndexOrNull();
        if (selected == null) {
            return;
        }

        var win = getWindowFromEvent(event);

        var dWrapped = diseaseWrappers.get(selected);
        var ctl = new AddDiseaseController(dWrapped.getWrappedDisease());
        ViewManager.showAddDiseaseView(win, ctl);
        rootPane.requestFocus();
        if (!ctl.hasResult()) {
            return;
        }
        Disease d;
        try {
            d = ctl.getResult();
        } catch (IncorrectNameException e) {
            ViewManager.showError(e.getPrintableMessage(), win);
            logger.error(e);
            return;
        }
        diseaseService.update(d);
        dWrapped.setWrappedDisease(d);

        logger.debug("Disease '" + d.getName() + "' added");
    }

    public void setDiseaseService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
        loadDiseases();
        logger.info("Diseases loaded");
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

    private Integer getSelectedIndexOrNull() {
        if (diseaseTable.getSelectionModel().isEmpty()) {
            return null;
        }
        var selectedList = diseaseTable.getSelectionModel().getSelectedIndices();
        if (selectedList.size() != 1) {
            return null;
        }
        return selectedList.get(0);
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
