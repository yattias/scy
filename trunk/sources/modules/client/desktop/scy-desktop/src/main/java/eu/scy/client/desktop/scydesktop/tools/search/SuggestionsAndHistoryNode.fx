/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.util.Sequences;

/**
 * @author sikken
 */
public class SuggestionsAndHistoryNode extends CustomNode {

   public var entrySelected: function(: HistoryEntry): Void;
   var suggestions: HistoryEntry[];
   var history: HistoryEntry[];
   def titleOption = ##"Suggestions and history";
   def suggestionsOption = ##"Suggestions";
   def historyOption = ##"History";
   def choiceBox = ChoiceBox {
              items: []
           }
   def selectedIndex = bind choiceBox.selectedIndex on replace { selectionChanged() };

   public function addHistoryEntry(historyEntry: HistoryEntry): Void {
      if (Sequences.indexOf(history, historyEntry) < 0) {
         insert historyEntry after history[9999];
         delete  suggestions;
         rebuildChoices()
      }
   }

   public function setSuggestions(suggestions: HistoryEntry[]): Void {
      this.suggestions = suggestions;
      rebuildChoices();
      if (sizeof suggestions > 0) {
         choiceBox.show();
      }
   }

   function selectionChanged() {
      FX.deferAction(function():Void{
            choiceBox.select(0);
         });
      def selectedHistoryEntry = findSelectedHistoryEntry();
      if (selectedHistoryEntry != null) {
         entrySelected(selectedHistoryEntry)
      }
   }

   public override function create(): Node {
      rebuildChoices();
      choiceBox
   }

   function rebuildChoices() {
      var anythingToSelect = false;
      var choices = [titleOption, suggestionsOption] as Object[];
      for (suggestion in suggestions) {
         insert suggestion after choices[99999];
         anythingToSelect = true;
      }
      insert "" after choices[99999];
      insert historyOption after choices[99999];
      for (hist in history) {
         insert hist after choices[99999];
         anythingToSelect = true;
      }
      choiceBox.items = choices;
      choiceBox.select(0);
      choiceBox.disable = not anythingToSelect;
   }

   function findSelectedHistoryEntry(): HistoryEntry {
      var index = selectedIndex - 2;
      if (index >= 0) {
         if (index < sizeof suggestions) {
            return suggestions[index]
         }
         index -= sizeof suggestions + 2;
         if (index >= 0) {
            return history[index]
         }
      }
      return null
   }

}
