package ro.unitbv.mip.librarydemo.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HomeView {

    public BorderPane getView() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Welcome Message
        Label welcomeLabel = new Label("Welcome to the Library Demo Application!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        VBox centerBox = new VBox(welcomeLabel);
        centerBox.setAlignment(Pos.CENTER);
        layout.setCenter(centerBox);

        // Footer
        Label footerLabel = new Label("© 2025 Library Demo Inc.");
        VBox footerBox = new VBox(footerLabel);
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(10, 0, 0, 0));
        layout.setBottom(footerBox);

        return layout;
    }
}

