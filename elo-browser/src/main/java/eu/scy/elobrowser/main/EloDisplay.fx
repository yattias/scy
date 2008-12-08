/*
 * EloDisplay.fx
 *
 * Created on 6-okt-2008, 19:43:36
 */

package eu.scy.elobrowser.main;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import roolo.api.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * @author sikken
 */

// place your code here
public class EloDisplay extends CustomNode
{
   public var title = "title";
   public var radius = 20;
   public var strokeColor = Color.BLACK;
   public var fillColor = Color.color(0,0.5,0,0.25);
   public var textFont = Font
   { 
      size: 12; 
     // style: FontStyle.PLAIN;
      name:"Verdana";
   }
   
   public override function create(): Node
   {
      return Group
      {
	 content:
	 [
	    Circle
	    {
	       centerX: 0;
	       centerY: 0;
	       radius: bind radius;
	       stroke:bind strokeColor;
	       fill: bind fillColor;
	    },
	    Text
	    {
	       x: 0;
	       y: bind radius + textFont.size;
           textAlignment:TextAlignment.CENTER;
	       content: bind title;
	       stroke: bind strokeColor;
	       font: bind textFont;
	    }
	 ]
      };
   }
}