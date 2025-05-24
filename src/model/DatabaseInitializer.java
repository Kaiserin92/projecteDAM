package model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * @author giser
 * Encarregada d'inicialitzar la base de dades.
 * Llegeix les sentències SQL del fitxer "/musicdb.sql" i les executa per establir l'estructura
 * i les dades inicials de la base de dades.
 */
public class DatabaseInitializer {
    
    /**
     * @author giser
     * Inicialitza la base de dades executant les sentències SQL contingudes en el fitxer "/musicdb.sql".
     * El mètode llegeix el fitxer SQL, ignora les línies que són comentaris o estan buides,
     * separa les consultes per punt i coma i les executa una per una.
     * Amb aquesta modificació, s'executa la inicialització només si la taula "song" no existeix.
     */
    public static void initialize() {
        // Utilitzem un try-with-resources per assegurar-nos que la connexió es tanca adequadament
        try (Connection conn = DatabaseConnection.getConnection()){
            
            // Comprovem si la taula "song" existeix (assumeix que la base de dades ja està inicialitzada si existeix)
            boolean isInitialized = false;
            java.sql.DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, "song", new String[] {"TABLE"})) {
                if(rs.next()){
                    isInitialized = true;
                }
            }
            if (isInitialized) {
                System.out.println("Base de dades ja inicialitzada, saltant inicialització.");
                return;
            }
            
            // Obte les dades del fitxer SQL ubicat dins del directori de recursos
            InputStream is = DatabaseInitializer.class.getResourceAsStream("/musicdb.sql");
            if (is == null) {
                // Si no es troba el fitxer, s'informa de l'error i surt del mètode
                System.err.println("No s'ha trobat l'arxiu a /musicdb.sql");
                return;
            }
            // Llegeix el fitxer
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            
            // Iterem per cada línia del fitxer
            while ((line = reader.readLine()) != null) {
                // Ignorem les línies que són comentaris (comencen amb "--") o que estan buides
                if (!line.trim().startsWith("--") && !line.trim().isEmpty()) {
                    // Afegim la línia al StringBuilder i una nova línia per separar les sentències
                    sqlBuilder.append(line).append("\n");
                }
            }
            reader.close();
            
            // Separamos les sentències SQL per punt i coma
            String[] queries = sqlBuilder.toString().split(";");
            // Creem un statement per executar les consultes
            Statement stmt = conn.createStatement();
            // Iterem sobre cada consulta
            for (String query : queries) {
                // Si la consulta no està buida, la executem
                if (!query.trim().isEmpty()) {
                    stmt.execute(query);
                }
            }
            stmt.close();
            System.out.println("Base de dades inicialitzada correctament.");
        } catch (Exception e) {
            // En cas de qualsevol error, s'imprimeix un missatge i la pila d'excepcions
            System.err.println("Error al inicialitzar la base de dades: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
