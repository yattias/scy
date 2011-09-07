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
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import roolo.search.IQuery;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import java.lang.IllegalArgumentException;

/**
 * @author SikkenJ
 */
public class QuerySelecterDisplay extends CustomNode {

   public-init var querySelecter: QuerySelecter;
   public-init var querySelecterUsage: QuerySelecterUsage;
   public-init var eloIconFactory: EloIconFactory;
   public-init var windowColorScheme: WindowColorScheme;
   public-init var tooltipManager: TooltipManager;
   public-init var selectionChanged: function(): Void;
   public-init var basedOnElo: ScyElo;
   public-init var iconNameFilter: function(String): String;
   public-read var noOptions = false;
   def resourceBundleWrapper = new ResourceBundleWrapper(this);
   def querySelecterChoiceBox = ChoiceBox {};
   def iconSize = 20.0;
   def spacing = 5.0;
   def selectedIndex = bind querySelecterChoiceBox.selectedIndex on replace { newItemSelected() }
   def anyOption = getDisplayString("any");
   def textTooltipHeader = "filter on";
   def eloBasedTooltipHeader = "compare on";
   var created = false;
   var selecterItems: String[];

   function getDisplayString(key: String): String{
      resourceBundleWrapper.getString("{querySelecter.getClass().getSimpleName()}-{key.toLowerCase()}")
   }

   function getEloIcon(): EloIcon {
      var desiredIconName = querySelecter.getEloIconName();
      if (iconNameFilter != null) {
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
      querySelecterChoiceBox.items = getDisplayItems();
      querySelecterChoiceBox.select(0);
      HBox {
         spacing: spacing
         content: [
//            icon,
            querySelecterChoiceBox
         ]
      }
   }

   function getDisplayItems(): String[] {
      var displayItems: String[];
      delete  selecterItems;
      for (selecterOption in querySelecter.getDisplayOptions()) {
         def displayName = getDisplayString(selecterOption);
         insert displayName after displayItems[9999];
         insert selecterOption after selecterItems[9999]
      }
      noOptions = sizeof displayItems == 0;
      insert anyOption before displayItems[0];
      displayItems
   }

   function newItemSelected() {
      if (selectedIndex == 0) {
         querySelecter.setSelectedOption("");
      } else {
         querySelecter.setSelectedOption(selecterItems[selectedIndex - 1]);
      }
      if (created) {
         selectionChanged();
      }
   }

   public function setFilterOptions(query: IQuery) {
      querySelecter.setFilterOptions(query);
   }

   public function getSelectedOption(): String {
      querySelecter.getSelectedOption()
   }

   public function setSelectedOption(option: String): Void {
      def newIndex = getIndexForOption(option);
      querySelecterChoiceBox.select(newIndex);
      querySelecter.setSelectedOption(option);
   }

   function getIndexForOption(option: String): Integer {
      if (option == "") {
         return 0
      }
      for (item in selecterItems) {
         if (item == option) {
            return indexof item + 1
         }
      }

      throw new IllegalArgumentException("option is not in the list {selecterItems}")
   }

}
