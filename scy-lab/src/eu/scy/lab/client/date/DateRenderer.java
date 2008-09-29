/**
 * 
 */
package eu.scy.lab.client.date;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

/**
 * @author Sven
 *
 */
public class DateRenderer implements Renderer{

	private DateTimeFormat dateTimeFormat;

	   public DateRenderer(String format) {
	      this.dateTimeFormat = DateTimeFormat.getFormat(format);
	   }
	      
	   public DateRenderer() {
	      this.dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
	   }
	   
	   public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
	         Store store) {
	      return value!=null?dateTimeFormat.format((Date) value):"";
	   }
	   
	   public DateTimeFormat getDateTimeFormat(){
		   return this.dateTimeFormat;
	   }
	
	}

