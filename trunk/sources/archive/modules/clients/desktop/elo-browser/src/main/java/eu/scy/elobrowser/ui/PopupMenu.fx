/*
 * PopupMenu.fx
 *
 * Created on 22-dec-2008, 11:31:37
 */

package eu.scy.elobrowser.ui;

import eu.scy.elobrowser.ui.PopupMenu;
import eu.scy.elobrowser.ui.UI_Colors;
import javafx.scene.CustomNode;
import javafx.scene.effect.DropShadow;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author sikken
 */

 // place your code here


public class PopupMenu extends CustomNode {
	public var popupMenuItems:PopupMenuItem[];
	public var font = Font{
		name:"Verdana",
		size:12}
	public var backgroundColor =UI_Colors.backgroundColor;
	public var hooverColor = UI_Colors.backgroundHoverColor;
	public var textColot = UI_Colors.textColor;
	public var textHooverColor = UI_Colors.textHoverColor;
	public var width = 100;
	var hightOffset = font.size / 2;

	public override function create(): Node {
		var vBox:VBox;
		var totalHeight =0;
		return Group {

			content: [
				vBox = VBox{
					effect:DropShadow{
						offsetX:4;
						offsetY:4;
					}
					content:[
						for (popupMenuItem in popupMenuItems){
							var rect:Rectangle;
							var text:Text;
							Group{
								content:[
									rect = Rectangle {
										x: 0,
										y: 0
										width: width,
										height: hightOffset + font.size
										fill: backgroundColor
									}
									text = Text{
										translateX:10;
										translateY:font.size;
										font:font
										content:popupMenuItem.label
										fill:textColot;
									}
								]
								onMouseEntered: function( e: MouseEvent ):Void {
									rect.fill = hooverColor;
									text.fill = textHooverColor;
								}
								onMouseExited: function( e: MouseEvent ):Void {
									rect.fill = backgroundColor;
									text.fill = textColot;
								}
							}
						}
					]
				}
				Rectangle{
					x:0;
					y:0;
					width:width;
					height:popupMenuItems.size() * (hightOffset + font.size)
					fill:Color.TRANSPARENT;
					stroke:Color.BLACK
				}
			]
		};
	}
}

	public class PopupMenuItem{
		public var label = "label";
		public var action:function(e:MouseEvent):Void;
	}

function run() {
	var popupMenuItems=[
		PopupMenuItem{
		label:"item 1"},
		PopupMenuItem{
		label:"item 2"}
		PopupMenuItem{
		label:"item 3"}
	];

	Stage {
		title: "PopupMenu test"
		scene: Scene {
			width: 200
			height: 200
			content: [ PopupMenu{
					translateX:10;
					translateY:10;
					popupMenuItems: popupMenuItems} ]
		}
	}
}