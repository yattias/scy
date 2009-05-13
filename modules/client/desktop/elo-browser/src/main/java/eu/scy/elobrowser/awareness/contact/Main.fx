/*
* Main.fx
 *
 * Created on 24.02.2009, 14:25:46
 */

package eu.scy.elobrowser.awareness.contact;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Sven
 */

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



        def contactWindow = ContactWindow{
            contacts: [contact1,contact5,contact2,contact3,contact4];
        }

    //Buttons for testing purpose only

        //       def button =  SwingButton {
        //            text: "Change online state";
        //            translateX: 400;
        //            translateY: 20;
        //            action: function() {
        //contact5.contact.onlineState = OnlineState.AWAY;
        //            }
        //        }

        //        def expandButton = SwingButton{
        //            text: "expand Text";
        //            translateX: 300;
        //            translateY: 200;
        //            onMouseClicked: function(evt: MouseEvent):Void {
        //                for (contactFrame in contactWindow.visibleContacts) {
        //                    if (contactFrame.size != WindowSize.NORMAL){
        //                        contactFrame.expand();
        //                    }
        //                }
        //                contactWindow.actualizePositions();
        //            };
        //
        //        }

        //        def reduceButton = SwingButton{
        //            text: "reduce Text";
        //            translateX: 300;
        //            translateY: 300;
        //            onMouseClicked: function(evt: MouseEvent):Void {
        //                for (contactFrame in contactWindow.visibleContacts) {
        //                    if (contactFrame.size != WindowSize.SMALL){
        //                        contactFrame.reduce();
        //                    }
        //                }
        //                contactWindow.actualizePositions();
        //            };
        //        }

        Stage {
            title: "Contact"
            width: 620
            height: 520
            visible: true;
            scene: Scene {
            //                content: [contactWindow,expandButton, reduceButton];
                content: [contactWindow];
            }
        }