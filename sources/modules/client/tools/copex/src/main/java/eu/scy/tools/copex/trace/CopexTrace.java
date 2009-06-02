/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import java.sql.Date;
import java.sql.Time;
import org.jdom.Element;

/**
 * représente un item dans le fichier trace
 * @author MBO
 */
public class CopexTrace {

    // ATTRIBUTS
    /* identifiant */
    private String id;
    /* date  */
    private Date date;
    /* heure */
    private Time time;
    /* espace */
    private String assignement;
    /* action */
    private EdPTrace action;

    // CONSTRUCTEURS 
    public CopexTrace(String id, Date date, Time time, EdPTrace action) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.assignement = "PROCEDURE-Editor";
        this.action = action;
    }

    // GETTER AND SETTER
    public EdPTrace getAction() {
        return action;
    }

    public String getAssignement() {
        return assignement;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }
    
    // METHODES 
    public String toStringXML(){
        String xml = "<item \n";
        xml += "    identifier='"+this.id+"' \n";
        xml += "    time='"+getXMLTime()+"' \n";
        xml += "    type= '"+action.getType()+"' \n";
        xml += "    assignement= '"+this.assignement+"' \n";
        xml += "    <structural> \n";
        if(action instanceof TraceAction)
            xml += ((TraceAction)action).getParameterToXML();
        xml += "    </structural> \n ";
        xml += "</item> \n";
        return xml;
    }
    
    /* convertit le temps au format suivant : 
     * YYYY-DD-MMTHH:MM:SS
     */
    private String getXMLTime(){
        String xmlTime = "";
        return xmlTime;
    }

    private Element toXML(){
        return null;
    }
    
    
}
