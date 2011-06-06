package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.diagram.controller.IDiagramController;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 12:03:12
 */
public class DiagramController implements IDiagramController {
	private final static Logger logger = Logger.getLogger(DiagramController.class);

	protected IDiagramModel model;

	public DiagramController(IDiagramModel diagramModel) {
		this.model = diagramModel;
	}

	@Override
	public void setName(String name) {
		model.setName(name);
	}

	@Override
	public void add(INodeModel n, boolean preventOverlap) {
		add(n);
	}

	@Override
	public void add(INodeModel n) {
		model.addNode(n);
	}

	@Override
	public void add(ILinkModel l) {
		model.addLink(l);
	}

	@Override
	public void removeAll() {
		Collection<INodeModel> toBeRemoved = new HashSet<INodeModel>();
		for (INodeModel n : model.getNodes())
			toBeRemoved.add(n);
		for (INodeModel n : toBeRemoved) remove(n);
	}

	@Override
	public void remove(INodeModel n) {
		if (!n.getConstraints().getCanDelete()) {
			logger.warn("Tried to delete a locked node");
			return;
		}

		HashSet<INodeLinkModel> linksToRemove = new HashSet<INodeLinkModel>();
		for (ILinkModel link : model.getLinks()) {
			if (link instanceof INodeLinkModel) {
				INodeLinkModel nodeLink = (INodeLinkModel) link;
				if (n.equals(nodeLink.getFromNode()) || n.equals(nodeLink.getToNode())) {
					linksToRemove.add(nodeLink);
				}
			}
		}
		model.removeNode(n);
		for (ILinkModel link : linksToRemove) remove(link);
	}

	public synchronized void remove(ILinkModel l) {
		model.removeLink(l);
	}
}
