/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Variable action nommee parametree
 * @author Marjolaine
 */
public class InitialActionVariable implements Cloneable{
    public final static String TAG_INITIAL_ACTION_VARIABLE = "initial_action_variable";
    public final static String TAG_INITIAL_ACTION_VARIABLE_REF = "initial_action_variable_ref";
    public final static String TAG_INITIAL_ACTION_VARIABLE_CODE = "id";
    public final static String TAG_INITIAL_ACTION_VARIABLE_NB_PARAM = "nb_param";
    public final static String TAG_INITIAL_ACTION_VARIABLE_LIBELLE = "libelle";

    /*identifiant */
    private long dbKey ;
    /*code */
    private String code;
    /* nombre de parametres */
    private int nbParam;
    /* libelle */
    private List<LocalText> listLibelle;
    /* tableau des parametres */
    private InitialActionParam[] tabParam;

    // CONSTRUCTOR
    public InitialActionVariable(long dbKey, Locale locale, String code, int nbParam, List<LocalText> listLibelle, InitialActionParam[] tabParam) {
        this.dbKey = dbKey;
        this.code = code;
        this.nbParam = nbParam;
        this.listLibelle = listLibelle;
        this.tabParam = tabParam;
        sortParam(locale);
    }

    public InitialActionVariable(Element xmlElem, Locale locale, long idActionParam, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial) throws JDOMException {
		if (xmlElem.getName().equals(TAG_INITIAL_ACTION_VARIABLE)) {
			code = xmlElem.getChild(TAG_INITIAL_ACTION_VARIABLE_CODE).getText();
            listLibelle = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_INITIAL_ACTION_VARIABLE_LIBELLE).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listLibelle.add(new LocalText(e.getText(), l));
            }
            nbParam = Integer.parseInt(xmlElem.getChild(TAG_INITIAL_ACTION_VARIABLE_NB_PARAM).getText());
            if(nbParam >0){
                tabParam = new InitialActionParam[nbParam];
                int i=0;
                for (Iterator<Element> variableElem = xmlElem.getChildren(InitialParamData.TAG_INITIAL_PARAM_DATA).iterator(); variableElem.hasNext();) {
                    Element e = variableElem.next();
                    tabParam[i++] = new InitialParamData(e, idActionParam++);
                }
                List<InitialParamQuantity> listParamQuantity = new LinkedList();
                for (Iterator<Element> variableElem = xmlElem.getChildren(InitialParamQuantity.TAG_INITIAL_PARAM_QUANTITY).iterator(); variableElem.hasNext();) {
                    Element e = variableElem.next();
                    InitialParamQuantity q = new InitialParamQuantity(e, idActionParam++, listPhysicalQuantity);
                    tabParam[i++] = q;
                    listParamQuantity.add(q);
                }
                for (Iterator<Element> variableElem = xmlElem.getChildren(InitialParamMaterial.TAG_INITIAL_PARAM_MATERIAL).iterator(); variableElem.hasNext();) {
                    Element e = variableElem.next();
                    tabParam[i++] = new InitialParamMaterial(e, idActionParam++, listTypeMaterial, listParamQuantity);
                }
                sortParam(locale);
            }
        }
		else {
			throw(new JDOMException("Initial Action Variable expects <"+TAG_INITIAL_ACTION_VARIABLE+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    public InitialActionVariable(Element xmlElem, List<InitialActionVariable> list)  throws JDOMException{
        if (xmlElem.getName().equals(TAG_INITIAL_ACTION_VARIABLE_REF)) {
			code = xmlElem.getChild(TAG_INITIAL_ACTION_VARIABLE_CODE).getText();
            for(Iterator<InitialActionVariable> q = list.iterator();q.hasNext();){
                InitialActionVariable p = q.next();
                if(p.getCode().equals(code)){
                    this.dbKey = p.getDbKey();
                    this.nbParam = p.getNbParam();
                    this.listLibelle = p.getListLibelle();
                    this.tabParam = p.getTabParam();
                }
            }
        }else {
			throw(new JDOMException("InitialActionVariable expects <"+TAG_INITIAL_ACTION_VARIABLE_REF+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }


    // GETTER AND SETTER
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getLibelle(Locale locale) {
         return CopexUtilities.getText(listLibelle, locale);
    }

    public void setLibelle(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listLibelle);
        if(id ==-1){
            this.listLibelle.add(text);
        }else{
            this.listLibelle.set(id, text);
        }
    }

    public void setLibelle(String text){
        for(Iterator<LocalText> t = listLibelle.iterator();t.hasNext();){
            t.next().setText(text);
        }
    }

    public List<LocalText> getListLibelle() {
        return listLibelle;
    }

    public void setListLibelle(List<LocalText> listLibelle) {
        this.listLibelle = listLibelle;
    }

    public int getNbParam() {
        return nbParam;
    }

    public void setNbParam(int nbParam) {
        this.nbParam = nbParam;
    }

    public InitialActionParam[] getTabParam() {
        return tabParam;
    }

    public void setTabParam(InitialActionParam[] tabParam) {
        this.tabParam = tabParam;
    }

    public void setTabParam(Locale locale, InitialActionParam[] tabParam) {
        this.tabParam = tabParam;
        sortParam(locale);
    }

    @Override
    public Object clone()  {
        try {
            InitialActionVariable v = (InitialActionVariable) super.clone() ;
            v.setDbKey(this.getDbKey());
            v.setCode(new String (this.getCode()));
            v.setNbParam(this.getNbParam());
            List<LocalText> listLibelleC = new LinkedList();
            for (Iterator<LocalText> t = listLibelle.iterator(); t.hasNext();) {
                listLibelleC.add((LocalText)t.next().clone());
            }
            v.setListLibelle(listLibelleC);
            InitialActionParam[] tabParamC = null;
            if (tabParam != null){
                tabParamC = new InitialActionParam[this.tabParam.length];
                for (int i=0; i<tabParam.length; i++){
                    tabParamC[i] = (InitialActionParam)this.tabParam[i].clone();
                }
            }
            v.setTabParam(tabParamC);
            return v;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
        }
    }

    /* range les parametres dans l'ordre d'apparition */
    private void sortParam(Locale locale){
        InitialActionParam[] tabParamC = new InitialActionParam[nbParam];
        String s = new String (getLibelle(locale));

        String po="{";
        String pf="}";

        for (int i=0; i<nbParam; i++){
            int id = s.indexOf(po);
            if (id != -1){
                int idF = s.indexOf(pf);
                if (idF != -1){
                    long dbK = -1;
                    try{
                        dbK = Long.parseLong(s.substring(id+1, idF));
                    }catch(NumberFormatException e){

                    }
                    for (int j=0; j<nbParam; j++){
                        if (tabParam[j].getDbKey() == dbK){
                            tabParamC[i] = tabParam[j];
                            break;
                        }
                    }
                    if (idF < s.length()-1)
                        s = s.substring(idF+1);
                }
            }
        }
        this.tabParam = tabParamC ;
    }

   

    /* retourne le texte du libelle avant le parametre i (et superieur au parametre i-1)
     *ex : texte {123} et surtout {124} ok ? => 0 : texte
     *                              => 1 : et surtout
     * si id -1 : texte fin :       => -1 : ok ?
     */
    public String getTextLibelle(Locale locale, int i){
        String libelle = getLibelle(locale);
        String pf = "}";
        if (i == -1){
            // indice du dernier {}
            int id = libelle.lastIndexOf(pf);
            if (id == -1){
                // pas trouve => renvoir la chaine complete
                return libelle;
            }else{
                if (id == libelle.length() -1)
                    return "";
                else
                    return libelle.substring(id+1);
            }
        }else{
            long idP =tabParam[i].getDbKey() ;
            String t = "{"+idP+"}";
            int id = libelle.indexOf(t) ;
            if (i == 0){
                return libelle.substring(0,id);
            }else{
                long dbPrec = tabParam[i-1].getDbKey() ;
                String tprec = "{"+dbPrec+"}";
                int idPrec = libelle.indexOf(tprec);
                return libelle.substring(idPrec+tprec.length(), id);
            }
        }
    }


    // toXML
    public Element toXML(){
        Element element = new Element(TAG_INITIAL_ACTION_VARIABLE);
		element.addContent(new Element(TAG_INITIAL_ACTION_VARIABLE_CODE).setText(code));
        if(listLibelle != null && listLibelle.size() > 0){
            for (Iterator<LocalText> t = listLibelle.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_INITIAL_ACTION_VARIABLE_LIBELLE);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        element.addContent(new Element(TAG_INITIAL_ACTION_VARIABLE_NB_PARAM).setText(Integer.toString(nbParam)));
        if(nbParam >0){
            for (int i=0; i<nbParam; i++){
                element.addContent(tabParam[i].toXML());
            }
        }
		return element;
    }

    // toXML
    public Element toXMLRef(){
        Element element = new Element(TAG_INITIAL_ACTION_VARIABLE_REF);
		element.addContent(new Element(TAG_INITIAL_ACTION_VARIABLE_CODE).setText(code));
        return element;
    }

    
}
