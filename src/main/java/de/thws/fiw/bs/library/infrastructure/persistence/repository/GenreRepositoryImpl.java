package de.thws.fiw.bs.library.infrastructure.persistence.repository;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Genre;
import de.thws.fiw.bs.library.domain.ports.GenreRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenreRepositoryImpl implements GenreRepository {
    private final Connection connection;

    public GenreRepositoryImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public Genre save(Genre genre) {
        String sql = "INSERT INTO genres (genrename, beschreibung) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, genre.getGenrename());
            stmt.setString(2, genre.getBeschreibung());
            stmt.executeUpdate();

            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                genre.setId(generatedKeys.getLong(1));
            }

            return genre;
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Speichern des Genres", e);
        }
    }

    @Override
    public void update(Genre genre) {
        String sql = "UPDATE genres SET genrename = ?, beschreibung = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, genre.getGenrename());
            stmt.setString(2, genre.getBeschreibung());
            stmt.setLong(3, genre.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Aktualisieren des Genres", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM genres WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Löschen des Genres", e);
        }
    }

    @Override
    public Genre findById(Long id) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Long genreId = rs.getLong("id");
                String genrename = rs.getString("genrename");
                String beschreibung = rs.getString("beschreibung");
    
                Genre genre = new Genre(genrename, beschreibung);
                genre.setId(genreId);
    
                
                Set<Book> books = getBooksForGenre(genreId);
                genre.setBooks(books);
    
                return genre;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen des Genres", e);
        }
        return null;
    }
    
    

    @Override
    public Genre findByName(String name) {
        String sql = "SELECT * FROM genres WHERE genrename = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Long genreId = rs.getLong("id");
                String genrename = rs.getString("genrename");
                String beschreibung = rs.getString("beschreibung");
    
                Genre genre = new Genre(genrename, beschreibung);
                genre.setId(genreId);
    
                
                Set<Book> books = getBooksForGenre(genreId);
                genre.setBooks(books);
    
                return genre;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Suchen nach Genre", e);
        }
        return null;
    }
    
    
    @Override
    public List<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genres";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Long genreId = rs.getLong("id");
                String genrename = rs.getString("genrename");
                String beschreibung = rs.getString("beschreibung");
    
                Genre genre = new Genre(genrename, beschreibung);
                genre.setId(genreId);
    
                
                Set<Book> books = getBooksForGenre(genreId);
                genre.setBooks(books);
    
                genres.add(genre);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen aller Genres", e);
        }
        return genres;
    }
    

    private Set<Book> getBooksForGenre(Long genreId) throws SQLException {
    Set<Book> books = new HashSet<>();

    String sqlBooks = "SELECT b.* " +
                      "FROM books b " +
                      "JOIN book_genre bg ON b.id = bg.book_id " +
                      "WHERE bg.genre_id = ?";
    try (PreparedStatement stmt2 = connection.prepareStatement(sqlBooks)) {
        stmt2.setLong(1, genreId);

        try (ResultSet rs2 = stmt2.executeQuery()) {
            while (rs2.next()) {
                Long bookId = rs2.getLong("id");
                String title = rs2.getString("title");
                String isbn = rs2.getString("isbn");
                boolean isAvailable = rs2.getBoolean("is_available");


                Book book = new Book();
                book.setId(bookId);
                book.setTitle(title);
                book.setIsbn(isbn);
                book.setAvailable(isAvailable);

                books.add(book);
            }
        }
    }
    return books;
}


    
}
