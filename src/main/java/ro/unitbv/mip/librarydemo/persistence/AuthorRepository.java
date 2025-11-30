package ro.unitbv.mip.librarydemo.persistence;

import jakarta.persistence.EntityManager;
import ro.unitbv.mip.librarydemo.model.Author;
import java.util.List;

public class AuthorRepository {

    private EntityManager getEntityManager() {
        return PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
    }

    public Author add(Author author) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(author);
            em.getTransaction().commit();
            return author;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Author> findAll() {
        try (EntityManager em = getEntityManager()) {
            return em.createQuery("SELECT a FROM Author a ORDER BY a.name", Author.class).getResultList();
        }
    }
}
