package ro.unitbv.mip.librarydemo;

import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import ro.unitbv.mip.librarydemo.controller.MainController;
import ro.unitbv.mip.librarydemo.model.Publication;
import ro.unitbv.mip.librarydemo.model.presentation.PublicationPM;
import ro.unitbv.mip.librarydemo.view.MainView;
import ro.unitbv.mip.librarydemo.view.PublicationFormView;

import java.util.Optional;

public class NavigationManager {
    private final BorderPane root;
    private final MainController controller;
    private final ObservableList<PublicationPM> publicationList;

    public NavigationManager(BorderPane root, MainController controller, ObservableList<PublicationPM> pubList) {
        this.root = root;
        this.controller = controller;
        this.publicationList = pubList;
    }

    public void showMainView() {
        MainView mainView = new MainView(this, controller, publicationList);
        root.setCenter(mainView.getView());
    }

    public void showPublicationForm(Optional<Publication> publication) {
        PublicationFormView formView = new PublicationFormView(this, controller, publication);
        root.setCenter(formView.getView());
    }
}