package eu.scy.client.tools.formauthor;

import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class DataForm extends JFrame {
	private DataFormView dfv;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DataForm();
	}

	private DataForm() {
		super("SCY Data Form Author");
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				dfv.update(null, null);
			}
		});

		DataFormModel dfm = new DataFormModel();
		dfv = new DataFormView(dfm, this.getContentPane());
		new DataFormController(dfm, dfv, this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setSize(880, 500);
		setVisible(true);
	}

}
