package ro.unitbv.mip.librarydemo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.unitbv.mip.librarydemo.controller.MainController;
import ro.unitbv.mip.librarydemo.model.presentation.PublicationPM;
import ro.unitbv.mip.librarydemo.persistence.AuthorRepository;
import ro.unitbv.mip.librarydemo.persistence.PersistenceManager;
import ro.unitbv.mip.librarydemo.persistence.PublicationRepository;

public class LibraryApplication extends Application {

    @Override
    public void start(Stage stage) {
        // --- PASUL 1: Crearea Dependențelor (Repositories & Modelul UI) ---
        PublicationRepository publicationRepository = new PublicationRepository();
        AuthorRepository authorRepository = new AuthorRepository();
        ObservableList<PublicationPM> publicationList = FXCollections.observableArrayList();

        // --- PASUL 2: Crearea Controlerului și Injectarea Dependențelor ---
        MainController controller = new MainController(publicationRepository, authorRepository, publicationList);

        // --- PASUL 3: Crearea Vederilor și a Navigației ---
        // Vederea principală este creată și legată de controler și de modelul UI.
        BorderPane root = new BorderPane();
        NavigationManager navigationManager = new NavigationManager(root, controller, publicationList);

        // --- PASUL 4: Încărcarea Datelor Inițiale și Afișarea Scenei ---
        navigationManager.showMainView(); // Afișează pagina principală
        controller.loadData(); // Pornește încărcarea datelor

        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Library MVC");
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