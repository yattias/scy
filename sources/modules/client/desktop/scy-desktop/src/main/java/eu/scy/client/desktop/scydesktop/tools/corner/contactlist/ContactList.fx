/*
 * ContactList.fx
 *
 * Created on 09.11.2009, 13:39:03
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import javafx.scene.CustomNode;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.Node;

import javafx.scene.Group;

/**
 * @author Sven
 */

public class ContactList extends CustomNode{

    override public function create():Node{
        Group{};
    }

}

function run(){

    Stage {
      title: "Test Contactlist"
      scene: Scene {
         width: 200
         height: 200
         content: [
         ]
      }
   }
}

