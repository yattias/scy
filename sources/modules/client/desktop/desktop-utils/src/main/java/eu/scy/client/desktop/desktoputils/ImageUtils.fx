/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import java.awt.image.BufferedImage;
import java.applet.Applet;
import java.awt.Frame;
import javax.swing.JFrame;
import javafx.geometry.BoundingBox;
import javafx.reflect.FXLocal;
import javafx.scene.layout.Container;
import java.awt.Graphics2D;
import java.io.File;
import java.lang.Void;
import javax.imageio.ImageIO;
import javafx.util.Math;
import java.awt.Dimension;

/**
 * @author Rakesh Menon
 */
def context = FXLocal.getContext();
def nodeClass = context.findClass("javafx.scene.Node");
def getFXNode = nodeClass.getFunction("impl_getPGNode");

public function getContainer(): java.awt.Container {

   var container: java.awt.Container;

   if ("{__PROFILE__}" == "browser") { // Applet
      container = FX.getArgument("javafx.applet") as Applet;
   } else { // Standalone
      var frames = Frame.getFrames();
      // We may improve this logic so as to find the
      // exact Stage (Frame) based on its title
      container = (frames[0] as JFrame).getContentPane();
   }

   return container;
}

public function nodeToImageX(node: Node, bounds: Bounds): BufferedImage {
   var g2: Graphics2D;

   if (node instanceof Container) {
      (node as Resizable).width = bounds.width;
      (node as Resizable).height = bounds.height;
      (node as Container).layout();
   } else if (node instanceof Resizable) {
      (node as Resizable).width = bounds.width;
      (node as Resizable).height = bounds.height;
   }

   def nodeBounds = node.layoutBounds;

   def sgNode = (getFXNode.invoke(context.mirrorOf(node)) as FXLocal.ObjectValue).asObject();
   def g2dClass = (context.findClass("java.awt.Graphics2D") as FXLocal.ClassType).getJavaImplementationClass();
   def boundsClass = (context.findClass("com.sun.javafx.geom.Bounds2D") as FXLocal.ClassType).getJavaImplementationClass();
   def affineClass = (context.findClass("com.sun.javafx.geom.transform.BaseTransform") as FXLocal.ClassType).getJavaImplementationClass();

   def getBounds = sgNode.getClass().getMethod("getContentBounds", boundsClass, affineClass);
   def bounds2D = getBounds.invoke(sgNode, new com.sun.javafx.geom.Bounds2D(),
      new com.sun.javafx.geom.transform.Affine2D());

   var paintMethod = sgNode.getClass().getMethod("render", g2dClass, boundsClass, affineClass);
   def bufferedImage = new java.awt.image.BufferedImage(nodeBounds.width, nodeBounds.height,
      java.awt.image.BufferedImage.TYPE_INT_ARGB);

   g2 = (bufferedImage.getGraphics() as Graphics2D);
   g2.setPaint(java.awt.Color.WHITE);
   g2.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
   paintMethod.invoke(sgNode, g2, bounds2D,
   new com.sun.javafx.geom.transform.Affine2D());
   g2.dispose();

   // restore node bounds
   Container.layoutNode(node, nodeBounds.minX, nodeBounds.minY, nodeBounds.width, nodeBounds.height);

   return bufferedImage;
}

public function nodeToImageXX(node: Node, bounds: Bounds): BufferedImage {
   var g2: Graphics2D;

   def originalScaleX = node.scaleX;
   def originalScaleY = node.scaleY;

   def scaleXFactor = bounds.width / node.layoutBounds.width;
   def scaleYFactor = bounds.height / node.layoutBounds.height;

   def scaleFactor = Math.min(scaleXFactor, scaleYFactor);
   node.scaleX *= scaleFactor;
   node.scaleY *= scaleFactor;
   def nodeBounds = node.layoutBounds;

   def sgNode = (getFXNode.invoke(context.mirrorOf(node)) as FXLocal.ObjectValue).asObject();
   def g2dClass = (context.findClass("java.awt.Graphics2D") as FXLocal.ClassType).getJavaImplementationClass();
   def boundsClass = (context.findClass("com.sun.javafx.geom.Bounds2D") as FXLocal.ClassType).getJavaImplementationClass();
   def affineClass = (context.findClass("com.sun.javafx.geom.transform.BaseTransform") as FXLocal.ClassType).getJavaImplementationClass();

   def getBounds = sgNode.getClass().getMethod("getContentBounds", boundsClass, affineClass);
   def bounds2D = getBounds.invoke(sgNode, new com.sun.javafx.geom.Bounds2D(),
      new com.sun.javafx.geom.transform.Affine2D());

   var paintMethod = sgNode.getClass().getMethod("render", g2dClass, boundsClass, affineClass);
   def bufferedImage = new java.awt.image.BufferedImage(nodeBounds.width*scaleFactor, nodeBounds.height*scaleFactor,
      java.awt.image.BufferedImage.TYPE_INT_ARGB);

   g2 = (bufferedImage.getGraphics() as Graphics2D);
   g2.setPaint(java.awt.Color.WHITE);
   g2.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
   paintMethod.invoke(sgNode, g2, bounds2D, new com.sun.javafx.geom.transform.Affine2D());
   g2.dispose();

   // restore node scale
   node.scaleX = originalScaleX;
   node.scaleY = originalScaleY;

   println("bufferedImage size: {bufferedImage.getWidth()}*{bufferedImage.getHeight()}");

   return bufferedImage;
}

public function nodeToImage(node: Node, bounds: Bounds): BufferedImage {
   var g2: Graphics2D;

//   def originalLayoutX = node.layoutX;
//   def originalLayoutY = node.layoutY;
   // place node at 0,0
//   Container.positionNode(node, 0.0, 0.0);

   def nodeBounds = node.layoutBounds;

   def sgNode = (getFXNode.invoke(context.mirrorOf(node)) as FXLocal.ObjectValue).asObject();
   def g2dClass = (context.findClass("java.awt.Graphics2D") as FXLocal.ClassType).getJavaImplementationClass();
   def boundsClass = (context.findClass("com.sun.javafx.geom.Bounds2D") as FXLocal.ClassType).getJavaImplementationClass();
   def affineClass = (context.findClass("com.sun.javafx.geom.transform.BaseTransform") as FXLocal.ClassType).getJavaImplementationClass();

   def getBounds = sgNode.getClass().getMethod("getContentBounds", boundsClass, affineClass);
   def bounds2D = getBounds.invoke(sgNode, new com.sun.javafx.geom.Bounds2D(),
      new com.sun.javafx.geom.transform.Affine2D());

   var paintMethod = sgNode.getClass().getMethod("render", g2dClass, boundsClass, affineClass);
   def bufferedImage = new java.awt.image.BufferedImage(nodeBounds.width, nodeBounds.height,
      java.awt.image.BufferedImage.TYPE_INT_ARGB);

   g2 = (bufferedImage.getGraphics() as Graphics2D);
   g2.setPaint(java.awt.Color.WHITE);
   g2.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
   paintMethod.invoke(sgNode, g2, bounds2D, new com.sun.javafx.geom.transform.Affine2D());
   g2.dispose();

   // put node back to its original position
//   Container.positionNode(node, originalLayoutX, originalLayoutY);

   println("bufferedImage size: {bufferedImage.getWidth()}*{bufferedImage.getHeight()}");

   def resizedImage = UiUtils.resizeBufferedImage(bufferedImage, new Dimension(bounds.width,bounds.height));
   println("resizedImage size: {resizedImage.getWidth()}*{resizedImage.getHeight()}");

   return resizedImage;
}

public function nodeToSquareImage(node: Node, bounds: Bounds): BufferedImage {
   var g2: Graphics2D;
   def nodeBounds = node.layoutBounds;
   def sgNode = (getFXNode.invoke(context.mirrorOf(node)) as FXLocal.ObjectValue).asObject();
   def g2dClass = (context.findClass("java.awt.Graphics2D") as FXLocal.ClassType).getJavaImplementationClass();
   def boundsClass = (context.findClass("com.sun.javafx.geom.Bounds2D") as FXLocal.ClassType).getJavaImplementationClass();
   def affineClass = (context.findClass("com.sun.javafx.geom.transform.BaseTransform") as FXLocal.ClassType).getJavaImplementationClass();
   def getBounds = sgNode.getClass().getMethod("getContentBounds", boundsClass, affineClass);
   def bounds2D = getBounds.invoke(sgNode, new com.sun.javafx.geom.Bounds2D(), new com.sun.javafx.geom.transform.Affine2D());
   var paintMethod = sgNode.getClass().getMethod("render", g2dClass, boundsClass, affineClass);
   var bufferedImage;
   if (nodeBounds.width < nodeBounds.height) {
       bufferedImage = new java.awt.image.BufferedImage(nodeBounds.width, nodeBounds.width, java.awt.image.BufferedImage.TYPE_INT_ARGB);
   } else {
       bufferedImage = new java.awt.image.BufferedImage(nodeBounds.height, nodeBounds.height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
   }
   g2 = (bufferedImage.getGraphics() as Graphics2D);
   g2.setPaint(java.awt.Color.WHITE);
   g2.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
   paintMethod.invoke(sgNode, g2, bounds2D, new com.sun.javafx.geom.transform.Affine2D());
   g2.dispose();
   def resizedImage = UiUtils.resizeBufferedImage(bufferedImage, new Dimension(bounds.width,bounds.height));
   return resizedImage;
}

public function saveAsImage(node: Node, file: File): Void {
   if (file == null) { return; }
   def image = nodeToImage(node, BoundingBox {
         width: node.layoutBounds.width
         height: node.layoutBounds.height
      });
   ImageIO.write(image, "png", file);
}

