/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.pdsELO;

import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion ;

/**
 * part of the ProcessedDatasetELO which contains
 * - the ignored data
 * - list of operations
 * - lsit of visualizations
 * @author Marjolaine Bodin
 */
public class ProcessedData {
    /*tag name  */
    public static final String TAG_PROCESSED_DATA = "processed_dataset" ;
    public static final String TAG_PROCESSED_DATA_NAME = "name";

    /* name*/
    private String name;
    /*list of ignored data */
    private IgnoredData ignoredData;
    /* list processed header */
    private List<ProcessedHeader> listProcessedHeaders;
    /* list operations */
    private List<Operation> listOperations;
    /* list visualizations */
    private List<Visualization> listVisualization;

    public ProcessedData(String name, IgnoredData ignoredData, List<ProcessedHeader> listProcessedHeaders, List<Operation> listOperations, List<Visualization> listVisualization) {
        this.name = name;
        this.ignoredData = ignoredData;
        this.listProcessedHeaders = listProcessedHeaders ;
        this.listOperations = listOperations;
        this.listVisualization = listVisualization ;
    }

    public ProcessedData(Element xmlElem) throws JDOMException {
	if (xmlElem.getName().equals(TAG_PROCESSED_DATA)) {
            if(xmlElem.getChild(TAG_PROCESSED_DATA_NAME) != null )
                name = xmlElem.getChild(TAG_PROCESSED_DATA_NAME).getText() ;
            else name = "Dataset";
            ignoredData = new IgnoredData(xmlElem.getChild(IgnoredData.TAG_IGNORED_DATA));
            listProcessedHeaders = new LinkedList<ProcessedHeader>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(ProcessedHeader.TAG_PROCESSED_HEADER).iterator(); variableElem.hasNext();) {
                listProcessedHeaders.add(new ProcessedHeader(variableElem.next()));
            }
            listOperations = new LinkedList<Operation>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(Operation.TAG_OPERATION).iterator(); variableElem.hasNext();) {
                listOperations.add(new Operation(variableElem.next()));
            }
            listVisualization = new LinkedList<Visualization>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(Visualization.TAG_VISUALIZATION).iterator(); variableElem.hasNext();) {
                Element el = variableElem.next();
                String type = el.getChild(Visualization.TAG_VISUALIZATION_TYPE).getText();
                if (type.equals(DataConstants.TYPE_VIS_BAR)){
                    listVisualization.add(new BarVisualization(el));
                }else if (type.equals(DataConstants.TYPE_VIS_GRAPH))
                    listVisualization.add(new GraphVisualization(el));
                else if (type.equals(DataConstants.TYPE_VIS_PIE))
                    listVisualization.add(new PieVisualization(el));
            }
	} else {
            throw(new JDOMException("ProcessedData expects <"+TAG_PROCESSED_DATA+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public ProcessedData(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public IgnoredData getIgnoredData() {
        return ignoredData;
    }

    public List<Operation> getListOperations() {
        return listOperations;
    }

    public List<Visualization> getListVisualization() {
        return listVisualization;
    }

    public String getName() {
        return name;
    }

    public List<ProcessedHeader> getListProcessedHeaders() {
        return listProcessedHeaders;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_PROCESSED_DATA);
        element.addContent(new Element(TAG_PROCESSED_DATA_NAME).setText(this.name));
	for (Iterator<Operation> op = listOperations.iterator(); op.hasNext();) {
            element.addContent(op.next().toXML());
	}
        for (Iterator<ProcessedHeader> h = listProcessedHeaders.iterator(); h.hasNext();) {
            element.addContent(h.next().toXML());
	}
        if (ignoredData != null){
            Element ig = ignoredData.toXML() ;
            if (ig != null)
                element.addContent(ig);
        }
        for (Iterator<Visualization> vis = listVisualization.iterator(); vis.hasNext();) {
            element.addContent(vis.next().toXML());
	}
	return element;
    }


}
