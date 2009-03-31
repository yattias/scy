/*
 * ContactWindow.fx
 *
 * Created on 22.03.2009, 23:45:27
 */

package eu.scy.elobrowser.awareness.contact;

import eu.scy.elobrowser.awareness.contact.ContactFrame;
import eu.scy.elobrowser.awareness.contact.MessageWindow;
import eu.scy.elobrowser.awareness.contact.OnlineState;
import eu.scy.elobrowser.awareness.contact.WindowSize;
import java.lang.Object;
import java.lang.System;
import javafx.animation.SimpleInterpolator;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.CustomNode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.Scene;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @author Sven
 */

public class ContactWindow extends CustomNode{

    public var offset: Number = 10;

    public var dragging: Boolean = false;

    public var messageWindowInstanciated: Boolean = false;

    public var contacts: ContactFrame[];
    public var visibleContacts = bind contacts[contact|contact.contact.onlineState != OnlineState.OFFLINE] on replace {
        actualizePositions();
    };
    //total Number of lines in contacts Window
    var lineNumbers: Number = bind ((
    (sizeof visibleContacts) / 2) as Integer);

    var lastClick: Long;   //For the doubleClickListener

    var ghostImage: ImageView;


    init{
        actualizePositions();
        for (contact in contacts){

            contact.frame.onMousePressed = function(evt:MouseEvent):Void{
                if (evt.primaryButtonDown){
                    System.out.println("Primary Mouse Button Down");
                    dragging = true;
                    ghostImage = ImageView{
                        opacity: 0.3;
                        x: contact.image.x;
                        y: contact.image.y;
                    //                        x: bind evt.dragX;
                        //                        y: bind evt.dragY;
                        image: Image{
                            width: contact.image.image.width;
                            height: contact.image.image.height;
                            preserveRatio: false;
                            url: "{__DIR__}{contact.contact.imageURL}";
                        }
                    };
                      insert ghostImage into content;
                }
                if(evt.secondaryButtonDown){
                    System.out.println("Secondary Mouse Button Down");
                    var messageWindow: MessageWindow = MessageWindow{
                        //FIXME replace with variable username;
                        sender: "Anonymous";
                        receiver: contact.contact.name;
                        opacity: 0.0;
                    }
                        insert messageWindow into content;
                    messageWindow.visible = true;
                    Timeline {

                        keyFrames: [at (0.2s){messageWindow.opacity => 1.0 tween SimpleInterpolator.LINEAR}];
                    }.play();
                }
            };

            contact.onMouseReleased = function(evt:MouseEvent):Void{
                dragging = false;
                delete ghostImage from content;
                ghostImage.visible = false;
            };
            contact.onMouseDragged= function(evt:MouseEvent):Void{
                if (dragging) {
                    ghostImage.translateX = evt.dragX;
                    ghostImage.translateY = evt.dragY;
                }
            };

            var lastClickContact: Long = 0;
        }
    };

    postinit {
        for (contact in visibleContacts){
            contact.reduce();
        }
        actualizePositions();
        frameResize();
    };


    public function actualizePositions():Void {
        var count = 0;
        for (contact in visibleContacts){
            var goalX = (count mod 2) * contact.width + offset;
            var goalY = (
            (count /  2) as Integer) * contact.height + offset;
            Timeline{
                keyFrames: [at (0.3s){contact.x => goalX tween SimpleInterpolator.LINEAR; },
                at (0.3s){contact.y => goalY tween SimpleInterpolator.LINEAR}];
            }.play();
            contact.image.opacity=contact.getOpacity();
            count++;
        }
    }
    
    public function frameResize():Void{
        Timeline{ 
            keyFrames: [at (0.3s){frame.height => (((
                (sizeof visibleContacts)
                /  2) as Integer) * visibleContacts[0].height) + 2 * offset + 8 tween SimpleInterpolator.LINEAR},
                    at (0.3s){frame.width =>
                (2 * visibleContacts[0].width) + 2 * offset + 8 tween SimpleInterpolator.LINEAR}];
//                at (0.3s){background.opacity => backgroundOpacity tween SimpleInterpolator.LINEAR}];
        }.play();
    };

    def windowEffectQueue = DropShadow {
        color: Color.BLACK
       

    };

    public def gradient: LinearGradient = LinearGradient{
        proportional: true;
        stops: [
            Stop{
                color: Color.LIGHTSTEELBLUE;
                offset: 0.0;
            },
            Stop{
                color: Color.LIGHTBLUE;
                offset: 1.0;
            }]

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
    var color = Color.LIGHTSKYBLUE;
//    var windowEffect = Effect;

     public var background:Group = Group{ // the white background of the window
        def width = bind frame.width;
        def height = bind frame.height;
					content: [
                        Rectangle { // top part until the arc
							x: 0,
							y: 0
							width: bind width,
							height: bind height - controlLength
							strokeWidth: borderWidth
							fill: windowBackgroundColor
							stroke: windowBackgroundColor
						},
                        Rectangle { // bottom right part until the arc
							x: 0,
							y: bind height - controlLength
							width: bind width - controlLength,
							height: bind controlLength
							strokeWidth: borderWidth
							fill: windowBackgroundColor
							stroke: windowBackgroundColor
						},
                        Arc { // the bottom right rotate arc part
							centerX: bind (width - (controlLength + borderWidth / 2 + closeStrokeWidth / 2)),
                            centerY: bind (height - controlLength - borderWidth/2 - closeStrokeWidth/2),
                            radiusX: controlLength,
                            radiusY: controlLength
                            startAngle: 270,
                            length: 90,
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
					startY: bind height,
					endX: 0,
					endY: 0
					strokeWidth: borderWidth
					stroke: bind color
				}
                Line { // the top border line
					startX: 0,
					startY: 0
					endX: bind width,
					endY: 0
					strokeWidth: borderWidth
					stroke: bind color
				}
                Line { // the right border line
					startX: bind width,
					startY: 0
					endX: bind width,
					endY: bind (height - controlLength - borderWidth/2 - closeStrokeWidth/2),
					strokeWidth: borderWidth,
					stroke: bind color,
				}
                Line { // the bottom border line
					startX: 0,
					startY: bind height,
					endX: bind (width - (controlLength + borderWidth / 2 + closeStrokeWidth / 2)),
					endY: bind height,
					strokeWidth: borderWidth;
					stroke: bind color;
				}
                Arc { // the bottom left "disabled" rotate arc
					centerX: bind (width - (controlLength + closeStrokeWidth / 2)+ closeStrokeWidth / 2 ),
					centerY: bind (height - controlLength - closeStrokeWidth/2+ closeStrokeWidth / 2 ),
					radiusX: controlLength,
					radiusY: controlLength
					startAngle: 270,
                    length: 90,
					type: ArcType.OPEN
					fill: Color.TRANSPARENT;
					strokeWidth: borderWidth
					stroke: bind color
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
        height: (((
            (sizeof visibleContacts) /  2) as Integer) * visibleContacts[0].height) + 2 * offset ;
        width: (2 * visibleContacts[0].width) + 2 * offset;

        onMouseEntered: function(evt:MouseEvent):Void{
            requestFocus();
            toFront();
            focused = true;
            if  (visibleContacts[0].size == WindowSize.SMALL){
                frameResize();
                for (contact in visibleContacts){
                    contact.expandName();
                }
                Timeline{
                keyFrames: [at (0.3s){background.opacity => 1.0 tween SimpleInterpolator.LINEAR}]
                }.play();

                actualizePositions();
            }
        };
        onMouseExited: function(evt:MouseEvent):Void{
            focused = false;
            if  (visibleContacts[0].size == WindowSize.HOVER and not dragging){
                frameResize();
                for (contact in visibleContacts){
                    contact.reduce();
                }
                 Timeline{
                keyFrames: [at (0.3s){background.opacity => 0.0 tween SimpleInterpolator.LINEAR}]
                }.play();
                actualizePositions();
                toBack();
            }
        };
        onMouseReleased: function(evt:MouseEvent):Void{
            delete ghostImage from content;
//            if  (visibleContacts[0].size == WindowSize.HOVER and not dragging){
//                frameResize();
//                for (contact in visibleContacts){
//                    contact.reduce();
//                }
//                actualizePositions();
//            }
        }

        //        //For window-dragging-support
        //        onMouseDragged:function(evt:MouseEvent):Void {
        //            translateX = evt.dragX;
        //            translateY = evt.dragY;
        //
        //        };


        onMousePressed: function(evt:MouseEvent):Void{
            if (evt.button == MouseButton.PRIMARY){
                if ((System.currentTimeMillis() - lastClick) < 400 and (System.currentTimeMillis() - lastClick) > 1){
                    if  (visibleContacts[0].size == WindowSize.HOVER){
                        frameResize();
                        for (contact in visibleContacts){
                            contact.expand();
                        }
                        actualizePositions();
                    }
                    else if  (visibleContacts[0].size == WindowSize.NORMAL){
                        frameResize();
                        for (contact in visibleContacts){
                            contact.reduceToNameOnly();
                        }
                        actualizePositions();
                    }

                    lastClick = 0;
                }
                else {
                    lastClick = System.currentTimeMillis();
                }
            };
        };
    };

    public var content: Node[] = [frame,frameborder,visibleContacts];

    override public function create():
    Node{
        actualizePositions();
        return Group{
            content: bind content;
        };
    }



}

    function run(__ARGS__ : String[]){

        

        Stage {
            title: "Contact"
            width: 620
            height: 520
            visible: true;
            scene: Scene {
            //                content: [contactWindow,expandButton, reduceButton];
//                content: [contactWindow];
            }
        }
    }