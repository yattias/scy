package eu.scy.agents.conceptmap.gui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.Control;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

public class ConceptMapView extends JPanel implements ChangeListener, ActionListener, Control {

    private Graph graph;

    private Visualization vis;

    private Display display;

    private Timer tmLayouter;

    private HashMap<String, Node> nodes;

    private static final Schema DECORATOR_SCHEMA = PrefuseLib.getVisualItemSchema();
    static {
        DECORATOR_SCHEMA.setDefault(VisualItem.INTERACTIVE, true);
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128));
        DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", 7));
    }

    public ConceptMapView() {
        vis = new Visualization();
        nodes = new HashMap<String, Node>();
        try {

            graph = new Graph(true);
            graph.addColumn("name", String.class);
            graph.addColumn("type", String.class);
            graph.addColumn("elabel", String.class);

            Node n1 = graph.addNode();
            n1.setString("name", "bla");
            n1.setString("type", "znode");
            Node n2 = graph.addNode();
            n2.setString("name", "bla");
            n2.setString("type", "proposal");
            Node n3 = graph.addNode();
            n3.setString("name", "bla");
            n3.setString("type", "match");

            vis.addGraph("graph", graph);

            // add the graph to the visualization as the data group "graph"
            // nodes and edges are accessible as "graph.nodes" and "graph.edges"

            // draw the "name" label for NodeItems
            LabelRenderer r = new LabelRenderer("name");
            r.setRoundedCorner(8, 8); // round the corners
            r.setHorizontalAlignment(Constants.CENTER);

            EdgeRenderer r2 = new EdgeRenderer(Constants.EDGE_TYPE_CURVE, Constants.EDGE_ARROW_FORWARD);
            r2.setArrowHeadSize(10, 15);
            r2.setDefaultLineWidth(1.5);
            // create a new default renderer factory
            // return our name label renderer as the default for all non-EdgeItems
            // includes straight line edges for EdgeItems by default
            DefaultRendererFactory rf = new DefaultRendererFactory(r, r2);
            rf.add(new InGroupPredicate("edgeDeco"), new LabelRenderer("elabel"));
            vis.setRendererFactory(rf);
            DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128));
            vis.addDecorators("edgeDeco", "graph.edges", DECORATOR_SCHEMA);

            // create our nominal color palette
            // pink for females, baby blue for males
            int[] palette = new int[] { ColorLib.rgb(100, 255, 100), ColorLib.rgb(255, 100, 100), ColorLib.rgb(255, 255, 100) };
            // map nominal data values to colors using our provided palette
            DataColorAction fill = new DataColorAction("graph.nodes", "type", Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
            // use black for node text
            ColorAction text = new ColorAction("graph.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0));
            // use light grey for edges
            ColorAction edges = new ColorAction("graph.edges", VisualItem.STROKECOLOR, ColorLib.gray(200));
            ColorAction edges2 = new ColorAction("graph.edges", VisualItem.FILLCOLOR, ColorLib.gray(200));
            ColorAction edges3 = new ColorAction("graph.edges", VisualItem.TEXTCOLOR, ColorLib.gray(0));
            // create an action list containing all color assignments
            ActionList color = new ActionList();
            color.add(fill);
            color.add(text);
            color.add(edges);
            color.add(edges2);
            color.add(edges3);

            // create an action list with an animated layout
            // the INFINITY parameter tells the action list to run indefinitely
            ActionList layout = new ActionList(Activity.INFINITY);
            ForceDirectedLayout fdl = new ForceDirectedLayout("graph", false, false);

            // No idea, wtf that means, but it supports readability by increasing the node distance
            // :)
            fdl.getForceSimulator().getForces()[2].setParameter(1, 120);

            layout.add(fdl);
            layout.add(new RepaintAction());
            layout.add(new LabelLayout2("edgeDeco"));

            // add the actions to the visualization
            vis.putAction("color", color);
            vis.putAction("layout", layout);

            // create a new Display that pull from our Visualization
            display = new Display(vis);
            display.setHighQuality(true);
            display.setSize(1024, 768); // set display size
            display.addControlListener(new DragControl()); // drag items around
            display.addControlListener(new PanControl()); // pan with background left-drag
            display.addControlListener(new ZoomControl()); // zoom with vertical right-drag
            display.addControlListener(this);

            // NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout("graph",
            // Constants.ORIENT_TOP_BOTTOM, 50, 0, 8);
            // treeLayout.setLayoutAnchor(new Point2D.Double(25, 300));
            // vis.putAction("treeLayout", treeLayout);
            // ActionList animate = new ActionList(1000);
            // animate.setPacingFunction(new SlowInSlowOutPacer());
            // animate.add(new QualityControlAnimator());
            // animate.add(new VisibilityAnimator("graph"));
            // animate.add(new LocationAnimator("graph.nodes"));
            // animate.add(new ColorAnimator("graph.nodes"));
            // animate.add(new RepaintAction());
            // vis.putAction("animate", animate);
            // vis.alwaysRunAfter("treeLayout", "animate");

            tmLayouter = new Timer(2000, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    vis.cancel("layout");
                }

            });
            // layout

            setLayout(new BorderLayout(5, 5));

            // add(pnButtons, BorderLayout.NORTH);
            add(display, BorderLayout.CENTER);
            vis.run("color"); // assign the colors
            layoutGraph(false);
            graph.removeNode(n1);
            graph.removeNode(n2);
            graph.removeNode(n3);
            display.pan(display.getWidth() / 2, display.getHeight() / 2);
            display.zoom(new Point(display.getWidth() / 2, display.getHeight() / 2), 1.1d);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading graph. Exiting...");
        }
    }

    public void layoutGraph(boolean onlyPaint) {
        if (vis != null) {
            vis.run("color");
            if (!onlyPaint) {
                vis.run("layout");
                // tmLayouter.start();
            }
        }
    }

    public void stateChanged(ChangeEvent e) {}

    public void refresh() {}

    public void actionPerformed(ActionEvent e) {}

    public void itemClicked(VisualItem item, MouseEvent e) {}

    public void itemDragged(VisualItem item, MouseEvent e) {
        vis.run("layout");
    }

    public void itemEntered(VisualItem item, MouseEvent e) {}

    public void itemExited(VisualItem item, MouseEvent e) {
        tmLayouter.restart();
    }

    public void itemKeyPressed(VisualItem item, KeyEvent e) {}

    public void itemKeyReleased(VisualItem item, KeyEvent e) {}

    public void itemKeyTyped(VisualItem item, KeyEvent e) {}

    public void itemMoved(VisualItem item, MouseEvent e) {}

    public void itemPressed(VisualItem item, MouseEvent e) {}

    public void itemReleased(VisualItem item, MouseEvent e) {}

    public void itemWheelMoved(VisualItem item, MouseWheelEvent e) {}

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseWheelMoved(MouseWheelEvent e) {}

    public void addConcept(String concept) {
        Node n = graph.addNode();
        n.setString("name", concept);
        n.setString("type", "znode");
        nodes.put(concept.toLowerCase(), n);
        layoutGraph(false);
    }

    public void addLink(String from, String to, String label) {
        if (from.equals(to)) {
            return;
        }
        Node fromNode = nodes.get(from.toLowerCase());
        Node toNode = nodes.get(to.toLowerCase());
        prefuse.data.Edge e = graph.addEdge(fromNode, toNode);
        e.setString("elabel", label);
        layoutGraph(false);
    }

    class LabelLayout2 extends Layout {

        public LabelLayout2(String group) {
            super(group);
        }

        @Override
        public void run(double frac) {
            Iterator<?> iter = m_vis.items(m_group);
            while (iter.hasNext()) {
                DecoratorItem decorator = (DecoratorItem) iter.next();
                VisualItem decoratedItem = decorator.getDecoratedItem();
                Rectangle2D bounds = decoratedItem.getBounds();

                double x = bounds.getCenterX();
                double y = bounds.getCenterY();

                /*
                 * modification to move edge labels more to the arrow head double x2 = 0, y2 = 0; if
                 * (decoratedItem instanceof EdgeItem){ VisualItem dest =
                 * ((EdgeItem)decoratedItem).getTargetItem(); x2 = dest.getX(); y2 = dest.getY(); x
                 * = (x + x2) / 2; y = (y + y2) / 2; }
                 */

                setX(decorator, null, x);
                setY(decorator, null, y);
            }
        }
    } // end of inner class LabelLayout

    public void clearGraph() {
        graph.clear();
        nodes.clear();
    }

    public void setNodeType(String nodeLabel, String type) {
        System.out.println("setting " + nodeLabel + " to " + type);
        Node node = nodes.get(nodeLabel.toLowerCase());
        if (node == null) {
            System.out.println("beep: " + nodeLabel);
        } else {
            node.set("type", type);
            layoutGraph(false);
        }
    }
}
