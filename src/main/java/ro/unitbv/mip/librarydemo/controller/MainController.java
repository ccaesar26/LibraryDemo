package ro.unitbv.mip.librarydemo.controller;

import javafx.collections.ObservableList;
import ro.unitbv.mip.librarydemo.model.Author;
import ro.unitbv.mip.librarydemo.model.Book;
import ro.unitbv.mip.librarydemo.model.Magazine;
import ro.unitbv.mip.librarydemo.model.Publication;
import ro.unitbv.mip.librarydemo.model.presentation.PublicationPM;
import ro.unitbv.mip.librarydemo.persistence.AuthorRepository;
import ro.unitbv.mip.librarydemo.persistence.PublicationRepository;
import ro.unitbv.mip.librarydemo.util.PublicationMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    // Dependințe injectate prin constructor
    private final PublicationRepository publicationRepository;
    private final AuthorRepository authorRepository;
    private final ObservableList<PublicationPM> publicationList;

    public MainController(PublicationRepository pubRepo, AuthorRepository authRepo, ObservableList<PublicationPM> pubList) {
        this.publicationRepository = pubRepo;
        this.authorRepository = authRepo;
        this.publicationList = pubList;
    }

    public void loadData() {
        List<Publication> entities = publicationRepository.findAll();
        List<PublicationPM> presentationModels = entities.stream()
                .map(PublicationMapper::toPM)
                .collect(Collectors.toList());
        publicationList.setAll(presentationModels);
    }

    public void deletePublication(PublicationPM pmToDelete) {
        if (pmToDelete == null) return;
        publicationRepository.delete(pmToDelete.getOriginalEntity());
        loadData(); // Reîncarcă datele pentru a reflecta schimbarea
    }

    public void saveBook(Publication publication, String title, String isbn, int pageCount, Author author) {
        Book book = (publication instanceof Book) ? (Book) publication : new Book(title, isbn, pageCount);
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPageCount(pageCount);
        book.setAuthor(author);
        publicationRepository.saveOrUpdate(book);
        loadData();
    }

    public void saveMagazine(Publication publication, String title, int issueNr, String month, Author author) {
        Magazine magazine = (publication instanceof Magazine) ? (Magazine) publication : new Magazine(title, issueNr, month);
        magazine.setTitle(title);
        magazine.setIssueNumber(issueNr);
        magazine.setMonth(month);
        magazine.setAuthor(author);
        publicationRepository.saveOrUpdate(magazine);
        loadData();
    }

    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    public Author saveAuthor(String name) {
        return authorRepository.add(new Author(name));
    }
}