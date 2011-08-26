/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.impl.TextTooltip;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import roolo.search.IQueryComponent;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.desktoputils.art.EloIcon;

/**
 * @author SikkenJ
 */
public class QuerySelecterDisplay extends CustomNode, TooltipCreator {

   public-init var querySelecter: QuerySelecter;
   public-init var querySelecterUsage: QuerySelecterUsage;
   public-init var eloIconFactory: EloIconFactory;
   public-init var windowColorScheme: WindowColorScheme;
   public-init var tooltipManager: TooltipManager;
   public-init var selectionChanged: function(): Void;
   public-init var basedOnElo: ScyElo;
   public-init var iconNameFilter: function(String):String;
   public-read var noOptions = false;
   def querySelecterChoiceBox = ChoiceBox {};
   def iconSize = 20.0;
   def spacing = 5.0;
   def selectedItem = bind querySelecterChoiceBox.selectedItem as String on replace { newItemSelected() }
   def textNoneOption = "All";
   def eloBasedNoneOption = "Ignore";
   def noneOption = getNoneOption();
   def textTooltipHeader = "filter on";
   def eloBasedTooltipHeader = "compare on";
   def tooltipHeader = getTooltipHeader();
   var created = false;

   function getNoneOption():String{
      if (QuerySelecterUsage.TEXT==querySelecterUsage){
         textNoneOption
      } else if (QuerySelecterUsage.ELO_BASED==querySelecterUsage){
         eloBasedNoneOption
      } else {
         ""
      }
   }

   function getTooltipHeader():String{
      if (QuerySelecterUsage.TEXT==querySelecterUsage){
         textTooltipHeader
      } else if (QuerySelecterUsage.ELO_BASED==querySelecterUsage){
         eloBasedTooltipHeader
      } else {
         ""
      }
   }

   function getEloIcon(): EloIcon{
      var desiredIconName = querySelecter.getEloIconName();
      if (iconNameFilter!=null){
         desiredIconName = iconNameFilter(desiredIconName)
      }
      return eloIconFactory.createEloIcon(desiredIconName);
   }

   public override function create(): Node {
      FX.deferAction(function(): Void {
         created = true
      });
      querySelecter.setBasedOnElo(basedOnElo);
      def icon = getEloIcon();
      icon.size = iconSize;
      icon.windowColorScheme = windowColorScheme;
      tooltipManager.registerNode(icon, this);
      var displayItems = querySelecter.getDisplayOptions().toArray() as String[];
      noOptions = sizeof displayItems==0;
      insert noneOption before displayItems[0];
      querySelecterChoiceBox.items = displayItems;
      querySelecterChoiceBox.select(0);
      HBox {
         spacing: spacing
         content: [
            icon,
            querySelecterChoiceBox
         ]
      }
   }

   function newItemSelected() {
      if (selectedItem == noneOption) {
         querySelecter.setSelectedOption("");
      } else {
         querySelecter.setSelectedOption(selectedItem);
      }
      if (created) {
         selectionChanged();
      }
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      TextTooltip {
         content: "{tooltipHeader} {querySelecter.getEloIconTooltip()}"
         windowColorScheme: windowColorScheme
      }
   }

   public function getQueryComponent(): IQueryComponent {
      if (selectedItem == noneOption) {
         return null;
      } else {
         return querySelecter.getQueryComponent()
      }
   }

}
