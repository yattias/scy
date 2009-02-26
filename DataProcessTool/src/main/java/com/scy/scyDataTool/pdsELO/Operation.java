/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scy.scyDataTool.pdsELO;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion ;

/**
 * An operation describes :
 * - applies on column or row
 * - symbol (name editing by the student)
 * - description,
 * - type,
 * - name  : BAR CHART, PIE CHART, SCATTER PLOT
 * - reference: list of the column / row on which applies the operation
 * - result: list of results
 * @author Marjolaine
 */
public class Operation {
    /* TAG NAMES */
    public final static String TAG_OPERATION = "operation";
    private final static String TAG_OPERATION_COLUMN = "column";
    private final static String TAG_OPERATION_ROW = "row";
    private final static String TAG_OPERATION_SYMBOL = "symbol";
    private final static String TAG_OPERATION_DESCRIPTION = "description";
    private final static String TAG_OPERATION_TYPE = "type";
    private final static String TAG_OPERATION_NAME = "name";
    private final static String TAG_OPERATION_REFERENCE = "reference";
    private final static String TAG_OPERATION_PARAM="op_param";
    private final static String TAG_OPERATION_PARAM_VALUE = "value";
    private final static String TAG_OPERATION_REF_ID = "id";
    private final static String TAG_OPERATION_RESULT = "result";
    private final static String TAG_OPERATION_RES_VALUE = "value";

    /*on column or on row */
    private boolean isOnCol;
    private String symbol;
    private String description ;
    private String type ;
    private String name;
    private List<String> references;
    private List<String> results;
    private List<String> allParam;

    public Operation(boolean isOnCol, String symbol, String description, String type, String name, List<String> references, List<String> results, List<String> allParam) {
        this.isOnCol = isOnCol;
        this.symbol = symbol;
        this.description = description;
        this.type = type;
        this.name = name;
        this.references = references;
        this.results = results;
        this.allParam = allParam ;
    }


    public Operation(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_OPERATION)) {
            Element elOp = xmlElem.getChild(TAG_OPERATION_COLUMN) ;
			this.isOnCol = elOp !=null;
            if (elOp == null)
                elOp = xmlElem.getChild(TAG_OPERATION_ROW);
            this.symbol =elOp.getChild(TAG_OPERATION_SYMBOL).getText() ;
            this.description = elOp.getChild(TAG_OPERATION_DESCRIPTION).getText();
            this.type = elOp.getChild(TAG_OPERATION_TYPE).getText() ;
            this.name = elOp.getChild(TAG_OPERATION_NAME).getText();
            if (elOp.getChild(TAG_OPERATION_PARAM) != null){
                for (Iterator<Element> variableElem = elOp.getChild(TAG_OPERATION_PARAM).getChildren(TAG_OPERATION_PARAM_VALUE).iterator(); variableElem.hasNext();) {
                    this.allParam.add(variableElem.next().getText());
                }
            }
            this.references = new LinkedList<String>() ;
            for (Iterator<Element> variableElem = elOp.getChild(TAG_OPERATION_REFERENCE).getChildren(TAG_OPERATION_REF_ID).iterator(); variableElem.hasNext();) {
                this.references.add(variableElem.next().getText());
            }
            this.results = new LinkedList<String>() ;
            for (Iterator<Element> variableElem = elOp.getChild(TAG_OPERATION_RESULT).getChildren(TAG_OPERATION_RES_VALUE).iterator(); variableElem.hasNext();) {
                this.results.add(variableElem.next().getText());
            }

		} else {
			throw(new JDOMException("Operation expects <"+TAG_OPERATION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

	public Operation(String xmlString) throws JDOMException {
		this(new JDomStringConversion().stringToXml(xmlString));
	}

    public boolean isIsOnCol() {
        return isOnCol;
    }

    public String getName() {
        return name;
    }

    public List<String> getReferences() {
        return references;
    }

    public List<String> getResults() {
        return results;
    }

    public String getSymbol() {
        return symbol;
    }

    public List<String> getAllParam() {
        return allParam;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_OPERATION);
        Element elem ;
        elem = isOnCol ? new Element(TAG_OPERATION_COLUMN) : new Element(TAG_OPERATION_ROW);
        elem.addContent(new Element(TAG_OPERATION_SYMBOL).setText(symbol));
        elem.addContent(new Element(TAG_OPERATION_DESCRIPTION).setText(description));
        elem.addContent(new Element(TAG_OPERATION_TYPE).setText(type));
        elem.addContent(new Element(TAG_OPERATION_NAME).setText(name));
        if(this.allParam != null && this.allParam.size() > 0){
            Element elemP = new Element(TAG_OPERATION_PARAM);
            for (Iterator<String> param = allParam.iterator(); param.hasNext();) {
                Element valueElem = new Element(TAG_OPERATION_PARAM_VALUE);
                valueElem.setText(param.next());
                elemP.addContent(valueElem);
            }
            elem.addContent(elemP);
        }
        Element valueElem;
        Element elemR = new Element(TAG_OPERATION_REFERENCE);
		for (Iterator<String> ref = references.iterator(); ref.hasNext();) {
			valueElem = new Element(TAG_OPERATION_REF_ID);
			valueElem.setText(ref.next());
			elemR.addContent(valueElem);
		}
        elem.addContent(elemR);
        elemR = new Element(TAG_OPERATION_RESULT);
		for (Iterator<String> res = references.iterator(); res.hasNext();) {
			valueElem = new Element(TAG_OPERATION_RES_VALUE);
			valueElem.setText(res.next());
			elemR.addContent(valueElem);
		}
        elem.addContent(elemR);
        element.addContent(elem);
		return element;
    }


}
