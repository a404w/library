package de.thws.fiw.bs.library.infrastructure.persistence.repository;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection;

    public UserRepositoryImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        try {
            connection.setAutoCommit(false); // Transaktion starten

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getEmail());
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }

                saveBorrowedBooks(user); // Bücher speichern
                connection.commit(); // Transaktion abschließen
                return user;
            } catch (SQLException e) {
                connection.rollback(); // Falls ein Fehler auftritt, alles zurücksetzen
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Speichern des Benutzers", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getEmail());
                stmt.setLong(3, user.getId());
                stmt.executeUpdate();

                deleteBorrowedBooks(user.getId());
                saveBorrowedBooks(user);

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Aktualisieren des Benutzers", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
                deleteBorrowedBooks(id);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Löschen des Benutzers", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        System.out.println("🔍 SQL-Query ausführen: " + sql + " mit ID = " + id);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Long userId = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                System.out.println("✅ Benutzer gefunden: ID = " + userId + ", Name = " + name);

                User user = new User(name, email);
                user.setId(userId);

                return user;
            } else {
                System.out.println("⚠️ Kein Benutzer mit ID " + id + " gefunden.");
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL-Fehler in findById: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Unerwarteter Fehler in findById: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                User user = new User(name, email);
                user.setId(id);
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen aller Benutzer", e);
        }

        return users;
    }

    public User findByName(String name) {
        String sql = "SELECT * FROM users WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Long userId = rs.getLong("id");
                User user = new User(rs.getString("name"), rs.getString("email"));
                user.setId(userId);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen des Benutzers nach Name", e);
        }
        return null;
    }

    private void saveBorrowedBooks(User user) throws SQLException {
        if (user.getBorrowedBooks() == null || user.getBorrowedBooks().isEmpty())
            return;

        String sql = "INSERT INTO borrowed_books (user_id, book_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Book book : user.getBorrowedBooks()) {
                stmt.setLong(1, user.getId());
                stmt.setLong(2, book.getId());
                stmt.executeUpdate();
            }
        }
    }

    private void deleteBorrowedBooks(Long userId) throws SQLException {
        String sql = "DELETE FROM borrowed_books WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }

    private Set<Book> getBorrowedBooks(Long userId) throws SQLException {
        Set<Book> books = new HashSet<>();
        String sql = "SELECT b.id, b.title, b.isbn, b.is_available FROM books b " +
                "JOIN borrowed_books ub ON b.id = ub.book_id WHERE ub.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getString("title"), rs.getString("isbn"), null, null,
                        rs.getBoolean("is_available"));
                book.setId(rs.getLong("id"));
                books.add(book);
            }
        }
        return books;
    }
}
