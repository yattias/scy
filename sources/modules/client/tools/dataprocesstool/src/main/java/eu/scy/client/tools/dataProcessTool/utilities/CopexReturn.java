/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.utilities;

/**
 *
 * @author 
 * can show an error ao a warning in fitex
 * String text : error text
 * boolean ok :true if no error
 * boolean warning : true if it's a warning
 * boolean confirm : true if the user has to confirm => in this case, text is the confirmation message
 * boolean okCancel : true if it's a confirmation OK/Cancel, else Yes/No
 * 
 * if error : ok=false, text=error msg, warning=false, confirm=false, okCancel=false
 * if ok : ok=true, text="", warning=false, confirm=false, okCancel=false
 * if warning : ok=false, text = msg warning, warning=true, confirm=false, okCancel=false
 * if warning to confirm : ok=false, text=confirmation msg, warning=true, confirm=true, okCancel=true if Ok/Cancel okCancel=false if Yes/No
 */
public class CopexReturn {
    /*
     * msg text
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
     * ok/Cancel type or not (yes/No)
     */
    private boolean okCancel;

    /*
     * no error by default
     */
    public CopexReturn() {
        this.text = "";
        this.ok = true;
        this.warning = false;
        this.confirm = false;
        this.okCancel = false;
    }

    /*
     * error or warning
     */
    public CopexReturn(String text, boolean warning) {
        this.text = text;
        this.warning = warning;
        this.ok = false;
        this.confirm = false;
        this.okCancel = false;
    }

    /*
     * returns the error tex
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
    /*
     * return true if it's an error
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
     * returns true if a confirmation has to be asked
     */
    public boolean mustConfirm(){
        return this.confirm ;
    }
    /*
     *returns true if type  OkCancel => else type Yes/No
     */
    public boolean isOkCancel(){
        return this.okCancel ;
    }
    /*
     * set Confirmation
     */
    public void setConfirm(String text){
        this.confirm = true;
        this.text = text;
        this.okCancel = true;
    }
    /*
     * set confirmation
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
