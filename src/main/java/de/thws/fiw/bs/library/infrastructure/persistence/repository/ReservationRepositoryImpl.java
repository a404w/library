package de.thws.fiw.bs.library.infrastructure.persistence.repository;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Reservation;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.ReservationRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepository {
    private final Connection connection;

    public ReservationRepositoryImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservations (user_id, book_id, reservation_date) VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false); // Transaktion starten

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, reservation.getUser().getId());
                stmt.setLong(2, reservation.getBook().getId());
                stmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationDate()));
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    reservation.setId(generatedKeys.getLong(1));
                }

                connection.commit();
                return reservation;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Fehler beim Speichern der Reservierung", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Datenbankfehler beim Speichern der Reservierung", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void update(Reservation reservation) {
        String sql = "UPDATE reservations SET user_id = ?, book_id = ?, reservation_date = ? WHERE id = ?";
        try {
            connection.setAutoCommit(false);
            System.out.println("🔄 SQL Update wird ausgeführt für ID: " + reservation.getId());

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, reservation.getUser().getId());
                stmt.setLong(2, reservation.getBook().getId());
                stmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationDate()));
                stmt.setLong(4, reservation.getId());
                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new RuntimeException(
                            "❌ Fehler: Keine Reservierung mit ID " + reservation.getId() + " gefunden.");
                }

                connection.commit();
                System.out.println("✅ SQL Update erfolgreich für ID: " + reservation.getId());
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Fehler beim Aktualisieren der Reservierung", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Datenbankfehler beim Aktualisieren der Reservierung", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Löschen der Reservierung", e);
        }
    }

    @Override
    public Reservation findById(Long id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen der Reservierung mit ID " + id, e);
        }
        return null;
    }

    @Override
    public List<Reservation> findByUserId(Long userId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen der Reservierungen für User-ID " + userId, e);
        }
        return reservations;
    }

    @Override
    public List<Reservation> findByBookId(Long bookId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen der Reservierungen für Buch-ID " + bookId, e);
        }
        return reservations;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen aller Reservierungen", e);
        }
        return reservations;
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long userId = rs.getLong("user_id");
        Long bookId = rs.getLong("book_id");
        LocalDateTime reservationDate = rs.getTimestamp("reservation_date").toLocalDateTime();

        UserRepositoryImpl userRepo = new UserRepositoryImpl();
        BookRepositoryImpl bookRepo = new BookRepositoryImpl();

        User user = userRepo.findById(userId);
        Book book = bookRepo.findById(bookId);

        if (user == null || book == null) {
            throw new RuntimeException(
                    "Fehler: Buch oder Nutzer existiert nicht! UserID=" + userId + ", BookID=" + bookId);
        }

        Reservation reservation = new Reservation(user, book, reservationDate);
        reservation.setId(id);
        return reservation;
    }
}
