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
            String query = "UPDATE COPEX_TASK SET DESCRIPTION = 'Determiner la structure a modifier' WHERE ID_TASK = 1981 ;";
            //String query = "UPDATE MATERIAL SET DESCRIPTION = 'Organisme animal ou vegetal adapte a son milieu' WHERE ID_MATERIAL in (169,170) ;";

            String description = "<p>Vous allez chercher a repondre au probleme suivant :  Comment modifier l'information genetique d'un etre vivant " +
                    "et verifier la presence de cette modification ? </p>" +
                    "<p align='center'>&#8226;</p>" +
                    "<p>Pour resoudre ce probleme vous allez tout d'abord determiner quel est le support de l'information genetique " +
                    "et la cible de la modification que vous allez effectuer. " +
                    "La deuxieme etape sera de determiner le mode d'action de cette modification " +
                    "par le moyen de votre choix. " +
                    "Enfin, vous chercherez comment verifier la presence de cette modification." +
                    " </p><p align='center'>&#8226;</p>" +
                    "<p >Pour realiser cette mission, 4 productions vous sont demandees : </p>" +
                    "<ul>" +
                    "  <li>Un texte decrivant vos idees pour resoudre le probleme. </li>" +
                    "  <li>Un dessin ou un schema representant la facon dont vous allez resoudre le probleme. </li>" +
                    "  <li>Un protocole decrivant les actions a suivre. </li>" +
                    "  <li>Et enfin, un texte presentant les resultats que vous attendez. </li>" +
                    "</ul>" +
                    "<p align='center'>&#8226;</p>" +
                    "<p > Afin de fournir ces quatre documents a la fin de la seance, vous naviguerez dans le site web COPEX pour utiliser " +
                    "l'ensemble des outils mis a votre disposition. " +
                    "</p><p align='center'>&#8226;</p>" +
                    "<p > Attention : l'ensemble des documents que vous allez concevoir doivent etre assez clair et precis pour etre lus et compris " +
                    "par un eleve de seconde. </p>" +
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
            System.out.println("reussi");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
