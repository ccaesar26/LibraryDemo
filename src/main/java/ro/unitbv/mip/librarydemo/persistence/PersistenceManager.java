package ro.unitbv.mip.librarydemo.persistence;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceManager {

    // 1. The single, static, final instance (Eager initialization)
    private static final PersistenceManager INSTANCE = new PersistenceManager();
    private final EntityManagerFactory emf;

    // 2. Private constructor to prevent external instantiation
    private PersistenceManager() {
        try {
            // This line is expensive! It's executed only once.
            this.emf = Persistence.createEntityManagerFactory("LibraryPU");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing EntityManagerFactory");
        }
    }

    // 3. Public static method to get the single instance
    public static PersistenceManager getInstance() {
        return INSTANCE;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
