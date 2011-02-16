/*
 * ContactFrame.fx
 *
 * Created on 02.12.2009, 13:00:50
 */
package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import javafx.scene.control.ProgressBar;

import javafx.scene.CustomNode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.animation.Timeline;
import javafx.animation.SimpleInterpolator;
import javafx.scene.transform.Scale;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * @author Sven
 */
 public def HOVER_COLOR:Color= Color.LIGHTBLUE;
 public def HOVER_COLOR_OPACITY:Float = 0.3;
 public def ON_MOUSE_PRESSED_COLOR:Color = Color.BLUE;
 public def ON_MOUSE_PRESSED_COLOR_OPACITY:Float = 0.3;
 public def BACKGROUND_COLOR :Color =  Color.TRANSPARENT;

public class ContactFrame extends CustomNode {

    public var contact: Contact;
    public def INITIAL_SIZE:WindowSize = WindowSize.SMALL;
    public-read var size: WindowSize = INITIAL_SIZE on replace {
                updateView();
            };
    public var borderSize = 4;
    public def width = WindowSize.DEFAULT_ITEM_WIDTH;
    public def height = bind imageView.boundsInParent.height + 2*borderSize;

    def image = Image {
                    width: size.getDefaultImageWidth();
                    height: size.getDefaultImageHeight();
                    preserveRatio: false;
                    url: contact.imageURL;
                    backgroundLoading:true;
                    placeholder:Image{
                        width: size.getDefaultImageWidth();
                        height: size.getDefaultImageHeight();
                        preserveRatio: false;
                        url: "{contact.imageURL}&showIcon";
                    }

                }
                
    def imageView: ImageView = ImageView {
                translateX: borderSize;
                translateY: borderSize;
                image: image;
                cache: true;
                transforms: bind [imageScaler]
            };

   def imageScaler:Scale = Scale{
       pivotX:0;
       pivotY:0;
       x:calcScaleX();
       y:calcScaleY();
   }

   //XXX background removed
   public def background = Rectangle{
       width:bind this.width;
       height:bind this.height;
       fill: Color.TRANSPARENT;
   }

    def nameLabel = Text {
                content: bind contact.name;
                font:Font{
                   embolden:true;
                }
                scaleY: 1.0;
                cache: true;
            };

    def stateLabel = Text {
                content: bind contact.onlineState.toString();
                scaleY: 1.0;
                cache: true;
                opacity: (if(size==WindowSize.NORMAL)1 else 0);
                visible: (if(size==WindowSize.NORMAL)true else false);
            };
    def missionLabel = Text {
                content: bind "In Mission: {contact.currentMission}";
                scaleY: 1.0;
                cache: true;
                opacity: (if(size==WindowSize.NORMAL)1 else 0);
                visible: (if(size==WindowSize.NORMAL)true else false);
            };
    //XXX information not available from awareness service
    //def progressBar: ProgressBar = ProgressBar {
    //            progress: bind contact.progress;
    //            width: bind this.width - imageView.boundsInParent.width - 3 * borderSize;
    //            height: 15;
    //            opacity: (if(size==WindowSize.NORMAL)1 else 0);
    //            visible: (if(size==WindowSize.NORMAL)true else false);
    //            onMouseEntered: this.onMouseEntered;
    //        };
    def infoBox = bind VBox {
                translateX: bind 2 * borderSize + size.getImageWidth();
                translateY: borderSize;
                //content: [nameLabel,stateLabel, missionLabel, progressBar]
                //XXX mission-progress not available from Awareness service
                //content: [nameLabel,stateLabel, missionLabel]
                content:[]
            };

    //public var imageSize: Number = 64;

    public function hover() {
        size = WindowSize.HOVER;
    }

    public function expand() {
        size = WindowSize.NORMAL
    }

    public function reduce() {
        size = WindowSize.SMALL;
    }

    override protected function create(): Node {
        Group {
            content: bind [
                //background,
                imageView,
                infoBox
            ];
        }
    }

    public function calcScaleX():Float{
        return (size.getImageWidth() as Float) / (image.width as Float);
    }

    public function calcScaleY():Float{
        return (size.getImageHeight() as Float) / (image.height as Float);
    }

    def boldFont:Font = Font.font("", FontWeight.BOLD, FontPosture.REGULAR, 20);


    def normalFont:Font = Font{

    }


    public function updateView() {
            if (image.width!=0){
                Timeline {
                    keyFrames: [
                        at(0.05s){
                            //progressBar.opacity => (if(size==WindowSize.NORMAL) 1 else 0) tween SimpleInterpolator.LINEAR;
                            //progressBar.visible => (if(size==WindowSize.NORMAL) true else false) tween SimpleInterpolator.LINEAR;
                            missionLabel.opacity => (if(size==WindowSize.NORMAL) 1 else 0) tween SimpleInterpolator.LINEAR;
                            missionLabel.visible => (if(size==WindowSize.NORMAL) true else false) tween SimpleInterpolator.LINEAR;
                            stateLabel.opacity => (if(size==WindowSize.NORMAL) 1 else 0) tween SimpleInterpolator.LINEAR;
                            stateLabel.visible => (if(size==WindowSize.NORMAL) true else false) tween SimpleInterpolator.LINEAR;
                            nameLabel.font => (if(size==WindowSize.NORMAL or size == WindowSize.HOVER) boldFont else normalFont) tween SimpleInterpolator.LINEAR;
                            imageScaler.x => calcScaleX() tween SimpleInterpolator.LINEAR;
                            imageScaler.y => calcScaleY() tween SimpleInterpolator.LINEAR;
                        }
                        //at(0.1s){
                        //    imageScaler.x => calcScaleX() tween SimpleInterpolator.LINEAR;
                        //    imageScaler.y => calcScaleY() tween SimpleInterpolator.LINEAR;
                        //}
                     ]
                    }.play();
            }
    }

    override var onMouseEntered = function(e:MouseEvent){
        background.fill = HOVER_COLOR;
        background.opacity = HOVER_COLOR_OPACITY;
        hover();
        //FIXME scroll down if the expansion of the contact frame will be outside of clipping area
    }

    public function unmarkContact(){
        background.fill = Color.TRANSPARENT;
        background.opacity = 1;
        reduce();
    }


    override var onMouseExited = function(e:MouseEvent){
            unmarkContact();
    }

 
    init {
        size = INITIAL_SIZE;
        //progressBar.onMouseEntered =  onMouseEntered;
    }
}