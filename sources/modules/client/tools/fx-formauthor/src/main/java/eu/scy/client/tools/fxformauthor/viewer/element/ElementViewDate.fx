/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer.element;
import javafx.scene.text.Text;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataElement;

/**
 * @author pg
 */

public class ElementViewDate extends IFormViewElement, AbstractElementView {
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
                    insert Text {
                                    content: new String(data);
                                    wrappingWidth: bind width-20;
                                }
                    into dataDisplay;
                    println("data != null.. loading DATE");
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
