/*
 * ChatNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.client.tools.fxchattool.registration;

import java.net.URI;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import javafx.scene.CustomNode;



/**
 * @author jeremyt
 */

 // place your code here
public class ProgressNode extends CustomNode {


       var imageProgress = ImageView {
            image: Image {
                url: "{__DIR__}progress.png";
                //url: "http://www.gearlive.com/blogimages/goo_goo_onesie.jpg";
            }
        }


    public-init var eloChatActionWrapper:EloChatActionWrapper;
    public var scyWindow:ScyWindow on replace {
        setScyWindowTitle()
    };

    public function loadElo(uri:URI){
        eloChatActionWrapper.loadElo(uri);
        setScyWindowTitle();
    }

    function setScyWindowTitle(){
        if (scyWindow == null) {
            return;
        }

        scyWindow.title = "Chat: {eloChatActionWrapper.getDocName()}";
        var eloUri = eloChatActionWrapper.getEloUri();
        if (eloUri != null) {
            scyWindow.id = eloUri.toString();
        }
        else {
            scyWindow.id = "";
        }
    };



   public override function create(): Node {
     return Group {
         blocksMouse:true;
         content: [
              imageProgress
             ]
      };
   }
}
