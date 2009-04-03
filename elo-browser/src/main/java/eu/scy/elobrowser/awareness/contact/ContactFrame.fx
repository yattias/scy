/*
 * ContactWindow.fx
 *
 * Created on 22.03.2009, 19:22:25
 */

package eu.scy.elobrowser.awareness.contact;

import java.lang.Object;
import javafx.animation.SimpleInterpolator;
import javafx.animation.Timeline;
import javafx.ext.swing.SwingButton;
import javafx.scene.CustomNode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextOrigin;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import eu.scy.elobrowser.awareness.contact.Contact;
import eu.scy.elobrowser.awareness.contact.ContactFrame;
import eu.scy.elobrowser.awareness.contact.OnlineState;
import eu.scy.elobrowser.awareness.contact.WindowSize;

/**
 * @author Sven
 */

public class ContactFrame extends CustomNode{

    public var contact: Contact;
    public var isChatting:Boolean = false;
    public var size: WindowSize;
    public var imageSize: Number = 64;
    //    def width =  bind calculateFrameWidth;
    //    def height = bind calculateFrameHeight;
    public var x: Number;
    public var y: Number;
    public def height: Number = bind size.getHeight();
    public def width: Number = bind size.getWidth();
    public var lastClick: Long = 0;


    //Declare font-style
    public def font: Font = Font{

    };

    public function getOpacity():Number{
        if (contact.onlineState == OnlineState.AWAY) {
            return 0.5;
        }
                else {
            return 1.0;
        }
    };


    def progressBarBorder: Rectangle = Rectangle{
        x: bind missionLabel.x;
        y: bind missionLabel.y + 10;
        width: size.getWidth() * 0.5;
        height: 15;
        fill: Color.TRANSPARENT;
        stroke: Color.BLACK;
        cache:true;
    }

    def progressBarFill: Rectangle = Rectangle{
        //        fill: Color.BLACK;
        fill: LinearGradient{
            proportional: true;
            stops: [
                Stop{offset:0.0, color: Color.BLACK;
                },
                Stop{offset:1.0,color: Color.DARKGRAY;
                }];
        };

        stroke: Color.TRANSPARENT;
        x: bind progressBarBorder.x;
        y: bind progressBarBorder.y;
        width: bind progressBarBorder.width * contact.progress;
        height: bind progressBarBorder.height;
        effect: Lighting{
            
        }
        cache:true;

    };

    def progressLabel: Text = Text{
        content: "{(contact.progress * 100)}%";
        fill: Color.GRAY;
        effect: DropShadow{ color:Color.BLACK;
        };
        x: bind progressBarBorder.x;
        y: bind progressBarBorder.y;
        //        x: bind progressBarBorder.x + (progressBarBorder.width / 2) ;
        //        y: bind progressBarBorder.y + progressBarBorder.height / 2 + 5;
        textAlignment: TextAlignment.CENTER;
        textOrigin: TextOrigin.TOP;
        translateX: bind (progressBarBorder.width - progressLabel.boundsInLocal.width) / 2 + 12;
        translateY: bind (progressBarBorder.height - progressLabel.boundsInLocal.height) / 2 + 12;
        cache:true;
    };

    var scaleProgress: Scale = Scale{
        pivotX: progressBarBorder.x;
        pivotY: progressBarBorder.y;
        x: 0.0;
        y: 0.0
    };

    public var progressBar: Group = Group{
        transforms: bind  [scaleProgress];
        content: [progressBarBorder,progressBarFill,progressLabel]
        
    };

    public var frame: Rectangle=Rectangle{
        x: bind this.x;
        y: bind this.y;
        fill: Color.TRANSPARENT;
        stroke: Color.TRANSPARENT;
        width: 80;
        height: 80;
//        cache:true;
    };

    public def image = ImageView{
        x: bind frame.x + ((frame.width - imageSize) / 3) + 4;
        y: bind frame.y + 4;
//        effect: DropShadow{
//            color: Color.BLACK;
//            radius: 10;
//        };
        image: Image{

            width: 64;
            height: 64;
            preserveRatio: false;
            url: "{__DIR__}{contact.imageURL}";
        };
        cache: true;
    };

    public function selectStateImage():String{
        if (contact.onlineState == OnlineState.AWAY){
            return "img/bulb_orange.png";
        } else if (contact.onlineState == OnlineState.ONLINE){
            return "img/bulb_green.png";
        }
        else {
            return "";
        }
        ;
    };
 
 
    public def stateImage = ImageView{
        x: bind image.x;
        y: bind image.y;
        opacity: 0.0;
        image: Image{
            width: 16;
            preserveRatio: true;
            url: "{__DIR__}{selectStateImage()}";
//            url: "{__DIR__}bulb_green_x64.png";

        };
        cache: true;
    };

    def nameLabel = Text{
        content: bind contact.name;
        x: bind image.x;
        y: bind image.y + image.image.height + 15;
        scaleY: 0.0;
        cache:true;
    };



    def stateLabel = Text{
        content: bind contact.onlineState.toString();
        x: bind image.x;
        y: bind nameLabel.y + 15;
        scaleY: 0.0;
        cache:true;
    };

    def missionLabel = Text{
        content: bind "In Mission: {contact.currentMission}";
        x: bind image.x;
        y: bind stateLabel.y + 15;
        scaleY: 0.0;
        cache:true;
    };

    

    init{
    
        size= WindowSize.SMALL;
        frame.onMouseEntered = function(evt:MouseEvent):Void{
            frame.stroke = Color.BLACK;  
            frame.strokeDashArray = [3.0,3.0];
            def color = Color{
                opacity:0.3;
            }
            frame.fill = color.WHITE;
            
//            frame.effect = Lighting{
//                light: DistantLight { azimuth: -135
//                }
//                surfaceScale: 5
//            }

        };

        frame.onMouseExited = function(evt:MouseEvent):Void{
            frame.stroke = Color.TRANSPARENT;
            frame.strokeDashArray = [1.0];
            frame.fill = Color.TRANSPARENT;
            frame.effect = null;
        };


//        frame.onMouseClicked = function(evt:MouseEvent):Void{
//
//            if (evt.button == MouseButton.PRIMARY){
//                if ((System.currentTimeMillis() - lastClick) < 400 and (System.currentTimeMillis() - lastClick) > 1){
//                    //DoubleClick
//                    var messageWindow = MessageWindow{
//                        //FIXME replace with variable username;
//                        sender: "Sven Manske";
//                        receiver: this.contact.name;
//                        opacity: 0.0;
//                    }
//
//                    messageWindow.visible = true;
//                    Timeline {
//                        keyFrames: [at (0.2s){messageWindow.opacity => 1.0 tween SimpleInterpolator.LINEAR}];
//                    }.play();
//
//                    System.out.println("open Messagewindow");
//
//
//
//                    //Reset timer after double click performed, otherwise 3 Clicks = 2 Doubleclicks
//                    lastClick = 0;
//                }
//                else {
//                    lastClick = System.currentTimeMillis();
//                }
//            };
//        };

    };


    override public function create():Node{
        return Group{content: [frame,image,nameLabel,stateLabel,missionLabel,progressBar,stateImage];
            
        };
    };

    public function expand():Void{
        size=WindowSize.NORMAL;
        Timeline{
            keyFrames: [at(0s){progressBar.visible => true tween SimpleInterpolator.LINEAR},
at (0.3s){nameLabel.scaleY => 1.0 tween SimpleInterpolator.LINEAR; missionLabel.scaleY => 1.0 tween SimpleInterpolator.LINEAR; stateLabel.scaleY => 1.0 tween SimpleInterpolator.LINEAR; frame.height =>
                size.getHeight() tween SimpleInterpolator.LINEAR;frame.width =>
                size.getWidth() tween SimpleInterpolator.LINEAR; scaleProgress.x => 1.0 tween SimpleInterpolator.LINEAR;scaleProgress.y => 1.0 tween SimpleInterpolator.LINEAR;progressBar.scaleY => 1.0 tween SimpleInterpolator.LINEAR; stateImage.opacity =>1.0 tween SimpleInterpolator.LINEAR; image.opacity =>1.0 tween SimpleInterpolator.LINEAR}];
        }.play();
    }

    public function expandName():Void{
        size=WindowSize.HOVER;
        Timeline{
            keyFrames: [at (0.3s){nameLabel.scaleY => 1.0 tween SimpleInterpolator.LINEAR; frame.height => size.getHeight() tween SimpleInterpolator.LINEAR;frame.width =>size.getWidth() tween SimpleInterpolator.LINEAR;scaleProgress.y => 0.0 tween SimpleInterpolator.LINEAR;progressBar.scaleY => 0.0 tween SimpleInterpolator.LINEAR; stateImage.opacity =>1.0 tween SimpleInterpolator.LINEAR; image.opacity =>1.0 tween SimpleInterpolator.LINEAR; progressBar.visible => false tween SimpleInterpolator.LINEAR; progressBar.visible => false tween SimpleInterpolator.LINEAR}];
        }.play();
    }

    public function reduce():Void{
        size=WindowSize.SMALL;
        Timeline{
            keyFrames: [at (0.3s){nameLabel.scaleY => 0.0 tween SimpleInterpolator.LINEAR; missionLabel.scaleY => 0.0 tween SimpleInterpolator.LINEAR; stateLabel.scaleY => 0.0 tween SimpleInterpolator.LINEAR; frame.height => size.getHeight() tween SimpleInterpolator.LINEAR;frame.width =>size.getWidth() tween SimpleInterpolator.LINEAR; scaleProgress.y => 0.0 tween SimpleInterpolator.LINEAR;progressBar.scaleY => 0.0 tween SimpleInterpolator.LINEAR; stateImage.opacity =>0.0 tween SimpleInterpolator.LINEAR; image.opacity =>1.0 tween SimpleInterpolator.LINEAR; image.opacity => getOpacity() tween SimpleInterpolator.LINEAR; progressBar.visible => false tween SimpleInterpolator.LINEAR}];
        }.play();
    }

    public function reduceToNameOnly():Void{
        size=WindowSize.HOVER;
        Timeline{
            keyFrames: [at (0.3s){nameLabel.scaleY => 1.0 tween SimpleInterpolator.LINEAR; missionLabel.scaleY => 0.0 tween SimpleInterpolator.LINEAR; stateLabel.scaleY => 0.0 tween SimpleInterpolator.LINEAR; frame.height => size.getHeight() tween SimpleInterpolator.LINEAR;frame.width =>size.getWidth() tween SimpleInterpolator.LINEAR; scaleProgress.y => 0.0 tween SimpleInterpolator.LINEAR;progressBar.scaleY => 0.0 tween SimpleInterpolator.LINEAR; stateImage.opacity =>1.0 tween SimpleInterpolator.LINEAR; progressBar.visible => false tween SimpleInterpolator.LINEAR}];
        }.play();
    }
}

function run(__ARGS__ : String[]){

    def contact1 = ContactFrame{
        size: WindowSize.NORMAL;
        contact: Contact{
            currentMission: "Testmission";
            imageURL: "img/Manske.jpg";
            name: "Sven Manske";
            onlineState: OnlineState.ONLINE;
        };

    };

    def contact2 = ContactFrame{
        size: WindowSize.NORMAL;
        contact: Contact{
            currentMission: "Another Mission";
            imageURL: "img/Giemza.jpg";
            name: "Adam G";
            onlineState: OnlineState.AWAY;
        };

    };

    def expandButton = SwingButton{
        text: "expand Text";
        translateX: 200;
        translateY: 200;
        onMouseClicked: function(evt: MouseEvent):Void {
            contact1.expand();
        };

    }

    def reduceButton = SwingButton{
        text: "reduce Text";
        translateX: 300;
        translateY: 200;
        onMouseClicked: function(evt: MouseEvent):Void {
            contact1.reduce();
        };
    }

    Stage {
        title: "Contact"
        width: 620
        height: 420
        visible: true;
        scene: Scene {
            content: [contact1,expandButton,reduceButton];
        }
    }
}