/*
 * SwingPopupMenu2.fx
 *
 * Created on 23-dec-2008, 11:49:45
 */

package eu.scy.elobrowser.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import javafx.ext.swing.SwingComponent;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * @author sikken
 */

 // place your code here

public class SwingPopupMenu extends SwingComponent{

	public var fill=Color.TRANSPARENT on replace {
		 component.setBackground(fill.getAWTColor());
	};

	public var label: String on replace{
		popupMenu.setLabel(label);
    }

	public var items:SwingMenuItem[] on replace{
		 popupMenu.removeAll();
		for (item in items){
			popupMenu.add(item.menuItem);
		}
    }

	public var shapes:Node[];
	public var showOnLeftClick = false;
	public var showOnRightClick = true;

	var popupMenu: JPopupMenu;
	var component:JComponent;

	public override function createJComponent(){
		popupMenu = new JPopupMenu();
		component = new JPanel();
		//component.setComponentPopupMenu(popupMenu);
		component.setBackground(fill.getAWTColor());
		component.setPreferredSize(new Dimension(1000,1000));
		component.setEnabled(true);
		component.addMouseListener(MouseAdapter{
			override function mousePressed(e: java.awt.event.MouseEvent):Void{
				if (shouldShowPopupMenu(e)){
					popupMenu.show(component, e.getX(), e.getY());
				}
			}
		});
		return component;
	}

	function shouldShowPopupMenu(e: java.awt.event.MouseEvent):Boolean{
		// println("button: {e.getButton()}");
		if ((showOnLeftClick and e.getButton() == java.awt.event.MouseEvent.BUTTON1)
		or (showOnRightClick and e.getButton() == java.awt.event.MouseEvent.BUTTON3)){
			return mouseInShape(e.getX(),e.getY())
		}
		return false;
	}

	function mouseInShape(x:Integer,y:Integer):Boolean{
		if (shapes.size() > 0){
			for (shape in shapes){
				 var difX = translateX-shape.translateX;
				 var difY = translateY-shape.translateY;
				//println("mouseInShape({x},{y}):{shape.contains(x,y)}");
				if (shape.contains(x+difX,y+difY))
				return true;
			}
			return false;
		}
		//println("mouseInShape({x},{y}), but no shape");

		return true;
	}
}


function run() {
	var popupMenuItems=[
		SwingMenuItem{
			label:"item 1"
			action:function(){
				println("item 1 selected")

			}
		},
		SwingMenuItem{
			label:"item 2",
		enabled:false}
		SwingMenuItem{
			label:"item 3"}
	];
	var popupMenu = SwingPopupMenu{
		items: popupMenuItems}
	var rect:Node =
	Rectangle {
		translateX:50;
		translateY:50;
		x: 10,
		y: 10
		width: 40,
		height: 40
		fill: Color.YELLOW
	}
	var poly = Polygon {
		translateX:10;
		translateY:10;
		points: [ 0,0, 100,0, 100,100 ]
		fill: Color.YELLOW
	}
	var image = ImageView {
		translateX:10;
		translateY:10;
		image: Image {
			url: "{__DIR__}../main/images/unknownElo.png"
		}
	}

	Stage {
		title: "PopupMenu test"
		scene: Scene {
			width: 200
			height: 200
			content: [
				image,
				rect,
				//				Circle {
				//					centerX: 50,
				//					centerY: 50
				//					radius: 40
				//					fill: Color.BLACK
				//				}

				SwingPopupMenu{
					translateX:10;
					translateY:10,
					width:100;
					height:100;
					items: popupMenuItems,
					label:"testing"
					fill:Color.color(0.9,0.9,0.9,0.7);
					cursor:Cursor.HAND;
					shapes:[image,rect];
					onMouseEntered: function( e: MouseEvent ):Void {
						popupMenu.cursor=Cursor.HAND;
					}
					onMouseExited: function( e: MouseEvent ):Void {
						popupMenu.cursor=Cursor.DEFAULT;
					}
				}
			]
		}
	}
}
