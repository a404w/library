package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
//import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReturnServiceTest {

    private BookRepository bookRepository;
    private UserRepository userRepository;
    private ReturnService returnService;

    @BeforeEach
    void setUp() {
        // Mock-Repositories initialisieren
        bookRepository = new MockBookRepository();
        userRepository = new MockUserRepository();

        // Service erstellen
        returnService = new ReturnService(bookRepository, userRepository);

        // Initialdaten für Tests
        Book book = new Book(1L, "Example Book", "12345", "Fiction", Set.of("Author"), false);
        User user = new User(1L, "Test User", "test@example.com", new HashSet<>(Set.of(book)));

        bookRepository.save(book);
        userRepository.save(user);
    }

    @Test
    void returnBook_Success() {
        // Act
        returnService.returnBook(1L, 1L);

        // Assert
        Book book = bookRepository.findById(1L);
        User user = userRepository.findById(1L).orElseThrow();

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
        User user = userRepository.findById(1L).orElseThrow();
        user.getBorrowedBooks().clear(); // Nutzer hat keine ausgeliehenen Bücher

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> returnService.returnBook(1L, 1L));
        assertEquals("Book not borrowed by this user", exception.getMessage());
    }
}
