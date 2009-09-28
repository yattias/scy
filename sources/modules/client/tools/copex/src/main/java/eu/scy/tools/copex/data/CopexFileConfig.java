/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.data;

import eu.scy.tools.copex.utilities.CopexReturn;
import java.util.ArrayList;
import org.jdom.Element;

/**
 * lecture du fichier de configuration de copex
 * le fichier de configuration precise les objets qui sont generaux dans copex, a savoir:
 * - les grandeurs,
 * - les materiels,
 * - les actions pre structurees
 * @author Marjolaine
 */
public class CopexFileConfig {
    /* lecture du fichier de configuration */
    public CopexReturn loadCopexConfig(Element e, ArrayList v){
        return new CopexReturn();
    }
}
