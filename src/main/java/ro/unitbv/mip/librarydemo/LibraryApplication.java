package ro.unitbv.mip.librarydemo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.unitbv.mip.librarydemo.controller.MainController;
import ro.unitbv.mip.librarydemo.model.presentation.PublicationPM;
import ro.unitbv.mip.librarydemo.persistence.AuthorRepository;
import ro.unitbv.mip.librarydemo.persistence.PersistenceManager;
import ro.unitbv.mip.librarydemo.persistence.PublicationRepository;

public class LibraryApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Inițializare Model
        PublicationRepository publicationRepository = new PublicationRepository();
        AuthorRepository authorRepository = new AuthorRepository();
        ObservableList<PublicationPM> publicationList = FXCollections.observableArrayList();

        // Inițializare Controler
        MainController mainController = new MainController(publicationRepository, authorRepository, publicationList);

        // Inițializare NavigationManager
        BorderPane rootLayout = new BorderPane();
        NavigationManager navigationManager = new NavigationManager(rootLayout, mainController, publicationList);

        // Create MenuBar
        MenuBar menuBar = createMenuBar();
        rootLayout.setTop(menuBar);

        // Afișarea vederii principale
        navigationManager.showMainView();
        mainController.loadData();

        // Configurarea scenei principale
        Scene scene = new Scene(rootLayout, 800, 600);
        primaryStage.setTitle("Library Demo Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);
        menuBar.getMenus().add(helpMenu);
        return menuBar;
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Library Demo Application");
        alert.setContentText("This is a simple library management application created for demonstration purposes.\n\nVersion: 1.0");
        alert.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        PersistenceManager.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}