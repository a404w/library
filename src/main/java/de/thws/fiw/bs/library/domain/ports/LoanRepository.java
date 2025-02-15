package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.Loan;
import java.util.List;

public interface LoanRepository {
    Loan save(Loan loan); 

    void delete(Long id); 

    void update(Loan loan);

    Loan findById(Long id);

    List<Loan> findByUserId(Long userId); 

    List<Loan> findByBookId(Long bookId); 

    List<Loan> findAll();
}