package eu.scy.scymapper.impl;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.dgc.VMID;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import eu.scy.actionlogging.SQLSpacesActionLogger;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig;
import eu.scy.scymapper.impl.logging.ConceptMapActionLogger;
import eu.scy.scymapper.impl.ui.FadeNotificator;
import eu.scy.scymapper.impl.ui.Notificator;
import eu.scy.scymapper.impl.ui.notification.KeywordSuggestionPanel;

public class SCYMapperPanelCollide extends SCYMapperPanel {

    private final static FadeNotificator.Position NOTIFY_POSITION = FadeNotificator.Position.EAST;

    private SCYMapperStandaloneConfig standaloneConfig;

    private JButton requestConceptHelpButton;

    private JButton requestRelationHelpButton;
    
    // TODO for testing purposes only
    TupleSpace commandSpace;

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
		super.initComponents();
    	standaloneConfig = SCYMapperStandaloneConfig.getInstance();

		switch (standaloneConfig.getHelpMode()) {
		case VOLUNTARY:
			// Provide voluntary help
			requestConceptHelpButton = new JButton("Request Concept Help");
			requestConceptHelpButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// changed for testing purposes
					// won't use the agents framework, instead write an example proposal tuple directly in TS
//					requestConceptHelpButton.setEnabled(false);
//					requestRelationHelpButton.setEnabled(false);
//					requestConceptHelp();
					try {
                		commandSpace = new TupleSpace(new User("SCYMapper"), "localhost", 2525, "command");
                        Tuple tuple = new Tuple("notification", "dummy-id", "bibi blocksberg", "scymapper","sender", "mission", "session", "proposal=Proposal 1", "proposal=Proposal 2");
                		commandSpace.write(tuple);
					} catch (TupleSpaceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			});

			requestRelationHelpButton = new JButton("Request Relation Help");
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
        	// TODO Provide help after standaoneConfig.getContinuousHelpWaitTime() seconds
			break;

		case NOHELP:
        	// NOHELP means nothing to do ;)
		default:
			// Same as no help
			break;
		}
    }

    @Override
    public void suggestKeywords(List<String> keywords, String type) {

    	suggestionPanel = new KeywordSuggestionPanel();

        if (keywords.isEmpty()) {
            return;
        }

        checkForAlreadyPresentConcepts(keywords);

        if (keywords.size() == 1) {
            suggestionPanel.setSuggestion(keywords.get(0), configuration.getNodeFactories(), cmapPanel);
        } else {
            suggestionPanel.setSuggestions(keywords, configuration.getNodeFactories(), cmapPanel);
        }

        suggestionPanel.setSize(300, cmapPanel.getHeight()-2);
        suggestionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.darkGray, 1),
                BorderFactory.createRaisedBevelBorder()));

        if (notificator != null) {
            notificator.hide();
        }
        
        notificator = createNotificator(this, suggestionPanel);

        JButton close = new JButton("Close");
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

    protected void requestConceptHelp() {
    	actionLogger.logRequestConceptHelp();
    }
    protected void requestRelationHelp() {
    	actionLogger.logRequestRelationHelp();
    }
}
