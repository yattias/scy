/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.copexDataTool.db;

import com.scy.scyDataTool.utilities.CopexReturn;
import com.scy.scyDataTool.utilities.ScyUtilities;
import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 *applet copex - scy
 * @author Marjolaine Bodin
 */
public class ScyApplet extends JApplet{
    //PROPERTY 
    public final static Color backgroundColor = SystemColor.control;
    /* locale */
    protected Locale locale ;
    /* ressource bundle */
    protected ResourceBundle bundle;
    /* identifiant user */
    protected long dbKeyUser;
    /* identifiant mission */
    protected long dbKeyMission;
    /* mode de l'applet */
    protected int mode;
    /* nom de l'utilisateur */
    protected String userName;
    /* prenom de l'utilisateur */
    protected String firstName;

    public String getDirectoryELO() {
        return "../ELO/";
    }
    
    //METHODES
     /* initialisation de l'applet */
    protected void initScyApplet(){
         
        // Initialisation du look and feel
        try{
            String myLookAndFeel=UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(myLookAndFeel);
        }catch(Exception e){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
            JOptionPane.showMessageDialog(this , "ERREUR ans l'initialisation du lookAndFeel : "+e, "ERROR",JOptionPane.ERROR_MESSAGE);
        }
        // TODO recuperation de la provenance de l'appet : SCY OU COPEX
        // recuperation des parametres de l'applet :
        String u = getParameter("USER");
        String m = getParameter("MISSION");
        String mo = getParameter("MODE");
        userName = getParameter("USERNAME");
        firstName = getParameter("USERFIRSTNAME");
        try{
            dbKeyUser = Long.valueOf(u);
            dbKeyMission = Long.valueOf(m);
            mode = Integer.valueOf(mo);
        }catch(Throwable t){
            System.out.println(t);
            displayError(new CopexReturn(getBundleString("MSG_ERROR_APPLET_PARAM"), false), getBundleString("TITLE_DIALOG_ERROR"));
            this.stop();

            this.destroy();
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    /* retourne la locale */
    public Locale getAppletLocale(){
        return this.locale;
    }
    /**
     * retourne l'image correspondant au nom
     */
    
    
    /* affichage des erreurs*/
    public boolean displayError(CopexReturn dr, String title) {
        if (dr.mustConfirm ()){
            int erreur = JOptionPane.showConfirmDialog(this ,dr.getText() , title,JOptionPane.OK_CANCEL_OPTION); 
            if (erreur == JOptionPane.OK_OPTION)
		return true;
		
	
	}else if (dr.isError()){
            JOptionPane.showMessageDialog(this ,dr.getText() , title,JOptionPane.ERROR_MESSAGE); 
		
	
	}else if (dr.isWarning()){
            JOptionPane.showMessageDialog(this ,dr.getText() , title,JOptionPane.WARNING_MESSAGE); 
		
	}
        return false;
    }
    /* retourne un message selon cle*/
    public String getBundleString(String key){
        String s = "";
        try{
            s = this.bundle.getString(key);
        }catch(Exception e){
            try{
                String msg = this.bundle.getString("ERROR_KEY");
                msg = ScyUtilities.replace(msg, 0, key);
                displayError(new CopexReturn(msg, false) , this.bundle.getString("TITLE_DIALOG_ERROR")); 
            }catch(Exception e2){
                displayError(new CopexReturn("Aucun message trouvé !", false) ,"ERROR");
             }
        }
        return s;
    }
    
    
     
     /// A REDEFINIR DANS LES APPLETS
     /* repertoire images*/
     public String getDirectoryImage(){
        return "Images/";
      }
     /* repertoire php */
    public String getDirectoryPhp(){
        return "../dataTool/InterfaceServer/";
      }
     /* repertoire data*/
     public String getDirectoryData(){
        return "../dataTool/InterfaceServer/data/";
      }
     
   
}
