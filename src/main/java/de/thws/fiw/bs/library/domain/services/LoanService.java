package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Loan;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.LoanRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.LoanRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan addLoan(Book book, User user) throws Exception {
        if (!book.isAvailable()) {
            throw new Exception("Buch wurde bereits ausgeliehen oder ist reserviert.");
        }
        book.setAvailable(false);
        return loanRepository.save(new Loan(book, user, LocalDate.now(), LocalDate.now().plusDays(14)));
    }

    public void deleteLoan(Long loanId) throws Exception {
        if (getLoanById(loanId).getBook().isAvailable()) {
            throw new Exception("Buch wurde noch nicht ausgeliehen oder reserviert.");
        }
        getLoanById(loanId).getBook().setAvailable(true);
        loanRepository.delete(loanId);
    }

    public void updateLoan(Loan loan) {
        loanRepository.update(loan);
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public List<Loan> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    public List<Loan> getLoansByBook(Long bookId) {
        return loanRepository.findByBookId(bookId);
    }

    public List<Loan> getAllLoans(){
        return loanRepository.findAll();
    }
}
