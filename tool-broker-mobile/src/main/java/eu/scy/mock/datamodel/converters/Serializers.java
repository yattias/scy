package eu.scy.mock.datamodel.converters;

import java.util.Hashtable;

/**
 * Created: 11.feb.2009 14:01:54
 *
 * @author Bjørge Næss
 */
public class Serializers {

	private Serializers() {}

	private static Hashtable Register = new Hashtable();

	public static JSONSerializer get(String className) {
		return (JSONSerializer) Register.get(className);
	}
	/**
	 * Register serializer for Class
	 */
	public static void add(String className, JSONSerializer s) {
		Register.put(className, s);
	}
}
