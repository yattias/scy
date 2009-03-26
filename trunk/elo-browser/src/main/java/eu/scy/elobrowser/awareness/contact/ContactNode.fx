/*
 * ContactNode.fx
 *
 * Created on 26.03.2009, 10:51:11
 */

package eu.scy.elobrowser.awareness.contact;

import eu.scy.elobrowser.awareness.contact.Contact;
import eu.scy.elobrowser.awareness.contact.ContactFrame;
import eu.scy.elobrowser.awareness.contact.ContactNode;
import eu.scy.elobrowser.awareness.contact.ContactWindow;
import eu.scy.elobrowser.awareness.contact.OnlineState;
import eu.scy.elobrowser.awareness.contact.WindowSize;
import eu.scy.scywindows.ScyWindow;
import java.lang.Object;
import java.lang.System;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Sven
 */

public class ContactNode extends CustomNode {

    public var contacts:ContactFrame[];
    
    def contactWindow: ContactWindow = ContactWindow{
          contacts: bind this.contacts;
    };

	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Contacts";
    	scyWindow.id = "contacts"
	}

   public override function create(): Node {
      return Group {
         blocksMouse:true;
         content: [
            VBox{
               translateY:5;
               spacing:5;
               content:[contactWindow]
            }
         ]
      };
   }

	public function createContactNode():ContactNode{
		return ContactNode{
		}
	}

	public function createContactWindow():ScyWindow{
		return
		createContactWindow(createContactNode());
	}

	public function createContactWindow(contactNode:ContactNode):ScyWindow{
		var contactWindow = ScyWindow{
			color:Color.GREEN
			title:"Drawing"
			scyContent: contactNode
			minimumWidth:320;
			minimumHeight:100;
			cache:true;
      }
        contactWindow.openWindow(340, 250);
		contactNode.scyWindow = contactWindow;
		return contactWindow;


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
                progress: 1.0;
            };

        };

        def contact2 = ContactFrame{
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Another Mission";
                imageURL: "img/Giemza.jpg";
                name: "Adam G";
                onlineState: OnlineState.AWAY;
                progress: 0.1;
            };
            x: 300;

        };

        def contact3 = ContactFrame{
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Testmission";
                imageURL: "img/Weinbrenner.jpg";
                name: "Stefan W.";
                onlineState: OnlineState.ONLINE;
                progress: 0.7;
            };

        };

        def contact4 = ContactFrame{
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Testmission";
                imageURL: "img/Gerling.jpg";
                name: "Philip G.";
                onlineState: OnlineState.ONLINE;
                progress: 0.5;
            };

        };

        def contact5 = ContactFrame{
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Testmission";
                imageURL: "img/Calendar.png";
                name: "Sascha N.";
                onlineState: OnlineState.OFFLINE;
                progress: 0.3;
            };

        };

def testContacts = [contact1,contact5,contact2,contact3,contact4];
    
        def contactNode = ContactNode{contacts: [contact1,contact5,contact2,contact3,contact4];};

     //Contacts for testing only

        Stage {
            title: "Contact"
            width: 820
            height: 620
            visible: true;
            scene: Scene {
            //                content: [contactWindow,expandButton, reduceButton];
                content: [contactNode.createContactWindow(contactNode)];
            }
        }

    }
