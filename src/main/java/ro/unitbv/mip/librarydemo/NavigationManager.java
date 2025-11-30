package ro.unitbv.mip.librarydemo;

import javafx.scene.layout.BorderPane;
import ro.unitbv.mip.librarydemo.model.Publication;
import ro.unitbv.mip.librarydemo.view.MainView;
import ro.unitbv.mip.librarydemo.view.PublicationFormView;

import java.util.Optional;

public class NavigationManager {
    private final BorderPane root;

    public NavigationManager(BorderPane root) {
        this.root = root;
    }

    public void showMainView() {
        MainView mainView = new MainView(this);
        root.setCenter(mainView.getView());
    }

    public void showPublicationForm(Optional<Publication> publication) {
        PublicationFormView formView = new PublicationFormView(this, publication);
        root.setCenter(formView.getView());
    }
}