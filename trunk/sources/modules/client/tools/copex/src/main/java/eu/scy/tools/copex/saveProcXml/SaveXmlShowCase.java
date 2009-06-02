/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.saveProcXml;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Marjolaine
 */
public class SaveXmlShowCase {

    public SaveXmlShowCase() {
        Question question = new Question("1", "", "question", "", null, "", "", null);
        Procedure proc = new Procedure(question);
        DataSheet ds = null;
        List listMaterialUse = null;
        ExperimentalProcedureXML expP = new ExperimentalProcedureXML("test", "1", proc, ds, listMaterialUse);
        // show an xml representation
        XMLOutputter fmt = new XMLOutputter();
        try {
            fmt.output(expP.toXML(), System.out);
        } catch (IOException ex) {
            Logger.getLogger(SaveXmlShowCase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
		new SaveXmlShowCase();
	}
}
