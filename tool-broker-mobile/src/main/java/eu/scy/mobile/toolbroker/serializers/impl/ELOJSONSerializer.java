package eu.scy.mobile.toolbroker.serializers.impl;

import eu.scy.mobile.toolbroker.model.ELO;
import eu.scy.mobile.toolbroker.serializers.JSONSerializer;
import eu.scy.mobile.toolbroker.serializers.Serializers;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Created: 12.feb.2009 11:14:15
 *
 * TODO: The JSON marshalling in Jersey is having issues when posting back to the webservice.
 *
 * @author Bj�rge N�ss
 */

public class ELOJSONSerializer extends JSONSerializer {
    public String getLocalId() {
        return "eu.scy.mobile.toolbroker.model.ELO";
    }
    public String getRemoteId() {
        return "eu.scy.ws.example.mock.api.ELO";
    }
    public JSONObject serialize(Object o) {
        ELO elo = (ELO) o;
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("class", getRemoteId());
            jsonObj.put("id", elo.getId());
            jsonObj.put("title", elo.getTitle());

            Object content = elo.getContent();

            JSONSerializer serializer = Serializers.getByLocalType(content.getClass().getName());
            if (serializer !=  null) jsonObj.put("content", serializer.serialize(content));
            else {
                System.err.println("Warning: No serializer found for content type. Encoded as string using toString()");
                jsonObj.put("content", content);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
    public Object deserialize(JSONObject obj) {

        ELO elo = new ELO();

        Enumeration keys = obj.keys();
        while (keys.hasMoreElements()) {

            String key = (String) keys.nextElement();
            Object value = null;
            try {
                value = obj.get(key);
            } catch (JSONException e) {}

            Object decodedValue = deserializeValue(value);
            if (key.equals("title")) elo.setTitle((String)decodedValue);
            else if (key.equals("id")) elo.setId(Integer.parseInt(decodedValue.toString()));
            else if (key.equals("children")) elo.setChildren((Vector) decodedValue);
            else if (key.equals("content")) elo.setContent(decodedValue);
        }
        return elo;
    }
}
