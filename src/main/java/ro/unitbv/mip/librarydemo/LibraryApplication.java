package ro.unitbv.mip.librarydemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.unitbv.mip.librarydemo.persistence.PersistenceManager;

public class LibraryApplication extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // --- Menu Bar ---
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(new SeparatorMenuItem(), exitItem);
        menuBar.getMenus().add(fileMenu);
        root.setTop(menuBar);

        // --- Navigation ---
        NavigationManager navigationManager = new NavigationManager(root);
        navigationManager.showMainView(); // Show the initial page

        exitItem.setOnAction(e -> stage.close());

        // --- Stage Setup ---
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("JPA Library Manager");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        PersistenceManager.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}