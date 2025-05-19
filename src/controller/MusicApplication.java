package controller;

import model.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author giser
 * Aplicació principal per a la gestió de música.
 * S'encarrega d'inicialitzar la base de dades i carregar la interfície gràfica de l'aplicació mitjançant un fitxer FXML.
 */
public class MusicApplication extends Application {

    /**
     * @author giser
     * Mètode d'inicialització de l'aplicació JavaFX.
     * Aquest mètode inicialitza la base de dades, carrega el fitxer FXML que defineix la interfície principal, configura la finestra principal
     * i finalment la mostra.
     * @param primaryStage la finestra principal de l'aplicació.
     * @throws Exception en cas d'error durant el carregament de la interfície.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inicialitza la base de dades
        DatabaseInitializer.initialize();
        
        // Carrega el fitxer FXML que defineix la interfície principal (vista)
        Parent root = FXMLLoader.load(getClass().getResource("/View/MainView.fxml"));
        
        // Estableix el títol de la finestra principal
        primaryStage.setTitle("Music Apllication");
        
        // Crea una nova escena amb la vista carregada i una mida determinada (640x480 píxels)
        primaryStage.setScene(new Scene(root, 640, 480));
        
        // Mostra la finestra principal
        primaryStage.show();
    }
    
    /**
     * Mètode principal que llança l'aplicació.
     */
    public static void main(String[] args) {
        // Llença l'aplicació, la qual invoca internament el mètode start()
        launch(args);
    }
}
