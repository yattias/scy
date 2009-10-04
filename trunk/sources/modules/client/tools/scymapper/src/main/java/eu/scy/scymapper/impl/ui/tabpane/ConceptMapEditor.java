package eu.scy.scymapper.impl.ui.tabpane;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.scy.awareness.IAwarenessService;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.IConceptMapManager;
import eu.scy.scymapper.impl.controller.DiagramController;
import eu.scy.scymapper.impl.ui.awareness.AwarenessView;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.model.DefaultConceptMapManager;

import javax.swing.*;
import java.awt.*;

/**
 * User: Bjoerge Naess
 * Date: 03.sep.2009
 * Time: 12:46:21
 */
public class ConceptMapEditor extends JPanel{

	private ConceptDiagramView diagramView;
	private AwarenessView awarenessPanel;
	private IAwarenessService awarenessService;
	private IConceptMapManager manager;

	private IConceptMap conceptMap;

	public ConceptMapEditor(IAwarenessService awarenessService, IConceptMap cmap) {

		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.awarenessService = awarenessService;
		this.manager = DefaultConceptMapManager.getInstance();
		this.conceptMap = cmap;
		initComponents();
	}

	public IConceptMap getConceptMap() {
		return conceptMap;
	}
	private void initComponents() {

		setLayout(new BorderLayout());
		diagramView = new ConceptDiagramView(new DiagramController(conceptMap.getDiagram()), conceptMap.getDiagram(), conceptMap.getDiagramSelectionModel());

		diagramView.setBackground(Color.WHITE);

		awarenessPanel = new AwarenessView(awarenessService);

		FormLayout layout = new FormLayout(
				"default:grow, 2dlu, 150dlu", // columns
				"default:grow");		// rows

		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();

		builder.add(new JScrollPane(diagramView), cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(awarenessPanel, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.FILL));

		add(builder.getPanel());
	}
}
