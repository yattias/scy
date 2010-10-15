package eu.scy.scymapper;

import com.jgoodies.looks.windows.WindowsLookAndFeel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.scy.client.common.datasync.DataSyncException;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.configuration.Configuration;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.impl.DiagramModel;
import eu.scy.scymapper.impl.SCYMapperPanel;
import eu.scy.scymapper.impl.configuration.SCYMapperToolConfiguration;
import eu.scy.scymapper.impl.model.DefaultConceptMap;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.exceptions.ResourceException;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;

/**
 * User: Bjoerge Naess
 * Date: 01.okt.2009
 * Time: 19:53:10
 */
public class SCYMapperStandalone extends JFrame {
	private final String CONTEXT_CONFIG_CLASS_PATH_LOCATION = "eu/scy/scymapper/scymapperToolConfig.xml";
	private static final String SCYMAPPER_ELOTYPE = "scy/mapping";
	private ApplicationContext appContext;
	private Icon offlineIcon;
	private Icon onlineIcon;
	private IELO currentELO;
	private ToolBrokerAPI toolBroker;
	private IConceptMap currentConceptMap;
	private final static Logger logger = Logger.getLogger(SCYMapperStandalone.class);
	private ISCYMapperToolConfiguration configuration;
	private IDataSyncService dataSyncService;
	private ISyncSession currentSession;
	private SCYMapperPanel scyMapperPanel;
	private JComponent loginStatus;
	private ISyncListener dummySyncListener = new ISyncListener() {
		@Override
		public void syncObjectAdded(ISyncObject syncObject) {
		}

		@Override
		public void syncObjectChanged(ISyncObject syncObject) {
		}

		@Override
		public void syncObjectRemoved(ISyncObject syncObject) {
		}
	};
	private String username = "";

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initLAF();
				SCYMapperStandalone app = new SCYMapperStandalone();
				app.setTitle("SCYMapper Concept Mapping Tool");
				try {
					app.setIconImage(ImageIO.read(getClass().getResource("scy-mapper.png")));
				} catch (IOException e) {
				}
				app.setVisible(true);
			}
		});
	}

	public SCYMapperStandalone() {
		init();
		getContentPane().add(BorderLayout.CENTER, createScyMapperPanel());
	}

	public SCYMapperStandalone(URI eloURI, ToolBrokerAPI toolBroker) {
		init();
		this.toolBroker = toolBroker;
		getContentPane().add(BorderLayout.CENTER, createScyMapperPanel(eloURI));
	}

	public SCYMapperStandalone(IConceptMap cmap) {
		init();
		getContentPane().add(BorderLayout.CENTER, createScyMapperPanel(cmap));
	}

	void init() {
		appContext = new ClassPathXmlApplicationContext(CONTEXT_CONFIG_CLASS_PATH_LOCATION);
		configuration = (ISCYMapperToolConfiguration) appContext.getBean("configuration");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationByPlatform(true);
		onlineIcon = new ImageIcon(getClass().getResource("online.png"));
		offlineIcon = new ImageIcon(getClass().getResource("offline.png"));
		loginStatus = new JLabel(offlineIcon, JLabel.LEFT);
		((JLabel) loginStatus).setText("Offline");
		getContentPane().setLayout(new BorderLayout());
		initMenuBar();
	}

	private static void initLAF() {
		//String laf = UIManager.getSystemLookAndFeelClassName();
		//for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//            if ("Nimbus".equals(info.getName())) {
//                laf = info.getClassName();
//                break;
//            }
//        }

		try {
			UIManager.setLookAndFeel(new WindowsLookAndFeel());
		} catch (Exception e) {
			System.err.println("Can't set look & feel:" + e);
		}
	}

	private SCYMapperPanel createScyMapperPanel(IConceptMap cmap) {
		scyMapperPanel = new SCYMapperPanel(cmap, configuration);
		currentConceptMap = cmap;
		return scyMapperPanel;
	}

	private SCYMapperPanel createScyMapperPanel() {
		IDiagramModel diagram = new DiagramModel();
		currentConceptMap = new DefaultConceptMap(null, diagram);
		if (configuration.getPredefinedNodes() != null) diagram.addNodes(configuration.getPredefinedNodes());
		return createScyMapperPanel(currentConceptMap);
	}

	private JPanel createScyMapperPanel(URI eloURI) {
		loadElo(eloURI);
		return createScyMapperPanel(currentConceptMap);
	}

	private void initMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu viewMenu = new JMenu("View");
		JMenu sessionMenu = new JMenu("Session");
		
		menuBar.add(fileMenu);
                //some menu items are disables, as they are not useful in the standalone version
		//menuBar.add(viewMenu);
		//menuBar.add(sessionMenu);
		
		loginStatus = new JLabel(offlineIcon, JLabel.LEFT);
		((JLabel) loginStatus).setText("Offline");
		
		//menuBar.add(loginStatus);

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SCYMapperStandalone.this.dispose();
			}
		});
		fileMenu.add(new CreateConceptMapAction());
		//fileMenu.add(new OpenConceptMapAction());
		//fileMenu.add(new SaveAction());
		//fileMenu.add(new SaveAsAction());
		//fileMenu.add(new JPopupMenu.Separator());
		fileMenu.add(new ImportConceptMapAction());
		fileMenu.add(new ExportConceptMapAction());
		fileMenu.add(new JPopupMenu.Separator());
		fileMenu.add(exitItem);


		JMenuItem debugModeItem = new JCheckBoxMenuItem(new SetDebugModeAction());
		debugModeItem.setSelected(SCYMapperToolConfiguration.getInstance().isDebug());
		viewMenu.add(debugModeItem);

		JMenuItem viewShadowModeItem = new JCheckBoxMenuItem(new ViewShadowAction());
		viewShadowModeItem.setSelected(SCYMapperToolConfiguration.getInstance().getViewShadow());
		viewMenu.add(viewShadowModeItem);

		JMenu debugMenu = new JMenu("Debug");

		sessionMenu.add(new LoginAction());

		String ofHost = Configuration.getInstance().getOpenFireHost();
		if (ofHost.equals("scy.collide.info")) {
			debugMenu.add(new ShortcutAction("Pa1", "maupa"));
			debugMenu.add(new ShortcutAction("Pa2", "maupa"));
			debugMenu.add(new ShortcutAction("bjoerge", "bjoerge"));
			debugMenu.add(new ShortcutAction("adam", "adam"));
			debugMenu.add(new ShortcutAction("henrik", "henrik"));
			debugMenu.add(new ShortcutAction("wouter", "wouter"));
			debugMenu.add(new ShortcutAction("jakob", "jakob"));
			debugMenu.add(new ShortcutAction("stefan", "stefan"));
			debugMenu.add(new ShortcutAction("lars", "lars"));
			debugMenu.add(new ShortcutAction("anthony", "anthony"));
			debugMenu.add(new ShortcutAction("jeremy", "jeremy"));
			debugMenu.add(new ShortcutAction("marjolaine", "marjolaine"));
			debugMenu.add(new ShortcutAction("jan", "jan"));
			debugMenu.add(new ShortcutAction("philipp", "philipp"));
		} else if (ofHost.equals("scy.intermedia.uio.no")) {
			debugMenu.add(new ShortcutAction("obamao11", "obamao11"));
			debugMenu.add(new ShortcutAction("senders11", "senders11"));
			debugMenu.add(new ShortcutAction("djed11", "djed11"));
			debugMenu.add(new ShortcutAction("henrikh11", "henrikh11"));
			debugMenu.add(new ShortcutAction("henriks11", "henriks11"));
			debugMenu.add(new ShortcutAction("bobb11", "bobb11"));
		} else if (ofHost.equals("129.177.24.191")) {
			debugMenu.add(new ShortcutAction("obama", "obama"));
			debugMenu.add(new ShortcutAction("bjoerge", "bjoerge"));
			debugMenu.add(new ShortcutAction("henrik", "henrik"));
			debugMenu.add(new ShortcutAction("biden", "biden"));
			debugMenu.add(new ShortcutAction("merkel", "merkel"));
		}
		sessionMenu.add(debugMenu);

		sessionMenu.add(new LoginAction());
		sessionMenu.add(new CreateSessionAction());
		sessionMenu.add(new JoinSessionAction());
		sessionMenu.add(new ShowSessionIDAction());

		setJMenuBar(menuBar);
	}

	public void saveELO(boolean saveAs) {

		if (saveAs || currentConceptMap.getName() == null) {

			String name;

			do name = JOptionPane.showInputDialog("Enter name:", currentConceptMap.getName());
			while (name != null && name.trim().equals(""));

			if (name == null) return;
			currentConceptMap.setName(name);
		}

		IELO elo;
		if (currentELO == null || saveAs) {
			elo = toolBroker.getELOFactory().createELO();
		} else {
			elo = currentELO;
		}
		elo.setDefaultLanguage(Locale.ENGLISH);
		IMetadataKey uriKey = toolBroker.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
		IMetadataKey titleKey = toolBroker.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
		IMetadataKey typeKey = toolBroker.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
		IMetadataKey dateCreatedKey = toolBroker.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
		IMetadataKey authorKey = toolBroker.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
		elo.getMetadata().getMetadataValueContainer(titleKey).setValue(currentConceptMap.getName());
		elo.getMetadata().getMetadataValueContainer(titleKey).setValue(currentConceptMap.getName(), Locale.CANADA);
		elo.getMetadata().getMetadataValueContainer(typeKey).setValue(SCYMAPPER_ELOTYPE);
		elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(new Long(System.currentTimeMillis()));

		elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute("my vcard", System.currentTimeMillis()));
		IContent content = toolBroker.getELOFactory().createContent();
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(currentConceptMap);
		content.setXmlString(xml);
		elo.setContent(content);

		IMetadata metadata;
		if (elo.getUri() != null) {
			metadata = toolBroker.getRepository().updateELO(elo);
		} else {
			metadata = toolBroker.getRepository().addNewELO(elo);
		}
		toolBroker.getELOFactory().updateELOWithResult(elo, metadata);
		currentELO = elo;
	}

	public void loadElo(URI eloUri) throws ResourceException {
		logger.info("Trying to load elo " + eloUri);

		IELO elo = toolBroker.getRepository().retrieveELO(eloUri);

		currentELO = elo;

		if (elo == null) {
			throw new ResourceException("Could not find ELO " + eloUri);
		}

		IMetadataKey typeKey = toolBroker.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());

		String eloType = elo.getMetadata().getMetadataValueContainer(typeKey).getValue().toString();

		if (!eloType.equals(SCYMAPPER_ELOTYPE))
			throw new IllegalArgumentException("ELO (" + eloUri + ") is of wrong type: " + eloType + " should be " + SCYMAPPER_ELOTYPE);

		XStream xstream = new XStream(new DomDriver());

		currentConceptMap = (IConceptMap) xstream.fromXML(elo.getContent().getXmlString());
	}

	private class OpenConceptMapAction extends AbstractAction {
		private OpenConceptMapAction() {
			super("Open");
			putValue(SMALL_ICON, new ImageIcon(getClass().getResource("icons/open.png")));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (toolBroker == null) {
				JOptionPane.showMessageDialog(SCYMapperStandalone.this, "Please login first", "Not logged in", JOptionPane.ERROR_MESSAGE);
				return;
			}
			IQuery query = null;
			java.util.List<ISearchResult> searchResults = toolBroker.getRepository().search(query);
			URI[] uris = new URI[searchResults.size()];
			int i = 0;
			for (ISearchResult searchResult : searchResults) {
				uris[i++] = searchResult.getUri();
			}
			final URI eloURI = (URI) JOptionPane.showInputDialog(null, "Select concept map", "Select concept map", JOptionPane.QUESTION_MESSAGE, null, uris, null);
			if (eloURI != null) {
				SCYMapperStandalone app = new SCYMapperStandalone(eloURI, toolBroker);
				app.setVisible(true);
			}
		}
	}

	private class CreateConceptMapAction extends AbstractAction {
		private CreateConceptMapAction() {
			super("New Concept Map");
			putValue(SMALL_ICON, new ImageIcon(getClass().getResource("icons/new.png")));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			SCYMapperStandalone app = new SCYMapperStandalone();
			app.setVisible(true);
		}
	}

	private class SaveAction extends AbstractAction {
		private SaveAction() {
			super("Save");
			putValue(SMALL_ICON, new ImageIcon(getClass().getResource("icons/save.png")));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (toolBroker == null) {
				JOptionPane.showMessageDialog(SCYMapperStandalone.this, "Please login first", "Not logged in", JOptionPane.ERROR_MESSAGE);
				return;
			}

			saveELO(false);
		}
	}

	private class SaveAsAction extends AbstractAction {
		private SaveAsAction() {
			super("Save as...");
			putValue(SMALL_ICON, new ImageIcon(getClass().getResource("icons/save.png")));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (toolBroker == null) {
				JOptionPane.showMessageDialog(SCYMapperStandalone.this, "Please login first", "Not logged in", JOptionPane.ERROR_MESSAGE);
				return;
			}

			saveELO(true);
		}
	}

	private class ImportConceptMapAction extends AbstractAction {
		private ImportConceptMapAction() {
			//super("Import...");
                        super("Open...");
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".xml");
				}

				@Override
				public String getDescription() {
					return "XML Files";
				}
			});
			fileChooser.showOpenDialog(SCYMapperStandalone.this);
			File f = fileChooser.getSelectedFile();
			try {
				FileReader fr = new FileReader(f);
				XStream xstream = new XStream(new DomDriver());
				IConceptMap cmap = (IConceptMap) xstream.fromXML(fr);
				new SCYMapperStandalone(cmap).setVisible(true);
				fr.close();
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(SCYMapperStandalone.this, "Could not import file: " + e.getLocalizedMessage(), "Import failed",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(SCYMapperStandalone.this, "Could not import file: " + e.getLocalizedMessage(), "Import failed",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	private class ExportConceptMapAction extends AbstractAction {
		private ExportConceptMapAction() {
			//super("Export...");
                        super("Save as...");
		}

		@Override
		public void actionPerformed(ActionEvent evt) {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".xml");
				}

				@Override
				public String getDescription() {
					return "XML Files";
				}
			});
			fileChooser.setSelectedFile(new File(currentConceptMap.getName() + ".xml"));
			fileChooser.showSaveDialog(SCYMapperStandalone.this);
			File f = fileChooser.getSelectedFile();
			if (f.exists()) {
				int confirm = JOptionPane.showConfirmDialog(SCYMapperStandalone.this, "The file " + f.getName() + " already exists. Would you like to overwrite?", "File exists", JOptionPane.YES_NO_CANCEL_OPTION);
				if (confirm != JOptionPane.YES_OPTION) return;
			}
			try {
				FileWriter fw = new FileWriter(f);
				XStream xstream = new XStream(new DomDriver());
				xstream.toXML(currentConceptMap, fw);
				fw.close();
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(SCYMapperStandalone.this, "Could not export file: " + e.getLocalizedMessage(), "Export failed",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	private class LoginAction extends AbstractAction {
		private LoginAction() {
			super("Login");
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			username = JOptionPane.showInputDialog("Enter username");
			if (username != null) {
				String password = JOptionPane.showInputDialog("Enter password");
				if (password != null)
					doLogin(username, password);
			}
		}
	}

	private class CreateSessionAction extends AbstractAction {
		private CreateSessionAction() {
			super("Create and join session");
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			if (toolBroker == null) {
				JOptionPane.showMessageDialog(SCYMapperStandalone.this, "Please login first", "Not logged in", JOptionPane.ERROR_MESSAGE);
				return;
			}
			System.out.println("CREATING SESSION");
			currentSession = scyMapperPanel.createSession();
			displaySessionId();
		}
	}

	private class ShowSessionIDAction extends AbstractAction {
		private ShowSessionIDAction() {
			super("Display session ID");
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			displaySessionId();
		}
	}

	private class JoinSessionAction extends AbstractAction {
		private JoinSessionAction() {
			super("Join session");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (toolBroker == null) {
				JOptionPane.showMessageDialog(SCYMapperStandalone.this, "Please login first", "Not logged in", JOptionPane.ERROR_MESSAGE);
				return;
			}
			joinSession();
		}
	}

	private class ShortcutAction extends AbstractAction {
		private String username;
		private String password;

		public ShortcutAction(String username, String password) {
			super();
			putValue(Action.NAME, "Login as " + username);

			this.username = username;
			this.password = password;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			doLogin(ShortcutAction.this.username, ShortcutAction.this.password);
		}
	}

	private class ViewShadowAction extends AbstractAction {

		ViewShadowAction() {
			super();
			putValue(Action.NAME, "View shadows");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JCheckBoxMenuItem) {
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
				SCYMapperToolConfiguration.getInstance().setViewShadow(item.isSelected());
			}
		}
	}

	private class SetDebugModeAction extends AbstractAction {

		public SetDebugModeAction() {
			super();
			putValue(Action.NAME, "Debug mode");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JCheckBoxMenuItem) {
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
				SCYMapperToolConfiguration.getInstance().setDebug(item.isSelected());
			}
		}
	}

	void doLogin(String username, String password) {
		try {
			String ofHost = Configuration.getInstance().getOpenFireHost();
			logger.debug("Logging into OpenFire @ " + ofHost);

			if (toolBroker != null) {
				throw new IllegalAccessException("Already logged in");
			}

			toolBroker = new ToolBrokerImpl(username, password);
			dataSyncService = toolBroker.getDataSyncService();
			this.username = username;
			setTitle(username + " / " + getTitle());
			((JLabel) loginStatus).setIcon(onlineIcon);
			((JLabel) loginStatus).setText("Logged in as " + username);
			scyMapperPanel.setToolBroker(toolBroker, this.username);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(SCYMapperStandalone.this, "Error logging in: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	void displaySessionId() {
		if (currentSession == null) {
			JOptionPane.showMessageDialog(SCYMapperStandalone.this, "You are not in any session", "No session", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JTextField sessionTextField = new JTextField(currentSession.getId());
		sessionTextField.setEditable(false);
		Component[] comps = {new JLabel("Your session ID is:"), sessionTextField};
		JOptionPane.showMessageDialog(SCYMapperStandalone.this, comps, "Success", JOptionPane.INFORMATION_MESSAGE);
	}


	private void joinSession() {
		String sessId = JOptionPane.showInputDialog("Enter session ID");
		if (sessId != null) {
				scyMapperPanel.joinSession(sessId);
		}
	}

}
