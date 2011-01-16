package eu.scy.client.tools.formauthor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataFormEventsController {
	private DataFormEventsView dfev;
	private DataFormElementModel dfem;
	private DataFormEvents dfe;

	public DataFormEventsController(DataFormElementModel dfem,
			DataFormEventsView dfev,DataFormEvents dfe ) {		
		this.dfev = dfev;
		this.dfem = dfem;
		this.dfe = dfe;
		
		dfev.addAddEventListener(new AddEventListener());
		dfev.addCloseListener(new CloseListener());
	}

	public class AddEventListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dfem.addEvent();
		}
	}
	
	public class CloseListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dfe.dispose();
		}
	}

}
