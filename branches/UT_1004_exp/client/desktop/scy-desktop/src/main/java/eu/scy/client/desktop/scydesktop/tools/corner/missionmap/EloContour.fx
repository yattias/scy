/*
	* EloContour.fx
	 *
	 * Created on 22-mrt-2009, 11:26:27
	 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;

	/**
* @author sikken
 */

 // place your code here

 // place your code here
public class EloContour extends CustomNode {
	public var width: Number = 100;
	public var height: Number = 100;
	public var borderColor = Color.GREEN;
	public var borderWidth: Number = 4;
	public var controlLength: Number = 20;
	public var fillColor = Color.RED;


	public override function create(): Node {
		return Group {
			content: [
				Group{ // the white background of the window
					content: [
						Rectangle { // top part until the arc
							x: 0,
							y: 0
							width: bind width,
							height: bind height - controlLength
							strokeWidth: borderWidth
							fill: bind fillColor
							stroke: bind fillColor
						},
						Rectangle { // bottom left part until the arc
							x: bind controlLength,
							y: bind height - controlLength
							width: bind width - controlLength,
							height: bind controlLength
							strokeWidth: borderWidth
							fill: bind fillColor
							stroke: bind fillColor
						},
						Arc { // the bottom left rotate arc part
							centerX: controlLength,
							centerY: bind height - controlLength,
							radiusX: controlLength,
							radiusY: controlLength
							startAngle: 180,
							length: 90
							type: ArcType.ROUND
							strokeWidth: borderWidth
							fill: bind fillColor
							stroke: bind fillColor
						}
					]
				}
				Line { // the left border line
						startX: 0,
						startY: bind height - controlLength - borderWidth / 2
						endX: 0,
						endY: 0
						strokeWidth: borderWidth
						stroke: bind borderColor
					}
					Line { // the top border line
						startX: 0,
						startY: 0
						endX: bind width,
						endY: 0
						strokeWidth: borderWidth
						stroke: bind borderColor
					}
					Line { // the right border line
						startX: bind width,
						startY: 0
						endX: bind width,
						endY: bind height,
						strokeWidth: borderWidth
						stroke: bind borderColor
					}
					Line { // the bottom border line
						startX: bind width,
						startY: bind height
						endX: bind controlLength + borderWidth / 2,
						endY: bind height,
						strokeWidth: borderWidth
						stroke: bind borderColor
					}
					Arc { // the bottom left "disabled" rotate arc
						centerX: controlLength,
						centerY: bind height - controlLength,
						radiusX: controlLength,
						radiusY: controlLength
						startAngle: 180,
						length: 90
						type: ArcType.OPEN
						fill: Color.TRANSPARENT
						strokeWidth: borderWidth
						stroke: bind borderColor
					}
			]
		}
	}
}



	function run() {
		Stage {
			title: "Test EloContour"
			scene: Scene {
				width: 200
				height: 200
				content: [
					EloContour{
						translateX:10;
						translateY:10;
					}

				]
			}
		}

	}
