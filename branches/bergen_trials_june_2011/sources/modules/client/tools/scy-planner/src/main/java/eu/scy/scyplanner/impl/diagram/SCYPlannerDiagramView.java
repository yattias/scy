package eu.scy.scyplanner.impl.diagram;

import eu.scy.scymapper.api.diagram.controller.IDiagramController;
import eu.scy.scymapper.api.diagram.model.IDiagramListener;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.impl.controller.LinkController;
import eu.scy.scymapper.impl.controller.NodeController;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.impl.view.LASNodeView;
import eu.scy.scyplanner.impl.view.SCYPlannerLinkView;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

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
        model.addDiagramListener(this);

        setLayout(null);

        initializeGUI();
    }

    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    private void initializeGUI() {

        // Create views for links in my model
        for (ILinkModel link : model.getLinks()) {
            addLink((INodeLinkModel) link);
        }
        // Create views for nodes in my model
        for (INodeModel node : model.getNodes()) {
            addNode(node);
        }
    }

    private void addNode(INodeModel node) {        
        NodeViewComponent view = new LASNodeView(new NodeController(node), node);
        view.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Component c = e.getComponent();
                if (c instanceof LASNodeView) {
                    if (e.getClickCount() >= 2) {
                        LASNodeView node = (LASNodeView) c;
                        observable.observableChanged();
                        observable.notifyObservers(node.getModel());
                        observable.clearObservableChanged();
                    }
                }
            }
        });
        add(view);
    }

    private void addLink(INodeLinkModel link) {
        SCYPlannerLinkView view = new SCYPlannerLinkView(new LinkController(link), link);
        add(view);
    }

    @Override
    public void nodeRemoved(INodeModel n) {
        // System.out.println("ConceptDiagramView.nodeRemoved");
    }


    @Override
    public void updated(IDiagramModel diagramModel) {
        // System.out.println("ConceptDiagramView.updated");
    }

    @Override
    public void nodeSelected(INodeModel n) {
        // System.out.println("SCYPlannerDiagramView.nodeSelected");
    }

    @Override
    public void nodeAdded(INodeModel n) {
        addNode(n);
    }

    @Override
    public void linkAdded(ILinkModel link) {
        addLink((INodeLinkModel) link);
    }

    @Override
    public void linkRemoved(ILinkModel link) {
        // System.out.println("ConceptDiagramView.linkRemoved");
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
