/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer.element;
import javafx.scene.text.Text;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataElement;
import javafx.ext.swing.SwingUtils;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.LayoutInfo;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * @author pg
 */

public class ElementViewImage extends IFormViewElement, AbstractElementView {
    postinit {
        //loadFormElement();
    }
    override public function loadFormElement (fde : FormDataElement) : Void {
        if(fde != null) {
            this.fde = fde;
            loadFormElement();
        }
    }


    override function loadFormElement():Void {
        title = "Image: {fde.getTitle()}";
        var imageContent:VBox = VBox{};
        var imageHBox : HBox = HBox {spacing: 5.0};
        //display data..
        if(fde.getUsedCardinality() > 0) {
            var lasti:Number = 0;
            for(i in [0..fde.getUsedCardinality()-1]) {
                var item:Node;
                println("load form element.. {i}");
                var data:Byte[] = fde.getStoredData(i);
                if(data != null) {
                var img:BufferedImage = ImageIO.read(new ByteArrayInputStream(data));
                var image:Image = SwingUtils.toFXImage(img);
                item = ImageView {
                                    fitHeight: 200;
                                    fitWidth: 450;
                                    image: image;
                                    preserveRatio: true;
                                    onMouseReleased: function(e:MouseEvent):Void {
                                        putImageToFront(image); 
                                    }
                                    layoutInfo: LayoutInfo {
                                        margin: Insets {
                                            bottom: 5;
                                            top: 5;
                                        }
                                    }
                                }
                    //into imageHBox.content;
                    itemList.add(item);
                    println("data != null.. loading image");
                    if((i mod 5) == 4) {
                        insert imageHBox into imageContent.content;
                        imageHBox = HBox {spacing: 5.0};
                    }
                }
                else {
                    item = Text { content: "No Data Found. Entry #{i}"; }; // into item;
                    itemList.add(item);
                }
                lasti = i;
            }
            /*
            if((lasti mod 5) != 4) {
                insert imageHBox into imageContent.content;
            }
            insert imageContent into dataDisplay;
            */
        }
        if(itemList.size() == 0) {
            itemList.add(Text { font: defaultErrorFont; fill: Color.RED; content: "No Data Found." });
        }
    }
}
