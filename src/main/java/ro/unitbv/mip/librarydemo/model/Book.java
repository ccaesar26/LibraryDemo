package ro.unitbv.mip.librarydemo.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
// When a Book is saved, JPA will write "BOOK" in the 'pub_type' discriminator column
@DiscriminatorValue("BOOK")
public class Book extends Publication {

    private String isbn;

    @Column(name = "page_count")
    private Integer pageCount;

    // Required by JPA
    protected Book() {}

    public Book(String title, String isbn, Integer pageCount) {
        super(title);
        this.isbn = isbn;
        this.pageCount = pageCount;
    }

    // Getters & Setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getPageCount() { return pageCount; }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }
}