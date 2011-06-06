package eu.scy.scyplanner.impl.shapes;

import eu.scy.scymapper.api.shapes.INodeShape;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.sep.2009
 * Time: 12:06:54
 * To change this template use File | Settings | File Templates.
 */
public class ELOShape extends INodeShape {
    @Override
    public Point getConnectionPoint(Point point, Rectangle bounds) {
        Point center = new Point((int)bounds.getCenterX(), (int)bounds.getCenterY());

        double angle = Math.atan2(point.y - center.y, point.x - center.x);

        double x = center.x + (bounds.width-2)/2 * Math.cos(angle);
        double y = center.y + (bounds.height-2)/2 * Math.sin(angle);
        return new Point((int)x,(int)y);
        
    }

    @Override
    public void paint(Graphics g, Rectangle bounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        switch (getMode()) {
            case DRAW:                
                //g2.transform(AffineTransform.getRotateInstance((-Math.PI/2)/2,bounds.width/2,bounds.height/2));
                //g2.draw(new Rectangle2D.Float(0, 0, bounds.width - 1, bounds.height -1));
                break;
            case FILL:
                g2.transform(AffineTransform.getRotateInstance((-Math.PI/2)/2,bounds.width/2,bounds.height/2));
                g2.fill(new Rectangle2D.Float(0, 0, bounds.width - 1, bounds.height -1));
        }
    }
}
