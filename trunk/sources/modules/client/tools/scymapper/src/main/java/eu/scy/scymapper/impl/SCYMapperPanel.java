package eu.scy.scymapper.impl;

import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.impl.controller.datasync.DataSyncDiagramController;
import eu.scy.scymapper.impl.controller.datasync.DataSyncElementControllerFactory;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.palette.PalettePane;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * User: Bjoerge
 * Date: 27.aug.2009
 * Time: 13:29:56
 */
public class SCYMapperPanel extends JPanel {

	private final static Logger logger = Logger.getLogger(SCYMapperPanel.class);

    private ToolBrokerAPI toolBroker;
    private JSplitPane splitPane;
    private IConceptMap conceptMap;
    private ISCYMapperToolConfiguration configuration;
    private ConceptDiagramView conceptDiagramView;

    public SCYMapperPanel(IConceptMap cmap, ISCYMapperToolConfiguration configuration) {
        conceptMap = cmap;
        this.configuration = configuration;
        setLayout(new BorderLayout());
		initComponents();
	}

	public synchronized void setToolBroker(ToolBrokerAPI tbi) {
		this.toolBroker = tbi;
	}

    public synchronized void joinSession(ISyncSession session) {
        conceptDiagramView.setController(new DataSyncDiagramController(conceptMap.getDiagram(), session));
		conceptDiagramView.setElementControllerFactory(new DataSyncElementControllerFactory(session));
	}

	private void initComponents() {
        ConceptMapPanel cmapPanel = new ConceptMapPanel(conceptMap);
        cmapPanel.setBackground(Color.WHITE);
        conceptDiagramView = cmapPanel.getDiagramView();

        JPanel palettePane = new PalettePane(conceptMap, configuration, cmapPanel);
        palettePane.setPreferredSize(new Dimension(100, 0));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, palettePane, cmapPanel);

		add(splitPane, BorderLayout.CENTER);
	}
    public IConceptMap getConceptMap() {
        return conceptMap;
    }

    public void setConceptMap(IConceptMap conceptMap) {
        removeAll();
        this.conceptMap = conceptMap;
        initComponents();
        repaint();
    }
}
