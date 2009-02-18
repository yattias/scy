package eu.scy.mobile.toolbroker.serializers;

import eu.scy.mobile.toolbroker.serializers.impl.ELOJSONSerializer;

import java.util.Hashtable;

/**
 * Created: 11.feb.2009 14:01:54
 *
 * @author Bjørge Næss
 */
public class Serializers {

	private Serializers() {}

	private static Hashtable Register = new Hashtable() {{
		// Add new serializers here
		put("elo", new ELOJSONSerializer());
	}};

	public static JSONSerializer get(String className) throws SerializerNotFoundException {
		JSONSerializer serializer = (JSONSerializer) Register.get(className);
		if (serializer == null) throw new SerializerNotFoundException("Serializer not found. Is it added to the list of serializers?");
		return serializer;
	}
	/**
	 * Register serializer for Class
	 */
	public static void add(String className, JSONSerializer s) {
		Register.put(className, s);
	}
}
