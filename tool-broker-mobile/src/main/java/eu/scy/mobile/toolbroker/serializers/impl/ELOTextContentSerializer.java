package eu.scy.mobile.toolbroker.serializers.impl;

import org.json.me.JSONObject;
import org.json.me.JSONException;
import eu.scy.mobile.toolbroker.model.ELO;
import eu.scy.mobile.toolbroker.model.ELOTextContent;
import eu.scy.mobile.toolbroker.serializers.JSONSerializer;
import eu.scy.mobile.toolbroker.serializers.Serializers;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 25.feb.2009
 * Time: 13:49:57
 * To change this template use File | Settings | File Templates.
 */
public class ELOTextContentSerializer extends JSONSerializer {
    public String getLocalId() {
        return "eu.scy.mobile.toolbroker.model.ELOTextContent";
    }
    public String getRemoteId() {
        return "eu.scy.ws.example.mock.api.ELOTextContent";
    }
    public JSONObject serialize(Object o) {
        ELOTextContent textContent = (ELOTextContent) o;
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("class", getRemoteId());
            jsonObj.put("content", textContent.getContent());

            Object content = textContent.getContent();

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

        ELOTextContent elo = new ELOTextContent();

        Enumeration keys = obj.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            Object value = null;
            try {
                value = obj.get(key);
            } catch (JSONException e) {}
            Object decodedValue = deserializeValue(value);
            if (key.equals("content")) elo.setContent((String)decodedValue);
        }
        return elo;
    }
}
