package de.thws.fiw.bs.library.infrastructure.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Verbindet sich mit der H2-Datenbank in Docker
    private static final String JDBC_URL = "jdbc:h2:tcp://localhost:1521/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("⚠️ Fehler bei der Verbindung zur H2-Datenbank!", e);
        }
    }
}
