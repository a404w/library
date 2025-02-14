/*package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Author;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Genre;
import de.thws.fiw.bs.library.domain.model.Loan;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.domain.ports.LoanRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceTest {

    private BookRepository bookRepository;
    private UserRepository userRepository;
    private LoanRepository loanRepository;
    private LoanService loanService;

    @BeforeEach
    void setUp() {

        // Beispielautor und Genre erstellen
        Author author = new Author(1L, "Author Name");
        Genre genre = new Genre(1L, "Fiction", "Fictional books");

        // Beispielbuch
        Book book = new Book(1L, "Example Book", "12345", Set.of(genre), Set.of(author), true);

        // Beispielnutzer
        User user = new User(1L, "Test User", "test@example.com", new HashSet<>());

        // Daten in Mock-Repositories speichern
        bookRepository.save(book);
        userRepository.save(user);

        // LoanService erstellen
        loanService = new LoanService(loanRepository, userRepository, bookRepository);
    }

    @Test
    void borrowBook_Success() {
        // Act
        Loan loan = loanService.borrowBook(1L, 1L);

        // Assert
        assertNotNull(loan);
        assertEquals(1L, loan.getBook().getId());
        assertEquals(1L, loan.getUser().getId());

        Book book = bookRepository.findById(1L);
        assertFalse(book.isAvailable());

        List<Loan> userLoans = loanService.getLoansByUser(1L);
        assertEquals(1, userLoans.size());
    }

    @Test
    void borrowBook_BookNotAvailable() {
        // Arrange
        Book book = bookRepository.findById(1L);
        book.setAvailable(false);

        // Act & Assert
        Loan loan = loanService.borrowBook(1L, 1L);
        assertNull(loan); // Loan darf nicht erstellt werden
    }

    @Test
    void borrowBook_UserNotFound() {
        // Act
        Loan loan = loanService.borrowBook(1L, 999L); // Nutzer-ID 999 existiert nicht

        // Assert
        assertNull(loan); // Loan darf nicht erstellt werden
    }

    @Test
    void returnBook_Success() {
        // Arrange
        Loan loan = loanService.borrowBook(1L, 1L);
        assertNotNull(loan);

        // Act
        loanService.returnBook(loan.getId());

        // Assert
        Book book = bookRepository.findById(1L);
        assertTrue(book.isAvailable());

        List<Loan> userLoans = loanService.getLoansByUser(1L);
        assertEquals(0, userLoans.size());
    }

    @Test
    void returnBook_LoanNotFound() {
        // Act
        loanService.returnBook(999L); // Loan-ID 999 existiert nicht

        // Assert (keine Exception sollte geworfen werden)
        List<Loan> userLoans = loanService.getLoansByUser(1L);
        assertEquals(0, userLoans.size());
    }
}
*/