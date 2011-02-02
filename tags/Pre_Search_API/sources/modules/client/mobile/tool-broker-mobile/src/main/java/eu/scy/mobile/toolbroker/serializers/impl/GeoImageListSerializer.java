package eu.scy.mobile.toolbroker.serializers.impl;

import eu.scy.mobile.toolbroker.model.impl.GeoImageList;
import eu.scy.mobile.toolbroker.model.IGeoImageList;
import eu.scy.mobile.toolbroker.serializer.Serializer;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.util.Enumeration;

/**
 * Created: 12.feb.2009 11:17:33
 *
 * @author Bjørge Næss
 */
public class GeoImageListSerializer implements Serializer {
	private IGeoImageList instance;

    public String getLocalId() {
        return "eu.scy.mobile.toolbroker.model.impl.GeoImageList";
    }

    public String getRemoteId() {
        return "eu.scy.ws.example.mock.api.GeoImageCollector";
    }

    public Object deserialize(Object obj) {
		instance = new GeoImageList();

        JSONObject jsonObj = (JSONObject) obj;

		Enumeration keys = jsonObj.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			try {
				Object theObj = jsonObj.get(key);
				deserializeKey(key, theObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public Object serialize(Object o) {
        // System.out.println("o = " + o.getClass());
        IGeoImageList gic = (IGeoImageList) o;
        JSONObject jsonObj = new JSONObject();
        /**/
        try {
            jsonObj.put("class", getRemoteId());
            jsonObj.put("images", gic.getImages());
            jsonObj.put("name", gic.getName());
        } catch (JSONException e) {
            System.err.println("Error occurred when serializing object "+o+": " + e);
        }
         /**/
        return jsonObj;
	}

	private void deserializeKey(String key, Object value) {
		if (key.equals("name")) instance.setName((String) value);
			//else if (key.equals("id")) instance.setId(Integer.parseInt((String) value));
		else if (key.equals("images")) {
			JSONArray jsonArr = (JSONArray) value;
			for (int i = 0; i < jsonArr.length(); i++) {
				try {
					String location = jsonArr.getString(i);
					instance.addImage(location);

				} catch (JSONException e) {
					e.printStackTrace();
                }
            }
		}
	}
}
