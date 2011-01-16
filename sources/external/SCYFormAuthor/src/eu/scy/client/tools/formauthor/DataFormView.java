package eu.scy.client.tools.formauthor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class DataFormView extends JPanel implements Observer {
	private static final int _ELEMENTHEIGHT = 35;
	private DataFormModel dfm;
	private final JButton btnAddField;// = new JButton("Feld hinzufügen");
	private final JButton btnDeleteField;// = new JButton("Feld/er löschen");
	private final JButton btnMoveUpField;// = new JButton("Feld/er nach oben ");
	private final JButton btnMoveDownField;// = new
	// JButton("Feld/er nach unten");
	private JTextField jtfFormTitel = new JTextField();
	private JPanel dfvp = this;
	private JPanel jpCenter = new JPanel();
	private JPanel jpMenuBar;
	private JTextArea jtaBeschreibung;

	private JMenuItem jmiOpenFile;
	private JMenuItem jmiOpenTemplatesRepository;
	private JMenuItem jmiOpenRepository;
	private JMenuItem jmiClose;
	private JMenuItem jmiOpenConfiguration;
	private JMenuItem jmiSaveToFile;
	private JMenuItem jmiSaveToRepository;
	private JMenuItem jmNewForm;

	DataFormView(DataFormModel model, Container container) {
		this.dfm = model;
		dfm.addObserver(this);

		createMenue(container);

		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);

		jpMenuBar = new JPanel(fl);

		btnAddField = new JButton();
		btnAddField.setToolTipText(Localizer.getString("ADD_FIELD"));
		btnDeleteField = new JButton();
		btnDeleteField.setToolTipText(Localizer.getString("DELETE_FIELD"));
		btnMoveUpField = new JButton();
		btnMoveUpField.setToolTipText(Localizer.getString("MOVE_FIELD_UP"));
		btnMoveDownField = new JButton();
		btnMoveDownField.setToolTipText(Localizer.getString("MOVE_FIELD_DOWN"));

		ImageIcon icon;
		try {
			icon = IconCreator.createImageIcon(this.getClass().getResource(
					"images/arrow_up.png"), "vorheriger Datensatz");
			btnMoveUpField.setIcon(icon);
			icon = IconCreator.createImageIcon(this.getClass().getResource(
					"images/arrow_down.png"), "vorheriger Datensatz");
			btnMoveDownField.setIcon(icon);
			icon = IconCreator.createImageIcon(this.getClass().getResource(
					"images/textfield_delete.png"), "Feld/er löschen");
			btnDeleteField.setIcon(icon);
			icon = IconCreator.createImageIcon(this.getClass().getResource(
					"images/textfield_add.png"), "Feld/er löschen");
			btnAddField.setIcon(icon);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jpMenuBar.add(btnAddField);
		jpMenuBar.add(btnDeleteField);

		jpMenuBar.add(btnMoveUpField);
		jpMenuBar.add(btnMoveDownField);

		jpCenter.setLayout(new BoxLayout(jpCenter, BoxLayout.Y_AXIS));
		// jpCenter.setPreferredSize(new Dimension(880, 600));

		dfvp.setLayout(new BorderLayout());

		dfvp.add(jpMenuBar, BorderLayout.PAGE_START);

		createInfoPanel();
		JScrollPane jsp = new JScrollPane(jpCenter);
		dfvp.add(jsp, BorderLayout.CENTER);

		container.add(dfvp);
	}

	/**
	 * 
	 */
	private void createInfoPanel() {
		JPanel jpInfo = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		jpInfo.setPreferredSize(new Dimension(250, 100));

		Border etchedBdr = BorderFactory.createEtchedBorder();
		Border bdrDescription = BorderFactory.createEtchedBorder();
		Border bdrInfo = BorderFactory.createEtchedBorder();

		jpInfo.setBorder(bdrInfo);
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 0, 0);
		jpInfo.add(new JLabel(Localizer.getString("FORM_TITLE") + ":"), c);

		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0.005;
		c.insets = new Insets(0, 10, 10, 10);
		jpInfo.add(jtfFormTitel, c);

		c.gridy = 2;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0, 10, 0, 0);
		jpInfo
				.add(new JLabel(Localizer.getString("FORM_DESCRIPTION") + ":"),
						c);

		jtaBeschreibung = new JTextArea();
		jtaBeschreibung.setLineWrap(true);
		jtaBeschreibung.setWrapStyleWord(true);
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = 0.995;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 10, 10, 10);
		jpInfo.add(jtaBeschreibung, c);

		jtaBeschreibung.setBorder(bdrDescription);

		// jpInfo.setLayout(new BoxLayout(jpInfo, BoxLayout.Y_AXIS));

		dfvp.add(jpInfo, BorderLayout.LINE_END);
	}

	/**
	 * @param container
	 */
	private void createMenue(Container container) {
		JMenuBar jmbMenueLeiste;

		jmNewForm = new JMenuItem(Localizer.getString("NEW"));
		
		// Menü - Datei
		JMenu jmDatei = new JMenu(Localizer.getString("FILE"));
		// JMenu jmDatei = new JMenu("Datei");

		jmiOpenFile = new JMenuItem(Localizer.getString("OPEN_FILE"));
		jmiOpenTemplatesRepository = new JMenuItem(Localizer
				.getString("OPEN_TEMPLATES_REPOSITORY"));
		jmiOpenRepository = new JMenuItem(Localizer
				.getString("OPEN_REPOSITORY"));
		jmiClose = new JMenuItem(Localizer.getString("QUIT"));
		jmiSaveToFile = new JMenuItem(Localizer.getString("SAVE_FILE"));
		jmiSaveToRepository = new JMenuItem(Localizer
				.getString("SAVE_REPOSITORY"));
		
		jmDatei.add(jmNewForm);
		
		// JMenu
		JMenu jmiOpen = new JMenu(Localizer.getString("OPEN"));
		jmiOpen.add(jmiOpenFile);
		jmiOpen.add(jmiOpenTemplatesRepository);
		jmiOpen.add(jmiOpenRepository);
		jmDatei.add(jmiOpen);

		JMenu jmiSave = new JMenu(Localizer.getString("SAVE"));
		jmiSave.add(jmiSaveToFile);
		jmiSave.add(jmiSaveToRepository);
		jmDatei.add(jmiSave);
		jmDatei.addSeparator();
		jmDatei.add(jmiClose);

		// Menü - Konfiguration
		JMenu jmConfiguration = new JMenu(Localizer.getString("CONFIGURATION"));

		jmiOpenConfiguration = new JMenuItem(Localizer.getString("EDIT"));

		jmConfiguration.add(jmiOpenConfiguration);

		jmbMenueLeiste = new JMenuBar();
		jmbMenueLeiste.add(jmDatei);
		jmbMenueLeiste.add(jmConfiguration);
		container.add(jmbMenueLeiste, BorderLayout.NORTH);
	}

	public void update(Observable o, Object arg) {
		updateElements();
	}

	private void updateElements() {
		jpCenter.removeAll();
		for (DataFormElementModel element : dfm.getDfElements()) {
			DataFormElement dfe = new DataFormElement(element, jpCenter, dfm);
		}
		jtfFormTitel.setText(dfm.getTitle());
		jtaBeschreibung.setText(dfm.getDescription());
		// jpCenter.setPreferredSize(new Dimension(880,
		// dfm.getDfElements().size()
		// * _ELEMENTHEIGHT));

		jpCenter.validate();
		jpCenter.repaint();
		validate();
		repaint();

	}

	public void addCloseListener(ActionListener al) {
		jmiClose.addActionListener(al);
	}

	public void addFormTitleChangeListener(DocumentListener dl) {
		jtfFormTitel.getDocument().addDocumentListener(dl);
	}

	public void addAddFieldListener(ActionListener al) {
		btnAddField.addActionListener(al);
	}

	public void addDeleteFieldListener(ActionListener al) {
		btnDeleteField.addActionListener(al);
	}

	public void addOpenTemplatesRepositoryListener(ActionListener al) {
		jmiOpenTemplatesRepository.addActionListener(al);
	}

	public void addOpenRepositoryListener(ActionListener al) {
		jmiOpenRepository.addActionListener(al);
	}

	public void addMoveUpFieldListener(ActionListener al) {
		btnMoveUpField.addActionListener(al);
	}

	public void addMoveDownFieldListener(ActionListener al) {
		btnMoveDownField.addActionListener(al);
	}

	public void addSaveDataFormListener(ActionListener al) {
		jmiSaveToFile.addActionListener(al);
	}

	public void addOpenDataFormListener(ActionListener al) {
		jmiOpenFile.addActionListener(al);
	}

	public void addConfigurationListener(ActionListener al) {
		jmiOpenConfiguration.addActionListener(al);
	}

	protected DataFormModel model() {
		return dfm;
	}

	public String getFormTitle() {
		return jtfFormTitel.getText();
	}

	public String getFormDescription() {
		return jtaBeschreibung.getText();
	}

	public void addSaveDataFormRepositorListener(ActionListener al) {
		jmiSaveToRepository.addActionListener(al);
	}

	public void addFormDescriptionChangeListener(DocumentListener dl) {
		jtaBeschreibung.getDocument().addDocumentListener(dl);
	}

	public void addNewFormListener(ActionListener newFormListener) {
		jmNewForm.addActionListener(newFormListener);
	}

}
