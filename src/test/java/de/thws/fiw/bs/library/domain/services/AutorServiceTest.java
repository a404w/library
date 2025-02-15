package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Author;
import de.thws.fiw.bs.library.domain.ports.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorServiceTest {

    private AuthorService authorService;
    private List<Author> authors; // Direkte Speicherung ohne Repository-Klasse

    @BeforeEach
    void setUp() {
        authors = new ArrayList<>();
        authorService = new AuthorService(new AuthorRepository() {
            @Override
            public Author save(Author author) {
                author.setId((long) (authors.size() + 1)); // Simuliert eine Datenbank-ID
                authors.add(author);
                return author;
            }

            @Override
            public Author findById(Long id) {
                return authors.stream()
                        .filter(a -> a.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Author not found"));
            }

            @Override
            public List<Author> findAll() {
                return new ArrayList<>(authors);
            }

            @Override
            public void delete(Long id) {
                authors.removeIf(a -> a.getId().equals(id));
            }

            @Override
            public List<Author> getAuthorsByName(String name) {
                List<Author> result = new ArrayList<>();
                for (Author a : authors) {
                    if (a.getName().equalsIgnoreCase(name)) {
                        result.add(a);
                    }
                }
                return result;
            }

            @Override
            public void update(Author author) {
                delete(author.getId());
                authors.add(author);
            }
        });
    }

    @Test
    void addAuthor_ShouldSaveAuthor() {
        Author author = authorService.addAuthor("J.K. Rowling");
        assertNotNull(author.getId()); // Die ID sollte gesetzt sein
        assertEquals("J.K. Rowling", author.getName());
    }

    @Test
    void getAuthorById_ShouldReturnCorrectAuthor() {
        Author savedAuthor = authorService.addAuthor("George Orwell");
        Author retrievedAuthor = authorService.getAuthorById(savedAuthor.getId());

        assertEquals(savedAuthor.getId(), retrievedAuthor.getId());
        assertEquals("George Orwell", retrievedAuthor.getName());
    }

    @Test
    void getAllAuthors_ShouldReturnAllSavedAuthors() {
        authorService.addAuthor("J.R.R. Tolkien");
        authorService.addAuthor("Isaac Asimov");

        List<Author> authorsList = authorService.getAllAuthors();
        assertEquals(2, authorsList.size());
    }

    @Test
    void deleteAuthor_ShouldRemoveAuthor() {
        Author savedAuthor = authorService.addAuthor("H.G. Wells");
        authorService.deleteAuthor(savedAuthor.getId());

        List<Author> authorsList = authorService.getAllAuthors();
        assertEquals(0, authorsList.size());
    }

    @Test
    void getAuthorsByName_ShouldReturnMatchingAuthors() {
        authorService.addAuthor("Mark Twain");
        authorService.addAuthor("Mark Twain");
        authorService.addAuthor("Ernest Hemingway");

        List<Author> authorsList = authorService.getAuthorsByName("Mark Twain");
        assertEquals(2, authorsList.size());
    }

    @Test
    void updateAuthor_ShouldModifyExistingAuthor() {
        Author savedAuthor = authorService.addAuthor("Old Name");
        savedAuthor.setName("New Name");
        authorService.updateAuthor(savedAuthor);

        Author updatedAuthor = authorService.getAuthorById(savedAuthor.getId());
        assertEquals("New Name", updatedAuthor.getName());
    }
}
