/*
 * Main.fx
 *
 * Created on 02.12.2009, 13:12:56
 */
package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
 
/**
 * @author Sven
 */
function run() {

    def contact1 = Contact {
                currentMission: "Testmission";
                imageURL: "img/buddyicon.png";
                name: "Sven Manske";
                onlineState: OnlineState.ONLINE;
                progress: 1.0;
            };

    def contact2 = Contact {
                currentMission: "Another Mission";
                imageURL: "img/buddyicon.png";
                name: "Adam G";
                onlineState: OnlineState.AWAY;
                progress: 0.1;
            };

    def contact3 = Contact {
                currentMission: "Testmission";
                imageURL: "img/buddyicon.png";
                name: "Stefan W.";
                onlineState: OnlineState.ONLINE;
                progress: 0.7;
            };
    def contact6 = Contact {
                currentMission: "Testmission";
                imageURL: "img/buddyicon.png";
                name: "Sven Manske";
                onlineState: OnlineState.ONLINE;
                progress: 1.0;
            };

    def contact7 = Contact {
                currentMission: "Another Mission";
                imageURL: "img/buddyicon.png";
                name: "Adam G";
                onlineState: OnlineState.AWAY;
                progress: 0.1;
            };

    def contact8 = Contact {
                currentMission: "Testmission";
                imageURL: "img/buddyicon.png";
                name: "Stefan W.";
                onlineState: OnlineState.ONLINE;
                progress: 0.7;
            };

    def contact4 = Contact {
                currentMission: "Testmission";
                imageURL: "img/buddyicon.png";
                name: "Philip G.";
                onlineState: OnlineState.ONLINE;
                progress: 0.5;
            };

    def contact9 = Contact {
                currentMission: "Testmission";
                imageURL: "img/buddyicon.png";
                name: "Philip G.";
                onlineState: OnlineState.ONLINE;
                progress: 0.5;
            };

    def contact10 = Contact {
                currentMission: "Testmission";
                imageURL: "img/buddyicon.png";
                name: "Philip G.";
                onlineState: OnlineState.ONLINE;
                progress: 0.5;
            };

    def contact11 = Contact {
                currentMission: "Testmission";
                imageURL: "img/buddyicon.png";
                name: "Philip G.";
                onlineState: OnlineState.ONLINE;
                progress: 0.5;
            };

    def contact5 = Contact {
                currentMission: "Testmission";
                imageURL: "img/buddyicon.png";
                name: "Sascha N.";
                onlineState: OnlineState.OFFLINE;
                progress: 0.3;
            };



    def contactContent = [contact1, contact5, contact2, contact3, contact4, contact6, contact7, contact8, contact9, contact10, contact11];

    def expandButton = Button {
                text: "expand Text";
                translateX: 200;
                translateY: 200;
                action: function (): Void {
                    //list.windowSize = WindowSize.NORMAL;
                    for (contactFrame in list.contactFrames) {
                        //contactFrame.expand();
                        contactFrame.expand();
                    }
                };
            }

    def reduceButton = Button {
                text: "reduce Text";
                translateX: 300;
                translateY: 200;
                action: function (): Void {
                    list.contactFrames[0].reduce();
                };
            }

    def list = ContactList {contacts: contactContent}

    Stage {
        title: "Test Contactlist"
        scene: Scene {
            width: 400
            height: 500
            content: [
                list,
                expandButton,
            ]
        }
    }
}
