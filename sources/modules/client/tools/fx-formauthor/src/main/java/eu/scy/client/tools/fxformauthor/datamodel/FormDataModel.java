/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.datamodel;

import java.util.ArrayList;

/**
 *
 * @author pg
 */
public class FormDataModel {
    private String description;
    private String title;
    private int version;
    private ArrayList<FormDataElement> elements;

    public FormDataModel() {
        elements = new ArrayList<FormDataElement>();
    }

    public void addElement(FormDataElement element) {
        elements.add(element);
    }

    public void removeElement(FormDataElement element) {
        elements.remove(element);
    }

    public ArrayList<FormDataElement> getElements() {
        return this.elements;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


}
