package eu.scy.mobile.toolbroker.serializers;

import org.json.me.JSONObject;
import org.json.me.JSONException;
import org.json.me.JSONArray;

import java.util.Vector;

/**
 * Created: 12.feb.2009 11:12:12
 *
 * @author Bjørge Næss
 */
public abstract class JSONSerializer {
    public abstract String getLocalId();
    public abstract String getRemoteId();
	public abstract Object deserialize(JSONObject obj);
	public abstract JSONObject serialize(Object obj);

    public static Object deserializeValue(Object value) {
        if (value instanceof JSONObject) {
            JSONObject jsonObj = (JSONObject) value;
            String type = null;
            try {
                type = jsonObj.getString("class");
            } catch (JSONException e) {
                System.err.println("Warning: Missing property 'class' for JSON object "+value.toString());
            }
            JSONSerializer s = Serializers.getByRemoteType(type);
            if (s == null) System.err.println("Warning: No serializer found for type "+type);
            else return s.deserialize(jsonObj);
        }
        else if (value instanceof JSONArray) {
            Vector list = new Vector();
            JSONArray jsonArr = (JSONArray) value;
            for (int i = 0; i < jsonArr.length(); i++) {
                try {
                    Object entry = deserializeValue(jsonArr.get(i));
                    list.addElement(entry);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
        else return value;

        return null;
    }
}
