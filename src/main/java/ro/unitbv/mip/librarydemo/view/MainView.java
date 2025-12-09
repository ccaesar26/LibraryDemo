package ro.unitbv.mip.librarydemo.view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ro.unitbv.mip.librarydemo.NavigationManager;
import ro.unitbv.mip.librarydemo.controller.MainController;
import ro.unitbv.mip.librarydemo.model.presentation.PublicationPM;

import java.util.Optional;

public class MainView {
    private final NavigationManager navigationManager;
    private final MainController controller;
    private final TableView<PublicationPM> tableView = new TableView<>();
    private final TextField searchField = new TextField();
    private final ComboBox<String> typeFilter = new ComboBox<>();
    private final PublicationDetailsView detailsView = new PublicationDetailsView();


    public MainView(NavigationManager navManager, MainController controller, ObservableList<PublicationPM> modelList) {
        this.navigationManager = navManager;
        this.controller = controller;
        this.tableView.setItems(modelList); // Legăm tabelul de lista observabilă
        setupTable();
        setupFiltering();
        setupSelectionListener();
    }

    public BorderPane getView() {
        // ... (codul de layout rămâne la fel)
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        searchField.setPrefWidth(334);
        var searchLabel = new Label("Search:");
        searchLabel.setPrefWidth(45);
        var typeLabel = new Label("Type:");
        typeLabel.setPrefWidth(30);
        var filterPanel = new HBox(10, searchLabel, searchField, new Label("Type:"), typeFilter);
        filterPanel.setAlignment(Pos.CENTER_LEFT);
        filterPanel.setPadding(new Insets(0, 0, 10, 0));
        layout.setTop(filterPanel);

        var controlPanel = createControlPanel();
        controlPanel.setPadding(new Insets(10, 0, 0, 0));

        layout.setCenter(tableView);
        layout.setRight(detailsView);
        layout.setBottom(controlPanel);
        return layout;
    }

    private void setupTable() {
        TableColumn<PublicationPM, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        titleCol.setPrefWidth(200);

        TableColumn<PublicationPM, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(cellData -> cellData.getValue().authorNameProperty());
        authorCol.setPrefWidth(150);

        TableColumn<PublicationPM, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        tableView.getColumns().setAll(titleCol, authorCol, typeCol);
    }

    private void setupSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                detailsView.updateDetails(newSelection.getOriginalEntity());
            } else {
                detailsView.updateDetails(null);
            }
        });
    }

    private void setupFiltering() {
        typeFilter.getItems().addAll("All", "Book", "Magazine");
        typeFilter.setValue("All");

        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                controller.filterData(newVal, typeFilter.getValue())
        );

        typeFilter.valueProperty().addListener((obs, oldVal, newVal) ->
                controller.filterData(searchField.getText(), newVal)
        );
    }

    private Pane createControlPanel() {
        // ... (logica butoanelor se schimbă pentru a apela controller-ul)
        Button addButton = new Button("Add New");
        addButton.setOnAction(e -> navigationManager.showPublicationForm(Optional.empty()));

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            PublicationPM selected = tableView.getSelectionModel().getSelectedItem();
            navigationManager.showPublicationForm(Optional.of(selected.getOriginalEntity()));
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            PublicationPM selected = tableView.getSelectionModel().getSelectedItem();
            handleDelete(selected);
        });

        editButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());

        var controlPanel = new HBox(10, addButton, editButton, deleteButton);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(0, 0, 0, 10));
        return controlPanel;
    }

    private void handleDelete(PublicationPM selected) {
        if (selected == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selected.getTitle() + "?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                controller.deletePublication(selected);
            }
        });
    }
}