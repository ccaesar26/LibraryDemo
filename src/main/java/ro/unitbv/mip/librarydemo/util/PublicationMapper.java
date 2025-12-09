package ro.unitbv.mip.librarydemo.util;

import ro.unitbv.mip.librarydemo.model.Book;
import ro.unitbv.mip.librarydemo.model.Publication;
import ro.unitbv.mip.librarydemo.model.presentation.PublicationPM;

public class PublicationMapper {

    // Convertește de la Entitate (DB) la Presentation Model (UI)
    public static PublicationPM toPM(Publication entity) {
        if (entity == null) {
            return null;
        }

        PublicationPM pm = new PublicationPM();
        pm.setId(entity.getId());
        pm.setTitle(entity.getTitle());
        pm.setAuthorName(entity.getAuthor() != null ? entity.getAuthor().getName() : "N/A");
        pm.setType(entity instanceof Book ? "Book" : "Magazine");
        pm.setOriginalEntity(entity); // Păstrăm referința la original

        return pm;
    }
}