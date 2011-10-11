/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import java.lang.System;

/**
 * @author SikkenJ
 */
def minuteWord = ##"minute";
def minutesWord = ##"minutes";
def hourWord = ##"hour";
def hoursWord = ##"hours";
def dayWord = ##"day";
def daysWord = ##"days";
def agoWord = ##"ago";

public function getLastModifiedDisplay(lastModified: Long): String {
   var lastModifiedString = "";
   def millisPast = System.currentTimeMillis() - lastModified;
   def minutesPast = millisPast / (1000 * 60);
   if (minutesPast < 60) {
      lastModifiedString = getMinutesDisplay(minutesPast);
   } else {
      def hoursPast = minutesPast / 60;
      def minutesLeft = minutesPast mod 60;
      if (hoursPast < 24) {
         lastModifiedString = getHoursDisplay(hoursPast);
      } else {
         def daysPast = hoursPast / 24;
         def hoursLeft = hoursPast mod 24;
         lastModifiedString = "{getDaysDisplay(daysPast)}"
      }
   }
   "{lastModifiedString} {agoWord}"
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
