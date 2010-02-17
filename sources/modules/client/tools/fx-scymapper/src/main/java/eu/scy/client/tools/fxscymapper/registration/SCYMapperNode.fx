/*
 * SCYMapperNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.client.tools.fxscymapper.registration;

import javafx.ext.swing.SwingComponent;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.control.Button;
import eu.scy.collaboration.api.CollaborationStartable;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;

import java.awt.Dimension;

import eu.scy.scymapper.impl.SCYMapperPanel;

import roolo.elo.api.IELO;

import org.apache.log4j.Logger;

import javax.swing.JOptionPane;

import org.springframework.util.StringUtils;

import java.net.URI;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import java.lang.System;


public class SCYMapperNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack, CollaborationStartable {

    public-init var scyMapperPanel:SCYMapperPanel;
    public-init var repositoryWrapper:ScyMapperRepositoryWrapper;
    public-init var currentELO:IELO;

    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};

    public var syncSession:ISyncSession;

    public var scyWindow:ScyWindow on replace {
        setScyWindowTitle()
    }

    var logger = Logger.getLogger(ScyMapperRepositoryWrapper.class.getName());

    var wrappedScyMapperPanel:SwingComponent;

    def spacing = 5.0;


    function setScyWindowTitle(){

    };

    public override function create(): Node {
      wrappedScyMapperPanel = SwingComponent.wrap(scyMapperPanel);
      return Group {
         blocksMouse:true;
         content: [
            VBox{
               translateY:spacing;
               spacing:spacing;
               content:[
                  HBox{
                     translateX:spacing;
                     spacing:spacing;
                     content:[
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

    override function eloSaveCancelled (elo : IELO) : Void {
       println("User cancelled saving of ELO");
    }

    override public function eloSaved (elo : IELO) : Void {
        this.currentELO = elo;
        println("ELO SUCCESSFULLY SAVED");
    }

    function resizeContent(){
        var size = new Dimension(width,height-wrappedScyMapperPanel.boundsInParent.minY-spacing);
        // setPreferredSize is needed
        scyMapperPanel.setPreferredSize(size);
        // setSize is not visual needed
        // but set it, so the component react to it
        scyMapperPanel.setSize(size);
        //println("resized whiteboardPanel to ({width},{height})");
    }

    public override function getPrefHeight(width: Number) : Number{
        return scyMapperPanel.getPreferredSize().getHeight();
    }

    public override function getPrefWidth(width: Number) : Number{
        return scyMapperPanel.getPreferredSize().getWidth();
    }

    public override function loadElo(uri:URI){
      if (uri != null) {
            currentELO = repositoryWrapper.loadELO(uri);
            var conceptMap = repositoryWrapper.getELOConceptMap(currentELO);
            scyMapperPanel.setConceptMap(conceptMap);
        }
   }

   public override function newElo():Void{
        currentELO = repositoryWrapper.createELO();

        var conceptMap = repositoryWrapper.getELOConceptMap(currentELO);
        scyMapperPanel.setConceptMap(conceptMap);
   }
    public override function startCollaboration(mucid:String){
        def datasync:IDataSyncService = scyWindow.scyDesktop.config.getToolBrokerAPI().getDataSyncService();
        syncSession = datasync.joinSession(mucid,SCYMapperSyncListener{});
        println("sync session with mucid {mucid} created.");
    }
}
