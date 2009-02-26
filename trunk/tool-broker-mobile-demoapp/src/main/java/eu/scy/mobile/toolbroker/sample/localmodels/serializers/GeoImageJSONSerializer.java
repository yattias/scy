package eu.scy.mobile.toolbroker.sample.localmodels.serializers;

import com.sun.me.web.request.Arg;
import com.sun.me.web.request.ProgressInputStream;
import com.sun.me.web.request.ProgressListener;
import eu.scy.mobile.toolbroker.sample.localmodels.GeoImageCollector;
import eu.scy.mobile.toolbroker.serializers.JSONSerializer;
import eu.scy.mobile.toolbroker.serializers.Serializers;
import eu.scy.mobile.toolbroker.model.ELO;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * Created: 12.feb.2009 11:17:33
 *
 * @author Bjørge Næss
 */
public class GeoImageJSONSerializer extends JSONSerializer {
	private GeoImageCollector instance;

    public String getLocalId() {
        return "eu.scy.mobile.toolbroker.sample.localmodels.GeoImageCollector";
    }

    public String getRemoteId() {
        return "eu.scy.ws.example.mock.api.GeoImageCollector";
    }

    public Object deserialize(JSONObject obj) {
		instance = new GeoImageCollector();

		Enumeration keys = obj.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
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
        System.out.println("o = " + o.getClass());
        GeoImageCollector gic = (GeoImageCollector) o;
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
