package de.thws.fiw.bs.library.infrastructure.persistence.repository;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Loan;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.LoanRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanRepositoryImpl implements LoanRepository {
    private final Connection connection;

    public LoanRepositoryImpl() {
        this.connection = DatabaseConnection.getConnection();
    }


    @Override
    public void save(Loan loan) {
        String sql = "INSERT INTO loans (book_id, user_id, loan_date, return_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, loan.getBook().getId());
            stmt.setLong(2, loan.getUser().getId());
            stmt.setDate(3, Date.valueOf(loan.getFrom()));
            stmt.setDate(4, Date.valueOf(loan.getTo()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Speichern der Ausleihe", e);
        }
    }
    

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM loans WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Löschen der Ausleihe", e);
        }
    }

    @Override
    public Loan findById(Long id) {
        String sql = "SELECT * FROM loans WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToLoan(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen der Ausleihe", e);
        }
        return null;
    }

    @Override
    public List<Loan> findByUserId(Long userId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen der Ausleihen eines Nutzers", e);
        }
        return loans;
    }

    @Override
    public List<Loan> findByBookId(Long bookId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen der Ausleihen eines Buches", e);
        }
        return loans;
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Long bookId = rs.getLong("book_id");
        Long userId = rs.getLong("user_id");
        LocalDate loanDate = rs.getDate("loan_date").toLocalDate();
        LocalDate returnDate = rs.getDate("return_date").toLocalDate();

        // Dummy Book und User (da die vollständigen Objekte aus anderen Tabellen kommen)
        Book book = new Book(bookId, "Dummy Title", "Dummy ISBN", null, null, true);
        User user = new User(userId, "Dummy Name", "dummy@email.com", null);

        return new Loan( book, user, loanDate, returnDate);
    }
}
