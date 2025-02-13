package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookRepositoryTest {

    @BeforeAll
    static void setupDatabase() {
        // Legt die benötigten Tabellen an (CREATE TABLE IF NOT EXISTS ...).
        // Ohne diesen Aufruf existieren die Tabellen ggf. noch nicht.
        DatabaseInitializer.initialize();
    }

    @Test
    void testSaveAndFindBook() throws SQLException {
        // Stelle die Verbindung zur DB her
        Connection conn = DatabaseConnection.getConnection();

        // Ein Buch einfügen und die generierte ID zurückbekommen
        String insertQuery = "INSERT INTO books (title, author) VALUES (?, ?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, "Clean Code");
            stmt.setString(2, "Robert C. Martin");
            ResultSet rs = stmt.executeQuery();

            // Prüfe, ob ein Datensatz zurückgekommen ist
            assertTrue(rs.next(), "Es sollte genau ein Buch eingefügt werden.");
            long bookId = rs.getLong("id");

            // Das Buch anhand der erhaltenen ID wieder auslesen
            String selectQuery = "SELECT title FROM books WHERE id = ?";
            try (PreparedStatement stmt2 = conn.prepareStatement(selectQuery)) {
                stmt2.setLong(1, bookId);
                ResultSet rs2 = stmt2.executeQuery();

                assertTrue(rs2.next(), "Das eingefügte Buch sollte wieder gefunden werden.");
                assertEquals("Clean Code", rs2.getString("title"), "Der Titel sollte mit dem gespeicherten Wert übereinstimmen.");
            }
        }
    }
}
