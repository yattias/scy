package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.diagram.ILinkController;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.ILinkModelListener;
import eu.scy.scymapper.api.styling.ILinkStyle;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 23.jun.2009
 * Time: 16:23:37
 */
public class LinkView extends JComponent implements ILinkModelListener {

    private ILinkController controller;
    private ILinkModel model;

    private int minWidth = 100;
    private int minHeight = 100;

    private final static Logger logger = Logger.getLogger(LinkView.class);

    public LinkView(ILinkController controller, ILinkModel model) {
        this.controller = controller;
        this.model = model;

        // I want to observe changes in my model
        this.model.addListener(this);

        setLayout(null);
        updatePosition();
    }

    public void paintComponent(Graphics g) {

        Point from = model.getFrom();
        Point to = model.getTo();

        if (from == null || to == null) {
            logger.warn("From or to is null");
            return;
        }
        Point relFrom = new Point(from);
        relFrom.translate(-getX(), -getY());

        Point relTo = new Point(to);
        relTo.translate(-getX(), -getY());

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ILinkStyle style = model.getStyle();

        g2.setStroke(style.getStroke());

        g2.setColor(model.isSelected() ? style.getSelectionColor() : style.getColor());

        model.getShape().paint(g2, relFrom, relTo);

        g2.dispose();

        // Continue painting other component on top
        super.paintComponent(g);

    }

    @Override
    public void addMouseListener(MouseListener l) {
        super.addMouseListener(new LinkMouseListenerProxy(l));
    }

    public void setFrom(Point p) {
        controller.setFrom(p);
    }

    public void setTo(Point p) {
        controller.setTo(p);
    }

    void updatePosition() {
        Point from = model.getFrom();
        Point to = model.getTo();
        if (from == null || to == null) return;

        int w, x, h, y;
        if (to.x < from.x) {
            x = to.x;
            w = from.x - to.x;
        } else {
            x = from.x;
            w = to.x - from.x;
        }
        if (to.y < from.y) {
            y = to.y;
            h = from.y - to.y;
        } else {
            y = from.y;
            h = to.y - from.y;
        }

        setBounds(x - minWidth / 2, y - minHeight / 2, w + minWidth, h + minHeight);
    }

    @Override
    public void updated(ILinkModel m) {
        updatePosition();
        repaint();
    }

    @Override
    public void selectionChanged(ILinkModel linkModel) {
        System.out.println("LinkView.selectionChanged");
        setBorder(linkModel.isSelected() ?
                BorderFactory.createLineBorder(new Color(0xc0c0c0), 1) :
                BorderFactory.createEmptyBorder());
    }

    public ILinkModel getModel() {
        return model;
    }

    public void setModel(ILinkModel model) {
        this.model = model;
    }

    public ILinkController getController() {
        return controller;
    }
}

/**
 * Proxies mouse listeners added to the link.
 * The purpose of this class is to check whether the mouse event occurs in near proximity to the actual painted link.
 * If the event occurs outside the given (currently hard-coded) proximity (of DISTANCE_TRESHOLD) to the line, then the mouse event
 * is redirected to the component below in the containers component hierarchy 
 */
class LinkMouseListenerProxy implements MouseListener {

    private static final double DISTANCE_TRESHOLD = 15.0;

    // For debug only
    private HashMap<JComponent, JLabel> distLabels = new HashMap<JComponent, JLabel>();
    private MouseListener theListener;

    public LinkMouseListenerProxy(MouseListener theListener) {
        this.theListener = theListener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (withinLineProximity(e))
            theListener.mouseClicked(e);
        else
            redirectMouseEvent(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (withinLineProximity(e))
            theListener.mousePressed(e);
        else
            redirectMouseEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (withinLineProximity(e))
            theListener.mouseReleased(e);
        else
            redirectMouseEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (withinLineProximity(e))
            theListener.mouseEntered(e);
        else
            redirectMouseEvent(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (withinLineProximity(e))
            theListener.mouseExited(e);
        else
            redirectMouseEvent(e);
    }

    boolean withinLineProximity(MouseEvent e) {
        Point relPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), e.getComponent().getParent());
        double distance = getDistanceToLine((LinkView) e.getComponent(), relPoint);
        //displayEventLocation((LinkView) e.getComponent(), e.getPoint(), "" + (int) distance);
        return (distance < DISTANCE_TRESHOLD);
    }

    void redirectMouseEvent(MouseEvent e) {

        JComponent container = (JComponent) e.getComponent().getParent();

        Point containerPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), container);

        // displayEventLocation(ConceptDiagramView.this, containerPoint, "X");
        Component[] components = getComponentsAt(containerPoint, container);

        Component reciever = null;
        for (Component component : components) {
            if (component != e.getComponent()) {
                // Skip if the iterator component has a lower z-index than the clicked
                // i.e. the iterator component is overlaying the event comp
                if (container.getComponentZOrder(e.getComponent()) >
                        container.getComponentZOrder(component)) continue;

                reciever = component;
                break;
            }
        }

        if (reciever == null) reciever = container;

        //double d = getDistanceToLine(linkView, e.getPoint());

        //displayEventLocation((JComponent) component, componentPoint, "" + (int) d, Color.red);

        // Send the event to the container
        MouseEvent evt = SwingUtilities.convertMouseEvent(e.getComponent(), e, reciever);

        //System.out.println("Dispatching event " + evt + " to " + reciever);
        //Forward events over the component layered underneath.

        reciever.dispatchEvent(evt);
    }

    public Component[] getComponentsAt(Point p, JComponent container) {
        java.util.List<Component> comps = new ArrayList<Component>();
        for (Component comp : container.getComponents()) {
            if (comp.getBounds().contains(p)) comps.add(comp);
        }
        return comps.toArray(new Component[comps.size()]);
    }

    /**
     * For debug purposes only
     * Displays a marker at the location where the event took place
     * @param comp The component to display the marker
     * @param p The point, relative to the container
     * @param label Text to display in label
     * @param c Background color of label
     */
    private void displayEventLocation(JComponent comp, Point p, String label, Color c) {

        JLabel distLabel;
        if (!distLabels.containsKey(comp)) {
            distLabel = new JLabel("");
            distLabel.setBackground(Color.cyan);
            distLabel.setOpaque(true);
            distLabel.setBackground(c);
            distLabel.setSize(25, 15);
            comp.add(distLabel);
        } else {
            distLabel = distLabels.get(comp);
        }
        distLabel.setText(label);
        distLabel.setLocation(p.x + 1, p.y + 1);
        comp.repaint();
    }

    /**
     * For debug purposes only
     * Displays a marker at the location where the event took place
     * @param comp The component to display the marker
     * @param p The point, relative to the container
     * @param label Text to display in label
     */
    private void showClick(JComponent comp, Point p, String label) {
        displayEventLocation(comp, p, label, new Color(100, 100, 100, 110));
    }

    double getDistanceToLine(LinkView linkView, Point p) {
        Point2D from = new Point2D.Double();
        from.setLocation(linkView.getModel().getFrom());

        Point2D to = new Point2D.Double();
        to.setLocation(linkView.getModel().getTo());

        return new Line2D.Double(from, to).ptLineDist(p.x, p.y);
    }
}