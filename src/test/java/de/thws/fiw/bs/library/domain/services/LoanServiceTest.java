package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Loan;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceTest {

    private LoanService loanService;
    private List<Loan> loanStorage;

    @BeforeEach
    void setUp() {
        loanStorage = new ArrayList<>();
        LoanRepository loanRepository = new LoanRepository() {
            @Override
            public Loan save(Loan loan) {
                loanStorage.add(loan);
                return loan;
            }

            @Override
            public void update(Loan loan) {
                delete(loan.getId());
                loanStorage.add(loan);
            }

            @Override
            public void delete(Long id) {
                loanStorage.removeIf(l -> l.getId().equals(id));
            }

            @Override
            public Loan findById(Long id) {
                return loanStorage.stream()
                        .filter(l -> l.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public List<Loan> findByUserId(Long userId) {
                return loanStorage.stream()
                        .filter(l -> l.getUser().getId().equals(userId))
                        .toList();
            }

            @Override
            public List<Loan> findByBookId(Long bookId) {
                return loanStorage.stream()
                        .filter(l -> l.getBook().getId().equals(bookId))
                        .toList();
            }

            @Override
            public List<Loan> findAll() {
                return new ArrayList<>(loanStorage);
            }
        };

        loanService = new LoanService(loanRepository);
    }

    @Test
    void addLoan_ShouldStoreLoan() throws Exception {
        Book book = new Book("Dune", "123456789", null, null, true);
        User user = new User("Alice", "alice@example.com", null);

        Loan loan = loanService.addLoan(book, user);

        assertNotNull(loan);
        assertEquals(book, loan.getBook());
        assertEquals(user, loan.getUser());
        assertFalse(book.isAvailable()); // Buch sollte nicht mehr verfügbar sein
    }

    @Test
    void addLoan_ShouldThrowExceptionIfBookNotAvailable() {
        Book book = new Book("Dune", "123456789", null, null, false);
        User user = new User("Bob", "bob@example.com", null);

        Exception exception = assertThrows(Exception.class, () -> loanService.addLoan(book, user));
        assertEquals("Buch wurde bereits ausgeliehen oder ist reserviert.", exception.getMessage());
    }

    @Test
    void getLoanById_ShouldReturnCorrectLoan() throws Exception {
        Book book = new Book("The Hobbit", "987654321", null, null, true);
        User user = new User("Charlie", "charlie@example.com", null);
        Loan loan = loanService.addLoan(book, user);

        Loan foundLoan = loanService.getLoanById(loan.getId());
        assertNotNull(foundLoan);
        assertEquals(loan, foundLoan);
    }

    @Test
    void getLoansByUser_ShouldReturnLoansForUser() throws Exception {
        Book book1 = new Book("Book 1", "111111", null, null, true);
        Book book2 = new Book("Book 2", "222222", null, null, true);
        User user = new User("Charlie", "charlie@example.com", null);

        loanService.addLoan(book1, user);
        loanService.addLoan(book2, user);

        List<Loan> userLoans = loanService.getLoansByUser(user.getId());
        assertEquals(2, userLoans.size());
    }

    @Test
    void getLoansByBook_ShouldReturnLoansForBook() throws Exception {
        Book book = new Book("1984", "333333", null, null, true);
        User user1 = new User("David", "david@example.com", null);
        User user2 = new User("Emma", "emma@example.com", null);

        loanService.addLoan(book, user1);
        loanService.addLoan(book, user2);

        List<Loan> bookLoans = loanService.getLoansByBook(book.getId());
        assertEquals(2, bookLoans.size());
    }

    @Test
    void updateLoan_ShouldModifyExistingLoan() throws Exception {
        Book book = new Book("Hyperion", "444444", null, null, true);
        User user = new User("Frank", "frank@example.com", null);
        Loan loan = loanService.addLoan(book, user);

        loan.setTo(LocalDate.now().plusDays(30));
        loanService.updateLoan(loan);

        Loan updatedLoan = loanService.getLoanById(loan.getId());
        assertEquals(LocalDate.now().plusDays(30), updatedLoan.getTo());
    }

    @Test
    void deleteLoan_ShouldRemoveLoan() throws Exception {
        Book book = new Book("Brave New World", "555555", null, null, true);
        User user = new User("George", "george@example.com", null);
        Loan loan = loanService.addLoan(book, user);

        loanService.deleteLoan(loan.getId());

        Loan deletedLoan = loanService.getLoanById(loan.getId());
        assertNull(deletedLoan);
    }

    @Test
    void deleteLoan_ShouldThrowExceptionIfBookIsAvailable() throws Exception {
        Book book = new Book("The Alchemist", "666666", null, null, true);
        User user = new User("Henry", "henry@example.com", null);
        Loan loan = loanService.addLoan(book, user);

        book.setAvailable(true); // Setzt Buch wieder verfügbar, um Fehler auszulösen

        Exception exception = assertThrows(Exception.class, () -> loanService.deleteLoan(loan.getId()));
        assertEquals("Buch wurde noch nicht ausgeliehen oder reserviert.", exception.getMessage());
    }
}
