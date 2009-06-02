/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.print;

import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import java.io.IOException;
import java.util.ArrayList;

/**
 * impression au format HTML
 * @author MBO
 */
public class PrintHTML {

    // ATTRIBUTS 
    
    // METHODES
    
    /* creation du fichier */
    static public CopexReturn printCopex(EdPPanel edP, String nameFile, CopexUser copexUser, ArrayList<ExperimentalProcedure> listProc, CopexMission mission, ArrayList<Material> listMaterial){
        try{
            HTMLFile printFile = new HTMLFile(edP, nameFile);
            printFile.setHead();
            printFile.setUser(copexUser);
            if (mission != null)
                printFile.setMission(mission);
            if (listMaterial != null)
                printFile.setListMaterial(listMaterial);
            printFile.setProcedures(listProc);
            printFile.closeBody();
            printFile.closeFile();
        }catch(IOException ioe){
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT") + " : "+ioe, false);
        }
        return new CopexReturn();
    }
}
