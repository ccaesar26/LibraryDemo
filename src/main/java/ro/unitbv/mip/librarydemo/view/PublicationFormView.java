package ro.unitbv.mip.librarydemo.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ro.unitbv.mip.librarydemo.NavigationManager;
import ro.unitbv.mip.librarydemo.controller.MainController;
import ro.unitbv.mip.librarydemo.model.Author;
import ro.unitbv.mip.librarydemo.model.Book;
import ro.unitbv.mip.librarydemo.model.Magazine;
import ro.unitbv.mip.librarydemo.model.Publication;
import ro.unitbv.mip.librarydemo.persistence.AuthorRepository;
import ro.unitbv.mip.librarydemo.persistence.PublicationRepository;

import java.util.Optional;

public class PublicationFormView {
    private final NavigationManager navigationManager;
    private final MainController controller;
    private final Optional<Publication> publicationToEdit;

    private final ComboBox<Author> authorComboBox = new ComboBox<>();
    private final TextField titleField = new TextField();
    private final ComboBox<String> typeComboBox = new ComboBox<>();
    private final GridPane bookForm = createBookForm();
    private final GridPane magazineForm = createMagazineForm();

    public PublicationFormView(NavigationManager navigationManager, MainController controller, Optional<Publication> publicationOpt) {
        this.navigationManager = navigationManager;
        this.controller = controller;
        this.publicationToEdit = publicationOpt;
    }

    public VBox getView() {
        Label titleLabel = new Label(publicationToEdit.isPresent() ? "Edit Publication" : "Add New Publication");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane mainFormGrid = createMainFormGrid();
        StackPane dynamicFormContainer = new StackPane(magazineForm, bookForm);

        setupDynamicFormLogic();
        refreshAuthors();
        publicationToEdit.ifPresent(this::populateForm);

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(100);
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(e -> handleSave());
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(100);
        cancelButton.setOnAction(e -> navigationManager.showMainView());
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setPrefWidth(300);
        buttonBox.alignmentProperty().set(Pos.BOTTOM_CENTER);

        VBox layout = new VBox(20, titleLabel, mainFormGrid, dynamicFormContainer, buttonBox);
        layout.setPadding(new Insets(20));
        return layout;
    }

    private void handleSave() {
        String title = titleField.getText();
        if (title.isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Title cannot be empty.").show();
            return;
        }

        Author author = authorComboBox.getValue();

        Publication originalEntity = publicationToEdit.orElse(null);

        if ("Book".equals(typeComboBox.getValue())) {
            String isbn = ((TextField) bookForm.lookup("#isbnField")).getText();
            int pageCount = Integer.parseInt(((TextField) bookForm.lookup("#pageCountField")).getText());

            controller.saveBook(originalEntity, title, isbn, pageCount, author);
        } else {
            int issueNr = Integer.parseInt(((TextField) magazineForm.lookup("#issueNrField")).getText());
            String month = ((TextField) magazineForm.lookup("#monthField")).getText();

            controller.saveMagazine(originalEntity, title, issueNr, month, author);
        }
        navigationManager.showMainView();
    }

    private GridPane createMainFormGrid() {

        GridPane mainFormGrid = new GridPane(10, 10);
        var titleLabel = new Label("Title:");
        titleLabel.setPrefWidth(100);
        mainFormGrid.add(titleLabel, 0, 0);
        mainFormGrid.add(titleField, 1, 0);
        mainFormGrid.add(new Label("Type:"), 0, 1);
        typeComboBox.getItems().addAll("Book", "Magazine");
        typeComboBox.setPrefWidth(280);
        mainFormGrid.add(typeComboBox, 1, 1);
        mainFormGrid.add(new Label("Author:"), 0, 2);
        Button addAuthorButton = new Button("+");
        addAuthorButton.setOnAction(e -> handleAddAuthor());
        HBox authorBox = new HBox(5, authorComboBox, addAuthorButton);
        authorComboBox.setPrefWidth(250);
        authorComboBox.setPromptText("Select an author");
        mainFormGrid.add(authorBox, 1, 2);
        return mainFormGrid;
    }

    private GridPane createBookForm() {
        GridPane grid = new GridPane(10, 10);
        TextField isbnField = new TextField();
        isbnField.setId("isbnField");
        isbnField.setPrefWidth(280);
        TextField pageCountField = new TextField();
        pageCountField.setPrefWidth(280);
        pageCountField.setId("pageCountField");
        var isbnLabel = new Label("ISBN:");
        isbnLabel.setPrefWidth(100);
        grid.add(isbnLabel, 0, 0);
        grid.add(isbnField, 1, 0);
        grid.add(new Label("Page Count:"), 0, 1);
        grid.add(pageCountField, 1, 1);
        return grid;
    }

    private GridPane createMagazineForm() {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        TextField issueNrField = new TextField();
        issueNrField.setId("issueNrField");
        TextField monthField = new TextField();
        monthField.setId("monthField");
        grid.add(new Label("Issue Nr:"), 0, 0);
        grid.add(issueNrField, 1, 0);
        grid.add(new Label("Month:"), 0, 1);
        grid.add(monthField, 1, 1);
        grid.setVisible(false);
        return grid;
    }

    private void setupDynamicFormLogic() {
        if (publicationToEdit.isEmpty()) { // Only set default for new entries
            typeComboBox.setValue("Book");
        }
        typeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isBook = "Book".equals(newVal);
            bookForm.setVisible(isBook);
            magazineForm.setVisible(!isBook);
        });
    }

    private void handleAddAuthor() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Author");
        dialog.setHeaderText("Enter the name of the new author:");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.isBlank()) {
                Author newAuthor = controller.saveAuthor(name);
                refreshAuthors();
                authorComboBox.setValue(newAuthor);
            }
        });
    }

    private void refreshAuthors() {
        authorComboBox.setItems(FXCollections.observableArrayList(controller.getAuthors()));
    }

    private void populateForm(Publication p) {
        titleField.setText(p.getTitle());
        if (p.getAuthor() != null) {
            authorComboBox.setValue(p.getAuthor());
        }
        if (p instanceof Book b) {
            typeComboBox.setValue("Book");
            ((TextField) bookForm.lookup("#isbnField")).setText(b.getIsbn());
            ((TextField) bookForm.lookup("#pageCountField")).setText(String.valueOf(b.getPageCount()));
        } else if (p instanceof Magazine m) {
            typeComboBox.setValue("Magazine");
            ((TextField) magazineForm.lookup("#issueNrField")).setText(String.valueOf(m.getIssueNumber()));
            ((TextField) magazineForm.lookup("#monthField")).setText(m.getMonth());
        }
    }
}