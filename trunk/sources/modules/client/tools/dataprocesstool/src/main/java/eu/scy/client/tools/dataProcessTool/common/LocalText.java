/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import java.util.Locale;

/**
 *
 * @author Marjolaine
 */
public class LocalText implements Cloneable {
    private String text;
    private Locale locale;

    public LocalText(String text, Locale locale) {
        this.text = text;
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Object clone()  {
       try {
            LocalText t = (LocalText) super.clone() ;

            t.setText(new String(this.text));
            t.setLocale(new Locale(this.locale.getLanguage()));
            return t;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
        }
    }
}
