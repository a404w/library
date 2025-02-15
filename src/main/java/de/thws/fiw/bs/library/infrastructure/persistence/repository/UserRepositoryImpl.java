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
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.executeUpdate();

            // Set generated ID
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }

            // Save borrowed books
            saveBorrowedBooks(user);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Speichern des Benutzers", e);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setLong(3, user.getId());
            stmt.executeUpdate();

            // Update borrowed books
            deleteBorrowedBooks(user.getId());
            saveBorrowedBooks(user);
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Aktualisieren des Benutzers", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            deleteBorrowedBooks(id); // Remove book relations
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Löschen des Benutzers", e);
        }
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User( 
                    rs.getString("name"), 
                    rs.getString("email"), 
                    getBorrowedBooks(id)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen des Benutzers", e);
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
                users.add(new User(
                    rs.getString("name"), 
                    rs.getString("email"), 
                    getBorrowedBooks(rs.getLong("id"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen aller Benutzer", e);
        }
        return users;
    }

    @Override
    public User findByName(String name) {
        String sql = "SELECT * FROM users WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User( 
                    rs.getString("name"), 
                    rs.getString("email"), 
                    getBorrowedBooks(rs.getLong("id"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen des Benutzers nach Name", e);
        }
        return null;
    }

    private void saveBorrowedBooks(User user) throws SQLException {
        if (user.getBorrowedBooks() == null || user.getBorrowedBooks().isEmpty()) return;

        String sql = "INSERT INTO user_books (user_id, book_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Book book : user.getBorrowedBooks()) {
                stmt.setLong(1, user.getId());
                stmt.setLong(2, book.getId());
                stmt.executeUpdate();
            }
        }
    }

    private void deleteBorrowedBooks(Long userId) throws SQLException {
        String sql = "DELETE FROM user_books WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }

    private Set<Book> getBorrowedBooks(Long userId) throws SQLException {
        Set<Book> books = new HashSet<>();
        String sql = "SELECT b.id, b.title, b.isbn, b.is_available FROM books b " +
                     "JOIN user_books ub ON b.id = ub.book_id WHERE ub.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book( 
                    rs.getString("title"), 
                    rs.getString("isbn"), 
                    null, null, rs.getBoolean("is_available")
                ));
            }
        }
        return books;
    }
}
