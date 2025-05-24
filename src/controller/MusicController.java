package controller;

import model.ComboItem;
import model.MusicDAO;
import model.DatabaseConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

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
    
    // Llista auxiliar per emmagatzemar els identificadors de les cançons.
    private List<Integer> songIds = new ArrayList<>();

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
        // Neteja la llista d'identificadors
        songIds.clear();
        
        // Obté els elements seleccionats als ComboBox
        ComboItem selectedArtist = artistComboBox.getValue();
        ComboItem selectedAlbum = albumComboBox.getValue();
        
        try {
            // Obtenim les cançons que compleixen amb els filtres d'artista i àlbum
            ResultSet rs = MusicDAO.getSongs(selectedArtist.getId(), selectedAlbum.getId());
            // Iterem sobre el ResultSet per afegir cada cançó a la llista
            while (rs.next()) {
                // Afegim l'identificador de la cançó a la llista auxiliar
                songIds.add(rs.getInt("id"));
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
    
    /**
     * Maneja la acció per afegir una cançó.
     * @param event l'esdeveniment generat en clicar el botó.
     */
    @FXML
    private void addSong(ActionEvent event) {
        // Creem un diàleg per afegir la cançó
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Afegir cançó");
        dialog.setHeaderText("Introdueix les dades de la nova cançó");
        ButtonType addButtonType = new ButtonType("Afegir", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Títol");

        TextField durationField = new TextField();
        durationField.setPromptText("Durada");

        // Desplegable per a seleccionar l'àlbum (les dades relacionades amb altres taules)
        ComboBox<ComboItem> albumComboBoxDialog = new ComboBox<>();
        try {
            ResultSet rsAlbums = MusicDAO.getAlbums();
            while (rsAlbums.next()) {
                ComboItem item = new ComboItem(rsAlbums.getInt("id"), rsAlbums.getString("title"));
                albumComboBoxDialog.getItems().add(item);
            }
            rsAlbums.getStatement().close();
        } catch (SQLException ex) {
            showAlert("Error al carregar els àlbums.");
        }

        grid.add(new Label("Títol:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Durada:"), 0, 1);
        grid.add(durationField, 1, 1);
        grid.add(new Label("Àlbum:"), 0, 2);
        grid.add(albumComboBoxDialog, 1, 2);

        dialog.getDialogPane().setContent(grid);
        titleField.requestFocus();

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == addButtonType) {
            String newTitle = titleField.getText().trim();
            String newDurationStr = durationField.getText().trim();
            if (newTitle.isEmpty() || newDurationStr.isEmpty() || albumComboBoxDialog.getSelectionModel().isEmpty()) {
                showAlert("Tots els camps han d'estar completats.");
                return;
            }
            double newDuration;
            try {
                newDuration = Double.parseDouble(newDurationStr);
            } catch (NumberFormatException nfe) {
                showAlert("La durada ha de ser un número.");
                return;
            }
            ComboItem selectedAlbum = albumComboBoxDialog.getSelectionModel().getSelectedItem();
            int newAlbumId = selectedAlbum.getId();

            try {
                Connection conn = DatabaseConnection.getConnection();
                // Obtenim un nou ID per la cançó (màxim existent + 1)
                int newId = 1;
                String maxSql = "SELECT MAX(id) AS maxId FROM song";
                PreparedStatement maxStmt = conn.prepareStatement(maxSql);
                ResultSet rsMax = maxStmt.executeQuery();
                if (rsMax.next()) {
                    newId = rsMax.getInt("maxId") + 1;
                }
                rsMax.close();
                maxStmt.close();

                // Inserim la nova cançó
                String insertSql = "INSERT INTO song (id, album_id, name, duration) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, newId);
                insertStmt.setInt(2, newAlbumId);
                insertStmt.setString(3, newTitle);
                insertStmt.setDouble(4, newDuration);
                int affected = insertStmt.executeUpdate();
                insertStmt.close();
                if (affected > 0) {
                    showAlert("Cançó afegida correctament.");
                } else {
                    showAlert("No s'ha pogut afegir la cançó.");
                }
                // Actualitzem la llista de cançons després d'afegir
                reloadSongsList();
            } catch (SQLException e) {
                showAlert("Error al afegir la cançó: " + e.getMessage());
            }
        }
    }


    /**
     * Maneja la acció per modificar una cançó.
     * @param event l'esdeveniment generat en clicar el botó.
     */
    @FXML
    private void modifySong(ActionEvent event) {
        // Comprovem si s'ha seleccionat una cançó
        int selectedIndex = songListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showAlert("Selecciona una cançó per modificar.");
            return;
        }
        // Obtenim l'identificador de la cançó seleccionada
        int songId = songIds.get(selectedIndex);
        
        try {
            // Recuperem les dades actuals de la cançó
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT name, duration, album_id FROM song WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, songId);
            ResultSet rs = pstmt.executeQuery();
            String currentTitle = "";
            double currentDuration = 0;
            int currentAlbumId = -1;
            if (rs.next()) {
                currentTitle = rs.getString("name");
                currentDuration = rs.getDouble("duration");
                currentAlbumId = rs.getInt("album_id");
            }
            rs.close();
            pstmt.close();
            
            // Creem un diàleg per modificar la cançó
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Modificar cançó");
            dialog.setHeaderText("Modifica les dades de la cançó");
            ButtonType updateButtonType = new ButtonType("Actualitzar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);
            
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            
            TextField titleField = new TextField();
            titleField.setPromptText("Títol");
            titleField.setText(currentTitle);
            
            TextField durationField = new TextField();
            durationField.setPromptText("Durada");
            durationField.setText(String.valueOf(currentDuration));
            
            // Desplegable per a seleccionar l'àlbum (les dades relacionades amb altres taules)
            ComboBox<ComboItem> albumComboBoxDialog = new ComboBox<>();
            try {
                ResultSet rsAlbums = MusicDAO.getAlbums();
                while (rsAlbums.next()) {
                    ComboItem item = new ComboItem(rsAlbums.getInt("id"), rsAlbums.getString("title"));
                    albumComboBoxDialog.getItems().add(item);
                    if (rsAlbums.getInt("id") == currentAlbumId) {
                        albumComboBoxDialog.getSelectionModel().select(item);
                    }
                }
                rsAlbums.getStatement().close();
            } catch (SQLException ex) {
                showAlert("Error al carregar els àlbums.");
            }
            
            grid.add(new Label("Títol:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Durada:"), 0, 1);
            grid.add(durationField, 1, 1);
            grid.add(new Label("Àlbum:"), 0, 2);
            grid.add(albumComboBoxDialog, 1, 2);
            
            dialog.getDialogPane().setContent(grid);
            titleField.requestFocus();
            
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == updateButtonType) {
                String newTitle = titleField.getText().trim();
                String newDurationStr = durationField.getText().trim();
                if (newTitle.isEmpty() || newDurationStr.isEmpty() || albumComboBoxDialog.getSelectionModel().isEmpty()) {
                    showAlert("Tots els camps han d'estar completats.");
                    return;
                }
                double newDuration;
                try {
                    newDuration = Double.parseDouble(newDurationStr);
                } catch (NumberFormatException nfe) {
                    showAlert("La durada ha de ser un número.");
                    return;
                }
                ComboItem selectedAlbum = albumComboBoxDialog.getSelectionModel().getSelectedItem();
                int newAlbumId = selectedAlbum.getId();
                
                String updateSql = "UPDATE song SET name = ?, duration = ?, album_id = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, newTitle);
                updateStmt.setDouble(2, newDuration);
                updateStmt.setInt(3, newAlbumId);
                updateStmt.setInt(4, songId);
                int affected = updateStmt.executeUpdate();
                updateStmt.close();
                if (affected > 0) {
                    showAlert("Cançó modificada correctament.");
                } else {
                    showAlert("No s'ha pogut modificar la cançó.");
                }
                // Actualitzem la llista de cançons després de modificar
                reloadSongsList();
            }
        } catch (SQLException e) {
            showAlert("Error al recuperar les dades de la cançó: " + e.getMessage());
        }
    }

    /**
     * Maneja la acció per eliminar una cançó.
     * @param event l'esdeveniment generat en clicar el botó.
     */
    @FXML
    private void deleteSong(ActionEvent event) {
        // Comprovem si s'ha seleccionat una cançó
        int selectedIndex = songListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showAlert("Selecciona una cançó per eliminar.");
            return;
        }
        // Obtenim l'identificador de la cançó seleccionada
        int songId = songIds.get(selectedIndex);
        
        // Mostrem un diàleg de confirmació
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmació");
        confirmAlert.setHeaderText("Vols eliminar la cançó seleccionada?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Obtenim la connexió a la base de dades
                Connection conn = DatabaseConnection.getConnection();
                // Creem la sentència preparada per eliminar la cançó
                String sql = "DELETE FROM song WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, songId);
                int affected = pstmt.executeUpdate();
                pstmt.close();
                if (affected > 0) {
                    showAlert("Cançó eliminada correctament.");
                } else {
                    showAlert("No s'ha pogut eliminar la cançó.");
                }
            } catch (SQLException e) {
                showAlert("Error al eliminar la cançó: " + e.getMessage());
            }
            // Actualitzem la llista de cançons després d'eliminar
            reloadSongsList();
        }
    }
}
