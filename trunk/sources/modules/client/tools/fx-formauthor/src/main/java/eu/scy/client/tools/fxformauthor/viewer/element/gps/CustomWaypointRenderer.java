/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer.element.gps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

/**
 *
 * @author pg
 */
public class CustomWaypointRenderer implements WaypointRenderer {
    BufferedImage img = null;

    public CustomWaypointRenderer() {
        try {
            img = ImageIO.read(getClass().getResource("pin.png"));
        } catch (Exception ex) {
            // System.out.println("couldn't read pin.png");
            // System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public boolean paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint waypoint) {
        if(img != null) {
            g.drawImage(img,-img.getWidth()/2,-img.getHeight(),null);
        }
        else {
            g.setStroke(new BasicStroke(3f));
            g.setColor(Color.BLUE);
            g.drawOval(-10,-10,20,20);
            g.setStroke(new BasicStroke(1f));
            g.drawLine(-10,0,10,0);
            g.drawLine(0,-10,0,10);
        }
        return false;
    }

}
