package eu.scy.mock.datamodel.converters;

import eu.scy.mock.datamodel.ELO;
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
					JSONSerializer s = Serializers.get(type);
					if (s == null) {
						System.err.println("No serializer found for "+type);
						instance.setContent("Unsupported content");
					}
					else instance.setContent(s.deserialize(jsonObj));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		//if (property.equals("geoImageCollectors")) instance.setTitle((String) value);
	}
}
