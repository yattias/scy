package eu.scy.mobile.toolbroker.serializers;

import eu.scy.mobile.toolbroker.serializers.impl.ELOJSONSerializer;
import eu.scy.mobile.toolbroker.serializers.impl.ELOTextContentSerializer;

import java.util.Vector;
import java.util.Hashtable;

/**
 * Created: 11.feb.2009 14:01:54
 *
 * @author Bjørge Næss
 */
public class Serializers {

	private Serializers() {}

	private static Vector Register = new Vector() {{
		addElement(new ELOJSONSerializer());
		addElement(new ELOTextContentSerializer());
    }};

	public static JSONSerializer getByLocalType(String typeId) {
        for (int i = 0; i < Register.size(); i++) {
            JSONSerializer serializer = (JSONSerializer) Register.elementAt(i);
            if (serializer.getLocalId().equals(typeId)) return serializer;
        }
        return null;
	}
	public static JSONSerializer getByRemoteType(String typeId) {
        for (int i = 0; i < Register.size(); i++) {
            JSONSerializer serializer = (JSONSerializer) Register.elementAt(i);
            if (serializer.getRemoteId().equals(typeId)) return serializer;
        }
        return null;
	}
	/**
	 * Register serializer for Class
     * @param s Serializer object
     */
	public static void add(JSONSerializer s) {
		Register.addElement(s);
	}
}
