package eu.scy.mobile.toolbroker.serializers.impl;

import eu.scy.mobile.toolbroker.model.ELO;
import eu.scy.mobile.toolbroker.serializers.JSONSerializer;
import eu.scy.mobile.toolbroker.serializers.SerializerNotFoundException;
import eu.scy.mobile.toolbroker.serializers.Serializers;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.util.Enumeration;

/**
 * Created: 12.feb.2009 11:14:15
 *
 * @author Bjørge Næss
 */
public class ELOJSONSerializer implements JSONSerializer {
	private ELO instance;

	public Object deserialize(JSONObject obj) {
		instance = new ELO();

		Enumeration keys = obj.keys();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			try {
				Object theObj = obj.get(key);
				deserializeKey(key, theObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public JSONObject serialize(Object o) {
		return null;
	}

	private void deserializeKey(String key, Object value) {
		if (key.equals("title")) instance.setTitle((String) value);
		else if (key.equals("id")) instance.setId(Integer.parseInt((String) value));
		else if (key.equals("children")) {
			JSONArray jsonArr = (JSONArray) value;
			for (int i =0; i < jsonArr.length(); i++) {
				try {
					ELO child = (ELO) deserialize(jsonArr.getJSONObject(i));
					instance.addChildELO(child);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		else if (key.equals("content")) {
			// Content type is always unknown, but type is specified in json-response by the @type attr
			JSONObject jsonObj = (JSONObject) value;
			try {
				String type = jsonObj.getString("@type");
				if (type.equals("xs:string")) {
					instance.setContent(jsonObj.getString("$"));
				}
				else {
					try {
						JSONSerializer s = Serializers.get(type);
						instance.setContent(s.deserialize(jsonObj));
					} catch (SerializerNotFoundException e) {
						instance.setContent(jsonObj.toString());
						System.err.println("Warning: No serializer found for content type "+type+" encoded as string ");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
