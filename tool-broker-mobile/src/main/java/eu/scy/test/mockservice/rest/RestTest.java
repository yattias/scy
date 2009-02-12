package eu.scy.test.mockservice.rest;

import com.sun.me.web.request.Request;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;
import org.json.me.JSONObject;

import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Created: 05.feb.2009 14:58:35
 *
 * @author Bjørge Næss
 */
public class RestTest extends MIDlet implements RequestListener {

	private final String SERVICE_URL = "http://localhost:9998/elo/1";
	private HttpConnection http;

	public RestTest() {}

	public Display getDisplay() {
		return Display.getDisplay(this);
	}

	protected void startApp() throws MIDletStateChangeException {
			Request.get(SERVICE_URL, null, null, this, null);
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
	}

	public void done(Object context, Response response) throws Exception {
		System.out.println("context = " + context);
		//final Result result = response.getResult();
		//System.out.println(result.getClass());

		JSONObject obj = new JSONObject("");
		//ELO elo = new ELO();
		//JSONUnmarshaller.UnMarshal(null, null);
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

