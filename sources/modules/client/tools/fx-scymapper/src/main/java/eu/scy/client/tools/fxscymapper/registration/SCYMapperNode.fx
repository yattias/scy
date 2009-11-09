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

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.control.Button;

import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;

import java.awt.Dimension;

import eu.scy.scymapper.impl.SCYMapperPanel;

import roolo.elo.api.IELO;

import org.apache.log4j.Logger;

import javax.swing.JOptionPane;

import org.springframework.util.StringUtils;

import java.net.URI;


public class SCYMapperNode extends CustomNode, Resizable {

    public-init var scyMapperPanel:SCYMapperPanel;
    public-init var repositoryWrapper:ScyMapperRepositoryWrapper;
    public-init var currentELO:IELO;

    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};

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
                                loadConceptMap();
                           }
                        }
                        Button {
                           text: "Save"
                           action: function() {
                                saveConceptMap();
                           }
                        }
                        Button {
                           text: "Save as"
                           action: function() {
                                saveConceptMapAs();
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
            currentELO = repositoryWrapper.createELO();

            var conceptMap = repositoryWrapper.getELOConceptMap(currentELO);
            scyMapperPanel.setConceptMap(conceptMap);
        }


    }
    function saveConceptMap() {

        if (currentELO.getUri() == null) {
            saveConceptMapAs();
        }
        else {
            var conceptMap = scyMapperPanel.getConceptMap();
            repositoryWrapper.setELOConceptMap(currentELO, conceptMap);
            repositoryWrapper.updateELO(currentELO);
            JOptionPane.showMessageDialog(null, "ELO successfully saved", "ELO saved", JOptionPane.PLAIN_MESSAGE);
        }

    }

    function loadConceptMap() {

        var uris = repositoryWrapper.findElos();
        var selectedUri = JOptionPane.showInputDialog(null, "Select concept map", "Select concept map", JOptionPane.QUESTION_MESSAGE, null, uris, null);
        if (selectedUri != null) {
            currentELO = repositoryWrapper.loadELO(selectedUri as URI);
            var conceptMap = repositoryWrapper.getELOConceptMap(currentELO);
            scyMapperPanel.setConceptMap(conceptMap);
        }
    }

    function saveConceptMapAs() {
        var conceptMap = scyMapperPanel.getConceptMap();

        var name = "";
        while (StringUtils.hasText(name) == false) {
            name = JOptionPane.showInputDialog("Enter name:", conceptMap.getName());
        }
        conceptMap.setName(name);

        repositoryWrapper.setELOConceptMap(currentELO, conceptMap);
        repositoryWrapper.saveELO(currentELO);
        JOptionPane.showMessageDialog(null, "ELO successfully saved as {conceptMap.getName()}", "Saved", JOptionPane.PLAIN_MESSAGE);
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

}
