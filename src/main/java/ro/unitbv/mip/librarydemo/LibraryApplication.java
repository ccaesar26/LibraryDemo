package ro.unitbv.mip.librarydemo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.unitbv.mip.librarydemo.controller.MainController;
import ro.unitbv.mip.librarydemo.model.presentation.PublicationPM;
import ro.unitbv.mip.librarydemo.persistence.AuthorRepository;
import ro.unitbv.mip.librarydemo.persistence.PersistenceManager;
import ro.unitbv.mip.librarydemo.persistence.PublicationRepository;
import ro.unitbv.mip.librarydemo.view.HomeView;
import ro.unitbv.mip.librarydemo.view.MainView;

public class LibraryApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Inițializare Model
        PublicationRepository publicationRepository = new PublicationRepository();
        AuthorRepository authorRepository = new AuthorRepository();
        ObservableList<PublicationPM> publicationList = FXCollections.observableArrayList();

        // Inițializare Controler
        MainController mainController = new MainController(publicationRepository, authorRepository, publicationList);
        mainController.loadData();

        // Create TabPane
        TabPane tabPane = new TabPane();

        // Home Tab
        Tab homeTab = new Tab("Home");
        HomeView homeView = new HomeView();
        homeTab.setContent(homeView.getView());
        homeTab.setClosable(false);

        // Browse Publications Tab
        Tab browseTab = new Tab("Browse Publications");
        BorderPane rootLayout = new BorderPane();
        NavigationManager navigationManager = new NavigationManager(rootLayout, mainController, publicationList);
        MainView mainView = new MainView(navigationManager, mainController, publicationList);
        rootLayout.setCenter(mainView.getView());
        browseTab.setContent(rootLayout);
        browseTab.setClosable(false);

        tabPane.getTabs().addAll(homeTab, browseTab);

        // Create MenuBar
        MenuBar menuBar = createMenuBar();
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(menuBar);
        mainLayout.setCenter(tabPane);

        // Configurarea scenei principale
        Scene scene = new Scene(mainLayout, 800, 600);
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