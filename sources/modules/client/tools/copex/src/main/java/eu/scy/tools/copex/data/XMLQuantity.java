/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.data;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Marjolaine
 */
public class XMLQuantity implements Cloneable{
    /*tag name  */
    public static final String TAG_QUANTITY = "quantity" ;
    public static final String TAG_QUANTITY_ID = "id" ;
    public static final String TAG_QUANTITY_MIN_VALUE = "min_value" ;
    public static final String TAG_QUANTITY_MAX_VALUE = "max_value" ;

    public XMLQuantity(Element xmlElem) throws JDOMException {

    }

     // toXML
    public Element toXML(){
        return null;
    }
}
