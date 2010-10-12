/*
 * ScyWindowAttributeComparator.fx
 *
 * Created on 29-mrt-2009, 12:02:35
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import java.lang.Object;
import java.util.Comparator;

/**
 * @author sikken
 */

public class ScyWindowAttributePriorityComparator extends Comparator {
    public override function compare(object1:Object,object2:Object):Integer{
        if (object1 instanceof ScyWindowAttribute and object2 instanceof ScyWindowAttribute){
            var scyWindowAttribute1 = object1 as ScyWindowAttribute;
            var scyWindowAttribute2 = object2 as ScyWindowAttribute;
            return scyWindowAttribute1.priority - scyWindowAttribute2.priority;
        }
        return 0;
    }

    public override function equals(object:Object):Boolean{
       return false;
    }
}
