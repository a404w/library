package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Loan;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.ports.LoanRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // Buch ausleihen
    public Loan borrowBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId);
        User user = userRepository.findById(userId);

        if (book == null || user == null || !book.isAvailable()) {
            return null; // Buch kann nicht ausgeliehen werden
        }

        Loan loan = new Loan(book, user, LocalDate.now(), LocalDate.now().plusDays(14)); // 14 Tage Frist
        loanRepository.save(loan);

        book.setAvailable(false);
        bookRepository.save(book);

        return loan;
    }

    // Buch zurückgeben
    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId);
        if (loan == null)
            return;

        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        loanRepository.delete(loanId); // Buch zurückgegeben → Loan löschen
    }

    // Alle Leihen eines Nutzers abrufen
    public List<Loan> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }
}
