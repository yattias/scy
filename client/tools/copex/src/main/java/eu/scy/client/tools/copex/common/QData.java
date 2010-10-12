/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * quantity de type data
 * @author Marjolaine
 */
public class QData extends Quantity {

    public QData(Element xmlElem, List<Quantity> list) throws JDOMException {
        super(xmlElem, list);
    }

    public QData(Element xmlElem, long dbKey, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem, dbKey, listPhysicalQuantity);
    }

    public QData(long dbKey, List<LocalText> listName, List<LocalText> listType, double value, List<LocalText> listUncertainty, CopexUnit unit) {
        super(dbKey, listName, listType, value, listUncertainty, unit);
    }
    public QData(long dbKey, Locale locale, String name, String type,  double value, String uncertainty, CopexUnit unit) {
        super(dbKey, locale, name, type, value, uncertainty, unit);
    }


    
    

}
