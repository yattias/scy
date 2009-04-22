/*
 * MetadataDisplayMappingWidget.fx
 *
 * Created on 2-dec-2008, 15:01:34
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.MetadataDisplayMappingWidget;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.model.mapping.editor.MetadataDisplayMappingListPanel;
import eu.scy.elobrowser.model.mapping.MappingElo;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author sikkenj
 */

public class MetadataDisplayMappingWidget extends CustomNode {
   public var roolo:Roolo;

   var metadataDisplayMappingListPanel = new MetadataDisplayMappingListPanel();
   
   function initialize() {
      metadataDisplayMappingListPanel.setMappingEloFactory(roolo.mappingEloFactory);
      metadataDisplayMappingListPanel.setRepository(roolo.repository);
      metadataDisplayMappingListPanel.setMetadataTypeManager(roolo.metadataTypeManager);
      metadataDisplayMappingListPanel.setFormatKey(roolo.typeKey);
      metadataDisplayMappingListPanel.loadMappingElos();
   }
   
   public override function create(): Node {
      initialize();
      return Group 
      {
         content: [
            SwingComponent.wrap(metadataDisplayMappingListPanel)
         ]

      };
   }

   public function getMappingElo():MappingElo {
      metadataDisplayMappingListPanel.getSelectedMappingElo();
   }
}

function run() {
      Stage
{
      title: "MDM list widget"
      width: 170
      height: 300
      onClose: function() {
         java.lang.System.exit( 0 );

      }
      visible: true
      onClose: function()  {
         java.lang.System.exit( 0 );
      }
      visible: true
      var roolo= Roolo.getRoolo();
      var metadataDisplayMappingWidget = MetadataDisplayMappingWidget
   {
         roolo: roolo;
   }

      scene: Scene {
         content: [
            metadataDisplayMappingWidget
         ]

      }
}
}