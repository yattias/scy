/*
 * CollaborationResponseAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

 import java.lang.String;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import javafx.scene.text.Text;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import eu.scy.client.desktop.scydesktop.art.ImageLoader;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;

/**
 * @author sven
 */
public class MessageDialogShowCommand extends ScyDesktopRemoteCommand {

    def DEFAULT_WIDTH: Double = 300;
    def HGAP: Double = 10;

    override public function getActionName(): String {
        "message_dialog_show"
    }
    
    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************message dialog show action*********************");
        def modalProperty = notification.getFirstProperty("modal");
        def modal: Boolean = if(not (modalProperty==null)) Boolean.parseBoolean(modalProperty) else true;
        def text: String = notification.getFirstProperty("text");
        logger.debug("modal: {modal}");
        def dialogWidth: Double = (if (notification.getFirstProperty("width") == null) DEFAULT_WIDTH else Math.min(scyDesktop.scene.width, Double.valueOf(notification.getFirstProperty("width"))));
        def imageLoader = ImageLoader.getImageLoader();
        def indicatorImage = ImageView {
                    image: imageLoader.getImage("info_red.png");
                }
        def dialogBoxContent: Group = Group {
                    content: [
                        HBox {
                            spacing: HGAP
                            content: [
                                indicatorImage,
                            VBox { content: [
                            Text { content: text,
                            wrappingWidth: (dialogWidth - 3 * HGAP - indicatorImage.layoutBounds.width) }
                            Button {
                            action: function () {
                            dialogBox.close()
                            }
                            text: "buttonText"
                            }
                            ]
                            }
                            ] }
                    ]
                }
        def dialogBox: DialogBox = DialogBox {
                    content: dialogBoxContent
                    targetScene: scyDesktop.scene
                    title: "ModalBox title"
                    modal: modal
                    scyDesktop:scyDesktop
                    closeAction: function () {

                    }
                    windowColorScheme: WindowColorScheme.getWindowColorScheme(EloImageInformation.getScyColors("general/new"));
                };
    }

}
