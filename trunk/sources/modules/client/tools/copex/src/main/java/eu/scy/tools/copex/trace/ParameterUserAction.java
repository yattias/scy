/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * parametres d'une action
 * @author MBO
 */
public class ParameterUserAction {
    // ATTRIBUTS
    /* identifiant base de donnees */
    private long dbKey;
    /* nom */
    private String name;
    /* valeur */
    private String value;

    // CONSTRUCTEUR
    public ParameterUserAction(String name, String value) {
        this.name = name;
        this.value = value;
    }

    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
    
}
