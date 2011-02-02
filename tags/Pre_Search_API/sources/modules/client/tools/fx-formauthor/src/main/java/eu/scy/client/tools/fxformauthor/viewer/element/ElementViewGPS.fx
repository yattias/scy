/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer.element;
import javafx.scene.text.Text;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataElement;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * @author pg
 */

public class ElementViewGPS extends IFormViewElement, AbstractElementView  {
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

        title = "GPS: {fde.getTitle()}";
        //display data..
        if(fde.getUsedCardinality() > 0) {
            for(i in [0..fde.getUsedCardinality()-1]) {
                println("load form element.. {i}");
                var data:Byte[] = fde.getStoredData(i);
                if(data != null) {
                    /*
                    insert Text {
                                    content: new String(data);
                                    wrappingWidth: bind width-20;
                                }
                    into dataDisplay;
                    */
                    var gps:Float[] = GPSTypeParser.getCoords(data);
                    var item:HBox = HBox {
                        spacing: 3.0;
                        content: [
                            Text{
                                font: defaultTextFont;
                                content: "GPS Coordinates: {gps[0]},{gps[1]}"
                            },
                            Button {
                                graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/map_magnify.png" } }
                                tooltip: Tooltip { text: "show on Map" }
                                action: function():Void {
                                    showCoordinates(gps[0],gps[1]);
                                }
                            }
                        ]
                    }

                    itemList.add(item);
                    //into dataDisplay;

                    println("data != null.. loading GPS");
                }
                else {
                    itemList.add(Text { content: "No Data Found. Entry #{i}"; }); // into dataDisplay;
                }
            }
        }
        if(itemList.size() == 0) {
            itemList.add(Text { font: defaultErrorFont; fill: Color.RED; content: "No Data Found." }); // into dataDisplay;
        }

    }
}
