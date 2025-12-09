package ro.unitbv.mip.librarydemo.model.presentation;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ro.unitbv.mip.librarydemo.model.Publication;

// Un JavaFX Bean care "împachetează" o entitate Publication
public class PublicationPM {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty authorName = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();

    // Referința către entitatea originală, utilă pentru operații de salvare/ștergere
    private Publication originalEntity;

    // Getters/Setters și metodele Property(), conform convenției JavaFX Bean
    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthorName() {
        return authorName.get();
    }

    public StringProperty authorNameProperty() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName.set(authorName);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public Publication getOriginalEntity() {
        return originalEntity;
    }

    public void setOriginalEntity(Publication originalEntity) {
        this.originalEntity = originalEntity;
    }
}