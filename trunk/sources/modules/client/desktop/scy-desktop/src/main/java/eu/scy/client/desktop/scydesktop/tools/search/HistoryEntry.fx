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

   public override function toString(): String {
      "{query} ({nrOfResults} results)"
   }

}
