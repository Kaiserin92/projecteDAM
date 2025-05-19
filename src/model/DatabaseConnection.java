package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author giser
 * Gestiona la connexió a la base de dades SQLite.
 */
public class DatabaseConnection {

	//URL de connexió per a la base de dades SQLite.
    private static final String DB_URL = "jdbc:sqlite:music.db";
    
    // Emmagatzema la connexió activa a la base de dades.
    private static Connection connection = null;
    
    /**
     * @author giser
     * Retorna una connexió activa a la base de dades.
     * Si la connexió no existeix o està tancada, s'estableix una nova connexió utilitzant l'URL definit.

     * @return una instància de {@link Connection} de la connexió activa.
     * @throws SQLException si es produeix algun error en establir la connexió.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Estableix la connexió a la base de dades utilitzant el URL definit
            connection = DriverManager.getConnection(DB_URL);
        }
        // Retorna la connexió activa
        return connection;
    }
}
