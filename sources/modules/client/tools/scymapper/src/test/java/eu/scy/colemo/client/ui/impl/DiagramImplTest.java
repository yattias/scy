package eu.scy.colemo.client.ui.impl;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 11.jun.2009
 * Time: 12:48:34
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.*;
import javax.imageio.ImageIO;

import eu.scy.colemo.client.ui.impl.model.ConceptLink;
import eu.scy.colemo.client.ui.impl.model.ConceptNode;
import eu.scy.colemo.client.ui.impl.model.DefaultNodeStyle;
import eu.scy.colemo.client.ui.api.links.IConceptLink;
import eu.scy.colemo.client.ui.api.nodes.IConceptNode;
import eu.scy.colemo.client.ui.api.nodes.INodeObserver;
import eu.scy.colemo.client.ui.api.diagram.IDiagram;
import eu.scy.colemo.client.ui.api.diagram.IDiagramObserver;
import eu.scy.colemo.client.ui.api.styling.INodeStyle;
import eu.scy.colemo.client.ui.impl.Diagram;
import eu.scy.colemo.client.ui.impl.controller.DiagramController;
import eu.scy.colemo.client.ui.impl.component.DiagramView;
import eu.scy.colemo.client.shapes.concepts.*;
import eu.scy.colemo.client.shapes.links.Arrow;
import eu.scy.colemo.client.shapes.ConceptShape;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.io.IOException;

/**
 * GUI test for the DiagramImplTest
 */
public class DiagramImplTest implements IDiagramObserver, INodeObserver {
    private JFrame frame;
    private IDiagram diagram;

    private IConceptNode selectedNode;
    private JLabel selectedLabel;

    public DiagramImplTest() {
    }

    public static void main(String[] args) {
        new DiagramImplTest().start();
    }

    public void start() {
        frame = new JFrame("Diagram Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 900);

        diagram = new Diagram();

        // Observe the diagram
        diagram.addObserver(this);

        // Ok, so I'm adding a few nodes to the impl before instantiating the impl component
        testAddNodes1();

        DiagramView view = new DiagramView(new DiagramController(diagram), diagram);

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
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(view, BorderLayout.CENTER);
        frame.getContentPane().add(testButton, BorderLayout.PAGE_START);
        frame.setVisible(true);

        selectedLabel = new JLabel("No node selected yet");
        frame.getContentPane().add(selectedLabel, BorderLayout.PAGE_END);

        // So, lets add a few additional nodes AFTER the component is initialized, and see what happens
        testAddNodes2();
        // If things are done rigth, the component should now contain a circle and a star
    }

    public void testAddNodes1() {

        ConceptNode n1 = new ConceptNode(new RoundRectangle());
        n1.setLocation(new Point(500, 50));
        n1.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        n1.setLabel("I am soo boring and gray");
        n1.setSize(new Dimension(180, 100));
        addNode(n1);

        IConceptNode svgNode = new ConceptNode();
        svgNode.setLabel("I'm a fried SVG egg");

        URL url = getClass().getResource("egg.svg");
        try {
            System.out.println("DiagramImplTest.testAddNodes1");
            ConceptShape s = new SVGConcept(url);
            svgNode.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+url);
        }

        svgNode.setLocation(new Point(100, 50));
        svgNode.setSize(new Dimension(101, 101));
        addNode(svgNode);

        IConceptLink link = new ConceptLink(n1, svgNode);
        link.getStyle().setColor(new Color(0x339900));
        link.setShape(new Arrow());
        link.setLabel("Hello");
        diagram.addLink(link);

    }

    public synchronized void testAddNodes2() {

        IConceptNode node = new ConceptNode();
        node.setStyle(new DefaultNodeStyle());
        node.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        node.getStyle().setBackground(new Color(0xcc0000));
        node.setShape(new Star());
        node.setLabel("I'm a primitive, red star");
        node.setLocation(new Point(300, 150));
        node.setSize(new Dimension(200, 200));
        addNode(node);

        IConceptNode n7 = new ConceptNode();
        n7.setStyle(new DefaultNodeStyle());
        n7.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        n7.getStyle().setBackground(new Color(0x0099ff));
        n7.setLabel("I like my color");
        n7.setLocation(new Point(300, 450));
        n7.setSize(new Dimension(150, 100));
        n7.setShape(new Ellipse());
        addNode(n7);

        IConceptLink link = new ConceptLink(node, n7);
        link.getStyle().setColor(new Color(0xffff00));
        link.getStyle().setStroke(new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f, new float[]{6.0f}, 0.0f));
        link.setShape(new Arrow());
        link.setLabel("I'm fat'n dashed!");
        diagram.addLink(link);


        IConceptNode factory = new ConceptNode();
        factory.setStyle(new DefaultNodeStyle());
        factory.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        factory.getStyle().setBackground(new Color(0x0099ff));
        factory.setLabel("I'm a polluting SVG");
        factory.setLocation(new Point(50, 450));
        factory.setSize(new Dimension(150, 200));
        URL url = getClass().getResource("factory.svg");
        addNode(factory);

        // Ok, so we are actually setting the shape AFTER we added it...
        // thanks to our good friend MVC, it should not matter
        try {
            ConceptShape s = new SVGConcept(url);
            factory.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+url.toString());
        }

        IConceptNode bergen = new ConceptNode();
        bergen.setStyle(new DefaultNodeStyle());
        bergen.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        bergen.getStyle().setBackground(new Color(0x0099ff));
        bergen.setLabel("Bergen JPEG FTW!");
        bergen.setLocation(new Point(450, 450));
        bergen.setSize(new Dimension(150, 200));
        URL url1 = getClass().getResource("bergen.jpg");
        try {
            ConceptShape s = new ImageShape(ImageIO.read(url1));
            bergen.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+url.toString());
        }
        addNode(bergen);
    }

    private void addNode(IConceptNode node) {
        // subscribe to changes in this node
        node.addObserver(this);
        diagram.addNode(node);
    }

    @Override
    public void linkAdded(IConceptLink link) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void linkRemoved(IConceptLink link) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeAdded(IConceptNode n) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeRemoved(IConceptNode n) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updated(IDiagram diagram) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeSelected(IConceptNode n) {
        System.out.println("DiagramImplTest.nodeSelected");
    }

    @Override
    public void moved(IConceptNode node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resized(IConceptNode node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void labelChanged(IConceptNode node) {
    }

    @Override
    public void styleChanged(IConceptNode conceptNode) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void shapeChanged(IConceptNode conceptNode) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeSelected(ConceptNode conceptNode) {
        selectedNode = conceptNode;
        selectedLabel.setText("You clicked: "+conceptNode.getLabel());
    }

    void simpleExample() {

        // Create a new diagram
        IDiagram diagram = new Diagram();

        // Create a new star-shaped concept star:
        IConceptNode star = new ConceptNode();
        star.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        star.getStyle().setBackground(new Color(0xcc0000));
        star.setShape(new Star());
        star.setLabel("I'm a star");
        star.setLocation(new Point(300, 150));
        star.setSize(new Dimension(200, 200));
        diagram.addNode(star);

        // Create a concept star from SVG:
        IConceptNode egg = new ConceptNode();
        egg.setLabel("I'm a fried SVG egg");
        URL url = getClass().getResource("egg.svg");
        try {
            ConceptShape s = new SVGConcept(url);
            egg.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+url);
        }
        egg.setLocation(new Point(100, 50));
        egg.setSize(new Dimension(101, 101));
        addNode(egg);
        diagram.addNode(egg);

        // Add a link between the star and the egg
        IConceptLink link = new ConceptLink(star, egg);
        link.getStyle().setColor(new Color(0xffff00));
        link.getStyle().setStroke(new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f, new float[]{6.0f}, 0.0f));
        link.setShape(new Arrow());
        link.setLabel("I'm fat'n dashed!");
        diagram.addLink(link);

    }
}

