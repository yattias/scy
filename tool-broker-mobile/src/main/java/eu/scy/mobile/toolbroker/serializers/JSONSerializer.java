package eu.scy.mobile.toolbroker.serializers;

import org.json.me.JSONObject;

/**
 * Created: 12.feb.2009 11:12:12
 *
 * @author Bjørge Næss
 */
public interface JSONSerializer {
	public Object deserialize(JSONObject obj);
	public JSONObject serialize(Object obj);
}
