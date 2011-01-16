package eu.scy.client.tools.formauthor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DataFormEventsView extends JPanel implements Observer {
	private ArrayList<DataFormElementEventModel> _dfeem;
	private DataFormElementModel _dfem;
	private JButton btnAddEvent;// = new JButton("+");
	private JButton btnClose;// = new JButton("Schließen");
	private JPanel jpCenter = new JPanel();
	private JPanel dfvp = this;

	DataFormEventsView(DataFormElementModel dfem, Container container) {
		this._dfeem = dfem.getEvents();
		this._dfem = dfem;
		_dfem.addObserver(this);

		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);

		JPanel jpMenuBar = new JPanel(fl);

		btnAddEvent = new JButton();
		btnAddEvent.setToolTipText(Localizer.getString("ADD_EVENT"));

		ImageIcon icon;
		try {
			icon = IconCreator.createImageIcon(this.getClass().getResource(
					"images/textfield_add.png"), "Event hinzufügen");
			btnAddEvent.setIcon(icon);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		btnClose = new JButton();
		btnClose.setToolTipText(Localizer.getString("CLOSE_EVENTS"));

		try {
			icon = IconCreator.createImageIcon(this.getClass().getResource(
					"images/application_form_delete.png"), "Events schließen");
			btnClose.setIcon(icon);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		jpMenuBar.add(btnAddEvent);
		jpMenuBar.add(btnClose);

		jpCenter.setLayout(new BoxLayout(jpCenter, BoxLayout.Y_AXIS));
		dfvp.setLayout(new BorderLayout());

		dfvp.add(jpMenuBar, BorderLayout.PAGE_START);

		JScrollPane jsp = new JScrollPane(jpCenter);
		dfvp.add(jsp, BorderLayout.CENTER);

		container.add(dfvp);

		update(null, null);

		// validate();
		// repaint();

	}

	public void addCloseListener(ActionListener al) {
		btnClose.addActionListener(al);
	}

	public void addAddEventListener(ActionListener al) {
		btnAddEvent.addActionListener(al);
	}

	public void update(Observable o, Object arg) {
		jpCenter.removeAll();
		for (DataFormElementEventModel event : _dfeem) {
			DataFormElementEvent dfee = new DataFormElementEvent(event,
					jpCenter, _dfem);
		}
		jpCenter.validate();
		jpCenter.repaint();
		validate();
		repaint();

	}
}
