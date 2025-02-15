package de.thws.fiw.bs.library.infrastructure.presistence;

import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseInitializer;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.UserRepositoryImpl;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryImplTest {
    private Connection connection;
    private UserRepositoryImpl userRepository;

    @BeforeAll
    void setUpDatabase() throws Exception {
        connection = DatabaseConnection.getConnection();
        DatabaseInitializer.initialize();
        userRepository = new UserRepositoryImpl();
    }

    @BeforeEach
    void insertTestData() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO users (name, email) VALUES ('Alice Wonderland', 'alice@example.com');");
            System.out.println(" Test-Daten für Benutzer eingefügt.");
        }
    }

    @AfterEach
    void clearDatabase() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM users;");
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


        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM users LIMIT 1")) {
            if (rs.next()) {
                realId = rs.getLong("id");
                System.out.println(" Tatsächliche ID zum Suchen: " + realId);
            } else {
                fail(" Keine Benutzer in der Datenbank gefunden!");
            }
        }

        assertNotNull(realId, " Die ID darf nicht null sein!");


        User user = userRepository.findById(realId);
        assertNotNull(user, " Der Benutzer sollte existieren!");
        System.out.println("🔍 Gefundener Benutzer: " + user.getName());

        assertEquals("Alice Wonderland", user.getName());
    }

    @Test
    @Order(2)
    void testSaveUser() {
        User user = new User("Bob Builder", "bob@example.com");
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Bob Builder", savedUser.getName());
        System.out.println(" Gespeicherter Benutzer: " + savedUser.getName());
    }

    @Test
    @Order(3)
    void testFindAll() {
        List<User> users = userRepository.findAll();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("Alice Wonderland", users.get(0).getName());
        System.out.println(" Anzahl gefundener Benutzer: " + users.size());
    }

    @Test
    @Order(4)
    void testUpdateUser() throws Exception {
        Long realId = null;


        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM users LIMIT 1")) {
            if (rs.next()) {
                realId = rs.getLong("id");
                System.out.println(" Tatsächliche ID zum Aktualisieren: " + realId);
            } else {
                fail(" Keine Benutzer in der Datenbank gefunden!");
            }
        }

        assertNotNull(realId, " Die ID darf nicht null sein!");

        User user = new User("Updated Alice", "alice.new@example.com");
        user.setId(realId);
        userRepository.update(user);

        User updatedUser = userRepository.findById(realId);
        assertNotNull(updatedUser, " Der aktualisierte Benutzer sollte existieren!");
        assertEquals("Updated Alice", updatedUser.getName());
        System.out.println(" Aktualisierter Benutzer: " + updatedUser.getName());
    }

    @Test
    @Order(5)
    void testDeleteUser() throws Exception {
        Long realId = null;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM users LIMIT 1")) {
            if (rs.next()) {
                realId = rs.getLong("id");
                System.out.println(" Tatsächliche ID zum Löschen: " + realId);
            } else {
                fail(" Keine Benutzer in der Datenbank gefunden!");
            }
        }

        assertNotNull(realId, " Die ID darf nicht null sein!");

        userRepository.delete(realId);

        User deletedUser = userRepository.findById(realId);
        assertNull(deletedUser, " Der Benutzer sollte gelöscht sein!");
        System.out.println(" Benutzer mit ID=" + realId + " erfolgreich gelöscht.");
    }
}
