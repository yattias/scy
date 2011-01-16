package eu.scy.client.tools.formauthor;

import javax.swing.JFrame;

public class DataFormEvents extends JFrame {

	DataFormEvents(DataFormElementModel dfem) {
		// DataFormElementModel _dfem = dfem;
		setSize(800, 800);
		setVisible(true);

		DataFormEventsView dfev = new DataFormEventsView(dfem, this
				.getContentPane());
		DataFormEventsController dfec = new DataFormEventsController(dfem,
				dfev, this);
		//		
		setSize(550, 400);
		setLocationRelativeTo(null);
		setVisible(true);
		repaint();

	}

}
