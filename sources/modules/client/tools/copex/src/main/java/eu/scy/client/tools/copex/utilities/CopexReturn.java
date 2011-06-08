/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

/**
 *
 * @author Marjolaine
 * allows to represent an error or a warning in copex
 * String text : error text
 * boolean ok : true if any error
 * boolean warning : true if it's a warning
 * boolean confirm : true if the user has to confirm => in this case, the text contains the message of confirmation
 * boolean okCancel : true if it's a confirmation  OK/Cancel, otherwise Yes/No
 * 
 * if error : ok=false, text=msg error, warning=false, confirm=false, okCancel=false
 * if ok : ok=true, text="", warning=false, confirm=false, okCancel=false
 * if warning : ok=false, text = msg warning, warning=true, confirm=false, okCancel=false
 * if warning to confirm: ok=false, text=msg confirm, warning=true, confirm=true, okCancel=true if Ok/Cancel okCancel=false if yes/no
 */
public class CopexReturn {
    /*
     * msg error
     */
    private String text;
    /*
     * error or not
     */
    private boolean ok;
     /*
     * warning or not
     */
    private boolean warning;
     /*
     * confirmation or not
     */
    private boolean confirm;
     /*
     *  type Ok/Cancel or not (yes/no)
     */
    private boolean okCancel;

    /*
     * Constructor by default => no error, ok
     */
    public CopexReturn() {
        this.text = "";
        this.ok = true;
        this.warning = false;
        this.confirm = false;
        this.okCancel = false;
    }

    /*
     * Constructor for an error or a warning
     */
    public CopexReturn(String text, boolean warning) {
        this.text = text;
        this.warning = warning;
        this.ok = false;
        this.confirm = false;
        this.okCancel = false;
    }

    /*
     * returns the error text
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
    /*
     * returns true if it's an error
     */
    public boolean isError(){
        return !this.ok & !this.warning;
    }

     /*
     * returns true if it's not an error and not a warning
     */
    public boolean isOk(){
        return this.ok ;
    }

    /*
     * returns true if it's a warning
     */
    public boolean isWarning(){
        return !this.ok && this.warning ;
    }

     /*
     * returns true if ask for a confirmation
     */
    public boolean mustConfirm(){
        return this.confirm ;
    }

    /*
     * returns true if type OkCancel =>otherwise type Yes/No
     */
    public boolean isOkCancel(){
        return this.okCancel ;
    }

    /*
     *set confirm text
     */
    public void setConfirm(String text){
        this.confirm = true;
        this.text = text;
        this.okCancel = true;
    }

    /*
     * ask confirm
     */
    public void setConfirm(String text, boolean okCancel){
        this.confirm = true;
        this.text = text;
        this.okCancel = okCancel;
    }
    /*
     * set to ok
     */
    public void setOk(){
        this.ok = true;
    }
     /*
     * set error
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
