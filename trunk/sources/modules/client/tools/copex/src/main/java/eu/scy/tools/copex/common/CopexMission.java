/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;


/**
 * represente une mission COPEX
 * @author MBO
 */
public class CopexMission implements Cloneable{
    // CONSTANTES
    /* statut de la mission : a traiter*/
    public static char STATUT_MISSION_TREAT = 'T';
    /* statut de la mission : en cours */
    public static char STATUT_MISSION_ON = 'C';
    /* statut de la mission : archive*/
    public static char STATUT_MISSION_ARCHIVE = 'A';
    
    
    // ATTRIBUTS
    /* cle primaire */ 
    private long dbKey;
    /* nom de la mission */
    private String name;
    /* code de la mission */
    private String code;
    /* resume */
    private String sumUp;
    /* description  : fichier attache */
    private String description;
    /* date de creation */
    /* date de derniere modification */
    /* statut cf constantes STATUT_MISSION_XXX */
    private char statut;
    /* active */
    private boolean activ;
   
    /* options */
    private OptionMission options;

    // CONSTRUCTEURS
    public CopexMission(long dbKey, String name, String code, String sumUp, char statut, OptionMission options){
        this.dbKey = dbKey;
        this.name = name;
        this.code = code;
        this.sumUp = sumUp;
        this.description = null;
        this.statut = statut;
        this.activ = true;
        this.options = options;
    }
    public CopexMission(long dbKey, String name, String code, String sumUp, String description, char statut, OptionMission options){
        this.dbKey = dbKey;
        this.name = name;
        this.code = code;
        this.sumUp = sumUp;
        this.description = description;
        this.statut = statut;
        this.activ = true;
        this.options = options;
    }
    public CopexMission(String name, String code, String sumUp, char statut, boolean activ, OptionMission options) {
        this.name = name;
        this.code = code;
        this.sumUp = sumUp;
        this.description = null;
        this.statut = statut;
        this.activ = activ;
        this.options = options;
    }

   public CopexMission(String code, String name, String sumUp, String description, OptionMission options){
       this.name = name;
       this.code = code;
       this.sumUp = sumUp;
       this.description = description;
       this.dbKey = -1;
       this.statut = STATUT_MISSION_ON ;
       this.activ = true;
       this.options = options;
   }
   
     @Override
    public Object clone()  {
        try {
            CopexMission mission = (CopexMission) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.name);
            String codeC = "";
            if (this.code != null)
                codeC = new String(this.code);
            String sumUpC = "";
            if (this.sumUp != null)
                sumUpC = new String(sumUp);
            String descriptionC = "";
            if (this.description != null)
                descriptionC = new String(description);
            char statutC = new Character(this.statut);
            boolean activC = this.activ;
            
            if(this.options != null)
                mission.setOptions((OptionMission)options.clone());
            mission.setDbKey(dbKeyC);
            mission.setName(nameC);
            mission.setCode(codeC);
            mission.setSumUp(sumUpC);
            mission.setDescription(descriptionC);
            mission.setStatut(statutC);
            mission.setActiv(activC);
            return mission;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    // GETTER AND SETTER
     public String getName() {
        return name;
    }

    public long getDbKey() {
        return dbKey;
    }

   
    public OptionMission getOptions() {
        return options;
    }

    public void setOptions(OptionMission options) {
        this.options = options;
    }

    public String getCode() {
        return code;
    }

    public String getSumUp() {
        return sumUp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public void setSumUp(String sumUp) {
        this.sumUp = sumUp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }
    
    
}
