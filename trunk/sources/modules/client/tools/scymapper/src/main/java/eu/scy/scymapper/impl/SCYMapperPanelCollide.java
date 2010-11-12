package eu.scy.scymapper.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.dgc.VMID;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import eu.scy.actionlogging.SQLSpacesActionLogger;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig.Help;
import eu.scy.scymapper.impl.logging.ConceptMapActionLogger;
import eu.scy.scymapper.impl.ui.FadeNotificator;
import eu.scy.scymapper.impl.ui.Localization;
import eu.scy.scymapper.impl.ui.Notificator;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.notification.ConceptBrowserPanel;
import eu.scy.scymapper.impl.ui.notification.DoubleKeywordSuggestionPanel;
import eu.scy.scymapper.impl.ui.notification.KeywordSuggestionPanel;
import eu.scy.scymapper.impl.ui.toolbar.ConceptMapToolBar;
import eu.scy.scymapper.impl.ui.toolbar.ConceptMapToolBarCollide;

public class SCYMapperPanelCollide extends SCYMapperPanel {

    private final static FadeNotificator.Position SUGGEST_KEYWORD_POSITION = FadeNotificator.Position.EAST;

    private final static FadeNotificator.Position LEXICON_POSITION = FadeNotificator.Position.LOWER_LEFT_CORNER;

    private Notificator lexiconNotificator;

    private SCYMapperStandaloneConfig standaloneConfig;

    private ConceptBrowserPanel conceptBrowserPanel;
    
    private JButton requestConceptHelpButton;

    private JButton requestRelationHelpButton;

    private JButton requestLexiconButton;
    
    private Timer timer;

    private Help helpMode;

    private Timer helpTimer;

    private long helpInterval;

    public SCYMapperPanelCollide(IConceptMap cmap, ISCYMapperToolConfiguration configuration, String sqlspacesHost, int sqlspacesPort, String userid) {
        super(cmap, configuration);
        actionLogger = new ConceptMapActionLogger(new SQLSpacesActionLogger(sqlspacesHost, sqlspacesPort, "actions"), getConceptMap().getDiagram(), userid);
    }

    @Override
    protected Notificator createNotificator(JComponent parent, JPanel panel) {
        return createNotificator(parent, panel, SUGGEST_KEYWORD_POSITION);
    }

    protected Notificator createNotificator(JComponent parent, JPanel panel, FadeNotificator.Position position) {
//    	int yOffset = -cmapPanel.getY();
//    	FadeNotificator fn = new FadeNotificator(parent, panel, position, 0, yOffset);
    	FadeNotificator fn = new FadeNotificator(parent, panel, position);
    	fn.setBorderPainted(false);
        return fn;
    }

    protected Notificator createNotificator(JComponent parent, JPanel panel, FadeNotificator.Position position, int xOffset, int yOffset) {
    	FadeNotificator fn = new FadeNotificator(parent, panel, position, xOffset, yOffset);
    	fn.setBorderPainted(false);
        return fn;
    }

    @Override
    protected ConceptMapToolBar createToolbar(IConceptMap conceptMap, ConceptDiagramView conceptDiagramView) {
        return new ConceptMapToolBarCollide(conceptMap, conceptDiagramView);
    }

    @Override
    protected void initComponents() {
        standaloneConfig = SCYMapperStandaloneConfig.getInstance();
        this.helpMode = standaloneConfig.getHelpMode();
        this.helpInterval = standaloneConfig.getContinuousHelpInterval();
        super.initComponents();
		switch (helpMode) {
		case VOLUNTARY:
			// Provide voluntary help
			requestConceptHelpButton = new JButton(Localization.getString("Mainframe.Toolbar.RequestConceptHelp"));
			requestConceptHelpButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    requestConceptHelpButton.setEnabled(false);
                    requestRelationHelpButton.setEnabled(false);
                    requestConceptHelp();
                }
			});

			requestRelationHelpButton = new JButton(Localization.getString("Mainframe.Toolbar.RequestRelationHelp"));
			requestRelationHelpButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					requestConceptHelpButton.setEnabled(false);
					requestRelationHelpButton.setEnabled(false);
					requestRelationHelp();
				}

			});
			toolBar.add(requestConceptHelpButton);
			toolBar.add(requestRelationHelpButton);

			invalidate();
			break;

		case CONTINUOUS:
            timer = new Timer(true);
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    suggestionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    suggestionPanel.setVisible(true);
                    helpTimer = new Timer(true);
                    helpTimer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            requestConceptHelp();
                            requestRelationHelp();
                        }

                    }, 0, helpInterval * 1000);
                }
            }, standaloneConfig.getContinuousHelpWaitTime() * 1000);
            invalidate();
			break;
            case NOHELP:
                // NOHELP means nothing to do ;)
            default:
                // Same as no help
                break;
        }

		requestLexiconButton = new JButton(Localization.getString("Mainframe.Toolbar.RequestLexicon"));
		requestLexiconButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(lexiconNotificator != null && lexiconNotificator.isVisible()) {
					lexiconNotificator.hide();
				} else {
					showLexicon();
				}
			}

		});
		toolBar.add(requestLexiconButton);
		
        conceptBrowserPanel = new ConceptBrowserPanel();
        conceptBrowserPanel.setVisible(false);
        add(BorderLayout.SOUTH, conceptBrowserPanel);
    }

    @Override
    public void createKeywordSuggestionPanel() {
        if (helpMode == Help.CONTINUOUS) {
            suggestionPanel = new DoubleKeywordSuggestionPanel();
        } else {
            suggestionPanel = new KeywordSuggestionPanel();
        }
        suggestionPanel.setVisible(false);
    }

    @Override
    public void suggestKeywords(String[] keywords, String[] categories, String type) {

        if (helpMode == Help.VOLUNTARY) {
            requestConceptHelpButton.setEnabled(true);
            requestRelationHelpButton.setEnabled(true);
            suggestionPanel = new KeywordSuggestionPanel();
        }

        if (type.equals("concept")) {
            suggestionPanel.setTitle(Localization.getString("Mainframe.KeywordSuggestion.ConceptTitle"));
    	} else {
            suggestionPanel.setTitle(Localization.getString("Mainframe.KeywordSuggestion.RelationTitle"));
    	}

        suggestionPanel.setSuggestions(keywords, categories, configuration.getNodeFactories(), type, helpMode == Help.CONTINUOUS);

        if (helpMode == Help.VOLUNTARY) {

            suggestionPanel.setSize(300, cmapPanel.getHeight() - 300);
            suggestionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.darkGray, 1), BorderFactory.createRaisedBevelBorder()));

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
			suggestionPanel.add(BorderLayout.SOUTH, close);

			notificator.show();
        }
    }

    /**
     * Creates a new ConceptBrowserPanel with a new set of lexicon entries and shows it on screen.
     */
    public void showLexicon() {

    	conceptBrowserPanel = new ConceptBrowserPanel();
    	conceptBrowserPanel.readLexicon();

    	conceptBrowserPanel.setSize(250, 250);
    	conceptBrowserPanel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
    	conceptBrowserPanel.setBackground(Color.WHITE);

        if (lexiconNotificator != null) {
        	lexiconNotificator.hide();
        }
        
        lexiconNotificator = createNotificator(this, conceptBrowserPanel, LEXICON_POSITION, cmapPanel.getX(), cmapPanel.getY()-8);

        JButton close = new JButton(Localization.getString("Mainframe.Input.Close"));
        close.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	lexiconNotificator.hide();
            }
        });
        conceptBrowserPanel.add(BorderLayout.SOUTH, close);

        lexiconNotificator.show();
    }

    protected void requestConceptHelp() {
    	actionLogger.logRequestConceptHelp();
    }
    protected void requestRelationHelp() {
    	actionLogger.logRequestRelationHelp();
    }
}
