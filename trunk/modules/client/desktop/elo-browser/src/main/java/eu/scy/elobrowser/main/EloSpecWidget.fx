/*
 * EloSpecWidget.fx
 *
 * Created on 4-sep-2008, 15:22:34
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.EloSpecWidget;
import eu.scy.elobrowser.main.Roolo;
import java.lang.*;
import javafx.ext.swing.*;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.stage.Stage;
import org.springframework.util.StringUtils;
import roolo.api.search.IQuery;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.elo.api.IMetadataKey;

/**
 * @author sikkenj
 */
public class EloSpecWidget extends CustomNode {
   public var roolo:Roolo;
   public var title = "Title";
   public var key:IMetadataKey= bind keySelector.selectedItem.value as IMetadataKey;
   public var operation:String= bind operationSelector.selectedItem.value as String;
   public var value1:String = bind value1Text.text;
   public var value2:String = bind value2Text.text;
   
   var titleLabel = SwingLabel{
      text:bind title}
   var keyLabel = SwingLabel{
      text:"Key"}
   var keySelector:SwingComboBox;
   var operationLabel = SwingLabel{
      text:"Operation"}
   var operationSelector:SwingComboBox;
   var value1Label = SwingLabel{
      text:"Value 1"}
   var value1Text = SwingTextField{}
   var value2Label = SwingLabel{
      text:"Value 2"}
   var value2Text = SwingTextField{}
   
   function initializeSelectors() {
      keySelector = SwingComboBox
       {
         items: for (key in roolo.getKeys())  {
                    SwingComboBoxItem
	      {
                    text: key.getId();
                    value: key;
                 }
              }
         editable: false;
         text: "Select key";
         selectedIndex:0;
      }
      operationSelector = SwingComboBox
       {
         items: for (operation in roolo.getSearchOperations())  {
                    SwingComboBoxItem
	      {
                    text: operation;
                    value: operation;
                 }
              }
         editable: false;
         text: "Select operation";
         selectedIndex:0;
      }
   }
   
   public function getSearchQuery():IQuery {
      var value1Object = getValueObject(value1);
      var value2Object = getValueObject(value2);
      return new BasicMetadataQuery(key,operation,value1Object,value2Object);
   }
   
   function getValueObject(value:String):Object {
      var valueObject:Object = null;
      if (StringUtils.hasText(value))
      {
         valueObject = key.displayToValue(value,null);
      }
      return valueObject;
   }

   public override function create(): Node {
      initializeSelectors();
      var yOffset = 0;
      var yStep = 30;
      def xOffset = 70;
      def inputWidth = Math.max(keySelector.width,operationSelector.width);
      keySelector.width = inputWidth;
      operationSelector.width = inputWidth;
      value1Text.width = inputWidth;
      value2Text.width = inputWidth;
      keySelector.translateX=xOffset;
      operationSelector.translateX=xOffset;
      value1Text.translateX=xOffset;
      value2Text.translateX=xOffset;
      yOffset = -yStep;
      return Group
      {
         translateX:5;
         translateY:5;
         content: [
            Group{
               translateY:yOffset + 1 * yStep;
               content:[keyLabel,keySelector]},
            Group{
               translateY:yOffset + 2 * yStep;
               content:[operationLabel,operationSelector]},
            Group{
               translateY:yOffset + 3 * yStep;
               content:[value1Label,value1Text]},
            Group{
               translateY:yOffset + 4 * yStep;
               content:[value2Label,value2Text]},
         ]

      };
   }
//   public override function create(): Node {
//      initializeSelectors();
//      return Group
//      {
//         content: [
//            VBox{
//               content: [
//                 titleLabel,
//                  HBox{
//                     content:[keyLabel,keySelector]},
//                  HBox{
//                     content:[operationLabel,operationSelector]},
//                  HBox{
//                     content:[value1Label,value1Text]},
//                  HBox{
//                     content:[value2Label,value2Text]},
//               ]
//       }
//         ]
//
//      };
}


function run() {

   Stage
{
      title: "Elo spec widget tester"
      width: 200
      height: 200
      onClose: function()  {
         java.lang.System.exit( 0 );
      }
      visible: true
      var roolo= Roolo.getRoolo();
      var eloSpecWidget = EloSpecWidget
   {
         roolo: roolo;
   }

      scene: Scene {
         content: [
	  eloSpecWidget,
            Text {
               x: 10,
               y: 170
               content: bind eloSpecWidget.key.getId();
            },
            Text {
               x: 10,
               y: 190
               content: bind eloSpecWidget.operation;
            },
            Text {
               x: 10,
               y: 210
               content: bind eloSpecWidget.value1;
            },
            Text {
               x: 10,
               y: 230
               content: bind eloSpecWidget.value2;
            }
         ]

      }
   }
}
