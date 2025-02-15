package de.thws.fiw.bs.library.infrastructure.presistence;

import de.thws.fiw.bs.library.domain.model.Author;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseInitializer;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.AuthorRepositoryImpl;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorRepositoryImplTest {
    private Connection connection;
    private AuthorRepositoryImpl authorRepository;

    @BeforeAll
    void setUpDatabase() throws Exception {
        connection = DatabaseConnection.getConnection();
        DatabaseInitializer.initialize();
        authorRepository = new AuthorRepositoryImpl();
    }

    @BeforeEach
    void insertTestData() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO authors (name) VALUES ('J.K. Rowling');");
            System.out.println(" Test-Daten für Autoren eingefügt.");
        }
    }

    @AfterEach
    void clearDatabase() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM authors;");
        }
    }

    @AfterAll
    void tearDownDatabase() throws Exception {
        connection.close();
    }

    @Test
    @Order(1)
    void testFindById() throws Exception {
        Long realId = null;

        // Tatsächliche ID ermitteln
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM authors LIMIT 1")) {
            if (rs.next()) {
                realId = rs.getLong("id");
                System.out.println(" Tatsächliche ID zum Suchen: " + realId);
            } else {
                fail(" Keine Autoren in der Datenbank gefunden!");
            }
        }

        assertNotNull(realId, " Die ID darf nicht null sein!");

        // Author suchen
        Author author = authorRepository.findById(realId);
        assertNotNull(author, " Der Autor sollte existieren!");
        System.out.println(" Gefundener Autor: " + author.getName());

        assertEquals("J.K. Rowling", author.getName());
    }

    @Test
    @Order(2)
    void testSaveAuthor() {
        Author author = new Author("George Orwell");
        Author savedAuthor = authorRepository.save(author);

        assertNotNull(savedAuthor.getId());
        assertEquals("George Orwell", savedAuthor.getName());
        System.out.println(" Gespeicherter Autor: " + savedAuthor.getName());
    }

    @Test
    @Order(3)
    void testFindAll() {
        List<Author> authors = authorRepository.findAll();

        assertFalse(authors.isEmpty());
        assertEquals(1, authors.size());
        assertEquals("J.K. Rowling", authors.get(0).getName());
        System.out.println(" Anzahl gefundener Autoren: " + authors.size());
    }

    @Test
    @Order(4)
    void testUpdateAuthor() throws Exception {
        Long realId = null;

        // Tatsächliche ID ermitteln
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM authors LIMIT 1")) {
            if (rs.next()) {
                realId = rs.getLong("id");
                System.out.println(" Tatsächliche ID zum Aktualisieren: " + realId);
            } else {
                fail(" Keine Autoren in der Datenbank gefunden!");
            }
        }

        assertNotNull(realId, " Die ID darf nicht null sein!");

        // Author aktualisieren
        Author author = new Author("Updated Author");
        author.setId(realId);
        authorRepository.update(author);

        // Aktualisierten Autor abrufen
        Author updatedAuthor = authorRepository.findById(realId);
        assertNotNull(updatedAuthor, " Der aktualisierte Autor sollte existieren!");
        assertEquals("Updated Author", updatedAuthor.getName());
        System.out.println(" Aktualisierter Autor: " + updatedAuthor.getName());
    }

    @Test
    @Order(5)
    void testDeleteAuthor() throws Exception {
        Long realId = null;

        // Tatsächliche ID ermitteln
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM authors LIMIT 1")) {
            if (rs.next()) {
                realId = rs.getLong("id");
                System.out.println(" Tatsächliche ID zum Löschen: " + realId);
            } else {
                fail(" Keine Autoren in der Datenbank gefunden!");
            }
        }

        assertNotNull(realId, " Die ID darf nicht null sein!");

        // Autor löschen
        authorRepository.delete(realId);

        // Prüfen, ob der Autor gelöscht wurde
        Author deletedAuthor = authorRepository.findById(realId);
        assertNull(deletedAuthor, " Der Autor sollte gelöscht sein!");
        System.out.println(" Autor mit ID=" + realId + " erfolgreich gelöscht.");
    }
}
