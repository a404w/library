package de.thws.fiw.bs.library.infrastructure.persistence.repository;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

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

            // Get generated ID from database
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }

            // Save borrowed books relations if they exist
            saveBorrowedBooks(user);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error saving user", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();

            // Remove book relations
            deleteBorrowedBooks(id);
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error deleting user", e);
        }
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Set<Book> borrowedBooks = getBorrowedBooks(id);
                return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"), borrowedBooks);
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error retrieving user", e);
        }
        return null;
    }

    @Override
    public Set<User> findAll() {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Set<Book> borrowedBooks = getBorrowedBooks(rs.getLong("id"));
                users.add(new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"), borrowedBooks));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error retrieving all users", e);
        }
        return users;
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
                books.add(new Book(rs.getLong("id"), rs.getString("title"), rs.getString("isbn"), null, null, rs.getBoolean("is_available")));
            }
        }
        return books;
    }
}
