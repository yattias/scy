/*
 * ArgumentsList.fx
 *
 * Created on 11-dec-2009, 9:23:03
 */
package eu.scy.client.desktop.scydesktop;

import java.lang.IllegalArgumentException;

/**
 * @author sikken
 */
public class ArgumentsList {

   public-init var arguments: String[];
   var index = 0;

   public function hasMoreArguments(): Boolean {
      if (arguments == null) {
         return false;
      }
      return sizeof arguments > index;
   }

   public function nextArgument(): String {
      return arguments[index++].trim();
   }

   public function nextStringValue(valueName: String): String {
      if (hasMoreArguments()) {
         return nextArgument();
      }
      throw new IllegalArgumentException("cannot find {valueName}");
   }

   public function nextBooleanValue(valueName: String): Boolean {
      var stringValue = nextStringValue(valueName);
      return "true".equalsIgnoreCase(stringValue);
   }

   public function nextNumberValue(valueName: String): Number {
      var stringValue = nextStringValue(valueName);
      return Float.parseFloat(stringValue);
   }

   public function nextIntegerValue(valueName: String): Integer {
      var stringValue = nextStringValue(valueName);
      return Integer.parseInt(stringValue);
   }
}
