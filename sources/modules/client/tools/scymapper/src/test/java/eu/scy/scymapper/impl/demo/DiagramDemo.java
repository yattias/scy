package eu.scy.scymapper.impl.demo;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import eu.scy.scymapper.api.diagram.model.IDiagramListener;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.DiagramModel;
import eu.scy.scymapper.impl.controller.DiagramController;
import eu.scy.scymapper.impl.model.DefaultDiagramSelectionModel;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.impl.shapes.nodes.RoundRectangle;
import eu.scy.scymapper.impl.shapes.nodes.Star;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;

/**
 * User: Bjoerge Naess Date: 27.aug.2009 Time: 15:09:32
 */
public class DiagramDemo extends JFrame implements IDiagramListener {

    private final static Logger logger = Logger.getLogger(DiagramDemo.class);

    private IDiagramModel diagramModel;

    private INodeModel selectedNode;

    private JLabel selectedLabel;

    private DefaultDiagramSelectionModel selectionModel;

    public DiagramDemo() {
        super("DiagramModel Test");
    }

    public static void main(String[] args) {
        new DiagramDemo().start();
    }

    public void start() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 900);

        selectionModel = new DefaultDiagramSelectionModel();
        diagramModel = new DiagramModel(selectionModel);

        // Observe the diagramModel
        diagramModel.addDiagramListener(this);

        // Ok, so I'm adding a few nodes to the impl before instantiating the impl component
        testAddNodes1();

        ConceptDiagramView view = new ConceptDiagramView(new DiagramController(diagramModel, selectionModel), diagramModel, selectionModel);

        JButton testButton = new JButton("Click me to change the shape, style and size of the last selected concept");
        testButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedNode != null) {
                    selectedNode.setShape(new Star());
                    selectedNode.getStyle().setOpaque(true);
                    selectedNode.getStyle().setBackground(new Color(0xff9900));
                }
            }
        });
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(view, BorderLayout.CENTER);
        getContentPane().add(testButton, BorderLayout.PAGE_START);
        setVisible(true);

        selectedLabel = new JLabel("No node selected yet");
        getContentPane().add(selectedLabel, BorderLayout.PAGE_END);

        // So, lets add a few additional nodes AFTER the component is initialized, and see what happens
        // If things are done rigth, the component should now contain a circle and a star
    }

    public void testAddNodes1() {

        NodeModel n1 = new NodeModel(new RoundRectangle());
        n1.setLocation(new Point(500, 50));
        n1.setLabel("I am soo boring and gray");
        n1.setSize(new Dimension(180, 100));
        addNode(n1);

        INodeModel svgNode = new NodeModel();
        svgNode.setLabel("I'm a fried SVG egg");

        URL url = getClass().getResource("shapes/egg.svg");
        // try {
        // logger.debug("DiagramImplTest.testAddNodes1");
        // INodeShape s = new SVGShape(url);
        // svgNode.setShape(s);
        // } catch (IOException e) {
        // logger.debug("File not found: " + url);
        // }

        svgNode.setLocation(new Point(100, 50));
        svgNode.setSize(new Dimension(101, 101));
        addNode(svgNode);

        INodeLinkModel link = new NodeLinkModel(n1, svgNode);
        link.getStyle().setBackground(new Color(0x339900));
        link.setShape(new Arrow());
        link.setLabel("Hello");
        diagramModel.addLink(link);

    }

    private void addNode(INodeModel node) {
        // subscribe to changes in this node
        diagramModel.addNode(node);
    }

    @Override
    public void linkRemoved(ILinkModel link) {
        // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeRemoved(INodeModel n) {
        // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updated(IDiagramModel diagramModel) {
        // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeSelected(INodeModel n) {
        logger.debug("DiagramDemo.nodeSelected");
    }

    void simpleExample() {

        // Create a new diagramModel
        IDiagramModel diagramModel = new DiagramModel(new DefaultDiagramSelectionModel());

        // Create a new star-shaped concept star:
        INodeModel star = new NodeModel();
        star.getStyle().setOpaque(true);
        star.getStyle().setBackground(new Color(0xcc0000));
        star.setShape(new Star());
        star.setLabel("I'm a star");
        star.setLocation(new Point(300, 150));
        star.setSize(new Dimension(200, 200));
        diagramModel.addNode(star);

        // Create a concept star from SVG:
        INodeModel egg = new NodeModel();
        egg.setLabel("I'm a fried SVG egg");
        URL url = getClass().getResource("egg.svg");
        // try {
        // INodeShape s = new SVGShape(url);
        // egg.setShape(s);
        // } catch (IOException e) {
        // System.err.println("File not found: " + url);
        // }
        egg.setLocation(new Point(100, 50));
        egg.setSize(new Dimension(101, 101));
        addNode(egg);
        diagramModel.addNode(egg);

        // Add a link between the star and the egg
        INodeLinkModel link = new NodeLinkModel(star, egg);
        link.getStyle().setBackground(new Color(0xffff00));
        link.getStyle().setStroke(new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f, new float[] { 6.0f }, 0.0f));
        link.setShape(new Arrow());
        link.setLabel("I'm fat'n dashed!");
        diagramModel.addLink(link);

    }

    @Override
    public void linkAdded(ILinkModel link, boolean focused) {
        // TODO Auto-generated method stub

    }

    @Override
    public void nodeAdded(INodeModel n, boolean focused) {
        // TODO Auto-generated method stub

    }
}
