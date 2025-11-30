package ro.unitbv.mip.librarydemo.persistence;

import jakarta.persistence.EntityManager;
import ro.unitbv.mip.librarydemo.model.Author;
import ro.unitbv.mip.librarydemo.model.Publication;

import java.util.List;

public class PublicationRepository {

    // Helper method to get a new EntityManager
    private EntityManager getEntityManager() {
        return PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
    }

    public Publication saveOrUpdate(Publication publication) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            // merge() handles both new (persist) and existing (update) entities
            Publication mergedPublication = em.merge(publication);
            em.getTransaction().commit();
            return mergedPublication;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Publication> findAll() {
        try (EntityManager em = getEntityManager()) {
            // SOLUȚIA: Folosim LEFT JOIN FETCH pentru a forța încărcarea autorilor
            // în aceeași interogare. Acest lucru previne eroarea LazyInitializationException.
            return em.createQuery(
                    "SELECT p FROM Publication p LEFT JOIN FETCH p.author ORDER BY p.title",
                    Publication.class
            ).getResultList();
        }
    }

    public void delete(Publication publication) {
        if (publication == null || publication.getId() == null) {
            return; // Verificare de siguranță
        }

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            // --- PATTERN-UL "FIND THEN REMOVE" ---
            // 1. Încărcăm entitatea în contextul de persistență curent folosind ID-ul.
            // Acest pas ne asigură că `toDelete` este în starea "managed".
            Publication toDelete = em.find(Publication.class, publication.getId());

            if (toDelete != null) {
                Author author = toDelete.getAuthor();

                // --- ACEASTA ESTE LINIA CRITICĂ A REZOLVĂRII ---
                // Îi spunem autorului să renunțe la referința către publicație.
                if (author != null) {
                    author.removePublication(toDelete);
                }

                // Acum, Hibernate nu mai are niciun conflict și poate șterge în siguranță.
                em.remove(toDelete);
            }

            em.getTransaction().commit(); // Aici se va executa efectiv comanda DELETE
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Este o bună practică să aruncăm excepția mai departe pentru a o vedea în consolă
            throw new RuntimeException("Could not delete publication", e);
        } finally {
            em.close();
        }
    }
}
