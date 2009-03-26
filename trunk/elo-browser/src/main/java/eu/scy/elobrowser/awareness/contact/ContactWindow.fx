/*
 * ContactWindow.fx
 *
 * Created on 22.03.2009, 23:45:27
 */

package eu.scy.elobrowser.awareness.contact;

import eu.scy.elobrowser.awareness.contact.Contact;
import eu.scy.elobrowser.awareness.contact.ContactFrame;
import eu.scy.elobrowser.awareness.contact.ContactWindow;
import eu.scy.elobrowser.awareness.contact.MessageWindow;
import eu.scy.elobrowser.awareness.contact.OnlineState;
import eu.scy.elobrowser.awareness.contact.WindowSize;
import java.lang.Object;
import java.lang.System;
import javafx.animation.SimpleInterpolator;
import javafx.animation.Timeline;
import javafx.scene.CustomNode;
import javafx.scene.effect.DropShadow;
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
                        //FIXME worked without overriding
//                        override public function closeWindow():Void{
//                            Timeline{
//                                keyFrames: [at (0.2s){this.opacity => 0.0 tween SimpleInterpolator.LINEAR}];
//
//                            }.play();
//                            blocksMouse=false;
//                            delete messageWindow from content;
//                        };
//                        override public function create():Node{
//                            return createNode();
//                        }

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

//            contact.frame.onMouseClicked = function(evt:MouseEvent):Void{
//                if(evt.button == MouseButton.SECONDARY){
//                    var messageWindow = MessageWindow{
//                        //FIXME replace with variable username;
//                        sender: "Anonymous";
//                        receiver: contact.contact.name;
//                        opacity: 0.0;
//                    }
//                        insert messageWindow into content;
//                    messageWindow.visible = true;
//                    Timeline {
//
//                        keyFrames: [at (0.2s){messageWindow.opacity => 1.0 tween SimpleInterpolator.LINEAR}];
//                    }.play();
//                }
//            };


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
        }.play();
    };

    def windowEffectQueue = DropShadow {
        color: Color.BLACK
       

    };

    public def gradient: LinearGradient = LinearGradient{
        proportional: true;
        stops: [
            Stop{
                color: Color.GREEN;
                offset: 0.0;
            },
            Stop{
                color: Color.DARKOLIVEGREEN;
                offset: 1.0;
            }]

    };


    public var frame: Rectangle = Rectangle{
        stroke: Color.BLACK;
        fill: gradient;
        arcHeight: 20;
        arcWidth: 20;
        opacity: 0.7;
        effect: windowEffectQueue;
        height: (((
            (sizeof visibleContacts) /  2) as Integer) * visibleContacts[0].height) + 2 * offset ;
        width: (2 * visibleContacts[0].width) + 2 * offset;

        onMouseEntered: function(evt:MouseEvent):Void{
            if  (visibleContacts[0].size == WindowSize.SMALL){
                frameResize();
                for (contact in visibleContacts){
                    contact.expandName();
                }
                actualizePositions();
            }
        };
        onMouseExited: function(evt:MouseEvent):Void{
            if  (visibleContacts[0].size == WindowSize.HOVER and not dragging){
                frameResize();
                for (contact in visibleContacts){
                    contact.reduce();
                }
                actualizePositions();
            }
        };
        onMouseReleased: function(evt:MouseEvent):Void{
            delete ghostImage from content;
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

    public var content: Node[] = [frame,visibleContacts];

    override public function create():
    Node{
        actualizePositions();
        return Group{
            content: bind content;
        };
    }



}

    function run(__ARGS__ : String[]){

        
    

//
//        def contactWindow = ContactWindow{
//            contacts: bind getContacts();
//        };

    //Buttons for testing purpose only

        //       def button =  SwingButton {
        //            text: "Change online state";
        //            translateX: 400;
        //            translateY: 20;
        //            action: function() {
        //contact5.contact.onlineState = OnlineState.AWAY;
        //            }
        //        }

        //        def expandButton = SwingButton{
        //            text: "expand Text";
        //            translateX: 300;
        //            translateY: 200;
        //            onMouseClicked: function(evt: MouseEvent):Void {
        //                for (contactFrame in contactWindow.visibleContacts) {
        //                    if (contactFrame.size != WindowSize.NORMAL){
        //                        contactFrame.expand();
        //                    }
        //                }
        //                contactWindow.actualizePositions();
        //            };
        //
        //        }

        //        def reduceButton = SwingButton{
        //            text: "reduce Text";
        //            translateX: 300;
        //            translateY: 300;
        //            onMouseClicked: function(evt: MouseEvent):Void {
        //                for (contactFrame in contactWindow.visibleContacts) {
        //                    if (contactFrame.size != WindowSize.SMALL){
        //                        contactFrame.reduce();
        //                    }
        //                }
        //                contactWindow.actualizePositions();
        //            };
        //        }

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