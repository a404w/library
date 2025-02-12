package de.thws.fiw.bs.library.infrastructure.persistence.repository;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class BookRepositoryImpl implements BookRepository {
    private final Connection connection;

    public BookRepositoryImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO books (id, title, isbn, genre, authors, isAvailable) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getGenre());
            stmt.setString(5, String.join(",", book.getAuthors()));
            stmt.setBoolean(6, book.isAvailable());
            stmt.executeUpdate();
            return book;
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Speichern des Buches", e);
        }
    }

    @Override
    public Book findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getString("genre"),
                        new HashSet<>(Arrays.asList(rs.getString("authors").split(","))),
                        rs.getBoolean("isAvailable")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen des Buches", e);
        }
        return null;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getString("genre"),
                        new HashSet<>(Arrays.asList(rs.getString("authors").split(","))),
                        rs.getBoolean("isAvailable")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen aller Bücher", e);
        }
        return books;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE authors LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getString("genre"),
                        new HashSet<>(Arrays.asList(rs.getString("authors").split(","))),
                        rs.getBoolean("isAvailable")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen der Bücher nach Autor", e);
        }
        return books;
    }

    @Override
    public List<Book> findByGenre(String genre) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE genre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, genre);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getString("genre"),
                        new HashSet<>(Arrays.asList(rs.getString("authors").split(","))),
                        rs.getBoolean("isAvailable")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen der Bücher nach Genre", e);
        }
        return books;
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET title = ?, isbn = ?, genre = ?, authors = ?, isAvailable = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getIsbn());
            stmt.setString(3, book.getGenre());
            stmt.setString(4, String.join(",", book.getAuthors()));
            stmt.setBoolean(5, book.isAvailable());
            stmt.setLong(6, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Aktualisieren des Buches", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Löschen des Buches", e);
        }
    }
}
