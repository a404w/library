/*package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Author;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Genre;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReturnServiceTest {

    private BookRepository bookRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Mock-Repositories initialisieren
        bookRepository = new MockBookRepository();

        // Service erstellen
        returnService = new ReturnService(bookRepository, userRepository);

        // Beispielautor und Genre erstellen
        Author author = new Author(1L, "Author Name");
        Genre genre = new Genre(1L, "Fiction", "Fictional books");

        // Beispielbuch mit Many-to-Many-Beziehungen zu Author und Genre
        Book book = new Book(1L, "Example Book", "12345", Set.of(genre), Set.of(author), false);

        // Beispielnutzer mit ausgeliehenem Buch
        User user = new User(1L, "Test User", "test@example.com", new HashSet<>(Set.of(book)));

        // Daten in Mock-Repositories speichern
        bookRepository.save(book);
        userRepository.save(user);
    }

    @Test
    void returnBook_Success() {
        // Act
        returnService.returnBook(1L, 1L);

        // Assert
        Book book = bookRepository.findById(1L);
        User user = userRepository.findById(1L);

        assertTrue(book.isAvailable());
        assertFalse(user.getBorrowedBooks().contains(book));
    }

    @Test
    void returnBook_UserNotFound() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> returnService.returnBook(1L, 999L)); // Nutzer-ID 999 existiert nicht
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void returnBook_BookNotBorrowedByUser() {
        // Arrange
        User user = userRepository.findById(1L);
        user.getBorrowedBooks().clear(); // Nutzer hat keine ausgeliehenen Bücher

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> returnService.returnBook(1L, 1L));
        assertEquals("User has not borrowed this book", exception.getMessage());
    }
}
*/