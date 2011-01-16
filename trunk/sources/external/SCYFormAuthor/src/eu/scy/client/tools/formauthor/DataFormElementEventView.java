package eu.scy.client.tools.formauthor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class DataFormElementEventView extends JPanel {

	private DataFormElementEventModel dfeem;
	private JButton jbtnDelete;// = new JButton("Event lšschen");
	private JComboBox jcbEventDataType = new JComboBox();
	private JComboBox jcbEventType = new JComboBox();

	public DataFormElementEventView(DataFormElementEventModel dfeem,
			Container container) {
		this.dfeem = dfeem;
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		fl.setHgap(2);
		fl.setVgap(1);

		setLayout(fl);

		add(new JLabel(Localizer.getString("EVENT_TYPE")));

		for (DataFormElementEventModel.DataFormElementEventTypes type : DataFormElementEventModel.DataFormElementEventTypes
				.values()) {
			jcbEventType.addItem(type);
		}

		for (DataFormElementEventModel.DataFormElementEventDataTypes type : DataFormElementEventModel.DataFormElementEventDataTypes
				.values()) {
			jcbEventDataType.addItem(type);
		}

		add(jcbEventType);

		add(new JLabel(Localizer.getString("EVENT_DATA_TYPE")));

		add(jcbEventDataType);

		jbtnDelete = new JButton();
		jbtnDelete.setToolTipText(Localizer.getString("DELETE_EVENT"));

		ImageIcon icon;
		try {
			icon = IconCreator.createImageIcon(this.getClass().getResource(
					"images/textfield_delete.png"), "Event lšschen");
			jbtnDelete.setIcon(icon);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		add(jbtnDelete);
		if (container.getWidth() < 500) {
			setMaximumSize(new Dimension(500, 35));
			setPreferredSize(new Dimension(500, 35));
		} else {
			setPreferredSize(new Dimension(container.getWidth(), 35));
			setMaximumSize(new Dimension(container.getWidth(), 35));

		}
		// setMaximumSize(new Dimension(container.getWidth(), 50));

		jcbEventType.setSelectedItem(dfeem.getEventType());
		jcbEventDataType.setSelectedItem(dfeem.getEventDataType());

		setBackground(new Color(240, 240, 240));
		Border bdrUnSelected = BorderFactory.createEtchedBorder();
		setBorder(bdrUnSelected);

		container.add(this);
		// this.validate();
		// this.repaint();
		// cont
	}

	public void addDeleteEventListener(ActionListener al) {
		jbtnDelete.addActionListener(al);
	}

	public void addChangeEventTypeListener(ActionListener al) {
		jcbEventType.addActionListener(al);
	}

	public void addChangeEventDataTypeListener(ActionListener al) {
		jcbEventDataType.addActionListener(al);
	}

	public DataFormElementEventModel.DataFormElementEventDataTypes getEventDataType() {
		return (DataFormElementEventModel.DataFormElementEventDataTypes) jcbEventDataType
				.getSelectedItem();
	}

	public DataFormElementEventModel.DataFormElementEventTypes getEventType() {
		return (DataFormElementEventModel.DataFormElementEventTypes) jcbEventType
				.getSelectedItem();
	}
}
