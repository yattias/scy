/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import java.util.Comparator;

/**
 * @author SikkenJ
 */
public class ContactNameComparator extends Comparator {

   public override function compare(object1: Object, object2: Object): Integer {
      if (object1 instanceof Contact and object2 instanceof Contact) {
         var contact1 = object1 as Contact;
         var contact2 = object2 as Contact;
         return contact1.name.compareTo(contact2.name);
      }
      return 0;
   }

   public override function equals(object: Object): Boolean {
      return false;
   }

}
