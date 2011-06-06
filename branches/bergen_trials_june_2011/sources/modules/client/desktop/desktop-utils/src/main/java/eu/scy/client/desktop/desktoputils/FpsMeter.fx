/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.desktoputils;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import java.lang.System;

/**
 * @author SikkenJ
 */

public class FpsMeter extends CustomNode {
   public-init var width= 1.0;
   public-init var height= 1.0;
   public-read var fps = 0.0;

   var frameCount:Long = 0;
   var startNanos = System.nanoTime();
   def milliSecondInNanos:Long = 1000000;
   def minimumUpdateNanos:Long = 500*milliSecondInNanos;

   def displayRect = Rectangle {
      x: 0, y: 0
      width: width, height: height
      fill: Color.RED
   }
   def color0 = Color.BLACK;
   def color1 = Color.WHITE;

   def fpsAnimation = Timeline {
   	repeatCount: Timeline.INDEFINITE
      keyFrames: [
         KeyFrame {
            time: 1ms
            canSkip:true
            action:drawAction;
         }
      ];
   }


   public override function create(): Node {
      fpsAnimation.play();
      return Group {
            content: [
               displayRect
            ]
         };
   }

   function drawAction():Void{
      ++frameCount;
      // force new draw action
      if (frameCount mod 2 ==0){
         displayRect.fill = color0
      }
      else{
         displayRect.fill = color1
      }
      var nanosPassed = System.nanoTime() - startNanos;
//      println("frameCount: {frameCount}, nanosPassed: {nanosPassed}, fps: {fps}");
      if (nanosPassed>=minimumUpdateNanos){
         fps = 1.e9*frameCount/nanosPassed;
         startNanos = System.nanoTime();
         frameCount = 0;
      }
   }
}
