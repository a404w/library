package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.Loan;
import java.util.List;

public interface LoanRepository {
    void save(Loan loan); // Speichert eine neue Ausleihe

    void delete(Long id); // Löscht eine Ausleihe

    Loan findById(Long id); // Holt eine Ausleihe per ID

    List<Loan> findByUserId(Long userId); // Holt alle Ausleihen eines Nutzers

    List<Loan> findByBookId(Long bookId); // Holt alle Ausleihen eines Buches
}