/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.geometry.Rectangle2D;
import javafx.geometry.Point2D;

/**
 * @author SikkenJ
 */
public class ArrowPoints {

   public-init var arrowBaseOffset = 12.0;
   public-init var borderWidth = 2.0;
   public-init var baseRect: Rectangle2D on replace { calculatePoints() };
   public var arrowPoint: Point2D on replace { calculatePoints() };
   public var arrowPosition = ArrowPosition.TOP_LEFT on replace { calculatePoints() };
   public-read var backgroundPoint1: Point2D;
   public-read var backgroundPoint2: Point2D;
   public-read var backgroundPoint3: Point2D;
   public-read var linePoint1: Point2D;
   public-read var linePoint2: Point2D;
   public-read var linePoint3: Point2D;

   function calculatePoints() {
      calculateBackgroundPoints();
      calculateLinePoints();
      printPoints()
   }

   function printPoints() {
      println("baseRect: {baseRect}");
      println("arrowPoint: {arrowPoint}");
      println("arrowPosition: {arrowPosition}");
      println("backgroundPoint1: {backgroundPoint1}");
      println("backgroundPoint2: {backgroundPoint2}");
      println("backgroundPoint3: {backgroundPoint3}");
      println("linePoint1: {linePoint1}");
      println("linePoint2: {linePoint2}");
      println("linePoint3: {linePoint3}");
   }

   function calculateBackgroundPoints() {
      backgroundPoint2 = arrowPoint;
      if (ArrowPosition.TOP_LEFT == arrowPosition) {
         backgroundPoint1 = Point2D {
                    x: baseRect.minX
                    y: baseRect.minY + arrowBaseOffset
                 }
         backgroundPoint3 = Point2D {
                    x: baseRect.minX + arrowBaseOffset
                    y: baseRect.minY
                 }
      } else if (ArrowPosition.TOP_RIGHT == arrowPosition) {
         backgroundPoint1 = Point2D {
                    x: baseRect.maxX
                    y: baseRect.minY + arrowBaseOffset
                 }
         backgroundPoint3 = Point2D {
                    x: baseRect.maxX - arrowBaseOffset
                    y: baseRect.minY
                 }
      } else if (ArrowPosition.BOTTOM_RIGHT == arrowPosition) {
         backgroundPoint1 = Point2D {
                    x: baseRect.maxX
                    y: baseRect.maxY - arrowBaseOffset
                 }
         backgroundPoint3 = Point2D {
                    x: baseRect.maxX - arrowBaseOffset
                    y: baseRect.maxY
                 }
      } else if (ArrowPosition.BOTTOM_LEFT == arrowPosition) {
         backgroundPoint1 = Point2D {
                    x: baseRect.minX
                    y: baseRect.maxY - arrowBaseOffset
                 }
         backgroundPoint3 = Point2D {
                    x: baseRect.minX + arrowBaseOffset
                    y: baseRect.maxY
                 }
      }
   }

   function calculateLinePoints() {
      var deltaPoint1: Point2D;
      var deltaPoint3: Point2D;
      if (ArrowPosition.TOP_LEFT == arrowPosition) {
         def arrowOffsetX = baseRect.minX - arrowPoint.x;
         def arrowOffsetY = baseRect.minY - arrowPoint.y;
         def lineOffsetX = arrowOffsetY / (arrowOffsetY + arrowBaseOffset) * borderWidth;
         def lineOffsetY = arrowOffsetX / (arrowOffsetY + arrowBaseOffset) * borderWidth;
         deltaPoint1 = Point2D {
                    x: borderWidth
                    y: -lineOffsetY
                 }
         deltaPoint3 = Point2D {
                    x: lineOffsetX
                    y: borderWidth
                 }
      } else if (ArrowPosition.TOP_RIGHT == arrowPosition) {
         def arrowOffsetX = arrowPoint.x - baseRect.maxX;
         def arrowOffsetY = baseRect.minY - arrowPoint.y;
         def lineOffsetX = arrowOffsetY / (arrowOffsetY + arrowBaseOffset) * borderWidth;
         def lineOffsetY = arrowOffsetX / (arrowOffsetY + arrowBaseOffset) * borderWidth;
         deltaPoint1 = Point2D {
                    x: -borderWidth
                    y: -lineOffsetY
                 }
         deltaPoint3 = Point2D {
                    x: lineOffsetX
                    y: borderWidth
                 }
      } else if (ArrowPosition.BOTTOM_RIGHT == arrowPosition) {
         def arrowOffsetX = arrowPoint.x - baseRect.maxX;
         def arrowOffsetY = arrowPoint.y - baseRect.maxY;
         def lineOffsetX = arrowOffsetY / (arrowOffsetY + arrowBaseOffset) * borderWidth;
         def lineOffsetY = arrowOffsetX / (arrowOffsetY + arrowBaseOffset) * borderWidth;
         deltaPoint1 = Point2D {
                    x: -borderWidth
                    y: lineOffsetY
                 }
         deltaPoint3 = Point2D {
                    x: lineOffsetX
                    y: -borderWidth
                 }
      } else if (ArrowPosition.BOTTOM_LEFT == arrowPosition) {
         def arrowOffsetX = baseRect.minX - arrowPoint.x;
         def arrowOffsetY = arrowPoint.y - baseRect.maxY;
         def lineOffsetX = arrowOffsetY / (arrowOffsetY + arrowBaseOffset) * borderWidth;
         def lineOffsetY = arrowOffsetX / (arrowOffsetY + arrowBaseOffset) * borderWidth;
         deltaPoint1 = Point2D {
                    x: borderWidth
                    y: lineOffsetY
                 }
         deltaPoint3 = Point2D {
                    x: -lineOffsetX
                    y: -borderWidth
                 }
      }
      linePoint1 = Point2D {
                 x: backgroundPoint1.x + deltaPoint1.x
                 y: backgroundPoint1.y + deltaPoint1.y
              }
      linePoint3 = Point2D {
                 x: backgroundPoint3.x + deltaPoint3.x
                 y: backgroundPoint3.y + deltaPoint3.y
              }
      def parrallelLine1End = Point2D {
                 x: backgroundPoint2.x + deltaPoint1.x
                 y: backgroundPoint2.y + deltaPoint1.y
              };
      def parrallelLine3End = Point2D {
                 x: backgroundPoint2.x + deltaPoint3.x
                 y: backgroundPoint2.y + deltaPoint3.y
              };
      linePoint2 = GeometryUtils.calculateIntersectionPoint(linePoint1, parrallelLine1End, linePoint3, parrallelLine3End);

   }

}
