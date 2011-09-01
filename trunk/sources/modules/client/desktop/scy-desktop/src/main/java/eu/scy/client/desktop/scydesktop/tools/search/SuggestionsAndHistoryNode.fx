/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;

/**
 * @author sikken
 */
public class SuggestionsAndHistoryNode extends CustomNode {

   public var entrySelected : function(:HistoryEntry): Void;
   var suggestions: HistoryEntry[];
   var history: HistoryEntry[];
   def titleOption = "Suggestions and history";
   def suggestionsOption = "Suggestions";
   def historyOption = "History";
   def choiceBox = ChoiceBox {
              items: []
           }
   def selectedItem = bind choiceBox.selectedItem on replace { selectionChanged() };

   public function addHistoryEntry(historyEntry: HistoryEntry): Void{
      insert historyEntry after history[9999];
      delete suggestions;
      rebuildChoices()
   }

   public function setSuggestions(suggestions: HistoryEntry[]): Void{
      this.suggestions = suggestions;
      rebuildChoices();
      if (sizeof suggestions>0){
         choiceBox.show();
      }
   }

   function selectionChanged() {
      println("User selected: {selectedItem}");
      choiceBox.select(0);
   }

   public override function create(): Node {
      rebuildChoices();
      choiceBox
   }

   function rebuildChoices() {
      var anythingToSelect = false;
      var choices = [titleOption,suggestionsOption] as Object[];
      for (suggestion in suggestions){
         insert suggestion after choices[99999];
         anythingToSelect = true;
      }
      insert "" after choices[99999];
      insert historyOption after choices[99999];
      for (hist in history){
         insert hist after choices[99999];
         anythingToSelect = true;
      }
      choiceBox.items = choices;
      choiceBox.select(0);
      choiceBox.disable = not anythingToSelect;
   }

}
