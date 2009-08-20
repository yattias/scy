/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.saveProcXml;

import eu.scy.tools.copex.common.ExperimentalProcedure;
import eu.scy.tools.copex.edp.CopexApplet;
import eu.scy.tools.copex.utilities.CopexReturn;
import java.io.IOException;
import java.net.URL;
/**
 * permet la sauvegarde de protocoles intermediaires
 * @author MBO
 */
public class SaveXMLProc {
    // METHODES
    
    /* creation du fichier */
    static public CopexReturn saveProc(CopexApplet applet, long idUser, ExperimentalProcedure proc, String date, String time){
            //String date = CopexUtilities.getCurrentDate()+"T";
            //String time = CopexUtilities.getCurrentTime().toString();
            date += "T";
            time = time.replace(':', '-');
            date += time;
            String nameFile = "proc-"+idUser+"-"+proc.getMission().getDbKey()+"-"+proc.getDbKey()+"-"+date+".xml";
            try{    
                XMLProcFile file = new XMLProcFile(applet,  nameFile, proc);
            }catch(IOException e){
                return new CopexReturn("Error save proc "+e, false);
            }
       
        return new CopexReturn();
    }

    /* creation du fichier */
    static public CopexReturn saveProc(URL copexURL, long idUser, ExperimentalProcedure proc, String date, String time){
            //String date = CopexUtilities.getCurrentDate()+"T";
            //String time = CopexUtilities.getCurrentTime().toString();
            date += "T";
            time = time.replace(':', '-');
            date += time;
            String nameFile = "proc-"+idUser+"-"+proc.getMission().getDbKey()+"-"+proc.getDbKey()+"-"+date+".xml";
            try{
                XMLProcFile file = new XMLProcFile(copexURL,  nameFile, proc);
            }catch(IOException e){
                return new CopexReturn("Error save proc "+e, false);
            }

        return new CopexReturn();
    }
}
