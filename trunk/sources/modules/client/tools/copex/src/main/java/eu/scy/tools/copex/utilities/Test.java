package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.db.AccesDB;
import eu.scy.tools.copex.db.DataBaseCommunication;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marjolaine
 */
public class Test {
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        URL url;
        try {
            url = new URL("http://copex.imag.fr/espaces/");
            DataBaseCommunication dbC = new DataBaseCommunication(url, MyConstants.DB_COPEX_EDP, 1, "1");
            String query = "UPDATE COPEX_TASK SET DESCRIPTION = 'Déterminer la structure à modifier' WHERE ID_TASK = 1981 ;";
            //String query = "UPDATE MATERIAL SET DESCRIPTION = 'Organisme animal ou végétal adapté à son milieu' WHERE ID_MATERIAL in (169,170) ;";

            String description = "<p>Vous allez chercher à répondre au problème suivant :  Comment modifier l'information génétique d'un être vivant " +
                    "et vérifier la présence de cette modification ? </p>" +
                    "<p align='center'>&#8226;</p>" +
                    "<p>Pour résoudre ce problème vous allez tout d'abord déterminer quel est le support de l'information génétique " +
                    "et la cible de la modification que vous allez effectuer. " +
                    "La deuxième étape sera de déterminer le mode d'action de cette modification " +
                    "par le moyen de votre choix. " +
                    "Enfin, vous chercherez comment vérifier la présence de cette modification." +
                    " </p><p align='center'>&#8226;</p>" +
                    "<p >Pour réaliser cette mission, 4 productions vous sont demandées : </p>" +
                    "<ul>" +
                    "  <li>Un texte décrivant vos idées pour résoudre le problème. </li>" +
                    "  <li>Un dessin ou un schéma représentant la façon dont vous allez résoudre le problème. </li>" +
                    "  <li>Un protocole décrivant les actions à suivre. </li>" +
                    "  <li>Et enfin, un texte présentant les résultats que vous attendez. </li>" +
                    "</ul>" +
                    "<p align='center'>&#8226;</p>" +
                    "<p > Afin de fournir ces quatre documents à la fin de la séance, vous naviguerez dans le site web COPEX pour utiliser " +
                    "l'ensemble des outils mis à votre disposition. " +
                    "</p><p align='center'>&#8226;</p>" +
                    "<p > Attention : l'ensemble des documents que vous allez concevoir doivent être assez clair et précis pour être lus et compris " +
                    "par un élève de seconde. </p>" +
                    "</p><p align='center'>&#8226;</p>" +
                    "<p > C'est parti ! Rendez-vous dans l'espace publication. </p>";
            
        
            description =  AccesDB.replace("\'",description,"''") ;
            //String query = "UPDATE COPEX_MISSION SET DESCRIPTION = '"+description+"' WHERE ID_MISSION = 5; ";
            System.out.println("query : "+query);
            ArrayList v = new ArrayList();
            String[] querys = new String[1];
            querys[0] = query ;
            CopexReturn cr = dbC.executeQuery(querys, v);
            if(cr.isError()){
                System.out.println("error "+cr.getText());
            }
            System.out.println("réussi");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
