module ro.unitbv.mip.libraryapp {
    // --- Dependințe necesare pentru JavaFX ---
    requires javafx.controls;

    // --- Dependințe necesare pentru JPA și Hibernate ---
    // API-ul standard Jakarta Persistence
    requires jakarta.persistence;
    // Implementarea Hibernate (care oferă serviciul de persistență)
    requires org.hibernate.orm.core;

    // --- Configurare pentru a permite funcționarea Hibernate ---
    // Aceasta este linia CRITICĂ: permite modulului să descopere implementări
    // ale PersistenceProvider (adică Hibernate).
    uses jakarta.persistence.spi.PersistenceProvider;

    // Hibernate folosește "reflection" pentru a accesa entitățile tale la runtime.
    // Trebuie să "deschidem" pachetul care conține entitățile către biblioteca Hibernate.
    opens ro.unitbv.mip.librarydemo.model to org.hibernate.orm.core;

    // --- Exportă pachetul principal pentru ca JavaFX să poată lansa aplicația ---
    exports ro.unitbv.mip.librarydemo;
}