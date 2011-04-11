package eu.scy.scymapper.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import eu.scy.client.common.datasync.DataSyncException;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.configuration.SCYMapperToolConfiguration;
import eu.scy.scymapper.impl.controller.datasync.DataSyncDiagramController;
import eu.scy.scymapper.impl.controller.datasync.DataSyncElementControllerFactory;
import eu.scy.scymapper.impl.logging.ConceptMapActionLogger;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.Localization;
import eu.scy.scymapper.impl.ui.Notificator;
import eu.scy.scymapper.impl.ui.SlideNotificator;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.notification.KeywordSuggestionPanel;
import eu.scy.scymapper.impl.ui.palette.PalettePane;
import eu.scy.scymapper.impl.ui.toolbar.ConceptMapToolBar;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * User: Bjoerge Date: 27.aug.2009 Time: 13:29:56
 */
public class SCYMapperPanel extends JPanel implements INotifiable {

    private final static Logger logger = Logger.getLogger(SCYMapperPanel.class);

    private ToolBrokerAPI toolBroker;
    
    private IConceptMap conceptMap;
    
    protected ISCYMapperToolConfiguration configuration;
    
    private ConceptDiagramView conceptDiagramView;
    
    private JTextField sessionId;
    
    private ISyncSession currentSession;
    
    protected ConceptMapPanel cmapPanel;
    
    protected KeywordSuggestionPanel suggestionPanel;

    protected ConceptMapActionLogger actionLogger;
    
    protected Notificator notificator;

    protected ConceptMapToolBar toolBar;
    
    private ISCYMapperToolConfiguration conf = SCYMapperToolConfiguration.getInstance();
    
    private ISyncListener dummySyncListener = new ISyncListener() {

        @Override
        public void syncObjectAdded(ISyncObject iSyncObject) {
        }

        @Override
        public void syncObjectChanged(ISyncObject iSyncObject) {
        }

        @Override
        public void syncObjectRemoved(ISyncObject iSyncObject) {
        }
    };
    
    public SCYMapperPanel(IConceptMap cmap, ISCYMapperToolConfiguration configuration) {
        conceptMap = cmap;
        this.configuration = configuration;
        setLayout(new BorderLayout());
        initComponents();
        validate();
    }

    public ConceptDiagramView getConceptDiagramView() {
        return conceptDiagramView;
    }

    public ConceptMapActionLogger getConceptMapActionLogger() {
        return actionLogger;
    }

    public void setToolBroker(ToolBrokerAPI tbi, String username) {
        toolBroker = tbi;
        if (toolBroker != null) {
            actionLogger = new ConceptMapActionLogger(toolBroker.getActionLogger(), getConceptMap().getDiagram(),
                    username);
        }
    }

    @Override
    public boolean processNotification(INotification notification) {
        if ("concept_proposal".equals(notification.getFirstProperty("type"))) {
            String[] keywords = notification.getPropertyArray("keyword");
            if (keywords != null) {
                List<String> keywordsAsList = new ArrayList<String>();
                for (String keyword : keywords) {
                    keywordsAsList.add(keyword);
                }
                suggestKeywords(keywords, null, "concept");
            }
            return true;
        } else if ("cmenricher agent".equals(notification.getSender())) {
            List<String> keywords = new ArrayList<String>();
            List<String> categories = new ArrayList<String>();
            String type = notification.getFirstProperty("proposal_type");
            
            notification.getPropertyArray("relation_proposal");
            for (String cprop : notification.getPropertyArray("concept_proposal")) {
                keywords.add(cprop);
                categories.add("concept_proposal");
            }
            for (String rprop : notification.getPropertyArray("relation_proposal")) {
                keywords.add(rprop);
                categories.add("relation_relation");
            }
            String[] keywordArray = (String[]) keywords.toArray(new String[keywords.size()]);
            String[] categoryArray = (String[]) categories.toArray(new String[categories.size()]);
            suggestKeywords(keywordArray, categoryArray, type);
            return true;
        } else {
            return false;
        }
    }

    public void suggestKeywords(String[] keywords, String[] category, String type) {

    	suggestionPanel = new KeywordSuggestionPanel(actionLogger);

        if (keywords.length == 0) {
            return;
        }

        suggestionPanel.setSuggestions(keywords, category, configuration.getNodeFactories(), "concept", false);

        suggestionPanel.setSize(300, cmapPanel.getHeight()-2);
        suggestionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.darkGray, 1),
                BorderFactory.createRaisedBevelBorder()));

        if (notificator != null) {
            notificator.hide();
        }
        
        notificator = createNotificator(this, suggestionPanel);

        JButton close = new JButton(Localization.getString("Mainframe.Input.Close"));
        close.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                notificator.hide();
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(close);
        suggestionPanel.add(BorderLayout.SOUTH, close);

        notificator.show();
    }

    protected Notificator createNotificator(JComponent parent, JPanel panel) {
        return new SlideNotificator(parent, panel);
    }

    protected void checkForAlreadyPresentConcepts(List<String> keywords) {
        Set<INodeModel> nodes = conceptMap.getDiagram().getNodes();
        for (INodeModel node : nodes) {
            if (keywords.contains(node.getLabel())) {
                keywords.remove(node.getLabel());
            }
        }
    }

    public void joinSession(ISyncSession session) {
    	DataSyncDiagramController diagramController = new DataSyncDiagramController(conceptMap.getDiagram(), session);
    	diagramController.setSession(currentSession, true);
        conceptDiagramView.setController(diagramController);
        conceptDiagramView.setElementControllerFactory(new DataSyncElementControllerFactory(session));
    }

    private void showNotification(INotification notification) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        Icon icon = UIManager.getIcon("OptionPane.informationIcon");
        JLabel label = new JLabel(Localization.getString("Mainframe.Notificiation.Recieved"), icon, SwingConstants.LEFT);
        panel.add(BorderLayout.NORTH, label);

        StringBuilder sb = new StringBuilder();

        sb.append("--- NOTIFICATION ---").append("\n\n").append("ToolID:	").append(notification.getToolId()).append(
                "\n").append("Mission:	").append(notification.getMission()).append("\n").append("Sender:	").append(
                notification.getSender()).append("\n").append("UserId:	").append(notification.getUserId()).append("\n").append("ToolId:	").append(notification.getToolId()).append("\n").append("Session:	").append(
                notification.getSession()).append("\n").append("Properties --------------\n");

        for (Map.Entry<String, String[]> entry : notification.getProperties().entrySet()) {
            sb.append("  ").append(entry.getKey()).append(":	").append(entry.getValue()).append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        panel.add(BorderLayout.CENTER, textArea);

        panel.setSize(400, 350);
        if (notificator != null) {
            notificator.hide();
        }
        notificator = createNotificator(SCYMapperPanel.this, panel);

        JButton close = new JButton(Localization.getString("Mainframe.Input.Close"));
        close.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                notificator.hide();
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(close);
        panel.add(BorderLayout.SOUTH, btnPanel);

        notificator.show();
    }

    private void showKeywordSuggestion() {

        String input = JOptionPane.showInputDialog(
        		Localization.getString("Dialog.Message.KeywordSuggestion.Line.1"), 
        		Localization.getString("Dialog.Message.KeywordSuggestion.Line.2")); 

        if (input == null) {
            return;
        }
        suggestKeywords(input.split(",\\s+"), null, "concept");

    }

    protected void initComponents() {

    	JPanel topToolBarPanel = new JPanel(new GridLayout(1, 0));
        
        // topToolBarPanel.setBorder(BorderFactory.createTitledBorder(Localization.getString("Mainframe.Toolbar.Session.Status")));

        //JPanel sessionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

//        sessionId = new JTextField(Localization.getString("Mainframe.Toolbar.Session.NoSession"));
//        sessionId.setEditable(false);
//        sessionId.setPreferredSize(new Dimension(200, 20));
//
//        JButton joinSessionButton = new JButton(Localization.getString("Mainframe.Toolbar.Session.Join"));
//        joinSessionButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String sessId = JOptionPane.showInputDialog(Localization.getString("Dialog.Input.SessionID"));
//                joinSession(sessId);
//            }
//        });
//        JButton createSessionButton = new JButton(Localization.getString("Mainframe.Toolbar.Session.Create"));
//        createSessionButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                createSession();
//            }
//        });
//        sessionPanel.add(new JLabel(Localization.getString("Mainframe.Toolbar.Session.Create") + " "));
//        sessionPanel.add(sessionId);
//        sessionPanel.add(createSessionButton);
//        sessionPanel.add(joinSessionButton);

        // This is actually never been used
//        JButton makeNotificationButton = new JButton("Create dummy notification");
//        makeNotificationButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                INotification notification = new Notification();
//                notification.setToolId("scymapper");
//                notification.setSender("obama");
//                notification.addProperty("dummyProperty", "DummyValue");
//                showNotification(notification);
//            }
//        });
//        JButton showNotificationButton = new JButton(Localization.getString("Mainframe.Notification.Show"));
//        showNotificationButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                notificator.show();
//            }
//        });
//        JButton hideNotificationButton = new JButton(Localization.getString("Mainframe.Notification.Hide"));
//        hideNotificationButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                notificator.hide();
//            }
//        });
//        JButton testSuggestKeywordButton = new JButton(Localization.getString("Mainframe.KeywordSuggestion.Button"));
//        testSuggestKeywordButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                showKeywordSuggestion();
//            }
//        });

 //        if (conf.isDebug()) {
//            sessionPanel.add(hideNotificationButton);
//            sessionPanel.add(makeNotificationButton);
//            sessionPanel.add(showNotificationButton);
//        }
//        sessionPanel.add(testSuggestKeywordButton);
//        topToolBarPanel.add(sessionPanel);
        
        cmapPanel = new ConceptMapPanel(conceptMap);
        cmapPanel.setBackground(Color.WHITE);
        conceptDiagramView = cmapPanel.getDiagramView();

        toolBar = createToolbar(conceptMap, conceptDiagramView);
        JPanel palettePane = new PalettePane(conceptMap, configuration, cmapPanel);
        topToolBarPanel.add(toolBar);
//        topToolBarPanel.add(palettePane);
        
        createKeywordSuggestionPanel();

        add(BorderLayout.NORTH, topToolBarPanel);
        add(BorderLayout.WEST, palettePane);
        add(BorderLayout.CENTER, cmapPanel);
        add(BorderLayout.EAST, suggestionPanel);
    }

    protected ConceptMapToolBar createToolbar(IConceptMap conceptMap, ConceptDiagramView conceptDiagramView) {
        return new ConceptMapToolBar(conceptMap, conceptDiagramView);
    }

    public void createKeywordSuggestionPanel() {
        suggestionPanel = new KeywordSuggestionPanel(actionLogger);
        suggestionPanel.setVisible(false);
    }

    public IConceptMap getConceptMap() {
        return conceptMap;
    }
    
    public void setConceptMap(IConceptMap conceptMap) {
        removeAll();
        this.conceptMap = conceptMap;
        initComponents();
        actionLogger.setDiagram(getConceptMap().getDiagram());
        for (INodeModel node: getConceptMap().getDiagram().getNodes()) {
            // add actionlogger as listener to all nodes, needed for logging!
            node.addListener(actionLogger);
        }
        for (ILinkModel link: getConceptMap().getDiagram().getLinks()) {
            // add actionlogger as listener to all links, needed for logging!
            link.addListener(actionLogger);
        }
        repaint();
    }

    private class CreateSessionAction extends AbstractAction {

        private CreateSessionAction() {
            super(Localization.getString("Mainframe.Menubar.Session.CreateJoinSession"));
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (toolBroker == null) {
                JOptionPane.showMessageDialog(SCYMapperPanel.this, Localization.getString("Dialog.Message.LoginFirst.Text"), Localization.getString("Dialog.Message.LoginFirst.Title"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            createSession();
//            displaySessionId();

        }
    }

//    private void displaySessionId() {
//        sessionId.setText(currentSession.getId());
//    }

    private class ShowSessionIDAction extends AbstractAction {

        private ShowSessionIDAction() {
            super(Localization.getString("Mainframe.Menubar.Session.ShowID"));
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
//            displaySessionId();
        }
    }

    private class JoinSessionAction extends AbstractAction {

        private JoinSessionAction() {
            super(Localization.getString("Mainframe.Menubar.Session.Join"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (toolBroker == null) {
                JOptionPane.showMessageDialog(SCYMapperPanel.this, Localization.getString("Dialog.Message.LoginFirst.Text"), Localization.getString("Dialog.Message.LoginFirst.Title"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String sessId = JOptionPane.showInputDialog(Localization.getString("Dialog.Input.SessionID"));
            joinSession(sessId);
        }
    }

    public ISyncSession joinSession(String sessId) {
		return joinSession(sessId, true);
	}

	public ISyncSession joinSession(String sessId, boolean writeCurrentStateToServer) {
		if (sessId != null) {
			try {
				DataSyncDiagramController diagramController = new DataSyncDiagramController(conceptMap.getDiagram());
				currentSession = toolBroker.getDataSyncService().joinSession(sessId, diagramController, "scymapper");
				diagramController.setSession(currentSession, writeCurrentStateToServer);
				conceptDiagramView.setController(diagramController);
				conceptDiagramView.setElementControllerFactory(new DataSyncElementControllerFactory(currentSession));
			} catch (DataSyncException e) {
				e.printStackTrace();
			}
		}
		return currentSession;
	}

    public ISyncSession createSession() {
        if (toolBroker == null) {
            JOptionPane.showMessageDialog(this, Localization.getString("Dialog.Message.ToolBrokerNull.Text"), Localization.getString("Dialog.Message.ToolBrokerNull.Title"), JOptionPane.ERROR_MESSAGE);
        }
        try {
            currentSession = toolBroker.getDataSyncService().createSession(dummySyncListener, "scymapper");
            joinSession(currentSession);
            actionLogger.setSession(currentSession);
            return currentSession;
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
    
    public void setEloURI(String eloURI) {
        actionLogger.setEloURI(eloURI);
    }
}
