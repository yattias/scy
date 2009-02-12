package eu.scy.mock;

import java.util.Hashtable;

/**
 * Created: 11.feb.2009 11:41:27
 *
 * @author Bjørge Næss
 */
public interface PropertyManageable {
	void setProps(Hashtable props);
	Hashtable getProps();
}
