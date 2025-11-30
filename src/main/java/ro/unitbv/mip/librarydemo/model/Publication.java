package ro.unitbv.mip.librarydemo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "publications")
// Inheritance Strategy: A single table for the entire hierarchy
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// Defines the column that differentiates between types (e.g., "BOOK" or "MAGAZINE")
@DiscriminatorColumn(name = "pub_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // Many-to-One relationship: Many Publications -> One Author
    // This creates the 'author_id' foreign key column in the 'publications' table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = true) // Author can be null
    private Author author;

    // Required by JPA
    protected Publication() {}

    public Publication(String title) {
        this.title = title;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(Author author) { this.author = author; }

    // În fișierul model/Publication.java, la finalul clasei

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication that)) return false; // Folosim instanceof pentru a permite moștenirea
        // Două entități sunt egale dacă ID-ul lor (care nu e null) este același.
        // Dacă ID-ul e null (entitate nouă), ne bazăm pe egalitatea din Object.
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        // Folosim o valoare fixă pentru consistență, sau getClass().hashCode()
        // pentru a evita coliziuni între entități diferite cu ID null.
        return getClass().hashCode();
    }
}
