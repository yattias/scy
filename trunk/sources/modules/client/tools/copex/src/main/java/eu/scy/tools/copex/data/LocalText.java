/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.data;

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

    
}
