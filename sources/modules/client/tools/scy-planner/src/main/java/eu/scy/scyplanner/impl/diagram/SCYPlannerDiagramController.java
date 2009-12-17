package eu.scy.scyplanner.impl.diagram;

import eu.scy.scymapper.api.diagram.controller.IDiagramController;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 11:51:09
 */
public class SCYPlannerDiagramController implements IDiagramController {
	IDiagramModel model;
	public SCYPlannerDiagramController(IDiagramModel model) {
		this.model = model;
	}

	@Override
	public void setName(String name) {
		model.setName(name);
	}

	@Override
	public void addNode(INodeModel n, boolean preventOverlap) {
		addNode(n);
	}

	@Override
	public void addNode(INodeModel n) {
		model.addNode(n);
	}

	@Override
	public void addLink(ILinkModel l) {
		model.addLink(l);
	}

    public void removeNode(INodeModel n) {
		model.removeNode(n);
	}

	@Override
	public void removeLink(ILinkModel l) {
		model.removeLink(l);
	}

	public void removeAll() {
		model.removeAll();
	}
}
