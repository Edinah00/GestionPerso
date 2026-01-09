package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private String url;
    private String username;
    private String password;
    private Connection connection;
    
    // Constructeur privé (Singleton)
    private DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            loadConfiguration();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL non trouvé", e);
        }
    }
    
    private void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("database.properties")) {
            
            if (input != null) {
                props.load(input);
                this.url = props.getProperty("db.url");
                this.username = props.getProperty("db.username");
                this.password = props.getProperty("db.password");
            } else {
                // Valeurs par défaut
                this.url = "jdbc:postgresql://localhost:5432/wellness_db";
                this.username = "postgres";
                this.password = "postgres";
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement config: " + e.getMessage());
        }
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("✓ Connexion établie à: " + url);
        }
        return connection;
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✓ Connexion fermée");
            } catch (SQLException e) {
                System.err.println("Erreur fermeture: " + e.getMessage());
            }
        }
    }
}