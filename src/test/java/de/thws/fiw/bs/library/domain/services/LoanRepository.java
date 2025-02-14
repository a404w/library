package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Loan;
import de.thws.fiw.bs.library.domain.model.Book;
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
    private List<Loan> loans; // Direkte Speicherung ohne Repository-Klasse

    @BeforeEach
    void setUp() {
        loans = new ArrayList<>();
        loanService = new LoanService(new LoanRepository() {
            @Override
            public Loan save(Loan loan) {
                loan.setId((long) (loans.size() + 1)); // Simulierte Datenbank-ID
                loans.add(loan);
                return loan;
            }

            @Override
            public void delete(Long id) {
                loans.removeIf(l -> l.getId().equals(id));
            }

            @Override
            public void update(Loan loan) {
                delete(loan.getId());
                loans.add(loan);
            }

            @Override
            public Loan findById(Long id) {
                return loans.stream()
                        .filter(l -> l.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Loan not found"));
            }

            @Override
            public List<Loan> findByUserId(Long userId) {
                List<Loan> result = new ArrayList<>();
                for (Loan l : loans) {
                    if (l.getUser().getId().equals(userId)) {
                        result.add(l);
                    }
                }
                return result;
            }

            @Override
            public List<Loan> findByBookId(Long bookId) {
                List<Loan> result = new ArrayList<>();
                for (Loan l : loans) {
                    if (l.getBook().getId().equals(bookId)) {
                        result.add(l);
                    }
                }
                return result;
            }

            @Override
            public List<Loan> findAll() {
                return new ArrayList<>(loans);
            }
        });
    }

    @Test
    void addLoan_ShouldSaveLoan() {
        Book book = new Book("1984", "123456789", null, null, true);
        User user = new User("John Doe", "john@example.com", null);

        Loan loan = loanService.addLoan(book, user);
        assertNotNull(loan.getId()); // Die ID sollte gesetzt sein
        assertEquals(book, loan.getBook());
        assertEquals(user, loan.getUser());
    }

    @Test
    void getLoanById_ShouldReturnCorrectLoan() {
        Book book = new Book("Brave New World", "987654321", null, null, true);
        User user = new User("Jane Doe", "jane@example.com", null);

        Loan savedLoan = loanService.addLoan(book, user);
        Loan retrievedLoan = loanService.getLoanById(savedLoan.getId());

        assertEquals(savedLoan.getId(), retrievedLoan.getId());
        assertEquals(book, retrievedLoan.getBook());
        assertEquals(user, retrievedLoan.getUser());
    }

    @Test
    void getAllLoans_ShouldReturnAllSavedLoans() {
        Book book1 = new Book("Dune", "111111111", null, null, true);
        User user1 = new User("Alice", "alice@example.com", null);
        loanService.addLoan(book1, user1);

        Book book2 = new Book("The Hobbit", "222222222", null, null, true);
        User user2 = new User("Bob", "bob@example.com", null);
        loanService.addLoan(book2, user2);

        List<Loan> loansList = loanService.getAllLoans();
        assertEquals(2, loansList.size());
    }

    @Test
    void deleteLoan_ShouldRemoveLoan() {
        Book book = new Book("Lord of the Rings", "333333333", null, null, true);
        User user = new User("Charlie", "charlie@example.com", null);
        Loan savedLoan = loanService.addLoan(book, user);

        loanService.deleteLoan(savedLoan.getId());

        List<Loan> loansList = loanService.getAllLoans();
        assertEquals(0, loansList.size());
    }

    @Test
    void getLoansByUser_ShouldReturnMatchingLoans() {
        User user = new User("David", "david@example.com", null);
        Book book1 = new Book("Neuromancer", "444444444", null, null, true);
        Book book2 = new Book("Snow Crash", "555555555", null, null, true);

        loanService.addLoan(book1, user);
        loanService.addLoan(book2, user);

        List<Loan> userLoans = loanService.getLoansByUser(user.getId());
        assertEquals(2, userLoans.size());
    }

    @Test
    void getLoansByBook_ShouldReturnMatchingLoans() {
        User user1 = new User("Emma", "emma@example.com", null);
        User user2 = new User("Frank", "frank@example.com", null);
        Book book = new Book("Foundation", "666666666", null, null, true);

        loanService.addLoan(book, user1);
        loanService.addLoan(book, user2);

        List<Loan> bookLoans = loanService.getLoansByBook(book.getId());
        assertEquals(2, bookLoans.size());
    }

    @Test
    void updateLoan_ShouldModifyExistingLoan() {
        Book book = new Book("Hyperion", "777777777", null, null, true);
        User user = new User("Grace", "grace@example.com", null);
        Loan savedLoan = loanService.addLoan(book, user);

        LocalDate newReturnDate = LocalDate.now().plusDays(30);
        savedLoan.setTo(newReturnDate);
        loanService.updateLoan(savedLoan);

        Loan updatedLoan = loanService.getLoanById(savedLoan.getId());
        assertEquals(newReturnDate, updatedLoan.getTo());
    }
}
