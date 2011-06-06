package eu.scy.elo.contenttype.dataset;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.jdom.Element;
import org.jdom.JDOMException;

import roolo.elo.JDomStringConversion;

//import roolo.helper.SerializationHelper;

public class DataSet implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3582487762894340144L;
	private String name = "DataSet"; // name of the node set by Jcrom
	private String path; // mandatory attribute -- requested by Jcrom

	private List<DataSetHeader> headers = null;
	private List<DataSetRow> rows = null;

	private Element element;
	private String elementStr;

	// This constructor is only used by jcrom for persistance purposes
	public DataSet() {
		// this.elementStr = SerializationHelper.nullSerialization;
	}

	public DataSet(List<DataSetHeader> headers) {
		// TODO check for consistent headers (i.e. all have same variablecount)
		this.headers = headers;
		rows = new LinkedList<DataSetRow>();
		// this.elementStr = SerializationHelper.nullSerialization;
	}

	public DataSet(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals("dataset")) {
			createHeadersFromXML(xmlElem.getChildren("header"));
			createRowsFromXML(xmlElem.getChildren("row"));
		} else {
			throw (new JDOMException(
					"DataSet expects <dataset> as root element, but found <"
							+ xmlElem.getName() + ">."));
		}

		// elementStr = SerializationHelper.nullSerialization;
	}

	public int getRowCount() {
	    return rows.size();
	}

	private void createHeadersFromXML(List<Element> headersElem)
			throws JDOMException {
		headers = new LinkedList<DataSetHeader>();
		DataSetHeader newHeader;
		for (Iterator<Element> headerElem = headersElem.iterator(); headerElem
				.hasNext();) {
			newHeader = new DataSetHeader(headerElem.next());
			headers.add(newHeader);
		}
	}

	private void createRowsFromXML(List<Element> rowsElem) throws JDOMException {
		rows = new LinkedList<DataSetRow>();
		for (Iterator<Element> rowElem = rowsElem.iterator(); rowElem.hasNext();) {
			addRow(new DataSetRow(rowElem.next()));
		}
	}

	public DataSet(String xmlString) throws JDOMException {
		this(new JDomStringConversion().stringToXml(xmlString));
	}

	public void addRow(DataSetRow values) {
		if (values.getLength() == headers.get(0).getColumnCount()) {
			rows.add(values);
		} else {
			throw (new ArrayIndexOutOfBoundsException(
					"The DataSetRow added to the DataSet has a wrong number of values. Expected number: "
							+ headers.get(0).getColumnCount()
							+ ", actual number: " + values.getLength()));
		}
	}

	public List<DataSetRow> getValues() {
		return rows;
	}

	public List<DataSetHeader> getHeaders() {
		return headers;
	}

	public DataSetHeader getHeader(Locale locale) {
		DataSetHeader thisheader;
		for (Iterator<DataSetHeader> header = headers.iterator(); header
				.hasNext();) {
			thisheader = header.next();
			if (thisheader.getLocale().equals(locale)) {
				return thisheader;
			}
		}
		return null;
	}

	public List<Locale> getLanguages() {
		List<Locale> locales = new LinkedList<Locale>();
		for (Iterator<DataSetHeader> header = headers.iterator(); header
				.hasNext();) {
			locales.add(header.next().getLocale());
		}
		return locales;
	}

	public Element toXML() {
		loadIfNecessary();

		if (element == null) {
			element = new Element("dataset");
			if (headers != null) {
				for (Iterator<DataSetHeader> header = headers.iterator(); header
						.hasNext();) {
					element.addContent(header.next().toXML());
				}
				if (rows != null) {
					for (Iterator<DataSetRow> row = rows.iterator(); row
							.hasNext();) {
						element.addContent(row.next().toXML());
					}
				}
			}
		}
		// elementStr = SerializationHelper.serializeValue(elementStr);
		return element;
	}

	public void removeAll() {
		rows.clear();
		element = null;
		// elementStr = SerializationHelper.nullSerialization;
	}

	private void loadIfNecessary() {
		// if (element == null &&
		// !elementStr.equals(SerializationHelper.nullSerialization)){
		// element = (Element)SerializationHelper.unSerializeValue(elementStr);
		// }
	}

}
