/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.data;

import eu.scy.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * strategy material
 * @author Marjolaine
 */
public class XMLStrategy implements Cloneable{
    /*tag name  */
    public static final String TAG_STRATEGY="strategy";
    public static final String TAG_STRATEGY_CODE="code";
    public static final String TAG_STRATEGY_IS_ITEM="is_item";
    public static final String TAG_STRATEGY_ITEM="item";
    public static final String TAG_STRATEGY_ADD_MAT="add_mat";
    public static final String TAG_STRATEGY_CHOOSE_MAT="choose_mat";
    public static final String TAG_STRATEGY_COMMENTS_MAT="comments_mat";

    private String code;
    private boolean isItem;
    private List<LocalText> listItem;
    private boolean addMat;
    private boolean chooseMat;
    private boolean commentsMat;

    public XMLStrategy(String code, boolean isItem, List<LocalText> listItem, boolean addMat, boolean chooseMat, boolean commentsMat) {
        this.code = code;
        this.isItem = isItem;
        this.listItem = listItem;
        this.addMat = addMat;
        this.chooseMat = chooseMat;
        this.commentsMat = commentsMat;
    }
    public XMLStrategy(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_STRATEGY)) {
			code = xmlElem.getChild(TAG_STRATEGY_CODE).getText();
            isItem = xmlElem.getChild(TAG_STRATEGY_IS_ITEM).getText().equals("1");
            listItem = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_STRATEGY_ITEM).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listItem.add(new LocalText(e.getText(), l));
            }
            addMat = xmlElem.getChild(TAG_STRATEGY_ADD_MAT).getText().equals("1");
            chooseMat = xmlElem.getChild(TAG_STRATEGY_CHOOSE_MAT).getText().equals("1");
            commentsMat = xmlElem.getChild(TAG_STRATEGY_COMMENTS_MAT).getText().equals("1");
		} else {
			throw(new JDOMException("Material expects <"+TAG_STRATEGY+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public boolean isAddMat() {
        return addMat;
    }

    public void setAddMat(boolean addMat) {
        this.addMat = addMat;
    }

    public boolean isChooseMat() {
        return chooseMat;
    }

    public void setChooseMat(boolean chooseMat) {
        this.chooseMat = chooseMat;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isCommentsMat() {
        return commentsMat;
    }

    public void setCommentsMat(boolean commentsMat) {
        this.commentsMat = commentsMat;
    }

    public boolean isIsItem() {
        return isItem;
    }

    public void setIsItem(boolean isItem) {
        this.isItem = isItem;
    }

    public List<LocalText> getListItem() {
        return listItem;
    }

    public void setListItem(List<LocalText> listItem) {
        this.listItem = listItem;
    }

    public String getItem(Locale locale){
        for (Iterator<LocalText> text = listItem.iterator(); text.hasNext();){
            LocalText t = text.next();
            if(t.getLocale().equals(locale))
                return t.getText();
        }
        return null;
    }

    // to XML
    public Element toXML(){
        Element element = new Element(TAG_STRATEGY);
        element.addContent(new Element(TAG_STRATEGY_CODE).setText(this.code));
        element.addContent(new Element(TAG_STRATEGY_IS_ITEM).setText(this.isItem ? "1" : "0"));
        if(listItem != null && listItem.size() > 0){
            for (Iterator<LocalText> t = listItem.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_STRATEGY_ITEM);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        element.addContent(new Element(TAG_STRATEGY_ADD_MAT).setText(this.addMat ? "1" : "0"));
        element.addContent(new Element(TAG_STRATEGY_CHOOSE_MAT).setText(this.chooseMat ? "1" : "0"));
        element.addContent(new Element(TAG_STRATEGY_COMMENTS_MAT).setText(this.commentsMat ? "1" : "0"));
        return element;
    }
}
