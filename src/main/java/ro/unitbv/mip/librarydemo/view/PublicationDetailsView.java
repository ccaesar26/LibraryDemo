package ro.unitbv.mip.librarydemo.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ro.unitbv.mip.librarydemo.model.Book;
import ro.unitbv.mip.librarydemo.model.Magazine;
import ro.unitbv.mip.librarydemo.model.Publication;

public class PublicationDetailsView extends VBox {

    public PublicationDetailsView() {
        setPrefWidth(240);
        setPadding(new Insets(10));
        setSpacing(8);
        getChildren().add(new Label("Select a publication to see details."));
    }

    public void updateDetails(Publication publication) {
        getChildren().clear();
        if (publication == null) {
            getChildren().add(new Label("Select a publication to see details."));
            return;
        }

        Label titleLabel = new Label(publication.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        getChildren().add(titleLabel);

        if (publication.getAuthor() != null) {
            getChildren().add(new Label("Author: " + publication.getAuthor().getName()));
        }

        if (publication instanceof Book) {
            Book book = (Book) publication;
            getChildren().add(new Label("Type: Book"));
            getChildren().add(new Label("ISBN: " + book.getIsbn()));
            getChildren().add(new Label("Pages: " + book.getPageCount()));
        } else if (publication instanceof Magazine) {
            Magazine magazine = (Magazine) publication;
            getChildren().add(new Label("Type: Magazine"));
            getChildren().add(new Label("Issue: " + magazine.getIssueNumber()));
            getChildren().add(new Label("Month: " + magazine.getMonth()));
        }
    }
}

