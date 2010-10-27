package eu.scy.scymapper.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.dgc.VMID;

import javax.swing.JButton;
import javax.swing.JPanel;

import eu.scy.actionlogging.SQLSpacesActionLogger;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.impl.logging.ConceptMapActionLogger;
import eu.scy.scymapper.impl.ui.FadeNotificator;
import eu.scy.scymapper.impl.ui.Notificator;

public class SCYMapperPanelCollide extends SCYMapperPanel {

    private final static FadeNotificator.Position NOTIFY_POSITION = FadeNotificator.Position.EAST;

    private JButton requestConceptHelpButton;
    private JButton requestRelationHelpButton;

    public SCYMapperPanelCollide(IConceptMap cmap, ISCYMapperToolConfiguration configuration, String sqlspacesHost, int sqlspacesPort) {
        super(cmap, configuration);
        actionLogger = new ConceptMapActionLogger(new SQLSpacesActionLogger(sqlspacesHost, sqlspacesPort, "actions"), getConceptMap().getDiagram(), new VMID().toString());
    }
    
    @Override
    protected Notificator createNotificator(SCYMapperPanel scyMapperPanel, JPanel panel) {
        return new FadeNotificator(scyMapperPanel, panel, NOTIFY_POSITION);
    }

    @Override
    protected void initComponents() {
        requestConceptHelpButton = new JButton("Request Concept Help");
        requestConceptHelpButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                requestConceptHelpButton.setEnabled(false);
                requestRelationHelpButton.setEnabled(false);
                requestConceptHelp();
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
        super.initComponents();
        toolBar.add(requestConceptHelpButton);
        toolBar.add(requestRelationHelpButton);
        invalidate();
        System.out.println(requestConceptHelpButton.getSize());
    }

    protected void requestConceptHelp() {
	actionLogger.logRequestConceptHelp();
    }
    protected void requestRelationHelp() {
	actionLogger.logRequestRelationHelp();
    }

}
