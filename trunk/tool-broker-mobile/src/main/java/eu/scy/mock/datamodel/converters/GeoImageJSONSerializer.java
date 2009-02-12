package eu.scy.mock.datamodel.converters;

import com.sun.me.web.request.Arg;
import com.sun.me.web.request.ProgressInputStream;
import eu.scy.mock.datamodel.GeoImageCollector;
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
public class GeoImageJSONSerializer implements JSONSerializer, com.sun.me.web.request.ProgressListener {
	private GeoImageCollector instance;

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
		return null;
	}

	private void deserializeKey(String key, Object value) {
		if (key.equals("name")) instance.setName((String) value);
			//else if (key.equals("id")) instance.setId(Integer.parseInt((String) value));
		else if (key.equals("images")) {
			JSONArray jsonArr = (JSONArray) value;
			for (int i = 0; i < jsonArr.length(); i++) {
				try {
					String location = jsonArr.getString(i);
					Image image;
					image = loadImage(location);
					if (image != null)
						instance.addImage(image);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Image loadImage(final String location) throws IOException {
        HttpConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpConnection) Connector.open(location);
                        conn.setRequestProperty("accept", "image/*");

            final int responseCode = conn.getResponseCode();
            if (responseCode != HttpConnection.HTTP_OK) {
                // TODO: handle redirects
                return null;
            }

            final int totalToReceive = conn.getHeaderFieldInt(Arg.CONTENT_LENGTH, 0);
            is = new ProgressInputStream(conn.openInputStream(), totalToReceive, this, null, 1024);

            final ByteArrayOutputStream bos = new ByteArrayOutputStream(Math.max(totalToReceive, 8192));
            final byte[] buffer = new byte[4096];
            for (int nread = is.read(buffer); nread >= 0; nread = is.read(buffer)) {
                bos.write(buffer, 0, nread);
            }
            return Image.createImage(new ByteArrayInputStream(bos.toByteArray()));
        }  finally {
            if (is != null) { is.close(); }
            if (conn != null) { conn.close(); }
        }
    }

	public void readProgress(Object o, int i, int i1) {
	}

	public void writeProgress(Object o, int i, int i1) {
	}
}
