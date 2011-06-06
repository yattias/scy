/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.pdsELO;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;

import eu.scy.elo.contenttype.dataset.DataSetRow;

/**
 *
 * @author Marjolaine
 */
public class ProcessedDatasetEloShowcase {
    String xmlString = "<processDataset>"
	+"<dataset>"
		+"<header language=\"en\">"
			+"<column>"
			+"	<symbol>V1</symbol>"
			+"	<description>first variable</description>"
			+"	<type>integer</type>"
		+"	</column>"
			+"<column>"
			+"	<symbol>V2</symbol>"
		+"		<description>second variable</description>"
		+"		<type>double</type>"
		+"	</column>"
		+"	<column>"
		+"		<symbol>V3</symbol>"
		+"		<description>tirth variable</description>"
			+"	<type>double</type>"
		+"	</column>"
		+"	<column>"
			+"	<symbol>V4</symbol>"
			+"	<description>fourth variable</description>"
			+"	<type>double</type>"
			+"</column>"
			+"<column>"
			+"	<symbol>V5</symbol>"
			+"	<description>fifth variable</description>"
			+"	<type>double</type>"
			+"</column>"
		+"</header>"
		+"<row>"
			+"<value>1</value>"
			+"<value>2</value>"
			+"<value>2</value>"
			+"<value>5</value>"
			+"<value>4</value>"
		+"</row>"
		+"<row>"
			+"<value>2</value>"
			+"<value>3</value>"
			+"<value>3.2</value>"
			+"<value>4.1</value>"
			+"<value>9.2</value>"
		+"</row>"
		+"<row>"
			+"<value>2</value>"
			+"<value>2.5</value>"
			+"<value>4.1</value>"
			+"<value>6.3</value>"
			+"<value>6.4</value>"
		+"</row>"
		+"<row>"
			+"<value>2</value>"
			+"<value>2.5</value>"
			+"<value>-40.2</value>"
			+"<value>10.9</value>"
			+"<value>14</value>"
		+"</row>"
		+"<row>"
			+"<value>-1</value>"
		+"	<value>4</value>"
		+"	<value>-2.22</value>"
		+"	<value>7.42</value>"
		+"	<value>15.9</value>"
		+"</row>"
	+"</dataset>"
	+"<processed_dataset>"
		+"<operation>"
			+"<!-- it can be row-->"
			+"<column>"
			+"	<symbol language=\"en\">Min</symbol>"
			+"	<description>Min</description>"
			+"	<type>double</type>"
            +"    <name>MIN</name>"
			+"	<reference>"
			+"		<id>0</id>"
			+"		<id>1</id>"
			+"		<id>2</id>"
			+"		<id>3</id>"
			+"		<id>4</id>"
			+"	</reference>"
             +"   <result>"
             +"        <value>-1</value>"
             +"        <value>2</value>"
             +"        <value>-40.2</value>"
             +"        <value>4.1</value>"
             +"        <value>4</value>"
             +"   </result>"
			+"</column>"
		+"</operation>"
		+"<operation>"
		+"	<!-- it can be row-->"
			+"<column>"
				+"<symbol language=\"en\">Max</symbol>"
				+"<description>Max</description>"
				+"<type>double</type>"
              +"  <name>MAX</name>"
			+"	<reference>"
			+"		<id>0</id>"
			+"		<id>1</id>"
			+"		<id>2</id>"
			+"		<id>3</id>"
			+"		<id>4</id>"
			+"	</reference>"
             +"   <result>"
               +"      <value>2</value>"
               +"      <value>4</value>"
                +"     <value>4.1</value>"
                +"     <value>10.9</value>"
                 +"    <value>15.9</value>"
               +" </result>"
			+"</column>"
		+"</operation>"
	+"</processed_dataset>"
+"</processDataset>";

    public ProcessedDatasetEloShowcase() {
        ProcessedDatasetELO pds = null;
        try {
            // generate dataset from xml string representation
            // construction directly from JDOMElement is also possible
            pds = new ProcessedDatasetELO(xmlString);

            // show an xml representation generated from dataset
            XMLOutputter fmt = new XMLOutputter();
            fmt.output(pds.toXML(), System.out);

            // show some of the content of the pds
            // System.out.println("\nDataset : ");
            String symbol = pds.getDataset().getHeader(Locale.ENGLISH).getColumns().get(0).getSymbol();
            String type = pds.getDataset().getHeader(Locale.ENGLISH).getColumns().get(0).getType();
            // System.out.println("\n\nsymbol / type of first variable : "+symbol+" / "+type);

            // show all values of first variable
            List<DataSetRow> rows = pds.getDataset().getValues();
            for (Iterator<DataSetRow> row = rows.iterator(); row.hasNext();) {
                // System.out.println(row.next().getValues().get(0));
            }

            // processed Dataset
            // System.out.println("\nProcessed Dataset : ");
            IgnoredData id = pds.getProcessedData().getIgnoredData();
            if (id != null){
                // System.out.println("Ignored Data : ");
                List<Data> idatas = id.getListIgnoredData() ;
                if (idatas != null){
                    for (Iterator<Data> d=idatas.iterator(); d.hasNext();){
                        // System.out.println("("+d.next().getRowId()+", "+d.next().getColumnId()+")");
                    }
                }
            }
            List<Operation> operations = pds.getProcessedData().getListOperations();
            if (operations != null){
                // System.out.println("Operations : ");
                for (Iterator<Operation> op = operations.iterator(); op.hasNext();){
                    Operation o = op.next() ;
                    // System.out.println(o.getName()+"("+(o.isIsOnCol() ? "onCol" : "OnRow")+")");
                    List<String> res = o.getResults() ;
                    for (Iterator<String> r=res.iterator(); r.hasNext();){
                        // System.out.println(r.next());
                    }
                }
            }

	} catch (JDOMException e) {
            e.printStackTrace();
	} catch (IOException e) {
            e.printStackTrace();
	}
        if (pds != null)
            pds.toXML();
    }

    public static void main(String[] args) {
        new ProcessedDatasetEloShowcase();
    }
}
