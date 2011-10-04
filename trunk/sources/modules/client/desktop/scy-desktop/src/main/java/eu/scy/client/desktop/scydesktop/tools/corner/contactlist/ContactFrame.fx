/*
 * ContactFrame.fx
 *
 * Created on 02.12.2009, 13:00:50
 */
package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;


import javafx.scene.CustomNode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.transform.Scale;
import javafx.scene.paint.Color;

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
    public-read var size: WindowSize = INITIAL_SIZE;

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
        imageView
    }

    public function calcScaleX():Float{
        return (size.getImageWidth() as Float) / (image.width as Float);
    }

    public function calcScaleY():Float{
        return (size.getImageHeight() as Float) / (image.height as Float);
    }

    init {
        size = INITIAL_SIZE;
    }

    override public function toString():String{
        return "ContactFrame[{contact}]";
    }
}