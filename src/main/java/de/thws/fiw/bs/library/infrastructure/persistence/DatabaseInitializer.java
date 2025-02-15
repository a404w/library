package de.thws.fiw.bs.library.infrastructure.persistence;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS authors (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL); " +

                    "CREATE TABLE IF NOT EXISTS genres (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "genrename VARCHAR(255) NOT NULL, " +
                    "beschreibung TEXT); " +

                    "CREATE TABLE IF NOT EXISTS books (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "isbn VARCHAR(255) NOT NULL UNIQUE, " +
                    "is_available BOOLEAN NOT NULL DEFAULT TRUE); " +

                    "CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) UNIQUE NOT NULL); " +

                    // **Verknüpfungstabelle für `books` und `authors` (Many-to-Many)**
                    "CREATE TABLE IF NOT EXISTS book_author (" +
                    "book_id BIGINT, " +
                    "author_id BIGINT, " +
                    "PRIMARY KEY (book_id, author_id), " +
                    "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE); " +

                    // **Verknüpfungstabelle für `books` und `genres` (Many-to-Many)**
                    "CREATE TABLE IF NOT EXISTS book_genre (" +
                    "book_id BIGINT, " +
                    "genre_id BIGINT, " +
                    "PRIMARY KEY (book_id, genre_id), " +
                    "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE); " +

                    // Verknüpfungstabelle für ausgeliehene Bücher (User ↔ Books)
                    "CREATE TABLE IF NOT EXISTS loans (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id BIGINT NOT NULL, " +
                    "book_id BIGINT NOT NULL, " +
                    "from_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "to_date TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE); " +

                    // **Verknüpfungstabelle für Reservierungen (User ↔ Books)**
                    "CREATE TABLE IF NOT EXISTS reservations (" +
                    "    id BIGINT AUTO_INCREMENT PRIMARY KEY, " + // ✅ ID hinzugefügt
                    "    user_id BIGINT, " +
                    "    book_id BIGINT, " +
                    "    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                    "    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE); ";

            stmt.executeUpdate(sql);

            System.out.println("✅ Alle Tabellen wurden erfolgreich erstellt!");

        } catch (Exception e) {
            throw new RuntimeException("❌ Fehler bei der Datenbank-Initialisierung", e);
        }
    }

    public static void main(String[] args) {
        initialize();
    }
}
