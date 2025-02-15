package de.thws.fiw.bs.library.infrastructure.persistence.repository;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Loan;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.domain.ports.LoanRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanRepositoryImpl implements LoanRepository {
    private final Connection connection;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanRepositoryImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.connection = DatabaseConnection.getConnection();
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Loan save(Loan loan) {
        String sql = "INSERT INTO loans (book_id, user_id, from_date, to_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, loan.getBook().getId());
            stmt.setLong(2, loan.getUser().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(loan.getFrom().atStartOfDay())); // from_date als Timestamp speichern
            

            if (loan.getTo() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(loan.getTo().atStartOfDay()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.executeUpdate();


            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                loan.setId(generatedKeys.getLong(1));
            }

            return loan;
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Speichern der Ausleihe", e);
        }
    }

    @Override
    public void update(Loan loan) {
        String sql = "UPDATE loans SET to_date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (loan.getTo() != null) {
                stmt.setTimestamp(1, Timestamp.valueOf(loan.getTo().atStartOfDay()));
            } else {
                stmt.setNull(1, Types.TIMESTAMP);
            }
            stmt.setLong(2, loan.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Aktualisieren der Ausleihe", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM loans WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Löschen der Ausleihe", e);
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
            throw new RuntimeException("Fehler beim Abrufen der Ausleihe", e);
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
            throw new RuntimeException("Fehler beim Abrufen der Ausleihen eines Nutzers", e);
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
            throw new RuntimeException("Fehler beim Abrufen der Ausleihen eines Buches", e);
        }
        return loans;
    }

    @Override
    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen aller Ausleihen", e);
        }
        return loans;
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Long loanId = rs.getLong("id");
        Long bookId = rs.getLong("book_id");
        Long userId = rs.getLong("user_id");

        LocalDate fromDate = rs.getTimestamp("from_date").toLocalDateTime().toLocalDate();

    
        Timestamp toTimestamp = rs.getTimestamp("to_date");
        LocalDate toDate = (toTimestamp != null) ? toTimestamp.toLocalDateTime().toLocalDate() : null;

    
        Book book = bookRepository.findById(bookId);
        User user = userRepository.findById(userId);

        if (book == null || user == null) {
            throw new RuntimeException("Buch oder Nutzer nicht gefunden! (bookId: " + bookId + ", userId: " + userId + ")");
        }

        Loan loan = new Loan(book, user, fromDate, toDate);
        loan.setId(loanId);

        return loan;
    }
}
