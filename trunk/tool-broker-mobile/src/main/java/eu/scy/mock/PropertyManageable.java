package eu.scy.mock;

import java.util.Hashtable;

/**
 * Created: 11.feb.2009 11:41:27
 *
 * @author Bj�rge N�ss
 */
public interface PropertyManageable {
	void setProps(Hashtable props);
	Hashtable getProps();
}
