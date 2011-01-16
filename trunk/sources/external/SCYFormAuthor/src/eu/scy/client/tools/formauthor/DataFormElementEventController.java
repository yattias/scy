package eu.scy.client.tools.formauthor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataFormElementEventController {
	// private DataFormModel dfm;
	private DataFormElementModel dfem;
	private DataFormElementEventView dfeev;
	private DataFormElementEventModel dfeem;

	public DataFormElementEventController(DataFormElementEventModel dfeem,
			DataFormElementEventView dfeev, DataFormElementModel dfem) {
		this.dfeem = dfeem;
		this.dfeev = dfeev;
		this.dfem = dfem;

		dfeev
				.addDeleteEventListener(new DeleteDataFormElementEventListener());
		dfeev.addChangeEventTypeListener(new EventTypeChangeListener());
		dfeev.addChangeEventDataTypeListener(new EventDataTypeChangeListener());
	
	}



	private class DeleteDataFormElementEventListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dfem.removeEvent(dfeem);
		}
	}

	private class EventTypeChangeListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dfeem.setEventType(dfeev.getEventType());
		}
	}
	private class EventDataTypeChangeListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dfeem.setEventDataType(dfeev.getEventDataType());
		}
	}

}
