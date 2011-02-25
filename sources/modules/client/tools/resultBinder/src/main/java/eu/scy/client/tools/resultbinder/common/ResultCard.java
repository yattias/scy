/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.resultbinder.common;

import java.util.HashMap;
import java.util.Iterator;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Marjolaine
 */
public class ResultCard {
    private final static String TAG_RESULT_CARD = "result_card";
    private final static String TAG_RESULT = "result";
    private final static String TAG_RESULT_CARD_NAME = "result_name";
    private final static String TAG_RESULT_CARD_VALUE = "result_value";

    private HashMap<String,Object> resultCard ;

    public ResultCard() {
        this.resultCard = new HashMap<String,Object>();
    }

    public ResultCard(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_RESULT_CARD)) {
            resultCard = new HashMap<String,Object>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_RESULT).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                if(e.getChild(TAG_RESULT_CARD_NAME) != null){
                    String resultName = e.getChild(TAG_RESULT_CARD_NAME).getText();
                    Object value = null;
                    if(e.getChild(TAG_RESULT_CARD_VALUE) != null){
                        String v = e.getChild(TAG_RESULT_CARD_VALUE).getText();
                        // double
                        try{
                            Double d = Double.parseDouble(v);
                            value = d;
                        }catch(NumberFormatException ex){
                            //boolean
                            if(v.equals("true") || v.equals("false")){
                                value = Boolean.valueOf(v);
                            }else{ //string
                                value = v;
                            }
                        }
                    }
                    resultCard.put(resultName, value);
                }
            }
        }else{
            throw(new JDOMException("ResultCard expects <"+TAG_RESULT_CARD+"> as root element, but found <"+xmlElem.getName()+">."));
        }
    }

    public Element toXML(){
        Element element = new Element(TAG_RESULT_CARD);
        for (Iterator<String> i = resultCard.keySet().iterator() ; i.hasNext() ; ){
            Element e = new Element(TAG_RESULT);
            String rc = i.next();
            Object value = resultCard.get(rc);
            e.addContent(new Element(TAG_RESULT_CARD_NAME).setText(rc));
            if(value != null){
                e.addContent(new Element(TAG_RESULT_CARD_VALUE).setText(value.toString()));
            }
            element.addContent(e);
        }
        return element;
    }

    public HashMap<String, Object> getResultCard() {
        return resultCard;
    }

    public void setResultCard(HashMap<String, Object> resultCard) {
        this.resultCard = resultCard;
    }
    
}
