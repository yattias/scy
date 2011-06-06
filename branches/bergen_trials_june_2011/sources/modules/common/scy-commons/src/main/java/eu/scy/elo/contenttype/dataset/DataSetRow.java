package eu.scy.elo.contenttype.dataset;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;

//import roolo.helper.SerializationHelper;

public class DataSetRow implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5519169485396027650L;
	private String name = "DataSetRow"; // name of the node set by Jcrom
	private String path; // mandatory attribute -- requested by Jcrom
	private List<String> values;

	private String elementStr;
	private Element element;

	// This constructor is only used by jcrom for persistance purposes
	public DataSetRow(){
		//this.elementStr = SerializationHelper.nullSerialization;
	}

	public DataSetRow(List<String> values) {
		this.values = values;
	//	this.elementStr = SerializationHelper.nullSerialization;
	}

	public DataSetRow(Element rowElem) throws JDOMException {
		//if (rowElem.getText().equals("row")) {
			values = new LinkedList<String>();
			for (Iterator<Element> valueElem = rowElem.getChildren("value")
					.iterator(); valueElem.hasNext();) {
				values.add(valueElem.next().getText());
			}
		//} else {
			//throw (new JDOMException(
					//"DataSetRow expects <row> as root element, but found <"
					//		+ rowElem.getName() + ">."));
		//}
	}

	public int getLength() {
		return values.size();
	}

	public List<String> getValues() {
		return values;
	}

	public Element toXML() {
		loadIfNecessary();

		if (element == null) {
			element = new Element("row");
			Element valueElem;
			for (Iterator<String> value = values.iterator(); value.hasNext();) {
				valueElem = new Element("value");
				valueElem.setText(value.next());
				element.addContent(valueElem);
			}
		}

		//elementStr = SerializationHelper.serializeValue(element);
		return element;
	}

    private void loadIfNecessary() {
//		if (element == null && !elementStr.equals(SerializationHelper.nullSerialization)){
//			element = (Element)SerializationHelper.unSerializeValue(elementStr);
//		}
	}
}