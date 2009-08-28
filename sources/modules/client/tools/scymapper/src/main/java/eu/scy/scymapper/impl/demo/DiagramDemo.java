package eu.scy.scymapper.impl.demo;

import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.impl.component.ConceptDiagramView;
import eu.scy.scymapper.impl.controller.DiagramController;
import eu.scy.scymapper.impl.shapes.concepts.*;
import eu.scy.scymapper.impl.shapes.INodeShape;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import eu.scy.scymapper.impl.DiagramModel;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.net.URL;
import java.io.IOException;

/**
 * User: Bjoerge Naess
 * Date: 27.aug.2009
 * Time: 15:09:32
 */
public class DiagramDemo  extends JFrame implements IDiagramModelObserver, INodeModelObserver {
    private IDiagramModel diagramModel;

    private INodeModel selectedNode;
    private JLabel selectedLabel;

	public DiagramDemo() {
		super("DiagramModel Test");
    }

    public static void main(String[] args) {
        new DiagramDemo().start();
    }

    public void start() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 900);

        diagramModel = new DiagramModel();

        // Observe the diagramModel
        diagramModel.addObserver(this);

        // Ok, so I'm adding a few nodes to the impl before instantiating the impl component
        testAddNodes1();

        ConceptDiagramView view = new ConceptDiagramView(new DiagramController(diagramModel), diagramModel);

        JButton testButton = new JButton("Click me to change the shape, style and size of the last selected concept");
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedNode != null) {
                    selectedNode.setShape(new Star());
                    selectedNode.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
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
        testAddNodes2();
        // If things are done rigth, the component should now contain a circle and a star
    }

    public void testAddNodes1() {

        NodeModel n1 = new NodeModel(new RoundRectangle());
        n1.setLocation(new Point(500, 50));
        n1.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        n1.setLabel("I am soo boring and gray");
        n1.setSize(new Dimension(180, 100));
        addNode(n1);

        INodeModel svgNode = new NodeModel();
        svgNode.setLabel("I'm a fried SVG egg");

        URL url = getClass().getResource("shapes/egg.svg");
        try {
            System.out.println("DiagramImplTest.testAddNodes1");
            INodeShape s = new SVGConcept(url);
            svgNode.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+url);
        }

        svgNode.setLocation(new Point(100, 50));
        svgNode.setSize(new Dimension(101, 101));
        addNode(svgNode);

        IConceptLinkModel link = new NodeLinkModel(n1, svgNode);
        link.getStyle().setColor(new Color(0x339900));
        link.setShape(new Arrow());
        link.setLabel("Hello");
        diagramModel.addLink(link);

    }

    public synchronized void testAddNodes2() {

        INodeModel node = new NodeModel();
        node.setStyle(new DefaultNodeStyle());
        node.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        node.getStyle().setBackground(new Color(0xcc0000));
        node.setShape(new Star());
        node.setLabel("I'm a primitive, red star");
        node.setLocation(new Point(300, 150));
        node.setSize(new Dimension(200, 200));
        addNode(node);

        INodeModel n7 = new NodeModel();
        n7.setStyle(new DefaultNodeStyle());
        n7.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        n7.getStyle().setBackground(new Color(0x0099ff));
        n7.setLabel("I like my color");
        n7.setLocation(new Point(300, 450));
        n7.setSize(new Dimension(150, 100));
        n7.setShape(new Ellipse());
        addNode(n7);

        IConceptLinkModel link = new NodeLinkModel(node, n7);
        link.getStyle().setColor(new Color(0xffff00));
        link.getStyle().setStroke(new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f, new float[]{6.0f}, 0.0f));
        link.setShape(new Arrow());
        link.setLabel("I'm fat'n dashed!");
        diagramModel.addLink(link);


        INodeModel factory = new NodeModel();
        factory.setStyle(new DefaultNodeStyle());
        factory.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        factory.getStyle().setBackground(new Color(0x0099ff));
        factory.setLabel("I'm a polluting SVG");
        factory.setLocation(new Point(50, 450));
        factory.setSize(new Dimension(150, 200));
        URL url = getClass().getResource("shapes/factory.svg");
        addNode(factory);

        // Ok, so we are actually setting the shape AFTER we added it...
        // thanks to our good friend MVC, it should not matter
        try {
            INodeShape s = new SVGConcept(url);
            factory.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+url.toString());
        }

        INodeModel bergen = new NodeModel();
        bergen.setStyle(new DefaultNodeStyle());
        bergen.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        bergen.getStyle().setBackground(new Color(0x0099ff));
        bergen.setLabel("Bergen JPEG FTW!");
        bergen.setLocation(new Point(450, 450));
        bergen.setSize(new Dimension(150, 200));
        URL url1 = getClass().getResource("shapes/bergen.jpg");
        try {
            INodeShape s = new ImageShape(ImageIO.read(url1));
            bergen.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+url.toString());
        }
        addNode(bergen);
    }

    private void addNode(INodeModel node) {
        // subscribe to changes in this node
        node.addObserver(this);
        diagramModel.addNode(node);
    }

    @Override
    public void linkAdded(ILinkModel link) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void linkRemoved(ILinkModel link) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeAdded(INodeModel n) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeRemoved(INodeModel n) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updated(IDiagramModel diagramModel) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeSelected(INodeModel n) {
        System.out.println("DiagramImplTest.nodeSelected");
    }

    @Override
    public void moved(INodeModel node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resized(INodeModel node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void labelChanged(INodeModel node) {
    }

    @Override
    public void styleChanged(INodeModel node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void shapeChanged(INodeModel node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeSelected(NodeModel conceptNode) {
        selectedNode = conceptNode;
        selectedLabel.setText("You clicked: "+conceptNode.getLabel());
    }

    void simpleExample() {

        // Create a new diagramModel
        IDiagramModel diagramModel = new DiagramModel();

        // Create a new star-shaped concept star:
        INodeModel star = new NodeModel();
        star.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
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
        try {
            INodeShape s = new SVGConcept(url);
            egg.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+url);
        }
        egg.setLocation(new Point(100, 50));
        egg.setSize(new Dimension(101, 101));
        addNode(egg);
        diagramModel.addNode(egg);

        // Add a link between the star and the egg
        IConceptLinkModel link = new NodeLinkModel(star, egg);
        link.getStyle().setColor(new Color(0xffff00));
        link.getStyle().setStroke(new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f, new float[]{6.0f}, 0.0f));
        link.setShape(new Arrow());
        link.setLabel("I'm fat'n dashed!");
        diagramModel.addLink(link);

    }
}

