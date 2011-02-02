/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer.element.gps;

import java.util.HashSet;
import java.util.Set;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;

/**
 *
 * @author pg
 */
public class BubblePosition {
    private GeoPosition geoPos = null;
    private String caption = null;
    private SpeechBubbleRenderer bubbleRenderer = null;
    private WaypointPainter painter = null;

    public BubblePosition(GeoPosition pos, String caption) {
        Set<Waypoint> waypoint = new HashSet<Waypoint>();
        this.geoPos = pos;
        this.caption = caption;
        waypoint.add(new Waypoint(geoPos.getLatitude(), geoPos.getLongitude()) );
        painter = new WaypointPainter();
        bubbleRenderer = new SpeechBubbleRenderer(caption);
        painter.setRenderer(bubbleRenderer);
        painter.setWaypoints(waypoint);
    }

    public WaypointPainter getPainter() {
        return this.painter;
    }

    public int getCloseX() {
        return bubbleRenderer.getCloseX();
    }

        public int getCloseY() {
        return bubbleRenderer.getCloseY();
    }

    public GeoPosition getPos() {
        return this.geoPos;
    }
}
