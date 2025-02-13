package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



class BookRepositoryTest {

    @Test
    void testSaveAndFindBook() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        
        // Buch in DB speichern
        String insertQuery = "INSERT INTO books (title, author) VALUES (?, ?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, "Clean Code");
            stmt.setString(2, "Robert C. Martin");
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Ein Buch sollte eingefügt werden");
            long bookId = rs.getLong("id");
            
            // Buch aus DB abrufen
            String selectQuery = "SELECT title FROM books WHERE id = ?";
            try (PreparedStatement stmt2 = conn.prepareStatement(selectQuery)) {
                stmt2.setLong(1, bookId);
                ResultSet rs2 = stmt2.executeQuery();
                assertTrue(rs2.next(), "Buch sollte gefunden werden");
                assertEquals("Clean Code", rs2.getString("title"), "Titel sollte übereinstimmen");
            }
        }
    }
}
