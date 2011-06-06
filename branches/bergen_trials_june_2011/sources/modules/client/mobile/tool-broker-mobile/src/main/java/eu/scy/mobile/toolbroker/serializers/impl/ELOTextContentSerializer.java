package eu.scy.mobile.toolbroker.serializers.impl;

import org.json.me.JSONObject;
import org.json.me.JSONException;
import eu.scy.mobile.toolbroker.model.impl.TextContent;
import eu.scy.mobile.toolbroker.model.ITextContent;
import eu.scy.mobile.toolbroker.serializers.Serializers;
import eu.scy.mobile.toolbroker.serializer.Serializer;

import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 25.feb.2009
 * Time: 13:49:57
 * To change this template use File | Settings | File Templates.
 */
public class ELOTextContentSerializer implements Serializer {
    public String getLocalId() {
        return "eu.scy.mobile.toolbroker.model.impl.TextContent";
    }
    public String getRemoteId() {
        return "eu.scy.ws.example.mock.api.ELOTextContent";
    }
    public Object serialize(Object o) {
        ITextContent textContent = (ITextContent) o;
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("class", getRemoteId());
            jsonObj.put("content", textContent.getContent());

            Object content = textContent.getContent();

            Serializer serializer = Serializers.getByLocalType(content.getClass().getName());
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
    public Object deserialize(Object obj) {

        ITextContent elo = new TextContent();

        JSONObject jsonObj = (JSONObject) obj;

        Enumeration keys = jsonObj.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            Object value = null;
            try {
                value = jsonObj.get(key);
            } catch (JSONException e) {}
            Object decodedValue = JSONUtil.deserializeValue(value);
            if (key.equals("content")) elo.setContent((String)decodedValue);
        }
        return elo;
    }
}
