/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import eu.scy.common.scyelo.ScyElo;
import javafx.scene.control.Label;
import java.lang.System;

/**
 * @author SikkenJ
 */
public class LastModifiedScyEloCellView extends CustomNode {

   public var scyElo: ScyElo on replace { scyEloChanged() };
   def titleDisplay = Label {};
   def lastModificationDisplay = Label {};
   def spacing = 5.0;
   def minuteWord = ##"minute";
   def minutesWord = ##"minutes";
   def hourWord = ##"hour";
   def hoursWord = ##"hours";
   def dayWord = ##"day";
   def daysWord = ##"days";

   public override function create(): Node {
      HBox {
         spacing: spacing
         content: [
            titleDisplay,
            lastModificationDisplay
         ]
      }
   }

   function scyEloChanged() {
      if (scyElo == null) {
         titleDisplay.text = "";
         lastModificationDisplay.text = "";
      }
      else {
         titleDisplay.text = scyElo.getTitle();
         lastModificationDisplay.text = "({getLastModifiedDisplay()})";
      }
   }

   function getLastModifiedDisplay(): String {
      var lastModified = scyElo.getDateLastModified();
      if (lastModified == null) {
         lastModified = scyElo.getDateCreated();
      }
      if (lastModified == null) {
         return "";
      }
      def millisPast = System.currentTimeMillis() - lastModified;
      def minutesPast = millisPast / (1000 * 60);
      if (minutesPast < 60) {
         return getMinutesDisplay(minutesPast);
      }
      def hoursPast = minutesPast / 60;
      def minutesLeft = minutesPast mod 60;
      if (hoursPast < 24) {
         return getHoursDisplay(hoursPast);
      }
      def daysPast = hoursPast / 24;
      def hoursLeft = hoursPast mod 24;
      return "{getDaysDisplay(daysPast)}"
   }

   function getMinutesDisplay(minutes: Integer): String {
      if (minutes == 1) {
         return "1 {minuteWord}"
      }
      return "{minutes} {minutesWord}"
   }

   function getHoursDisplay(hours: Integer): String {
      if (hours == 1) {
         return "1 {hourWord}"
      }
      return "{hours} {hoursWord}"
   }

   function getHoursMinutesDisplay(hours: Integer, minutes: Integer): String {
      if (hours == 0) {
         return getHoursDisplay(hours);
      }
      return "{getHoursDisplay(hours)}, {getMinutesDisplay(minutes)}"
   }

   function getDaysDisplay(days: Integer): String {
      if (days == 1) {
         return "1 {dayWord}"
      }
      return "{days} {daysWord}"
   }

}
