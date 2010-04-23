/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxpictureviewer.map;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * @author pg
 */
public class MapManager {
    private JXMapViewer map = null;
    //waypoint data:
    private HashMap<GeoPosition,String> positions; //informations hidden on blue thingys
    private Map<GeoPosition, BubblePosition> bubblePositions; //bubbles that r popped up
    private List<GeoPosition> regions; // positions of blue thingys
    //waypoint painter stuff
    private Painter[] painters;
    private Set<Waypoint> waypoints;
    private WaypointPainter wpPainter;
    private CompoundPainter cp;
    private Vector<Painter> paintersOrder;

    public MapManager() {
        bubblePositions = new HashMap<GeoPosition, BubblePosition>();
        positions = new HashMap<GeoPosition, String>();
        regions  = new ArrayList<GeoPosition>();

        waypoints = new HashSet<Waypoint>();
        wpPainter = new WaypointPainter();
        cp = new CompoundPainter();
        paintersOrder = new Vector<Painter>();
        
        map = new JXMapViewer();
        final int max = 17;
        TileFactoryInfo info = new TileFactoryInfo(1, max-2, max, 256, true, true,
                    "http://tile.openstreetmap.org",
                    "x", "y", "z") {
                    public String getTileUrl(int x, int y, int zoom) {
                        zoom = max-zoom;
                        String url = this.baseURL +"/"+zoom+"/"+x+"/"+y+".png";
                        return url;
                    }
        };
        TileFactory tf = new DefaultTileFactory(info);
        map.setTileFactory(tf);
        map.setZoom(17);
        //startposition:
//        map.setAddressLocation(new GeoPosition(51.427783,6.800172));


        map.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                System.out.println("mouseclick.");
                //check if bubble should pop up
                Rectangle rect = map.getViewportBounds();
                boolean poppedUp = false;
                for(GeoPosition gp: regions) {
                    Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
                    Point converted_pt = new Point((int)pt.getX()- rect.x, (int)pt.getY() - rect.y);
                    if(converted_pt.distance(e.getPoint()) < 5) {
                        if(bubblePositions.get(gp) == null) {
//                            System.out.println("close enaugh to waypoint.");
                            BubblePosition bubblePos = new BubblePosition(gp,positions.get(gp));
                            bubblePositions.put(gp, bubblePos );
                            paintersOrder.add(bubblePos.getPainter());
                            poppedUp = true;
                        }
                        else {
//                            System.out.println("already painted");
                        }
                    }
                }
                //now: check if something popped up - if not, check if you gotta delete something.
                // i <3 cpt. BRUTEFORCE
                if(!poppedUp) {
//                    System.out.println("check if there is something to delete..");
                    for (Iterator<GeoPosition> iterator = bubblePositions.keySet().iterator(); iterator.hasNext();) {
                        GeoPosition geoPos = iterator.next();
                        BubblePosition bubblePos = bubblePositions.get(geoPos);
                        Point2D pt = map.getTileFactory().geoToPixel(geoPos, map.getZoom());
                        Point converted_pt = new Point((int)pt.getX()- rect.x+bubblePos.getCloseX(), (int)pt.getY() - rect.y+bubblePos.getCloseY());
                        if(converted_pt.distance(e.getPoint()) < 10) {
//                            System.out.println("DELETE!");
                            paintersOrder.remove(bubblePositions.get(geoPos).getPainter());
                            iterator.remove();
                        }
                   }
                }
                //put everything into a nice painter packet
                painters = new Painter[paintersOrder.size()+1];
                //first blue waypoints...
                painters[0] = wpPainter;
                //then speechbubbles..
                int i = 1;
                //for(BubblePosition bubblePos:bubblePositions.values()) {
                for(Painter item: paintersOrder) {
                    //painters[i] = bubblePos.getPainter();
                    painters[i] = item;
                    i++;
                }
                cp.setPainters(painters);
                map.setOverlayPainter(cp);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                //myBubble.setVisible(false);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        wpPainter.setWaypoints(waypoints);
        wpPainter.setRenderer(new CustomWaypointRenderer());
        cp.setPainters(wpPainter);
        cp.setCacheable(false);
        map.setOverlayPainter(cp);

    }

    public void addPosition(double x, double y, String caption) {
        GeoPosition pos = new GeoPosition(x,y);
        regions.add(pos);
        positions.put(pos, caption);
        waypoints.add(new Waypoint(x,y));
        wpPainter.setWaypoints(waypoints);
        map.setOverlayPainter(cp);
    }

    public JXMapViewer getMap() {
        return this.map; //...sinn? ...vorhanden.
    }


    public void centerPosition(double x, double y) {
        map.setAddressLocation(new GeoPosition(x, y));
        map.recenterToAddressLocation();

    }

    /**
     *
     * @param factor new zoom level
     */
    public void zoom(int value) {
        map.setZoom(value);
    }

    public int getZoom() {
        return this.map.getZoom();
    }

}
