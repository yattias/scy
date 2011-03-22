package eu.scy.agents.queryexpansion.importer.gui;

import info.collide.swat.SWATClient;
import info.collide.swat.model.ID;
import info.collide.swat.model.SWATException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.Control;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class GraphPanel extends JPanel implements ChangeListener, ActionListener, Control {

    private SWATGraph graph;

    private JSpinner spRadius;

    private JButton btCenter;

    private Visualization vis;

    private Display display;

    private Timer tmLayouter;

    public GraphPanel() {
        vis = new Visualization();
    }

    public void setSWATClient(SWATClient sc, Set<ID> ignoredEntities, Set<ID> displayedProperties, Set<ID> nodesToFold) {
        try {

            graph = new SWATGraph(sc, this, ignoredEntities, displayedProperties, nodesToFold);
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
            vis.setRendererFactory(new DefaultRendererFactory(r, r2));

            // create our nominal color palette
            // pink for females, baby blue for males
            int[] palette = new int[] { ColorLib.rgb(241, 233, 34), ColorLib.rgb(226, 66, 224), ColorLib.rgb(0, 255, 100) };
            // map nominal data values to colors using our provided palette
            DataColorAction fill = new DataColorAction("graph.nodes", "type", Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
            // use black for node text
            ColorAction text = new ColorAction("graph.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0));
            // use light grey for edges
            ColorAction edges = new ColorAction("graph.edges", VisualItem.STROKECOLOR, ColorLib.gray(200));
            ColorAction edges2 = new ColorAction("graph.edges", VisualItem.FILLCOLOR, ColorLib.gray(200));
            // create an action list containing all color assignments
            ActionList color = new ActionList();
            color.add(fill);
            color.add(text);
            color.add(edges);
            color.add(edges2);

            // create an action list with an animated layout
            // the INFINITY parameter tells the action list to run indefinitely
            ActionList layout = new ActionList(Activity.INFINITY);
            layout.add(new ForceDirectedLayout("graph", false, false));
            layout.add(new RepaintAction());

            // add the actions to the visualization
            vis.putAction("color", color);
            vis.putAction("layout", layout);

            // create a new Display that pull from our Visualization
            display = new Display(vis);
            display.setHighQuality(true);
            display.setSize(720, 500); // set display size
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

            spRadius = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
            spRadius.addChangeListener(this);
            btCenter = new JButton("Click");
            btCenter.addActionListener(this);
            tmLayouter = new Timer(2000, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    vis.cancel("layout");
                }

            });
            // layout

            setLayout(new BorderLayout(5, 5));
            JPanel pnButtons = new JPanel();
            pnButtons.add(new JLabel("Radius:"));
            pnButtons.add(spRadius);
            pnButtons.add(btCenter);

//            add(pnButtons, BorderLayout.NORTH);
            add(display, BorderLayout.CENTER);
            vis.run("color"); // assign the colors
            layoutGraph(false);

            display.pan(display.getWidth() / 2, display.getHeight() / 2);
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

    public void stateChanged(ChangeEvent e) {
        try {
            graph.setRadius(((Integer) spRadius.getValue()).intValue());
            vis.run("color");
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
    }

    public void refresh() {
        graph.refresh();
        vis.run("layout");
    }

    public SWATGraph getGraph() {
        return graph;
    }

    public void actionPerformed(ActionEvent e) {}

    public void itemClicked(VisualItem item, MouseEvent e) {
        // try {
        // graph.setSelectedNode(item);
        // } catch (SWATException e1) {
        // e1.printStackTrace();
        // }
    }

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
}