package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseConnection;
import de.thws.fiw.bs.library.infrastructure.persistence.DatabaseInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookServiceTest {

    @BeforeAll
    static void setupDatabase() {

        DatabaseInitializer.initialize();
    }

    @Test
    void testSaveAndFindBook() throws SQLException {

        Connection conn = DatabaseConnection.getConnection();


        String insertQuery = "INSERT INTO books (title, author) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Clean Code");
            stmt.setString(2, "Robert C. Martin");
            

            int rowsInserted = stmt.executeUpdate();
            assertEquals(1, rowsInserted, "Es sollte genau ein Buch eingefügt werden.");


            try (ResultSet rs = stmt.getGeneratedKeys()) {
                assertTrue(rs.next(), "Es sollte einen generierten Key geben.");
                long bookId = rs.getLong(1);

                
                String selectQuery = "SELECT title, author FROM books WHERE id = ?";
                try (PreparedStatement stmt2 = conn.prepareStatement(selectQuery)) {
                    stmt2.setLong(1, bookId);
                    try (ResultSet rs2 = stmt2.executeQuery()) {
                        assertTrue(rs2.next(), "Das eingefügte Buch sollte gefunden werden.");
                        assertEquals("Clean Code", rs2.getString("title"), "Titel sollte mit dem gespeicherten Wert übereinstimmen.");
                        assertEquals("Robert C. Martin", rs2.getString("author"), "Autor sollte mit dem gespeicherten Wert übereinstimmen.");
                    }
                }
            }
        }
    }
}
