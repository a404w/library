package de.thws.fiw.bs.library.infrastructure.persistence.repository;

import de.thws.fiw.bs.library.domain.model.Author;
import de.thws.fiw.bs.library.domain.ports.AuthorRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepositoryImpl implements AuthorRepository {
    private final Connection connection;

    public AuthorRepositoryImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public Author save(Author author) {
        String sql = "INSERT INTO authors (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, author.getName());
            stmt.executeUpdate();

            // ID aus der Datenbank abrufen
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setId(generatedKeys.getLong(1));
            }

            return author;
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Speichern des Autors", e);
        }
    }

    @Override
    public void update(Author author) {
        String sql = "UPDATE authors SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author.getName());
            stmt.setLong(2, author.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Aktualisieren des Autors", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM authors WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Löschen des Autors", e);
        }
    }

    @Override
    public Author findById(Long id) {
        String sql = "SELECT * FROM authors WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Author(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen des Autors", e);
        }
        return null;
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT * FROM authors";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                authors.add(new Author(rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Abrufen aller Autoren", e);
        }
        return authors;
    }

    @Override
    public List<Author> getAuthorsByName(String name) {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT * FROM authors WHERE name LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                authors.add(new Author(rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Fehler beim Suchen nach Autoren", e);
        }
        return authors;
    }
}
