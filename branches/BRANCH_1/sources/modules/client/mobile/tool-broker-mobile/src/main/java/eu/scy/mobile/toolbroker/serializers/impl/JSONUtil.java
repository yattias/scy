package eu.scy.mobile.toolbroker.serializers.impl;

import org.json.me.JSONObject;
import org.json.me.JSONException;
import org.json.me.JSONArray;
import eu.scy.mobile.toolbroker.serializer.Serializer;
import eu.scy.mobile.toolbroker.serializers.Serializers;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.mar.2009
 * Time: 12:18:02
 * To change this template use File | Settings | File Templates.
 */
public class JSONUtil {
    public static Object deserializeValue(Object value) {
        if (value instanceof JSONObject) {
            JSONObject jsonObj = (JSONObject) value;
            String type = null;
            try {
                type = jsonObj.getString("class");
            } catch (JSONException e) {
                System.err.println("Warning: Missing property 'class' for JSON object "+value.toString());
            }
            Serializer s = Serializers.getByRemoteType(type);
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
