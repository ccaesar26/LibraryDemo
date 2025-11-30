package ro.unitbv.mip.librarydemo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // One-to-Many relationship: One Author -> Many Publications
    // mappedBy = "author" refers to the 'author' field in the Publication class
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Publication> publications = new ArrayList<>();

    // Required by JPA
    protected Author() {}

    public Author(String name) {
        this.name = name;
    }

    // Helper method to keep both sides of the relationship in sync
    public void addPublication(Publication publication) {
        this.publications.add(publication);
        publication.setAuthor(this);
    }

    public void removePublication(Publication publication) {
        this.publications.remove(publication);
        publication.setAuthor(null);
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return this.name;
    }

    // În fișierul model/Author.java, la finalul clasei

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id != null && id.equals(author.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
