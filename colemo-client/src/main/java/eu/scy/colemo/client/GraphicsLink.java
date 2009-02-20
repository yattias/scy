/*
 * Created on 04.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import eu.scy.colemo.server.uml.UmlLink;

import javax.swing.*;

/**
 * @author Øystein
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GraphicsLink {

    private ConceptNode from;
    private ConceptNode to;
    private GraphicsDiagram diagram;
    private UmlLink link;
    private Color color;
    private boolean dotted;

    private Point fromConnectioPoint = null;
    private Point toConnectionPoint = null;

    private JLabel labelComponent = new JLabel("Link!");

    /**
     *
     */
    public GraphicsLink(UmlLink link, GraphicsDiagram diagram) {
        this.link = link;
        this.diagram = diagram;
        from = diagram.getClass(link.getFrom());
        to = diagram.getClass(link.getTo());
        dotted = decideType();
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        fromConnectioPoint = from.getConnectionPoint(findDirection(from.getCenterPoint(), to.getCenterPoint()));
        toConnectionPoint = to.getConnectionPoint(findDirection(to.getCenterPoint(), from.getCenterPoint()));

        if (dotted) {
            g2d.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{6f}, 0f));
        }
        if (!dotted) {
            if (g2d != null) {
                g2d.setStroke(new BasicStroke());
            }

        }

        drawArrow(g2d, (int) fromConnectioPoint.getX(), (int) fromConnectioPoint.getY(), (int) toConnectionPoint.getX(), (int) toConnectionPoint.getY(), 10, 11);

    }

    public int getLabelComponentXPos() {
        if(fromConnectioPoint != null && toConnectionPoint != null) {
            int max = 0;
            int min = 0;
            if(fromConnectioPoint.getX() > toConnectionPoint.getX()) {
                max = (int) fromConnectioPoint.getX();
                min = (int) toConnectionPoint.getX();
            } else {
                max = (int) toConnectionPoint.getX();
                min = (int) fromConnectioPoint.getX();
            }

            return min + ((max - min) /2) - 50;
        }

        return 0;

    }

    public int getLabelComponentYPos() {
        if(fromConnectioPoint != null && toConnectionPoint != null) {
            int max = 0;
            int min = 0;
            if(fromConnectioPoint.getY() > toConnectionPoint.getY()) {
                max = (int) fromConnectioPoint.getY();
                min = (int) toConnectionPoint.getY();
            } else {
                max = (int) toConnectionPoint.getY();
                min = (int) fromConnectioPoint.getY();
            }

            return min + ((max - min) /2) + 10;
        }

        return 0;

    }

    public int findDirection(Point from, Point to) {
        double x = (to.getX() - from.getX());
        double y = (to.getY() - from.getY());

        if (Math.abs(x) > Math.abs(y)) {
            if (from.getX() < to.getX()) {
                return ConceptNode.EAST;
            } else {
                return ConceptNode.WEST;
            }
        } else {
            if (from.getY() > to.getY()) {
                return ConceptNode.NORTH;
            } else {
                return ConceptNode.SOUTH;
            }
        }
    }

    public boolean decideType() {
        return false;
        
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void drawArrow(java.awt.Graphics2D g, int x1, int y1, int x2, int y2, double K, double N) {

        if (K >= N) return; // draw nothing if height is greater than length
        double L = Math.sqrt(N * N - K * K);
        int x = x2 - x1;
        int y = y2 - y1;
        double A = Math.sqrt(x * x + y * y);

        int x3 = (int) (((A - K) / A) * x - (L / A) * y);
        int y3 = (int) (((A - K) / A) * y + (L / A) * x);
        int x5 = (int) (((A - K) / A) * x + (L / A) * y);
        int y5 = (int) (((A - K) / A) * y - (L / A) * x);

        int x4 = x1 + x3;
        int y4 = y1 + y3;
        int x6 = x1 + x5;
        int y6 = y1 + y5;
        if (g != null) {
            g.drawLine(x1, y1, x2, y2); // line
            g.drawLine(x2, y2, x4, y4); // blade left
            g.drawLine(x2, y2, x6, y6); // blade right    
        }


    }

    /**
     * @return Returns the from.
     */
    public ConceptNode getFrom() {
        return from;
    }

    /**
     * @return Returns the to.
     */
    public ConceptNode getTo() {
        return to;
    }

    public JComponent getLabelComponent() {
        return labelComponent;
    }
}
