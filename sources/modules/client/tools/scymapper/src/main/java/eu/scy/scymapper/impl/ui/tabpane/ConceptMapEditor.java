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
	private IConceptMapManager manager;

	private IConceptMap conceptMap;

	public ConceptMapEditor(IConceptMap conceptMap) {

		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.manager = DefaultConceptMapManager.getInstance();
		this.conceptMap = conceptMap;
		initComponents();
	}

	public IConceptMap getConceptMap() {
		return conceptMap;
	}
	private void initComponents() {

		setLayout(new BorderLayout());
		diagramView = new ConceptDiagramView(new DiagramController(conceptMap.getDiagram()), conceptMap.getDiagram(), conceptMap.getDiagramSelectionModel());

		diagramView.setBackground(Color.WHITE);
		FormLayout layout = new FormLayout(
				"default:grow", // columns
				"default:grow");		// rows

		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();

		builder.add(new JScrollPane(diagramView), cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		add(builder.getPanel());
	}
}
