package eu.scy.client.tools.formauthor;
import java.awt.Container;

public class DataFormElementEvent {
	DataFormElementEventModel dfeem;
	DataFormElementEventView dfeev;
	DataFormElementEventController dfeec;

	public DataFormElementEvent(DataFormElementEventModel dfeem,Container container,DataFormElementModel dfem) {
		this.dfeem = dfeem;
		dfeev = new DataFormElementEventView(dfeem, container);
		dfeec = new DataFormElementEventController(dfeem, dfeev,dfem);
//		container.validate();
//		container.repaint();
	}

}
