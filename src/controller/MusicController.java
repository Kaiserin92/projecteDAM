package controller;

import model.ComboItem;
import model.MusicDAO;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author giser
 * Controlador de la interfície que gestiona la música.
 * S'encarrega d'inicialitzar els ComboBox d'artistes i àlbums, carregar les dades des de la base de dades mitjançant
 * {@link model.MusicDAO} i actualitzar la llista de cançons en funció de la selecció realitzada.
 */
public class MusicController implements Initializable {

    /**
     * Llista de cançons mostrada a la interfície.
     */
    @FXML
    private ListView<String> songListView;
    
    /**
     * ComboBox per seleccionar artista.
     */
    @FXML
    private ComboBox<ComboItem> artistComboBox;
    
    /**
     * ComboBox per seleccionar àlbum.
     */
    @FXML
    private ComboBox<ComboItem> albumComboBox;

    /**
     * @author giser
     * Mètode d'inicialització de la vista.
     * Afegeix els elements per defecte als ComboBox, carrega els artistes i àlbums des de la base de dades i 
     * configura els esdeveniments per recarregar la llista de cançons quan hi hagi un canvi en la selecció.
     * @param location  la ubicació del fitxer FXML.
     * @param resources els recursos per a la localització.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
        // Afegim un element per defecte al ComboBox d'artistes amb opció "Tots els artistes"
        artistComboBox.getItems().add(new ComboItem(null, "Tots els artistes"));
        // Seleccionem per defecte aquest primer element
        artistComboBox.getSelectionModel().selectFirst();
        
        // Afegim un element per defecte al ComboBox d'àlbums amb opció "Tots els albums"
        albumComboBox.getItems().add(new ComboItem(null, "Tots els albums"));
        // Seleccionem per defecte aquest primer element
        albumComboBox.getSelectionModel().selectFirst();

        try {
            // Obtenim els artistes
            ResultSet rs = MusicDAO.getArtists();
            // Iterem sobre cada registre del ResultSet
            while (rs.next()) {
                // Afegim cada artista al ComboBox amb el seu id i nom
                artistComboBox.getItems().add(new ComboItem(rs.getInt("id"), rs.getString("name")));
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            // Mostrem un missatge d'error en cas de problema en carregar els artistes
            showAlert("Error al carregar artistes.");
        }

        try {
            // Obtenim els àlbums
            ResultSet rs = MusicDAO.getAlbums();
            // Iterem sobre els àlbums obtinguts
            while (rs.next()) {
                // Afegim cada àlbum al ComboBox amb el seu id i nom
                albumComboBox.getItems().add(new ComboItem(rs.getInt("id"), rs.getString("title")));
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            // Mostrem un missatge d'error en cas de problema en carregar els àlbums
            showAlert("Error al carregar albums.");
        }
        // Quan es canvia la selecció d'artistes es recarrega la llista de cançons
        artistComboBox.setOnAction(event -> reloadSongsList());
        // Quan es canvia la selecció d'àlbums es recarrega la llista de cançons
        albumComboBox.setOnAction(event -> reloadSongsList());
        
        // Inicialitza la llista de cançons amb les opcions per defecte
        reloadSongsList();
    }
    
    /**
     * @author giser
     * Recarrega la llista de cançons segons la selecció actual dels ComboBox d'artista i àlbum.
     * Neteja la llista actual de cançons i la torna a omplir amb les cançons obtingudes des de la base de dades, aplicant els filtres corresponents.
     */
    private void reloadSongsList() {
        // Neteja la llista visual de cançons prèviament mostrades
        songListView.getItems().clear();
        
        // Obté els elements seleccionats als ComboBox
        ComboItem selectedArtist = artistComboBox.getValue();
        ComboItem selectedAlbum = albumComboBox.getValue();
        
        try {
            // Obtenim les cançons que compleixen amb els filtres d'artista i àlbum
            ResultSet rs = MusicDAO.getSongs(selectedArtist.getId(), selectedAlbum.getId());
            // Iterem sobre el ResultSet per afegir cada cançó a la llista
            while (rs.next()) {
                // Cadena que mostra el títol, durada, àlbum i artista
                String display = rs.getString("title") + " | " + rs.getString("duration") + " | " +
                        rs.getString("album") + " | " + rs.getString("artist");
                // Afegim la cadena a la ListView
                songListView.getItems().add(display);
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            // Mostrem un missatge d'error si hi ha problemes en carregar les cançons
            showAlert("Error al carregar cançons.");
        }
    }
    
    /**
     * @author giser
     * Mostra una finestra d'alerta informativa amb el missatge indicat.
     * @param message el missatge que es mostrarà a l'alerta.
     */
    private void showAlert(String message) {
        // Alert informatiu
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Avis");
        alert.setHeaderText(message);
        // Mostra l'alerta i espera que l'usuari la tanqui
        alert.showAndWait();
    }
}
