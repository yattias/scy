/*
 * ScyEdge.fx
 *
 * Created on 27.03.2009, 12:49:46
 */

package eu.scy.scywindows;

import eu.scy.scywindows.ScyEdge;
import eu.scy.scywindows.ScyWindow;
import javafx.scene.shape.Line;

/**
 * @author pg
 */


public class ScyEdge extends Line{

    public var node1: ScyWindow;
    public var node2: ScyWindow;
    public var scyEdgeLayer: ScyEdge;

    /**
     * repaints the whole edge, used to snap it into nodes & repainting while
     * moving nodes.
     */
    public function repaint():Void {
        startX = node1.translateX + node1.width / 2;
        startY = node1.translateY + node1.height / 2;
        endX = node2.translateX + node2.width / 2;
        endY = node2.translateY + node2.height / 2;
        //System.out.println("repaint() called.");
        //System.out.println("repaint in edge called.")

    }


}