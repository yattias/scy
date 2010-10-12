/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

/**
 *
 * @author MBO
 * permet de representer une erreur ou un warning dans COPEX
 * String text : le texte de l'erreur 
 * boolean ok : a true si aucune erreur
 * boolean warning : a true si il s'agit d'un warning
 * boolean confirm : a true si l'utilisateur doit confirmer => dans ce cas text contient le message de confirmation
 * boolean okCancel : a true s'il s'agit d'une confirmation OK/Cancel, sinon Oui/Non
 * 
 * si error : ok=false, text=msg erreur, warning=false, confirm=false, okCancel=false
 * si ok : ok=true, text="", warning=false, confirm=false, okCancel=false
 * si warning : ok=false, text = msg warning, warning=true, confirm=false, okCancel=false
 * si warning a confirmer : ok=false, text=msg de confirm, warning=true, confirm=true, okCancel=true si Ok/Cancel okCancel=false si Oui/Non
 */
public class CopexReturn {
// ATTRIBUTS
    /*
     * texte du message d'erreur => internationaliser
     */
    private String text;
    /*
     * indique s'il s'agit d'une erreur ou non
     */
    private boolean ok;
     /*
     * indique s'il s'agit d'un warning ou non
     */
    private boolean warning;
     /*
     * indique s'il s'agit d'une confirmation ou non
     */
    private boolean confirm;
     /*
     * indique s'il s'agit d'un type Ok/Cancel ou non
     */
    private boolean okCancel;

    //CONSTRUCTEURS
    /*
     * Constructeur par defaut : pas d'erreur
     */
    public CopexReturn() {
        this.text = "";
        this.ok = true;
        this.warning = false;
        this.confirm = false;
        this.okCancel = false;
    }

    /*
     * Constructeur d'une erreur ou d'un warning
     */
    public CopexReturn(String text, boolean warning) {
        this.text = text;
        this.warning = warning;
        this.ok = false;
        this.confirm = false;
        this.okCancel = false;
    }

    //GETTER AND SETTER
    /*
     * REtourne le texte de l'erreur
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
    // OPERATIONS
    /*
     * retourne true si l'instance est une erreur
     */
    public boolean isError(){
        return !this.ok & !this.warning;
    }
     /*
     * retourne true si l'instance n'est ni une erreur ni un warning
     */
    public boolean isOk(){
        return this.ok ;
    }
    /*
     * retourne true si l'instance represente un warning
     */
    public boolean isWarning(){
        return !this.ok && this.warning ;
    }
     /*
     * retourne true si une confirmation doit etre demandee
     */
    public boolean mustConfirm(){
        return this.confirm ;
    }
    /*
     * retourne true si de type OkCancel => sinon de type Oui/Non
     */
    public boolean isOkCancel(){
        return this.okCancel ;
    }
    /*
     * demande de confirmation
     */
    public void setConfirm(String text){
        this.confirm = true;
        this.text = text;
        this.okCancel = true;
    }
    /*
     * demande de confirmation
     */
    public void setConfirm(String text, boolean okCancel){
        this.confirm = true;
        this.text = text;
        this.okCancel = okCancel;
    }
    /*
     * met a Ok
     */
    public void setOk(){
        this.ok = true;
    }
     /*
     * met en erreur
     */
    public void setError(){
        this.ok = false;
        this.warning = false;
    }

    @Override
    public String toString() {
        String str = "";
        if (isOk() )
               str = "OK : ";
        else if (isError())
                  str = "ERROR : ";
        else if (isWarning())
                str = "WARNING : ";
        str += getText();
        return str;
    }
}
