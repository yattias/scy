package eu.scy.scymapper.impl;

import eu.scy.awareness.IAwarenessService;
import eu.scy.scymapper.impl.component.ConceptDiagramView;
import eu.scy.scymapper.impl.controller.DiagramController;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.shapes.concepts.Star;
import eu.scy.scymapper.impl.shapes.concepts.Ellipse;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.sessionmanager.SessionManager;

import javax.swing.*;
import java.awt.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.builder.PanelBuilder;

/**
 * User: Bjoerge Naess
 * Date: 03.sep.2009
 * Time: 12:46:21
 */
public class ConceptMapEditorPane extends JPanel {

	private IDiagramModel diagramModel;
	private ConceptDiagramView diagramView;
	private AwarenessView awarenessPanel;
	private SessionManager session;
	private IAwarenessService awarenessService;

	public ConceptMapEditorPane(IAwarenessService awarenessService, IDiagramModel diagramModel) {
		this.awarenessService = awarenessService;
		this.diagramModel = diagramModel;
		initComponents();
	}

	private void initComponents() {

		setLayout(new BorderLayout());
		diagramView = new ConceptDiagramView(new DiagramController(diagramModel), diagramModel);
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
