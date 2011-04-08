/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import java.text.SimpleDateFormat;
import eu.scy.common.scyelo.ScyElo;
import java.util.Date;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;

/**
 * @author SikkenJ
 */
// place your code here
public def titleLabel = ##"Title";
public def authorsLabel = ##"Author(s)";
public def formatLabel = ##"Type";
public def roleLabel = ##"Role";
public def dateLabel = ##"Date";
public def createdAtLabel = ##"created";
public def lastModifiedAtLabel = ##"last modified";
public def relevanceLabel = ##"Relevance";

def dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

public function getAuthorsText(scyElo: ScyElo): String {
   if (scyElo == null) {
      return ""
   }
   def authors = scyElo.getAuthors();
   var authorsText = "";
   if (authors.size() > 0) {
      authorsText = "";
      for (author in authors) {
         if (indexof author > 0) {
            authorsText = "{authorsText}, ";
         }
         authorsText = "{authorsText}{author}";
      }
   }
   authorsText
}

public function getTechnicalFormatString(scyElo: ScyElo, newEloCreationRegistry: NewEloCreationRegistry): String {
   if (scyElo == null) {
      return ""
   }
   return newEloCreationRegistry.getEloTypeName(scyElo.getTechnicalFormat())
}

public function getDateString(scyElo: ScyElo): String {
   if (scyElo == null) {
      return ""
   }
   return "{createdAtLabel} {getDateString(scyElo.getDateCreated())}, {lastModifiedAtLabel} {getDateString(scyElo.getDateLastModified())}"
}

public function getLastModifiedDateString(scyElo: ScyElo): String {
   if (scyElo == null) {
      return ""
   }
   return "{lastModifiedAtLabel} {getDateString(scyElo.getDateLastModified())}"
}

function getDateString(millis: java.lang.Long): String {
   if (millis == null) {
      return ##"unknown"
   }
   dateFormat.format(new Date(millis))
}

public function getRoleString(scyElo: ScyElo): String {
   if (scyElo == null) {
      return ""
   }
   def role = scyElo.getFunctionalRole();
   if (role == null) {
      return ##"unknown";
   }
   return role.toString();
}

public function getUriString(scyElo: ScyElo): String {
   if (scyElo == null) {
      return ""
   }
   return "{scyElo.getUri()}"
}
