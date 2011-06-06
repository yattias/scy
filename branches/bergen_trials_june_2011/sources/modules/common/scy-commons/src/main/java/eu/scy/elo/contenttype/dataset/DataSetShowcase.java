package eu.scy.elo.contenttype.dataset;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

// this class shows some features of the DataSetAPI
// TODO: a set of JUnit tests
public class DataSetShowcase {

	String _xmlString = "<dataset>  <header language=\"en\">    <column>      <symbol>T</symbol>"
			+ "<description>T</description><type>double</type></column><column>"
			+ "<symbol>rc</symbol><description>rc</description><type>double</type>"
			+ "</column>    <column>      <symbol>time</symbol> <description />"
			+ "<type>double</type>    </column>  </header>  <row>    <value>20.0"
			+ "</value><value>1.0</value>    <value>10.0</value>  </row>  <row>"
			+ "<value>19.0</value><value>2.0</value>    <value>11.0</value>  </row>"
			+ " <row>    <value>18.0</value><value>3.0</value>    <value>12.0</value>"
			+ " </row></dataset>";
	String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>	<dataset>	  <header language=\"en\" />	</dataset>";

	public DataSetShowcase() {
		DataSet dataset;
		try {
			// generate dataset from xml string representation
			// construction directly from JDOMElement is also possible
			dataset = new DataSet(xmlString);
			
			// show an xml representation generated from dataset
			XMLOutputter fmt = new XMLOutputter();
			fmt.setFormat(Format.getPrettyFormat());
			fmt.output(dataset.toXML(), System.out);
			
			// show some of the content of the dataset
			String symbol = dataset.getHeader(Locale.ENGLISH).getColumns().get(0).getSymbol();
			String type = dataset.getHeader(Locale.ENGLISH).getColumns().get(0).getType();
			// System.out.println("\n\nsymbol / type of first variable : "+symbol+" / "+type);
			
			// show all values of first variable
			List<DataSetRow> rows = dataset.getValues();
			for (Iterator<DataSetRow> row = rows.iterator(); row.hasNext();) {
				// System.out.println(row.next().getValues().get(0));
			}	
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new DataSetShowcase();
	}
}
