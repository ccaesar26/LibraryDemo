package ro.unitbv.mip.librarydemo.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import ro.unitbv.mip.librarydemo.NavigationManager;
import ro.unitbv.mip.librarydemo.model.Book;
import ro.unitbv.mip.librarydemo.model.Publication;
import ro.unitbv.mip.librarydemo.persistence.PublicationRepository;

import java.util.Optional;

public class MainView {
    private final NavigationManager navigationManager;
    private final PublicationRepository repository = new PublicationRepository();
    private final TableView<Publication> tableView = new TableView<>();
    private final ObservableList<Publication> modelList = FXCollections.observableArrayList();

    public MainView(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        setupTable();
        refreshData();
    }

    public BorderPane getView() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));
        layout.setCenter(tableView);
        layout.setRight(createControlPanel());
        return layout;
    }

    private void setupTable() {
        TableColumn<Publication, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        titleCol.setPrefWidth(200);

        TableColumn<Publication, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(cellData -> {
            String authorName = (cellData.getValue().getAuthor() != null) ? cellData.getValue().getAuthor().getName() : "N/A";
            return new SimpleStringProperty(authorName);
        });
        authorCol.setPrefWidth(150);

        TableColumn<Publication, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> {
            String type = (cellData.getValue() instanceof Book) ? "Book" : "Magazine";
            return new SimpleStringProperty(type);
        });

        tableView.getColumns().addAll(titleCol, authorCol, typeCol);
        tableView.setItems(modelList);
    }

    private VBox createControlPanel() {
        Button addButton = new Button("Add New");
        addButton.setPrefWidth(100);
        Button editButton = new Button("Edit");
        editButton.setPrefWidth(100);
        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(100);

        // Bind disable property to selection
        editButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());

        addButton.setOnAction(e -> navigationManager.showPublicationForm(Optional.empty()));
        editButton.setOnAction(e -> {
            Publication selected = tableView.getSelectionModel().getSelectedItem();
            navigationManager.showPublicationForm(Optional.of(selected));
        });
        deleteButton.setOnAction(e -> handleDelete());

        VBox controlPanel = new VBox(10, addButton, editButton, deleteButton);
        controlPanel.setPadding(new Insets(0, 0, 0, 10));
        controlPanel.setAlignment(Pos.TOP_CENTER);
        return controlPanel;
    }

    private void handleDelete() {
        Publication selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this publication?");
        alert.setContentText(selected.getTitle());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            repository.delete(selected);
            refreshData();
        }
    }

    private void refreshData() {
        modelList.setAll(repository.findAll());
    }
}