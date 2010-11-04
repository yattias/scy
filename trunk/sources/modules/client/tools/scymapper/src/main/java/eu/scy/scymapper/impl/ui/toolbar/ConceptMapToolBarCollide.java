package eu.scy.scymapper.impl.ui.toolbar;

import javax.swing.BoxLayout;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;

public class ConceptMapToolBarCollide extends ConceptMapToolBar {

	public ConceptMapToolBarCollide(IConceptMap cmap, ConceptDiagramView diagramView) {
		conceptMap = cmap;
		this.diagramView = diagramView;
		diagramSelectionModel = conceptMap.getDiagramSelectionModel();

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(new ClearConceptMapButton());
		add(new RemoveConceptButton());
	}

}
