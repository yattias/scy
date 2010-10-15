/*
 * SCYMapperNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */
package eu.scy.client.tools.fxscymapper.registration;

import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.control.Button;
import eu.scy.collaboration.api.CollaborationStartable;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import eu.scy.scymapper.impl.SCYMapperPanel;
import roolo.elo.api.IELO;
import org.apache.log4j.Logger;
import javax.swing.JOptionPane;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import javafx.scene.layout.Container;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.awt.Dimension;

public class SCYMapperNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack, CollaborationStartable {


    var Dimension;
    public-init var scyMapperPanel: SCYMapperPanel;
    public-init var repositoryWrapper: ScyMapperRepositoryWrapper;
    public-init var currentELO: IELO;
    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};
    public var syncSession: ISyncSession;
    public var scyWindow: ScyWindow;
    var logger = Logger.getLogger(ScyMapperRepositoryWrapper.class.getName());
    var wrappedScyMapperPanel: Node;
    def spacing = 5.0;

    public override function create(): Node {
        wrappedScyMapperPanel = ScySwingWrapper.wrap(scyMapperPanel);
        return Group {
                    blocksMouse: true;
                    content: [
                        VBox {
                            translateY: spacing;
                            spacing: spacing;
                            content: [
                                HBox {
                                    translateX: spacing;
                                    spacing: spacing;
                                    content: [
                                        /*
                                        Button {
                                        text: "New"
                                        action: function() {
                                        newConceptMap();
                                        }
                                        }
                                        Button {
                                        text: "Load"
                                        action: function() {
                                        var uris = repositoryWrapper.findElos();
                                        var selectedUri = JOptionPane.showInputDialog(null, "Select concept map", "Select concept map", JOptionPane.QUESTION_MESSAGE, null, uris, null);
                                        loadElo(selectedUri as URI);
                                        }
                                        }
                                         */
                                        Button {
                                            text: "Save"
                                            action: function() {
                                                doSaveConceptMap();
                                            }
                                        }
                                        Button {
                                            text: "Save as"
                                            action: function() {
                                                doSaveConceptMapAs();
                                            }
                                        }
                                        Button {
                                            text: "Add to Portfolio"
                                            action: function() {
                                                addToPortfolio();
                                            }
                                        }
                                        /*Button {
                                            text: "test thumbnail"
                                            action: function () {
                                                testThumbnail();
                                            }
                                        }*/
                                    ]
                                }
                                wrappedScyMapperPanel
                            ]
                        }
                    ]
                };
    }

    function newConceptMap() {
        var answer = JOptionPane.showConfirmDialog(null, "This will discard any changes done to the current concept map. Do you want to continue?", "Confirm discard changes", JOptionPane.YES_NO_CANCEL_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            newElo();
        }
    }

    function doSaveConceptMap() {
        if (currentELO.getUri() == null) {
            doSaveConceptMapAs();
            return;
        }
        var conceptMap = scyMapperPanel.getConceptMap();
        repositoryWrapper.setELOConceptMap(currentELO, conceptMap);
        eloSaver.eloUpdate(currentELO, this);
    }

    function doSaveConceptMapAs() {
        var conceptMap = scyMapperPanel.getConceptMap();
        repositoryWrapper.setELOConceptMap(currentELO, conceptMap);
        eloSaver.eloSaveAs(currentELO, this);
    }

    function addToPortfolio() {
    //        (scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).logAddToPortfolio();
    }

    public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
      if (scyMapperPanel != null) {
        return eu.scy.client.desktop.scydesktop.utils.UiUtils.createThumbnail(scyMapperPanel, scyMapperPanel.getSize(), new Dimension(width, height));
      } else {
        return null;
      }
    }
    
    public function testThumbnail(): Void {
        var thumbnail = getThumbnail(64, 64);
        var icon = new ImageIcon(thumbnail);
        JOptionPane.showMessageDialog(null,
            "Look at this!",
            "thumbnail test",
            JOptionPane.INFORMATION_MESSAGE,
            icon);
    }

    public override function canAcceptDrop(object: Object): Boolean {
        println("SCYMAPPER: canAcceptDrop of {object.getClass()}");
        if (object instanceof ContactFrame) {
            return true;
        }
        return false;
    }

    public override function acceptDrop(object: Object): Void {
        println("SCYMapper: acceptDrop of {object.getClass()}");
        var c: ContactFrame = object as ContactFrame;
        println("SCYMapper: acceptDrop user: {c.contact.name}");
        scyWindow.windowManager.scyDesktop.config.getToolBrokerAPI().proposeCollaborationWith("{c.contact.awarenessUser.getJid()}/Smack", scyWindow.eloUri.toString(), scyWindow.mucId);
        println("scyDesktop: {scyWindow.windowManager.scyDesktop}");
    }

    override function eloSaveCancelled(elo: IELO): Void {
        println("User cancelled saving of ELO");
    }

    override public function eloSaved(elo: IELO): Void {
        this.currentELO = elo;
        println("ELO SUCCESSFULLY SAVED");
    }

    function resizeContent() {
        Container.resizeNode(wrappedScyMapperPanel, width, height - wrappedScyMapperPanel.boundsInParent.minY - spacing);
    }

    public override function getPrefHeight(width: Number): Number {
        return Container.getNodePrefHeight(wrappedScyMapperPanel, height) + wrappedScyMapperPanel.boundsInParent.minY + spacing;
    }

    public override function getPrefWidth(width: Number): Number {
        return Container.getNodePrefWidth(wrappedScyMapperPanel, width);
    }

    public override function loadElo(uri: URI) {
        logger.debug("loading{uri.toString()}");
        if (uri != null) {
            currentELO = repositoryWrapper.loadELO(uri);
            var conceptMap = repositoryWrapper.getELOConceptMap(currentELO);
            scyMapperPanel.setConceptMap(conceptMap);
            scyMapperPanel.setEloURI(uri.toString());
        }
    }

    public override function newElo(): Void {
        logger.debug("loading new elo");
        currentELO = repositoryWrapper.createELO();
        var conceptMap = repositoryWrapper.getELOConceptMap(currentELO);
        scyMapperPanel.setConceptMap(conceptMap);
    }

    public override function startCollaboration(mucid: String) {
        FX.deferAction(function(): Void {
            scyMapperPanel.joinSession(mucid);
            logger.debug("joined session, mucid: {mucid}");
        });
    }

}
