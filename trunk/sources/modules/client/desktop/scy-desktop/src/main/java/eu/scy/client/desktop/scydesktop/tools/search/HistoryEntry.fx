/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

/**
 * @author sikken
 */
public class HistoryEntry {

   public-init var query: String;
   public-init var nrOfResults: Integer;
   public-init var selecterOptions: String[];
   def resultsString = ##"results";

   public override function toString(): String {
      "{query} ({nrOfResults} {resultsString})"
   }

   public override function equals(object: Object): Boolean{
      if (object instanceof HistoryEntry){
         def otherHistoryEntry = object as HistoryEntry;
         if (query!=otherHistoryEntry.query){
            return false
         }
         if (selecterOptions!=otherHistoryEntry.selecterOptions){
            return false
         }
         return true
      }
      return false
   }


}
