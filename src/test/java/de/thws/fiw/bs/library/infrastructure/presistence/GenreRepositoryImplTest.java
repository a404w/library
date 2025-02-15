package de.thws.fiw.bs.library.infrastructure.presistence;

import de.thws.fiw.bs.library.domain.model.Genre;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseInitializer;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.GenreRepositoryImpl;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreRepositoryImplTest {
    private Connection connection;
    private GenreRepositoryImpl genreRepository;

    @BeforeAll
    void setUpDatabase() throws Exception {
        connection = DatabaseConnection.getConnection();
        DatabaseInitializer.initialize(); // Tabellen erstellen
        genreRepository = new GenreRepositoryImpl();
    }

    @BeforeEach
    void insertTestData() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO genres (genrename, beschreibung) VALUES ('Fantasy', 'Fantasy-Bücher');");
            System.out.println("Test-Daten eingefügt.");
        }
    }
    

    @AfterEach
    void clearDatabase() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM genres;");
        }
    }

    @AfterAll
    void tearDownDatabase() throws Exception {
        connection.close();
    }
    @Test
    @Order(0)
    void testDatabaseContent() throws Exception {
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM genres")) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(" Genre gefunden: ID=" + rs.getLong("id") + ", Name=" + rs.getString("genrename"));
            }

            assertTrue(found, " Es sind keine Testdaten in der Datenbank vorhanden!");
        }
    }

    @Test
    @Order(1)
    void testSaveGenre() {
        Genre genre = new Genre("Science Fiction", "Sci-Fi Bücher");
        Genre savedGenre = genreRepository.save(genre);

        assertNotNull(savedGenre.getId());
        assertEquals("Science Fiction", savedGenre.getGenrename());
    }

    @Test
    @Order(2)
    void testFindById() throws Exception {
        Long realId = null;
        
        // Richtig eingefügte ID herausfinden
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM genres LIMIT 1")) {
            if (rs.next()) {
                realId = rs.getLong("id");
                System.out.println("Tatsächliche ID: " + realId);
            } else {
                fail(" Keine Genre-Einträge in der Datenbank gefunden!");
            }
        }
    
        assertNotNull(realId, " Die ID darf nicht null sein!");
        
        Genre genre = genreRepository.findById(realId);
        assertNotNull(genre, " Genre sollte existieren!");
        System.out.println(" Gefundenes Genre: " + genre.getGenrename());
    
        assertEquals("Fantasy", genre.getGenrename());
    }
    

    @Test
    @Order(3)
    void testFindAll() {
        List<Genre> genres = genreRepository.findAll();

        assertFalse(genres.isEmpty());
        assertEquals(1, genres.size());
        assertEquals("Fantasy", genres.get(0).getGenrename());
    }
    @Test
    @Order(4)
    void testUpdateGenre() throws Exception {
        Long realId = null;
    
        // Tatsächliche ID ermitteln
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM genres LIMIT 1")) {
            if (rs.next()) {
                realId = rs.getLong("id");
                System.out.println("✅ Tatsächliche ID zum Aktualisieren: " + realId);
            } else {
                fail("🚨 Keine Genres in der Datenbank gefunden!");
            }
        }
    
        assertNotNull(realId, " Die ID darf nicht null sein!");
    
        // Genre aktualisieren
        Genre genre = new Genre("Updated Genre", "Updated Beschreibung");
        genre.setId(realId);
        genreRepository.update(genre);
    
        // Aktualisiertes Genre abrufen
        Genre updatedGenre = genreRepository.findById(realId);
        assertNotNull(updatedGenre, " Das aktualisierte Genre sollte existieren!");
        assertEquals("Updated Genre", updatedGenre.getGenrename());
        System.out.println("✅ Aktualisiertes Genre: " + updatedGenre.getGenrename());
    }
    

    @Test
    @Order(5)
    void testDeleteGenre() {
        genreRepository.delete(1L);
        Genre deletedGenre = genreRepository.findById(1L);

        assertNull(deletedGenre);
    }
}
