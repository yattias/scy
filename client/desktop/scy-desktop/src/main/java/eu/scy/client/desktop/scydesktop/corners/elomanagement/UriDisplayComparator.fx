/*
 * UriDisplayComparator.fx
 *
 * Created on 1-mrt-2010, 10:59:38
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import java.util.Comparator;
import java.lang.IllegalArgumentException;

/**
 * @author sikken
 */
public class UriDisplayComparator extends Comparator {

   override public function compare(arg0: Object, arg1: Object): Integer {
      if (arg0 == null) {
         if (arg1 == null) {
            return 0;
         }
         return 1;
      }
      if (arg1 == null) {
         return -1;
      }
      if (arg0 instanceof UriDisplay and arg1 instanceof UriDisplay) {
         var uriDisplay0 = arg0 as UriDisplay;
         var uriDisplay1 = arg1 as UriDisplay;
         return uriDisplay0.display.compareToIgnoreCase(uriDisplay1.display);
      }
      throw new IllegalArgumentException("both argument are not of type UriDisplay, but {arg0.getClass().getName()} and {arg1.getClass().getName()}");
   }

   override public function equals(arg0: Object): Boolean {
      super.equals(arg0);
   }

}
