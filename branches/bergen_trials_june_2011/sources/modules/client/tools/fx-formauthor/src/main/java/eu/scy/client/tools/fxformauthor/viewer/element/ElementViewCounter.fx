/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer.element;
import javafx.scene.text.Text;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataElement;
import javafx.scene.paint.Color;

/**
 * @author pg
 */

public class ElementViewCounter extends IFormViewElement, AbstractElementView {
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

        title = "Counter: {fde.getTitle()}";
        //display data..
        if(fde.getUsedCardinality() > 0) {
            for(i in [0..fde.getUsedCardinality()-1]) {
                println("load form element.. {i}");
                var data:Byte[] = fde.getStoredData(i);
                if(data != null) {
                    itemList.add(Text {
                                    content: new String(data);
                                    wrappingWidth: bind width-20;
                                    font: bind defaultTextFont; 
                                });
                    //into item;
                    println("data != null.. loading counter");
                }
                else {
                    itemList.add(Text { content: "No Data Found. Entry #{i}"; }); // into item;
                }
                //itemList.add(item);
            }
        }
        if(itemList.size() == 0) {
            itemList.add(Text { font: defaultErrorFont; fill: Color.RED; content: "No Data Found." }); // into item;
        }

    }
}
