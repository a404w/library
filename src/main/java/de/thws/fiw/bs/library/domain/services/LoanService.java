package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Loan;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.LoanRepository;

import java.time.LocalDate;
import java.util.List;

public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan borrowBook(Book book, User user) {
        return loanRepository.save(new Loan(book, user, LocalDate.now(), LocalDate.now().plusDays(14)));
    }

    public void returnBook(Long loanId) {
        loanRepository.delete(loanId); // Ausleihe löschen (Buch zurückgegeben)
    }

    public void updateLoan(Loan loan) {
        loanRepository.update(loan);
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public List<Loan> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId); // Alle Ausleihen eines Nutzers
    }

    public List<Loan> getLoansByBook(Long bookId) {
        return loanRepository.findByBookId(bookId); // Alle Ausleihen eines Buches
    }

    public List<Loan> getAllLoans(){
        return loanRepository.findAll();
    }
}
