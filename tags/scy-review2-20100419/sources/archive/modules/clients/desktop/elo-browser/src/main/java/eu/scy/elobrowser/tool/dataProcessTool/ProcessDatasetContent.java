/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tool.dataProcessTool;

import eu.scy.tools.dataProcessTool.pdsELO.ProcessedDatasetELO;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;
import roolo.elo.api.IContent;

/**
 *
 * @author Marjolaine
 */
public class ProcessDatasetContent implements IContent, Cloneable {

    /* processed dataset */
    private ProcessedDatasetELO pds;


    public ProcessDatasetContent() {
    }

    public ProcessedDatasetELO getPds() {
        return pds;
    }

    public void setPds(ProcessedDatasetELO pds) {
        this.pds = pds;
    }

    @Override
    public List<Locale> getLanguages() {
        if (pds != null)
            return pds.getLanguages();
        return new LinkedList();
    }

    @Override
    public void setLanguages(List<Locale> arg0) {
        if (pds != null)
            pds.setLanguages(arg0);
    }

    @Override
    public void setLanguage(Locale arg0) {
        if (pds != null)
            pds.setLanguage(arg0);
    }

    @Override
    public boolean supportsLanguage(Locale arg0) {
        if (pds != null)
            return pds.getLanguages().contains(arg0);
        else return false;
    }

    @Override
    public boolean isLanguageIndependent() {
        return false;
    }

    @Override
    public IContent clone() throws CloneNotSupportedException {
        ProcessDatasetContent content = (ProcessDatasetContent)super.clone();
        if (pds != null)
            content.setPds((ProcessedDatasetELO)pds.clone());
        return content;
    }

    @Override
    public String getXml() {
        if (pds != null) {
            return new JDomStringConversion().xmlToString(pds.toXML());
        } else {
            return "";
        }
    }

    @Override
    public void setXmlString(String arg0) {
        try {
            pds = new ProcessedDatasetELO(arg0);
        } catch (JDOMException e) {
            // TODO
        }
    }

    @Override
    public byte[] getBytes() {
        return null;
    }

    @Override
    public void setBytes(byte[] arg0) {
        
    }

    @Override
    public long getSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getXmlString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
