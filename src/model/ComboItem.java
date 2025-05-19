package model;

/**
 * @author giser
 * Representa un element per a ser utilitzat en un ComboBox.
 * Cada element conté un identificador i una descripció que es mostrarà a la interfície gràfica.
 */
public class ComboItem {
    
    private Integer id;
    private String description;
    
    /**
     * @author giser
     * Constructor que crea un element amb el id i la descripció.
     * @param id el identificador de l'element.
     * @param description la descripció de l'element.
     */
    public ComboItem(Integer id, String description){
        this.id = id;
        this.description = description;
    }
    
    /**
     * @author giser
     * Retorna l'id de l'element.
     * @return l'id de l'element.
     */
    public Integer getId(){
        return id;
    }
    
    /**
     * @author giser
     * Retorna la descripció de l'element.
     * @return la descripció de l'element.
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * @author giser
     * Retorna una representació en forma de cadena de l'element.
     * Aquesta implementació retorna la descripció de l'element, utilitzada per mostrar-lo correctament en el ComboBox.
     * @return la descripció de l'element.
     */
    @Override
    public String toString(){
        return description;
    }
}
