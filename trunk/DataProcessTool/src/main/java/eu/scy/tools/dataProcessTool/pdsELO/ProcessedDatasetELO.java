/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.pdsELO;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.content.dataset.*;
import roolo.elo.JDomStringConversion ;

/**
 * processed Dataset ELO, contains
 * - dataset
 * - processed data
 * - visualization of data
 * TODO language
 * @author Marjolaine Bodin
 */
public class ProcessedDatasetELO implements Cloneable {
    /* Tag name */
    public final static String TAG_ELO_CONTENT = "processDataset";
    public final static String TAG_ELO_DATASET = "dataset";
    /* dataset */
    private DataSet dataset ;
    /* processed data*/
    private ProcessedData processedData ;
    /* visualization list */

    public ProcessedDatasetELO(DataSet dataset, ProcessedData processedData) {
        this.dataset = dataset;
        this.processedData = processedData;
    }

    public ProcessedDatasetELO(String xmlString) throws JDOMException {
		this(new JDomStringConversion().stringToXml(xmlString));
	}

    public ProcessedDatasetELO(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_ELO_CONTENT)) {
			createDatasetFromXML(xmlElem.getChild(TAG_ELO_DATASET));
			createProcessedDatasetFromXML(xmlElem.getChild(ProcessedData.TAG_PROCESSED_DATA));
		} else {
			throw (new JDOMException(
					"Processed DataSet expects <"+TAG_ELO_CONTENT+"> as root element, but found <"
					+ xmlElem.getName() + ">."));
		}
	}



    public Element toXML() {
		Element element = new Element(TAG_ELO_CONTENT);
        element.addContent(dataset.toXML().detach());
        element.addContent(processedData.toXML().detach());
		return element;
	}

    public DataSet getDataset() {
        return dataset;
    }

    public ProcessedData getProcessedData() {
        return processedData;
    }

    /* creation Dataset*/
    private void createDatasetFromXML(Element datasetElem) throws JDOMException {
		dataset = new DataSet(datasetElem);
	}

    /* creation Processed Dataset*/
    private void createProcessedDatasetFromXML(Element pdsElem) throws JDOMException {
		processedData = new ProcessedData(pdsElem);
	}


    /* languages */
    public List<Locale> getLanguages() {
        // TODO
        return new LinkedList();
    }

    public void setLanguages(List<Locale> arg0) {
        // TODO
    }

    public void setLanguage(Locale arg0) {
        //TODO
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
