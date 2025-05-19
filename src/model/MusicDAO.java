package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author giser
 * Classe Data Access Object (DAO) per accedir a les dades relacionades amb la música.
 * Proporciona mètodes estàtics per obtenir artistes, àlbums i cançons des de la base de dades.
 */
public class MusicDAO {
    
    /**
     * @author giser
     * Obté la llista d'artistes des de la base de dades.
     * @return un {@link ResultSet} que conté els camps "id" i "name" de la taula "artist".
     * @throws SQLException si es produeix un error en l'execució de la consulta.
     */
    public static ResultSet getArtists() throws SQLException {
        // Obtenim la connexió a la base de dades
        Connection conn = DatabaseConnection.getConnection();
        // Creem un statement per executar consultes SQL
        Statement stmt = conn.createStatement();
        // Establim un temps màxim d'execució (timeout) de 5 segons per a la consulta
        stmt.setQueryTimeout(5);
      
        // Executem la consulta per obtenir els camps "id" i "name" de la taula "artist"
        return stmt.executeQuery("SELECT id, name FROM artist");
    }
    
    /**
     * @author giser
     * Obté la llista d'àlbums des de la base de dades.
     * @return un {@link ResultSet} que conté els camps "id" i "name", renombrat com a "title", de la taula "album".
     * @throws SQLException si es produeix un error en l'execució de la consulta.
     */
    public static ResultSet getAlbums() throws SQLException {
        // Obtenim la connexió a la base de dades
        Connection conn = DatabaseConnection.getConnection();
        // Creem un statement per executar consultes SQL
        Statement stmt = conn.createStatement();
        // Establim el timeout a 5 segons
        stmt.setQueryTimeout(5);
    
        // Executem la consulta per obtenir els camps "id" i "name" de la taula "album"
        return stmt.executeQuery("SELECT id, name AS title FROM album");
    }
    
    /**
     * @author giser
     * Obté la llista de cançons de la base de dades aplicant opcionalment filtres per artista i/o àlbum.
     * Si es proporciona un identificador d'artista i d'àlbum, la consulta retorna només les cançons que compleixen amb ambdós filtres.
     * Si només es proporciona un filtre, retorna les cançons que compleixen amb aquest filtre.
     * Si no es proporciona cap filtre, retorna totes les cançons.
     * @param artistId l'identificador de l'artista; pot ser <code>null</code> per ometre aquest filtre.
     * @param albumId  l'identificador de l'àlbum; pot ser <code>null</code> per ometre aquest filtre.
     * @return un {@link ResultSet} amb les cançons que compleixen els filtres indicats.
     * @throws SQLException si es produeix un error en l'execució de la consulta.
     */
    public static ResultSet getSongs(Integer artistId, Integer albumId) throws SQLException {
        // Obtenim la connexió a la base de dades
        Connection conn = DatabaseConnection.getConnection();
        // Creem un statement per executar la consulta SQL
        Statement stmt = conn.createStatement();
        // Establim el timeout a 5 segons
        stmt.setQueryTimeout(5);

        // Construïm la consulta bàsica que uneix les taules "song", "album" i "artist"
        String query = 
            "SELECT s.id, s.name AS title, s.duration, al.name AS album, ar.name AS artist " +
            "FROM song s " +
            "JOIN album al ON s.album_id = al.id " +
            "JOIN artist ar ON al.artist_id = ar.id";
        
        // Afegim condicions WHERE segons els filtres seleccionats
        if (artistId != null && albumId != null) {
            // Si s'han seleccionat tant artista com àlbum, es filtren les dues condicions
            query += " WHERE ar.id = " + artistId + " AND al.id = " + albumId;
        } else if (artistId != null) {
            // Si només s'ha seleccionat un artista, es filtra per artista
            query += " WHERE ar.id = " + artistId;
        } else if (albumId != null) {
            // Si només s'ha seleccionat un àlbum, es filtra per àlbum
            query += " WHERE al.id = " + albumId;
        }
        
        // Executem la consulta i retornem el ResultSet amb les cançons trobades
        return stmt.executeQuery(query);
    }
}
