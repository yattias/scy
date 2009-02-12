package eu.scy.mock.service;

import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;
import eu.scy.mock.datamodel.ELO;
import eu.scy.mock.datamodel.converters.ELOJSONSerializer;
import eu.scy.mock.datamodel.converters.GeoImageJSONSerializer;
import eu.scy.mock.datamodel.converters.JSONSerializer;
import eu.scy.mock.datamodel.converters.Serializers;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created: 11.feb.2009 12:21:06
 *
 * @author Bjørge Næss
 */
public class EloServiceClient implements RequestListener {
	private String serviceUrl;
	private String errorCode;
	private String errorMessage;
	private HttpConnection http;
	private String server_cookie;

	public EloServiceClient(String sUrl) {
		Serializers.add("ELO", new ELOJSONSerializer());
		Serializers.add("geoImageCollector", new GeoImageJSONSerializer());
		serviceUrl = sUrl;
	}
	public ELO retrieveELO(int id) {
		try {
			http = (HttpConnection) Connector.open(serviceUrl+"/"+id);
			http.setRequestProperty("Accept", "application/json");
			http.setRequestMethod(HttpConnection.GET);

			System.out.println("Using service at " + http.getURL());

			/*
			if (server_cookie == null) server_cookie = http.getHeaderField("set-cookie");
			else http.setRequestProperty("cookie", server_cookie);

			System.out.println("server_cookie'' = " + server_cookie);
*/
			InputStream iStrm = http.openInputStream();
			int length = (int) http.getLength();
			String str;
			if (length != -1) {
				byte serverData[] = new byte[length];
				iStrm.read(serverData);
				str = new String(serverData);
			} else {
				ByteArrayOutputStream bStrm = new ByteArrayOutputStream();

				int ch;
				while ((ch = iStrm.read()) != -1)
					bStrm.write(ch);

				str = new String(bStrm.toByteArray());

			}

			// I know the content, therefore I can do this mapping manually... TODO: Create a JSON-to-Class mapping-registry
			JSONObject json = new JSONObject(str);

			JSONSerializer serializer = Serializers.get("ELO");
			ELO elo = (ELO) serializer.deserialize(json);

			return elo;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
	private boolean isError(final Response response) {
		boolean isError = false;
		final Exception ex = response.getException();
		if (ex != null) {
			isError = true;
			errorCode = "";
			errorMessage = ex.getMessage();
		} else {
			try {
				final Result result = response.getResult();
				final String stat = result.getAsString("stat");
				if ("fail".equals(stat)) {
					isError = true;
					errorCode = result.getAsString("code");
					errorMessage = result.getAsString("message");
				}
			} catch (ResultException rex) {
				isError = true;
			}
		}
		return isError;
	}

	public void done(Object o, Response response) throws Exception {
		if (isError(response)) {
			System.out.println("Error: response = " + response.getResult());
			return;
		}

		final Result result = response.getResult();
		System.out.println("message = " + result.getAsString("message"));

/*        final String root = "photos.photo[" + i + "].";
		try {
			foto.id = result.getAsString(root + "id");
			foto.owner = result.getAsString(root + "owner");
			foto.secret = result.getAsString(root + "secret");
			foto.server = result.getAsString(root + "server");
			foto.farm = result.getAsString(root + "farm");
			foto.title = result.getAsString(root + "title");
			foto.ispublic = !"0".equals(result.getAsString(root + "ispublic"));
			photos[i] = foto;
		} catch (ResultException jex) {
			alert("Error parsing result " + i + " of getInteresting");
			return;
		}
        getDisplay().setCurrent(get_thumbnails());
        */
	}

	public void readProgress(Object o, int i, int i1) {
	}

	public void writeProgress(Object o, int i, int i1) {
	}
}
