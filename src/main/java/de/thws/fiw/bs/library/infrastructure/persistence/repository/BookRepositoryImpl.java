package de.thws.fiw.bs.library.infrastructure.persistence.repository;

import de.thws.fiw.bs.library.domain.model.Author;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Genre;
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
        String sql = "INSERT INTO books (id, title, isbn, isAvailable) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getIsbn());
            stmt.setBoolean(4, book.isAvailable());
            stmt.executeUpdate();

            // Falls die ID automatisch generiert wird
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getLong(1));
            }

            // Autoren und Genres mit dem Buch verknüpfen
            saveAuthors(book);
            saveGenres(book);
            return book;
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Speichern des Buches", e);
        }
    }
    private void saveGenres(Book book) throws SQLException {
        String sql = "INSERT INTO book_genre (book_id, genre_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Genre genre : book.getGenres()) {
                stmt.setLong(1, book.getId());
                stmt.setLong(2, genre.getId());
                stmt.executeUpdate();
            }
        }
    }

    private void saveAuthors(Book book) throws SQLException {
        String sql = "INSERT INTO book_author (book_id, author_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Author author : book.getAuthors()) {
                stmt.setLong(1, book.getId());
                stmt.setLong(2, author.getId());
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public Book findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen des Buches", e);
        }
        return null;
    }

    @Override
    public List<Book> findByAuthor(Author author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM books b " +
                     "JOIN book_author ba ON b.id = ba.book_id " +
                     "WHERE ba.author_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, author.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen der Bücher nach Autor", e);
        }
        return books;
    }

    @Override
    public List<Book> findByGenre(String genreName) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM books b " +
                     "JOIN book_genre bg ON b.id = bg.book_id " +
                     "JOIN genres g ON bg.genre_id = g.id " +
                     "WHERE g.genrename = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, genreName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen der Bücher nach Genre", e);
        }
        return books;
    }
    

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Long bookId = rs.getLong("id");
        String title = rs.getString("title");
        String isbn = rs.getString("isbn");
        boolean isAvailable = rs.getBoolean("isAvailable");
    
        // Autoren und Genres für das Buch abrufen
        Set<Author> authors = getAuthorsForBook(bookId);
        Set<Genre> genres = getGenresForBook(bookId);
    
        return new Book(bookId, title, isbn, genres, authors, isAvailable);
    }
    

    private Set<Author> getAuthorsForBook(Long bookId) throws SQLException {
        Set<Author> authors = new HashSet<>();
        String sql = "SELECT a.id, a.name FROM authors a " +
                     "JOIN book_author ba ON a.id = ba.author_id " +
                     "WHERE ba.book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                authors.add(new Author(rs.getLong("id"), rs.getString("name")));
            }
        }
        return authors;
    }

    private Set<Genre> getGenresForBook(Long bookId) throws SQLException {
        Set<Genre> genres = new HashSet<>();
        String sql = "SELECT g.id, g.genrename, g.beschreibung FROM genres g " +
                     "JOIN book_genre bg ON g.id = bg.genre_id " +
                     "WHERE bg.book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                genres.add(new Genre(rs.getLong("id"), rs.getString("genrename"), rs.getString("beschreibung")));
            }
        }
        return genres;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen aller Bücher", e);
        }
        return books;
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET title = ?, isbn = ?, isAvailable = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getIsbn());
            stmt.setBoolean(3, book.isAvailable());
            stmt.setLong(4, book.getId());
            stmt.executeUpdate();

            // Alte Autoren & Genres löschen und neue speichern
            deleteBookAuthors(book.getId());
            deleteBookGenres(book.getId());
            saveAuthors(book);
            saveGenres(book);
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Aktualisieren des Buches", e);
        }
    }

    private void deleteBookAuthors(Long bookId) throws SQLException {
        String sql = "DELETE FROM book_author WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, bookId);
            stmt.executeUpdate();
        }
    }

    private void deleteBookGenres(Long bookId) throws SQLException {
        String sql = "DELETE FROM book_genre WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, bookId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();

            // Auch die Relationstabellen bereinigen
            deleteBookAuthors(id);
            deleteBookGenres(id);
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Löschen des Buches", e);
        }
    }
}
