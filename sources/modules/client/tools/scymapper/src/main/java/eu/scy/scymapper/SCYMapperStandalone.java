package eu.scy.scymapper;

import com.jgoodies.looks.windows.WindowsLookAndFeel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.impl.DiagramModel;
import eu.scy.scymapper.impl.SCYMapperPanel;
import eu.scy.scymapper.impl.model.DefaultConceptMap;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.api.*;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;

/**
 * User: Bjoerge Naess
 * Date: 01.okt.2009
 * Time: 19:53:10
 */
public class SCYMapperStandalone extends JFrame implements ISyncListener {

    private final String CONTEXT_CONFIG_CLASS_PATH_LOCATION = "eu/scy/scymapper/standaloneConfig.xml";
    private static final String SCYMAPPER_ELOTYPE = "scy/mapping";
    private ApplicationContext appContext;
    private IRepository repository;
    private IELOFactory eloFactory;
    private IConceptMap currentConceptMap;
    private IMetadataTypeManager metadataTypeManager;
    private final static Logger logger = Logger.getLogger(SCYMapperStandalone.class);
    private ISCYMapperToolConfiguration configuration;
    private IDataSyncService dataSyncService;
    private ToolBrokerAPI toolBroker;
    private ISyncSession currentSession;
    private SCYMapperPanel scyMapperPanel;

    public static void main(String[] args) {
        initLAF();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
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
        getContentPane().add(createScyMapperPanel());
    }

    public SCYMapperStandalone(URI eloURI) {
        init();
        getContentPane().add(createScyMapperPanel(eloURI));
    }

    public SCYMapperStandalone(IConceptMap cmap) {
        init();
        getContentPane().add(createScyMapperPanel(cmap));
    }

    void init() {

        appContext = new ClassPathXmlApplicationContext(CONTEXT_CONFIG_CLASS_PATH_LOCATION);

        repository = (IRepository) appContext.getBean("repository");
        eloFactory = (IELOFactory) appContext.getBean("eloFactory");
        metadataTypeManager = (IMetadataTypeManager) appContext.getBean("metadataTypeManager");

        configuration = (ISCYMapperToolConfiguration) appContext.getBean("configuration");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1400, 900);
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

        scyMapperPanel.setRepository(repository);
        scyMapperPanel.setEloFactory(eloFactory);
        scyMapperPanel.setMetadataTypeManager(metadataTypeManager);
        currentConceptMap = cmap;
        return scyMapperPanel;
    }

    private SCYMapperPanel createScyMapperPanel() {
        IDiagramModel diagram = new DiagramModel();
        IConceptMap cmap = new DefaultConceptMap(IConceptMap.DEFAULT_CMAP_NAME, diagram);
        if (configuration.getPredefinedNodes() != null) diagram.addNodes(configuration.getPredefinedNodes());
        return createScyMapperPanel(cmap);
    }

    private JPanel createScyMapperPanel(URI eloURI) {
        IConceptMap cmap = loadElo(eloURI);
        return createScyMapperPanel(cmap);
    }

    private void initMenuBar() {

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SCYMapperStandalone.this.dispose();
            }
        });
        fileMenu.add(new CreateConceptMapAction());
        fileMenu.add(new OpenConceptMapAction());
        fileMenu.add(new SaveConceptMapAction());
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

		// Session menu
        JMenu sessionMenu = new JMenu("Session");

		JMenu debugMenu = new JMenu("Debug");
		debugMenu.add(new ShortcutAction("obama", "obama", true));
        debugMenu.add(new ShortcutAction("bjoerge", "bjoerge"));
        debugMenu.add(new ShortcutAction("biden", "biden"));
        debugMenu.add(new ShortcutAction("merkel", "merkel"));

		sessionMenu.add(debugMenu);

        sessionMenu.add(new LoginAction());
        sessionMenu.add(new CreateSessionAction());
        sessionMenu.add(new JoinSessionAction());
        sessionMenu.add(new ShowSessionIDAction());

        menuBar.add(sessionMenu);

        setJMenuBar(menuBar);
    }

    public void saveELO(IConceptMap cmap, boolean saveAs) {

        if (saveAs || cmap.getName() == null || cmap.getName().equals(IConceptMap.DEFAULT_CMAP_NAME)) {

            String name = "";

            do name = JOptionPane.showInputDialog("Enter name:", cmap.getName());
            while (name.trim().equals(""));

            cmap.setName(name);
        }

        IELO elo = eloFactory.createELO();
        elo.setDefaultLanguage(Locale.ENGLISH);
        IMetadataKey uriKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
        IMetadataKey titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
        IMetadataKey typeKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
        IMetadataKey dateCreatedKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
        IMetadataKey authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue(cmap.getName());
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue(cmap.getName(), Locale.CANADA);
        elo.getMetadata().getMetadataValueContainer(typeKey).setValue(SCYMAPPER_ELOTYPE);
        elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(new Long(System.currentTimeMillis()));

        elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute("my vcard", System.currentTimeMillis()));
        IContent content = eloFactory.createContent();
        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(cmap);
        content.setXmlString(xml);
        elo.setContent(content);
        IMetadata resultMetadata = repository.addNewELO(elo);
        eloFactory.updateELOWithResult(elo, resultMetadata);
    }

    public IConceptMap loadElo(URI eloUri) {
        logger.info("Trying to load elo " + eloUri);
        IELO elo = repository.retrieveELO(eloUri);
        if (elo != null) {
            IMetadataKey titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
            IMetadataKey typeKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
            IMetadataKey dateCreatedKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());

            String eloType = elo.getMetadata().getMetadataValueContainer(typeKey).getValue().toString();

            if (!eloType.equals(SCYMAPPER_ELOTYPE))
                throw new IllegalArgumentException("ELO (" + eloUri + ") is of wrong type: " + eloType + " should be " + SCYMAPPER_ELOTYPE);
            IMetadata metadata = elo.getMetadata();
            IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);

            XStream xstream = new XStream(new DomDriver());

            return (IConceptMap) xstream.fromXML(elo.getContent().getXmlString());
        }
        return null;
    }

    private class OpenConceptMapAction extends AbstractAction {
        private OpenConceptMapAction() {
            super("Open");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("icons/open.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            IQuery query = null;
            java.util.List<ISearchResult> searchResults = repository.search(query);
            URI[] uris = new URI[searchResults.size()];
            int i = 0;
            for (ISearchResult searchResult : searchResults) {
                uris[i++] = searchResult.getUri();
            }
            final URI eloURI = (URI) JOptionPane.showInputDialog(null, "Select concept map", "Select concept map", JOptionPane.QUESTION_MESSAGE, null, uris, null);
            if (eloURI != null) {
                SCYMapperStandalone app = new SCYMapperStandalone(eloURI);
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

    private class SaveConceptMapAction extends AbstractAction {
        private SaveConceptMapAction() {
            super("Save");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("icons/save.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            saveELO(currentConceptMap, false);
        }
    }

    private class LoginAction extends AbstractAction {
        private LoginAction() {
            super("Login");
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            String username = JOptionPane.showInputDialog("Enter username");
            String password = JOptionPane.showInputDialog("Enter password");
            doLogin(username, password);
        }
    }

    private class CreateSessionAction extends AbstractAction {
        private CreateSessionAction() {
            super("Create session");
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (toolBroker == null) {
                JOptionPane.showMessageDialog(SCYMapperStandalone.this, "Please login first", "Not logged in", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("CREATING SESSION");
            createSession();
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
        private boolean autojoin;

        public ShortcutAction(String username, String password) {
            this(username, password, false);
        }

        public ShortcutAction(String username, String password, boolean autojoin) {
            super();
            this.autojoin = autojoin;
            if (autojoin)
                putValue(Action.NAME, "Login as " + username + ", create a session and join it");
            else
                putValue(Action.NAME, "Login as " + username + " and join another session");

            this.username = username;
            this.password = password;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doLogin(this.username, this.password);

            if (autojoin) {
                createSession();
                scyMapperPanel.setSession(currentSession);
                displaySessionId();
            }
            else {
                joinSession();
            }

        }
    }

    void doLogin(String username, String password) {
        toolBroker = new ToolBrokerImpl(username, password);
        dataSyncService = toolBroker.getDataSyncService();
        setTitle(username+" / "+getTitle());
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
        currentSession = dataSyncService.joinSession(sessId, this);
        scyMapperPanel.setSession(currentSession);
    }

    private void createSession() {
        try {
            currentSession = dataSyncService.createSession(this);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
	@Override
	public void syncObjectAdded(ISyncObject syncObject) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void syncObjectChanged(ISyncObject syncObject) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void syncObjectRemoved(ISyncObject syncObject) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
