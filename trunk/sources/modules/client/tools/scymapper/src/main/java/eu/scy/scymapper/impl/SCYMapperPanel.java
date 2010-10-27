package eu.scy.scymapper.impl;

import eu.scy.client.common.datasync.DataSyncException;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.notification.Notification;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.configuration.SCYMapperToolConfiguration;
import eu.scy.scymapper.impl.controller.datasync.DataSyncDiagramController;
import eu.scy.scymapper.impl.controller.datasync.DataSyncElementControllerFactory;
import eu.scy.scymapper.impl.logging.ConceptMapActionLogger;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.Notificator;
import eu.scy.scymapper.impl.ui.SlideNotificator;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.notification.KeywordSuggestionPanel;
import eu.scy.scymapper.impl.ui.palette.PalettePane;
import eu.scy.scymapper.impl.ui.toolbar.ConceptMapToolBar;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Bjoerge Date: 27.aug.2009 Time: 13:29:56
 */
public class SCYMapperPanel extends JPanel {

    private final static Logger logger = Logger.getLogger(SCYMapperPanel.class);
    private ToolBrokerAPI toolBroker;
    private IConceptMap conceptMap;
    private ISCYMapperToolConfiguration configuration;
    private ConceptDiagramView conceptDiagramView;
    private JTextField sessionId;
    private ISyncSession currentSession;
    protected Notificator notificator;
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
    protected ConceptMapActionLogger actionLogger;
    private ConceptMapPanel cmapPanel;
    protected ConceptMapToolBar toolBar;

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

    public void setToolBroker(ToolBrokerAPI tbi, String username) {
        toolBroker = tbi;
        if (toolBroker != null) {
            toolBroker.registerForNotifications(new INotifiable() {

                @Override
                public void processNotification(INotification notification) {
                    if (notification.getToolId().equals("scymapper")
                            && notification.getFirstProperty("type").equals("concept_proposal")) {
                        String[] keywords = notification.getPropertyArray("keyword");
                        if (keywords != null) {
                            List<String> keywordsAsList = new ArrayList<String>();
                            for (String keyword : keywords) {
                                keywordsAsList.add(keyword);
                            }
                            suggestKeywords(keywordsAsList, "concept");
                        }
                    }
                }
            });
            actionLogger = new ConceptMapActionLogger(toolBroker.getActionLogger(), getConceptMap().getDiagram(),
                    username);
        }
    }

    public void suggestKeywords(java.util.List<String> keywords, String type) {

        KeywordSuggestionPanel panel = new KeywordSuggestionPanel();

        if (keywords.isEmpty()) {
            return;
        }

        checkForAlreadyPresentConcepts(keywords);

        if (keywords.size() == 1) {
            panel.setSuggestion(keywords.get(0), configuration.getNodeFactories(), cmapPanel);
        } else {
            panel.setSuggestions(keywords, configuration.getNodeFactories(), cmapPanel);
        }

        panel.setSize(400, 350);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.darkGray, 1),
                BorderFactory.createRaisedBevelBorder()));

        if (notificator != null) {
            notificator.hide();
        }
        
        notificator = createNotificator(SCYMapperPanel.this, panel);

        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                notificator.hide();
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(close);
        panel.add(BorderLayout.SOUTH, close);

        notificator.show();
    }

    protected Notificator createNotificator(SCYMapperPanel scyMapperPanel, JPanel panel) {
        return new SlideNotificator(scyMapperPanel, panel);
    }

    private void checkForAlreadyPresentConcepts(List<String> keywords) {
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
        JLabel label = new JLabel("Notification recieved", icon, SwingConstants.LEFT);
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

        JButton close = new JButton("Close");
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
                "What keyword would you like to have suggested to you (Separate keywords with comma)?",
                "Contains, extensions, to, the, Swing, GUI, toolkit");

        if (input == null) {
            return;
        }
        java.util.List<String> keywords = Arrays.asList(input.split(",\\s+"));
        suggestKeywords(keywords, "keywords");

    }

    protected void initComponents() {

        JPanel topToolBarPanel = new JPanel(new GridLayout(1, 0));
        // topToolBarPanel.setBorder(BorderFactory.createTitledBorder("Session status"));

        //JPanel sessionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

//        sessionId = new JTextField("No session");
//        sessionId.setEditable(false);
//        sessionId.setPreferredSize(new Dimension(200, 20));
//
//        JButton joinSessionButton = new JButton("Join session");
//        joinSessionButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String sessId = JOptionPane.showInputDialog("Enter session ID");
//                joinSession(sessId);
//            }
//        });
//        JButton createSessionButton = new JButton("Create session");
//        createSessionButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                createSession();
//            }
//        });
//        sessionPanel.add(new JLabel("Session: "));
//        sessionPanel.add(sessionId);
//        sessionPanel.add(createSessionButton);
//        sessionPanel.add(joinSessionButton);

        JButton makeNotificationButton = new JButton("Create dummy notification");
        makeNotificationButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                INotification notification = new Notification();
                notification.setToolId("scymapper");
                notification.setSender("obama");
                notification.addProperty("dummyProperty", "DummyValue");
                showNotification(notification);
            }
        });

        JButton showNotificationButton = new JButton("Show notification");
        showNotificationButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                notificator.show();
            }
        });
        JButton hideNotificationButton = new JButton("Hide notification");
        hideNotificationButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                notificator.hide();
            }
        });
        JButton testSuggestKeywordButton = new JButton("Suggest keywords");
        testSuggestKeywordButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showKeywordSuggestion();
            }
        });
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

        toolBar = new ConceptMapToolBar(conceptMap, conceptDiagramView);
        JPanel palettePane = new PalettePane(conceptMap, configuration, cmapPanel);
        topToolBarPanel.add(toolBar);
//        topToolBarPanel.add(palettePane);

        add(BorderLayout.NORTH, topToolBarPanel);
        add(BorderLayout.WEST, palettePane);
        add(BorderLayout.CENTER, cmapPanel);

    }

    public IConceptMap getConceptMap() {
        return conceptMap;
    }

    public void setConceptMap(IConceptMap conceptMap) {
        removeAll();
        this.conceptMap = conceptMap;
        initComponents();
        actionLogger.setDiagram(getConceptMap().getDiagram());
        repaint();
    }

    private class CreateSessionAction extends AbstractAction {

        private CreateSessionAction() {
            super("Create and join session");
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (toolBroker == null) {
                JOptionPane.showMessageDialog(SCYMapperPanel.this, "Please login first", "Not logged in",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("CREATING SESSION");
            createSession();
//            displaySessionId();

        }
    }

//    private void displaySessionId() {
//        sessionId.setText(currentSession.getId());
//    }

    private class ShowSessionIDAction extends AbstractAction {

        private ShowSessionIDAction() {
            super("Display session ID");
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
//            displaySessionId();
        }
    }

    private class JoinSessionAction extends AbstractAction {

        private JoinSessionAction() {
            super("Join session");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (toolBroker == null) {
                JOptionPane.showMessageDialog(SCYMapperPanel.this, "Please login first", "Not logged in",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String sessId = JOptionPane.showInputDialog("Enter session ID");
            joinSession(sessId);
        }
    }

    public void joinSession(String sessId) {
		joinSession(sessId, false);
	}

	public void joinSession(String sessId, boolean writeCurrentStateToServer) {
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
	}

    public ISyncSession createSession() {
        if (toolBroker == null) {
            JOptionPane.showMessageDialog(this, "Error: ToolBroker is null", "Error", JOptionPane.ERROR_MESSAGE);
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
