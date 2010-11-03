package eu.scy.scymapper.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.dgc.VMID;
import java.util.List;
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
import eu.scy.scymapper.impl.ui.notification.DoubleKeywordSuggestionPanel;
import eu.scy.scymapper.impl.ui.notification.KeywordSuggestionPanel;

public class SCYMapperPanelCollide extends SCYMapperPanel {

    private final static FadeNotificator.Position NOTIFY_POSITION = FadeNotificator.Position.EAST;

    private SCYMapperStandaloneConfig standaloneConfig;

    private JButton requestConceptHelpButton;

    private JButton requestRelationHelpButton;

    private Timer timer;

    private Help helpMode;

    private Timer helpTimer;

    private long helpInterval;

    public SCYMapperPanelCollide(IConceptMap cmap, ISCYMapperToolConfiguration configuration, String sqlspacesHost, int sqlspacesPort) {
        super(cmap, configuration);
        actionLogger = new ConceptMapActionLogger(new SQLSpacesActionLogger(sqlspacesHost, sqlspacesPort, "actions"), getConceptMap().getDiagram(), new VMID().toString());
    }

    @Override
    protected Notificator createNotificator(JComponent parent, JPanel panel) {
    	int yOffset = -cmapPanel.getY();
    	FadeNotificator fn = new FadeNotificator(parent, panel, NOTIFY_POSITION, 0, yOffset);
    	fn.setBorderPainted(false);
        return fn;
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
                        
                    }, 0, helpInterval);
                }
            }, standaloneConfig.getContinuousHelpWaitTime() * 10);
            invalidate();
			break;
            case NOHELP:
                // NOHELP means nothing to do ;)
            default:
                // Same as no help
                break;
        }
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
    public void suggestKeywords(List<String> keywords, String type) {

        if (helpMode == Help.VOLUNTARY) {
            requestConceptHelpButton.setEnabled(true);
            requestRelationHelpButton.setEnabled(true);
            suggestionPanel = new KeywordSuggestionPanel();
        }

        suggestionPanel.setSuggestions(keywords, configuration.getNodeFactories(), cmapPanel, type);

        if (helpMode == Help.VOLUNTARY) {

            suggestionPanel.setSize(300, cmapPanel.getHeight() - 2);
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

			JPanel btnPanel = new JPanel();
			btnPanel.add(close);
			suggestionPanel.add(BorderLayout.SOUTH, close);

			notificator.show();
        }
    }

    protected void requestConceptHelp() {
    	actionLogger.logRequestConceptHelp();
    }
    protected void requestRelationHelp() {
    	actionLogger.logRequestRelationHelp();
    }
}
