package eu.scy.scymapper.impl;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.scy.awareness.IAwarenessService;
import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.api.diagram.IDiagramSelectionModel;
import eu.scy.scymapper.impl.component.ConceptDiagramView;
import eu.scy.scymapper.impl.controller.DiagramController;
import eu.scy.scymapper.impl.model.DefaultDiagramSelectionModel;
import eu.scy.sessionmanager.SessionManager;

import javax.swing.*;
import java.awt.*;

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
	private IDiagramSelectionModel selectionModel;


	public ConceptMapEditorPane(IAwarenessService awarenessService, IDiagramModel diagramModel) {
		this.awarenessService = awarenessService;
		this.diagramModel = diagramModel;
		selectionModel = new DefaultDiagramSelectionModel();
		initComponents();
	}

	public IDiagramSelectionModel getSelectionModel() {
		return selectionModel;
	}

	private void initComponents() {

		setLayout(new BorderLayout());
		diagramView = new ConceptDiagramView(new DiagramController(diagramModel), diagramModel,  selectionModel);



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

	public IDiagramModel getModel() {
		return diagramModel;
	}
}
