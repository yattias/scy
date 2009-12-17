package eu.scy.scyplanner.impl.diagram;

import eu.scy.scymapper.api.diagram.controller.IDiagramController;
import eu.scy.scymapper.api.diagram.model.*;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.impl.controller.LinkConnectorController;
import eu.scy.scymapper.impl.controller.LinkController;
import eu.scy.scymapper.impl.controller.NodeController;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.model.SimpleLink;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.impl.ui.diagram.ConceptLinkView;
import eu.scy.scymapper.impl.ui.diagram.ConnectionPoint;
import eu.scy.scymapper.impl.ui.diagram.LinkView;
import eu.scy.scymapper.impl.ui.diagram.RichNodeView;

import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.impl.view.LASNodeView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 11:40:29
 */
public class SCYPlannerDiagramView extends JPanel implements IDiagramListener {
	private IDiagramModel model;
    private IDiagramController controller;

	private ObservableView observable = new ObservableView();

    public SCYPlannerDiagramView(IDiagramController controller, IDiagramModel model) {        
        this.controller = controller;
        this.model = model;

        setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());

        // Register myself as observer for changes in the model
        this.model.addDiagramListener(this);

        setLayout(null);

        initializeGUI();
    }

    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    private void initializeGUI() {

        // Create views for links in my model
        for (ILinkModel link : model.getLinks()) {
            addLink((INodeLinkModel)link);
        }
        // Create views for nodes in my model
        for (INodeModel node : model.getNodes()) {
            addNode(node);
        }
    }

    private void addNode(INodeModel node) {
        NodeViewComponent view = new LASNodeView(new NodeController(node), node);
		add(view);
    }

    private void addLink(INodeLinkModel link) {
        ConceptLinkView view = new ConceptLinkView(new LinkController(link), link);
        add(view);
    }

    @Override
    public void updated(IDiagramModel diagramModel) {
        System.out.println("ConceptDiagramView.updated");
    }

    @Override
    public void nodeSelected(INodeModel n) {
        System.out.println("SCYPlannerDiagramView.nodeSelected");
    }

    @Override
    public void nodeAdded(INodeModel n) {
        addNode(n);
    }

    @Override
    public void linkAdded(ILinkModel link) {
        addLink((INodeLinkModel)link);
    }

    @Override
    public void linkRemoved(ILinkModel link) {
        System.out.println("ConceptDiagramView.linkRemoved");
    }

    @Override
    public void nodeRemoved(INodeModel n) {
        System.out.println("ConceptDiagramView.nodeRemoved");
    }

    private class ObservableView extends Observable {
        private void observableChanged() {
            setChanged();
        }

        private void clearObservableChanged() {
            clearChanged();
        }
    }
}
