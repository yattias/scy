/*
 * MissionMapWindow.fx
 *
 * Created on 31.03.2009, 13:16:05
 */

package eu.scy.elobrowser.missionmap;


import java.lang.Object;
import javafx.scene.Cursor;
import javafx.scene.CustomNode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


/**
 * @author Sven
 */

public class MissionMapWindow extends CustomNode{

    public var content:Node;

    public var width:Number;
    public var height:Number;

    public var offset: Number = 10;

    var lastClick: Long;   //For the doubleClickListener

    init{

        actualizePositions();
    };

    postinit {
        actualizePositions();
        frameResize();
    };


    public function actualizePositions():Void {
//            Timeline{
//                keyFrames: [at (0.3s){contact.x => goalX tween SimpleInterpolator.LINEAR; },
//                at (0.3s){contact.y => goalY tween SimpleInterpolator.LINEAR}];
//            }.play();

        }


    public function frameResize():Void{
//        Timeline{
//            keyFrames: [at (0.3s){frame.height => (((
//                (sizeof visibleContacts)
//                /  2) as Integer) * visibleContacts[0].height) + 2 * offset + 8 tween SimpleInterpolator.LINEAR},
//                    at (0.3s){frame.width =>
//                (2 * visibleContacts[0].width) + 2 * offset + 8 tween SimpleInterpolator.LINEAR}];
////                at (0.3s){background.opacity => backgroundOpacity tween SimpleInterpolator.LINEAR}];
//        }.play();
    };

    def windowEffectQueue = DropShadow {
        color: Color.BLACK

    };

    def windowBackgroundColor = Color.WHITE;
    def controlColor = Color.WHITE;
	def controlLength = 18;
	def controlStrokeWidth = 4;
	def closeCrossInset = 4;
    def controlStrokeDashArray = [0.0,7.0];
    def controlStrokeDashOffset = 0;

    def iconSize = 16;

	def borderWidth = 4;
	def topLeftBlockSize = 17;
	def closedHeight = iconSize + topLeftBlockSize / 2 + borderWidth / 2;
    def closeColor = Color.WHITE;
    def closeMouseOverEffect: Effect = Glow{
        level: 1
    }
    var lineOffsetY = 3;
	var lineWidth = 1;
	var contentBorder = 2;
    def closeStrokeWidth = 2;
    var color = Color.GREEN;

     public var background:Group = Group{ // the white background of the window
        def width = bind frame.width;
        def height = bind frame.height;
					content: [
                        Rectangle { // top part until the arc
							x: bind controlLength,
							y: 0
							width: bind width - controlLength,
							height: controlLength,
							strokeWidth: borderWidth,
							fill: windowBackgroundColor,
							stroke: windowBackgroundColor,
						},
                        Rectangle { // bottom right part until the arc
							x: 0,
							y: bind controlLength,
							width: bind width,
							height: bind height - controlLength,
							strokeWidth: borderWidth,
							fill: windowBackgroundColor,
							stroke: windowBackgroundColor;
						},
                        Arc { // the bottom left rotate arc part
							centerX: controlLength,
                            centerY: controlLength,
                            radiusX: controlLength,
                            radiusY: controlLength
                            startAngle: 90,
                            length: 90
							type: ArcType.ROUND
							strokeWidth: borderWidth
							fill: windowBackgroundColor
							stroke: windowBackgroundColor
						}
					]
                    visible:true;
				};

    public var frameborder = Group{
        def width = bind frame.width;
        def height = bind frame.height;


        content: [
                background,
                Line { // the left border line
					startX: 0,
					startY: bind controlLength + borderWidth / 2 + closeStrokeWidth / 2
					endX: 0,
					endY: bind height,
					strokeWidth: borderWidth
					stroke: bind color
				}
                Line { // the top border line
					startX: bind controlLength + borderWidth / 2 + closeStrokeWidth / 2,
					startY: 0,
					endX: bind width,
					endY: 0,
					strokeWidth: borderWidth
					stroke: bind color
				}
                Line { // the right border line
					startX: bind width,
					startY: 0
					endX: bind width,
					endY: bind height,
					strokeWidth: borderWidth
					stroke: bind color
//					effect: bind windowEffect
				}
                Line { // the bottom border line
					startX: bind width,
					startY: bind height
					endX: 0,
					endY: bind height,
					strokeWidth: borderWidth
					stroke: bind color
//					effect: bind windowEffect
				}
                Arc { // the bottom left "disabled" rotate arc
					centerX: controlLength,
					centerY: controlLength,
					radiusX: controlLength,
					radiusY: controlLength
					startAngle: 90,
					length: 90
					type: ArcType.OPEN
					fill: Color.TRANSPARENT
					strokeWidth: borderWidth
					stroke: bind color
					//effect:bind windowEffect

                }
                Group{ // the content
                    blocksMouse: true;
                    cursor: Cursor.DEFAULT;
                    translateX: borderWidth / 2 + 1 + contentBorder
                    translateY: iconSize + topLeftBlockSize / 2 + 1 + contentBorder
                    clip: Rectangle {
                        x: 0,
                        y: 0
                        width: bind width - borderWidth - 2 * contentBorder - 1,
                        height: bind height - borderWidth - iconSize - topLeftBlockSize / 2 + 1 - 2 * contentBorder
                        fill: Color.BLACK
                    }
//                    content: bind scyContent
                    onMousePressed: function( e: MouseEvent ):Void {
                    }
                }]

    }

    public var frame: Rectangle = Rectangle{
//        stroke: Color.BLACK;
        stroke: Color.TRANSPARENT;
//        fill: gradient;
        fill: Color.TRANSPARENT;
        arcHeight: 20;
        arcWidth: 20;
        opacity: 0.7;
        effect: windowEffectQueue;
        height: bind this.height;
        width: bind this.width;

    };

    override public function create():
    Node{
        actualizePositions();
        return Group{
            content: bind [frame,frameborder,content];
        };
    }
}

    function run(__ARGS__ : String[]){

        Stage {
            title: ""
            width: 620
            height: 520
            visible: true;
            scene: Scene {
//                    content: [];
            }
        }
    }