/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * Action nommee du protocole initial
 * si parametree : peut etre de type choice, manipulation, acquisition, treatment
 * @author Marjolaine
 */
public class InitialNamedAction implements Cloneable {
    // PROPERTY
    /* identifiant bd */
    protected long dbKey;
    /* code */
    protected String code;
    /* libelle */
    protected String libelle;
    /* est parametree ?*/
    protected boolean isSetting ;
    /* si isSetting => variable de l'action */
    protected InitialActionVariable variable;
    /* dessin autorisé*/
    protected boolean draw;
    /* repeat autorisé*/
    protected boolean repeat;

    // CONSTRUCTOR
    public InitialNamedAction(long dbKey, String code, String libelle, boolean isSetting, InitialActionVariable variable, boolean draw, boolean repeat) {
        this.dbKey = dbKey;
        this.code = code;
        this.libelle = libelle;
        this.isSetting = isSetting ;
        this.variable = variable ;
        this.draw = draw;
        this.repeat = repeat;
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isSetting() {
        return isSetting;
    }

    public void setSetting(boolean isSetting) {
        this.isSetting = isSetting;
    }

    public InitialActionVariable getVariable() {
        return variable;
    }

    public void setVariable(InitialActionVariable variable) {
        this.variable = variable;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    

    // OVERRIDE
    @Override
    public Object clone(){
       try {
            InitialNamedAction a = (InitialNamedAction) super.clone() ;

            a.setDbKey(this.getDbKey());
            a.setCode(new String(this.getCode()));
            a.setLibelle(new String(this.libelle));
            a.setSetting(this.isSetting);
            InitialActionVariable variableC = null;
            if (this.variable != null){
                variableC = (InitialActionVariable)variable.clone();
            }
            a.setVariable(variableC);
            a.setDraw(this.isDraw());
            a.setRepeat(this.isRepeat());

            return a;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
        }
    }

    // METHODE
    
}
