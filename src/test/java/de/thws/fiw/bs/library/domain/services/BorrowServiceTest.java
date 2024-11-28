package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BorrowServiceTest {

    private BookRepository bookRepository;
    private UserRepository userRepository;
    private BorrowService borrowService;

    @BeforeEach
    void setUp() {
        // Mock-Repositories initialisieren
        bookRepository = new MockBookRepository();
        userRepository = new MockUserRepository();

        // Service erstellen
        borrowService = new BorrowService(bookRepository, userRepository);

        // Initialdaten für Tests
        bookRepository.save(new Book(1L, "Example Book", "12345", "Fiction", Set.of("Author"), true));
        userRepository.save(new User(1L, "Test User", "test@example.com", new HashSet<>()));
    }

    @Test
    void borrowBook_Success() {
        // Act
        borrowService.borrowBook(1L, 1L);

        // Assert
        Book book = bookRepository.findById(1L);
        User user = userRepository.findById(1L).orElseThrow();

        assertFalse(book.isAvailable());
        assertTrue(user.getBorrowedBooks().contains(book));
    }

    @Test
    void borrowBook_BookNotAvailable() {
        // Arrange
        Book book = bookRepository.findById(1L);
        book.setAvailable(false);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> borrowService.borrowBook(1L, 1L));
        assertEquals("Book is currently not available", exception.getMessage());
    }

    @Test
    void borrowBook_UserNotFound() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> borrowService.borrowBook(1L, 999L)); // Nutzer-ID 999 existiert nicht
        assertEquals("User not found", exception.getMessage());
    }
}
