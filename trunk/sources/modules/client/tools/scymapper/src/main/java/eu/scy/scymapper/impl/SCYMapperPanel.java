package eu.scy.scymapper.impl;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.palette.PalettePane;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.XMPPConnection;
import roolo.api.IRepository;
import roolo.elo.JDomBasicELOFactory;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

import javax.swing.*;
import java.awt.*;

/**
 * User: Bjoerge
 * Date: 27.aug.2009
 * Time: 13:29:56
 */
public class SCYMapperPanel extends JPanel {

	private final static Logger logger = Logger.getLogger(SCYMapperPanel.class);
	private JToolBar toolBar;
	private String currentToolSessionId;

	private XMPPConnection connection;
    private IELOFactory eloFactory = new JDomBasicELOFactory();
    private IRepository repository;
    private IMetadataTypeManager metadataTypeManager;
    private JSplitPane splitPane;
    private IConceptMap conceptMap;
    private ISCYMapperToolConfiguration configuration;

    public SCYMapperPanel(IConceptMap cmap, ISCYMapperToolConfiguration configuration) {
        conceptMap = cmap;
        this.configuration = configuration;
        initToolBroker();
        setLayout(new BorderLayout());
		initComponents();
	}

	private void initToolBroker() {
		//logger.debug("Getting datasync-service");
		//dataSyncService = toolBroker.getDataSyncService();
		//dataSyncService.init(toolBroker.getConnection(username, password));

		//dataSyncService.addDataSyncListener(this);
		//dataSyncService.createSession("eu.scy.scymapper", username);
	}

	private void initComponents() {
        ConceptMapPanel cmapPanel = new ConceptMapPanel(conceptMap);
        cmapPanel.setBackground(Color.WHITE);

        JPanel palettePane = new PalettePane(conceptMap, configuration, cmapPanel);
        palettePane.setPreferredSize(new Dimension(200, 0));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, palettePane, cmapPanel);

		add(splitPane, BorderLayout.CENTER);
	}

//	@Override
//	public void handleDataSyncEvent(IDataSyncEvent e) {
//		ISyncMessage syncMessage = e.getSyncMessage();
//		if (syncMessage.getEvent().equals(Configuration.getInstance().getClientEventCreateSession())) {
//			currentToolSessionId = syncMessage.getToolSessionId();
//		}
//		if (syncMessage.getEvent().equals(Configuration.getInstance().getClientEventSynchronize())) {
//			logger.debug("GOT SYNCH" + syncMessage.getContent());
//		}
//	}

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
    }

    public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager) {
        this.metadataTypeManager = metadataTypeManager;
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
