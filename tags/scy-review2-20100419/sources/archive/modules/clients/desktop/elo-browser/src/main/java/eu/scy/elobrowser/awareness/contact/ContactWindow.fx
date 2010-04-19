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
import eu.scy.elobrowser.main.user.User;
import eu.scy.elobrowser.notification.hack.CollaborationNotifier;
import eu.scy.elobrowser.tool.colemo.ColemoWindow;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
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
import eu.scy.elobrowser.main.user.User;

/**
 * @author Sven
 */

public class ContactWindow extends CustomNode, ChatInitiator {

    public var scyDesktop: ScyDesktop; 
    public var offset: Number = 10;

    public var dragging: Boolean = false;

    public var messageWindowInstanciated: Boolean = false;

    public var chatConnector: ChatConnector;

    public var contacts: ContactFrame[] on replace {
        updateContacts();
        actualizePositions();
    };
    public var visibleContacts = bind contacts[contact|contact.contact.onlineState != OnlineState.OFFLINE] on replace {
        actualizePositions();
    };
    //total Number of lines in contacts Window
    var lineNumbers: Number = bind ((
    (sizeof visibleContacts) / 2) as Integer);

    var ghostImage: ImageView;

    init{
        actualizePositions();
        frameResize();

        chatConnector.setChatInitiator(this);

        updateContacts();
    };

    public function updateContacts() : Void {
        for (contact in contacts){
            contact.frame.onMousePressed = function(evt:MouseEvent):Void{
                if (evt.primaryButtonDown){
                    System.out.println("Primary Mouse Button Down");
                    dragging = true;
                    scyDesktop.draggedContact = contact.contact;
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
                    startChat(contact.contact.name);
                }
            };

            contact.onMouseReleased = function(evt:MouseEvent):Void{
                if(scyDesktop.contactDragging) {
                    if(scyDesktop.targetWindow != null) {
                        //if(scyDesktop.targetWindow instanceof ColemoWindow) {
                        if(scyDesktop.targetWindow.id.endsWith(".scy") or scyDesktop.targetWindow.id.endsWith(".scymapping")) {
                            println("Contact {scyDesktop.draggedContact.name} dropped on Colemo window!");
                            CollaborationNotifier.notify(scyDesktop.draggedContact.name, "SCYMapper");
                        }
                    }
                    scyDesktop.contactDragging = false;
                }
                dragging = false;
                delete ghostImage from content;
                ghostImage.visible = false;
                scyDesktop.draggedContact = null;
            };
            contact.onMouseDragged= function(evt:MouseEvent):Void{
                if (dragging) {
                    scyDesktop.contactDragging = true;
                    ghostImage.translateX = evt.dragX;
                    ghostImage.translateY = evt.dragY;
                }
            };

            var lastClickContact: Long = 0;
        }

}


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

    override public function startChat(contactName: String) : Void {
        startChat(contactName, null);
    }

    override public function startChat(contactName: String, initialMessage: String) : Void {
        println(".... startChat with {contactName} .......");
        for (contact in contacts) {
            if (contact.contact.name == contactName) {
                if (not contact.isChatting){
                        contact.isChatting = true;
                        var messageWindow: MessageWindow = MessageWindow{
                            sender: User.instance.getUsername();
                            receiver: contact.contact.name;
                            opacity: 0.0;
                            con: chatConnector;
                        }
                        var scyMessageWindow: ScyWindow = ScyWindow{
                            translateY: 300;
                            opacity: 0.75;
                            title: "Chat with {messageWindow.receiver}"
                            id: "Chat{messageWindow.receiver}";
                            color: Color.BLUE
                            scyContent: messageWindow
                            allowClose: true;
                            allowResize: true;
                            allowMinimize: true;
                        }
                        chatConnector.addChatReceiver(messageWindow, contactName);
                        if (initialMessage != null) {
                            messageWindow.addChatRow(contactName, initialMessage);
                        }
                        scyMessageWindow.openWindow(messageWindow.width, messageWindow.height);
                        scyDesktop.addScyWindow(scyMessageWindow);
                        //                        insert messageWindow into content;
                        messageWindow.visible = true;
                        Timeline{
                            keyFrames: [at (0.2s){messageWindow.opacity => 1.0 tween SimpleInterpolator.LINEAR}];
                        }.play();

                    } else {
                        def scyWindow: ScyWindow = scyDesktop.findScyWindow("Chat{contact.contact.name}");
                        scyDesktop.activateScyWindow(scyWindow);
                        scyDesktop.showScyWindow(scyWindow);
                        scyWindow.toFront();
//                        scyWindow.showFrom(scyWindow.translateX, scyWindow.translateY);
//                        scyWindow.width = (scyWindow.scyContent as MessageWindow).width;
//                        scyWindow.height = (scyWindow.scyContent as MessageWindow).height;
                    }
            }

        }

    }


    public function frameResize():Void{
        Timeline{ 
            keyFrames: [at (0.3s){frame.height => (((
                (sizeof visibleContacts)
                /  2) as Integer) * visibleContacts[0].height) + 2 * offset + 8 + ((sizeof visibleContacts) mod 2 * visibleContacts[0].height) tween SimpleInterpolator.LINEAR},
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
        cache:true;
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
        cache:true;
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

            if (evt.clickCount == 2) {
                //doubleclick-action
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
            }
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
