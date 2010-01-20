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
import eu.scy.scymapper.impl.ui.diagram.ConceptLinkView;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.impl.view.LASNodeView;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 11:40:29
 */
public class SCYPlannerDiagramView extends JPanel implements IDiagramListener, MouseListener {
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
            addLink((INodeLinkModel) link);
        }
        // Create views for nodes in my model
        for (INodeModel node : model.getNodes()) {
            addNode(node);
        }
    }

    private void addNode(INodeModel node) {
        NodeViewComponent view = new LASNodeView(new NodeController(node), node);
        view.addMouseListener(this);
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
        addLink((INodeLinkModel) link);
    }

    @Override
    public void linkRemoved(ILinkModel link) {
        System.out.println("ConceptDiagramView.linkRemoved");
    }

    @Override
    public void nodeRemoved(INodeModel n) {
        System.out.println("ConceptDiagramView.nodeRemoved");
    }

    public void mouseClicked(MouseEvent e) {
        Component c = e.getComponent();
        System.out.println(c);
        if (c instanceof LASNodeView) {
            if (e.getClickCount() >= 2) {
                LASNodeView node = (LASNodeView) c;
                observable.observableChanged();
                observable.notifyObservers(node.getModel());
                observable.clearObservableChanged();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

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
