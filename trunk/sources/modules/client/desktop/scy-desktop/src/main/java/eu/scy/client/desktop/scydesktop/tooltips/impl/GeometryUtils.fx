/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.geometry.Point2D;

/**
 * @author SikkenJ
 */
public function calculateIntersectionPoint(line1Begin: Point2D, line1End: Point2D, line2Begin: Point2D, line2End: Point2D): Point2D {
   def d = (line1Begin.x - line1End.x) * (line2Begin.y - line2End.y) - (line1Begin.y - line1End.y) * (line2Begin.x - line2End.x);
   if (d == 0) return null;

   Point2D {
      x: ((line2Begin.x - line2End.x) * (line1Begin.x * line1End.y - line1Begin.y * line1End.x)
      - (line1Begin.x - line1End.x) * (line2Begin.x * line2End.y - line2Begin.y * line2End.x)) / d
      y: ((line2Begin.y - line2End.y) * (line1Begin.x * line1End.y - line1Begin.y * line1End.x)
      - (line1Begin.y - line1End.y) * (line2Begin.x * line2End.y - line2Begin.y * line2End.x)) / d
   }
}
