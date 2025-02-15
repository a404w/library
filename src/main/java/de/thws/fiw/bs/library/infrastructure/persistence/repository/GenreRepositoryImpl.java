package de.thws.fiw.bs.library.infrastructure.persistence.repository;

import de.thws.fiw.bs.library.domain.model.Genre;
import de.thws.fiw.bs.library.domain.ports.GenreRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

            // ID aus der Datenbank abrufen
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
                return new Genre(rs.getString("genrename"), rs.getString("beschreibung"));
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
                return new Genre(rs.getString("genrename"), rs.getString("beschreibung"));
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
                genres.add(new Genre(rs.getString("genrename"), rs.getString("beschreibung")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen aller Genres", e);
        }
        return genres;
    }
}
