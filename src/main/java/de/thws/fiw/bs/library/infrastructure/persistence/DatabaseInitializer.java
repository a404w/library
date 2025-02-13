package de.thws.fiw.bs.library.infrastructure.persistence;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // SQL-Befehl zum Erstellen von Tabellen
            String sql = "CREATE TABLE IF NOT EXISTS authors (" +
                    "id BIGINT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL); " +

                    "CREATE TABLE IF NOT EXISTS genres (" +
                    "id BIGINT PRIMARY KEY, " +
                    "genrename VARCHAR(255) NOT NULL, " +
                    "beschreibung TEXT); " +

                    "CREATE TABLE IF NOT EXISTS books (" +
                    "id BIGINT PRIMARY KEY, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "isbn VARCHAR(255) NOT NULL, " +
                    "is_available BOOLEAN NOT NULL DEFAULT TRUE); " +

                    "CREATE TABLE IF NOT EXISTS authors_books (" +
                    "author_id BIGINT, " +
                    "book_id BIGINT, " +
                    "PRIMARY KEY (author_id, book_id), " +
                    "FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE); " +

                    "CREATE TABLE IF NOT EXISTS books_genres (" +
                    "book_id BIGINT, " +
                    "genre_id BIGINT, " +
                    "PRIMARY KEY (book_id, genre_id), " +
                    "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE);";

            stmt.executeUpdate(sql);

            System.out.println("✅ Tabellen wurden erfolgreich erstellt!");

        } catch (Exception e) {
            throw new RuntimeException("❌ Fehler bei der Datenbank-Initialisierung", e);
        }
    }

    public static void main(String[] args) {
        initialize();
    }
}
