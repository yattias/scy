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

/**
 * @author pg
 */

public class ElementViewImage extends IFormViewElement, AbstractElementView {
    postinit {
        loadFormElement();
    }
    override public function loadFormElement (fde : FormDataElement) : Void {
        if(fde != null) {
            this.fde = fde;
            loadFormElement();
        }
    }


    function loadFormElement():Void {

        title = fde.getTitle();
        //display data..
        if(fde.getUsedCardinality() > 0) {
            for(i in [0..fde.getUsedCardinality()-1]) {
                println("load form element.. {i}");
                var data:Byte[] = fde.getStoredData(i);
                if(data != null) {
                var img:BufferedImage = ImageIO.read(new ByteArrayInputStream(data));
                /*
                target.loadPicture(img); =>
                 viewer.image = SwingUtils.toFXImage(img);
                */
                var image:Image = SwingUtils.toFXImage(img);
                /*
                    insert Text {
                                    content: new String(data);
                                    wrappingWidth: bind width-20;
                                }
                    into dataDisplay;
                */
                insert ImageView {
                                    fitHeight: 100;
                                    fitWidth: 100;
                                    image: image;
                                    preserveRatio: true;
                                    onMouseReleased: function(e:MouseEvent):Void {
                                        putImageToFront(image); 
                                    }
                                    //onmouseover: mauszeiger ändern
                                    layoutInfo: LayoutInfo {
                                        margin: Insets {
                                            bottom: 5;
                                            top: 5;
                                        }


                                    }


                                }
                    into dataDisplay;
                    println("data != null.. loading image");
                }
                else {
                    insert Text { content: "No Data Found. Entry #{i}"; } into dataDisplay;
                }
            }
        }
        if((sizeof dataDisplay) == 0) {
            insert Text { content: "No Data Found." } into dataDisplay;
        }

    }
}
