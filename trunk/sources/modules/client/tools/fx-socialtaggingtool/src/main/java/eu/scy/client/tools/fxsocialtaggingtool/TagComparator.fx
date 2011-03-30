/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxsocialtaggingtool;

import java.util.Comparator;

/**
 * @author giemza
 */

public class TagComparator extends Comparator {

    override public function compare (arg0 : Object, arg1 : Object) : Integer {
        def tag1 = arg0 as Tag;
        def tag2 = arg1 as Tag;

        return tag1.tagname.compareToIgnoreCase(tag2.tagname);
    }

}
