package eu.scy.client.tools.formauthor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

@SuppressWarnings("serial")
public class DataFormElementView extends JPanel implements Observer {

	private JTextField jtfTitel = new JTextField();
	private DataFormElementModel dfem;
	private JComboBox jcbType = new JComboBox();
	private JButton jbtnEvent;
	private JFormattedTextField jtfCardinality = new JFormattedTextField(NumberFormat.getInstance());
	private JButton jbtnShowData;
	private DataFormElementView dfev = this;

	public DataFormElementView(final DataFormElementModel dfem, Container container) {
		this.dfem = dfem;
		dfem.addObserver(this);
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (dfem.isSelected()) {
					dfem.setSelected(false);
				} else {
					dfem.setSelected(true);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

		});
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		fl.setHgap(2);
		fl.setVgap(1);

		setLayout(fl);

		add(new JLabel(Localizer.getString("FIELD_NAME") + ":"));

		jtfTitel.setPreferredSize(new Dimension(200, 28));
		add(jtfTitel);

		jtfTitel.setText(dfem.getTitle());

		for (DataFormElementModel.DataFormElementTypes type : DataFormElementModel.DataFormElementTypes.values()) {
			jcbType.addItem(type);
		}

		((NumberFormatter) jtfCardinality.getFormatter()).setAllowsInvalid(false);
		jtfCardinality.setPreferredSize(new Dimension(50, 28));

		jtfCardinality.setText(dfem.getCardinality());

		add(new JLabel(Localizer.getString("TYPE")));
		add(jcbType);
		add(new JLabel(Localizer.getString("CARDINALITY")));
		add(jtfCardinality);

		jbtnEvent = new JButton();
		jbtnEvent.setToolTipText(Localizer.getString("SHOW_EVENTS"));

		ImageIcon icon;
		try {
			icon = IconCreator.createImageIcon(this.getClass().getResource("images/note.png"), "vorheriger Datensatz");
			jbtnEvent.setIcon(icon);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		add(jbtnEvent);

		jbtnShowData = new JButton();
		jbtnShowData.setToolTipText(Localizer.getString("SHOW_DATA"));

		try {
			icon = IconCreator.createImageIcon(this.getClass().getResource("images/application_form_magnify.png"), "Daten anzeigen");
			jbtnShowData.setIcon(icon);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		add(jbtnShowData);
		if (dfem.getDataList().size() > 0) {
			jbtnShowData.setVisible(true);
		} else {
			jbtnShowData.setVisible(false);
		}

		jbtnShowData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ViewData(dfem);
			}
		});

		setPreferredSize(new Dimension(625, 35));
		if (container.getWidth() < 625) {
			setMaximumSize(new Dimension(625, 35));
		} else {
			setMaximumSize(new Dimension(container.getWidth(), 35));
		}
		jcbType.setSelectedItem(dfem.getType());

		update(null, null);

		container.add(this);
		validate();
	}

	public void addTitleChangeListener(DocumentListener dl) {
		jtfTitel.getDocument().addDocumentListener(dl);
	}

	public void addCardinalityChangeListener(DocumentListener dl) {
		jtfCardinality.getDocument().addDocumentListener(dl);
	}

	public void addTypeChangeListener(ActionListener al) {
		jcbType.addActionListener(al);
	}

	public void addEditEventsListener(ActionListener al) {
		jbtnEvent.addActionListener(al);
	}

	public String getCardinality() {
		return jtfCardinality.getText();
	}

	public String getTitle() {
		return jtfTitel.getText();
	}

	public DataFormElementModel.DataFormElementTypes getType() {
		return (DataFormElementModel.DataFormElementTypes) jcbType.getSelectedItem();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (dfem.isSelected()) {
			dfev.setBackground(Color.WHITE);
			Border bdrSelected = BorderFactory.createRaisedBevelBorder();
			dfev.setBorder(bdrSelected);
		} else {
			dfev.setBackground(new Color(240, 240, 240));
			Border bdrUnSelected = BorderFactory.createEtchedBorder();
			dfev.setBorder(bdrUnSelected);
		}
	}
}
