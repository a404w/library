package de.thws.fiw.bs.library.infrastructure.persistence;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // SQL-Befehl zum Erstellen von Tabellen
            String sql = "CREATE TABLE IF NOT EXISTS books (" +
                    "id IDENTITY PRIMARY KEY, title VARCHAR(255), author VARCHAR(255));" +
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY, name VARCHAR(255));";
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
