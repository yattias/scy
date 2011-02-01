package eu.scy.mobile.toolbroker.client;

import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import java.io.*;

/**
 * Created: 13.feb.2009 09:57:27
 *
 * @author Bjørge Næss
 */
public class JSONServiceClient implements RequestListener {
	private String errorCode;
	private String errorMessage;
	protected HttpConnection http;
	private String baseUrl;

	public JSONServiceClient(String baseURL) {
		this.baseUrl = baseURL;
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
			// System.out.println("Error: response = " + response.getResult());
			return;
		}

		final Result result = response.getResult();
		// System.out.println("message = " + result.getAsString("message"));
	}

	public void readProgress(Object o, int i, int i1) {
	}

	public void writeProgress(Object o, int i, int i1) {
	}

	public JSONObject getJSON(String path) {
		try {
			http = (HttpConnection) Connector.open(baseUrl+path);
			http.setRequestProperty("Accept", "application/json");
			http.setRequestMethod(HttpConnection.GET);
			// System.out.println("Requesting response from " + http.getURL());

			InputStream iStrm = http.openInputStream();
			int length = (int) http.getLength();
			String str;
			if (length != -1) {
				byte serverData[] = new byte[length];
				//noinspection ResultOfMethodCallIgnored
				iStrm.read(serverData);
				str = new String(serverData);
			} else {
				ByteArrayOutputStream bStrm = new ByteArrayOutputStream();

				int ch;
				while ((ch = iStrm.read()) != -1)
					bStrm.write(ch);

				str = new String(bStrm.toByteArray());
				// System.out.println("str = " + str);
				return new JSONObject(str);
			}
			// System.out.println("Response is -1");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
        finally {
            try {
                http.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return null;
	}
	public void postJSON(String path, JSONObject obj) {
		DataOutputStream os = null;
		DataInputStream is = null;
		StringBuffer responseMessage = new StringBuffer();

		try {
			http = (HttpConnection) Connector.open(baseUrl+path, Connector.READ_WRITE);
			http.setRequestProperty("Content-type", "application/json");
			http.setRequestProperty("Accept", "application/json");
			http.setRequestMethod(HttpConnection.POST);

			// Obtain a DataOutputStream object for the existing HTTP connection.
			os = http.openDataOutputStream();
            // System.out.println("Posting content to "+http.getURL()+": " + obj);

			byte[] request_body = obj.toString().getBytes();

            for( int i = 0; i < request_body.length; i++ ) {
				os.writeByte(request_body[i]);
			}

			is = new DataInputStream(http.openInputStream());

			// retrieve the response from server
			int ch;
			while( ( ch = is.read() ) != -1 ) {
				responseMessage.append( (char)ch );
			}

            // System.out.println("Response from post: "+responseMessage.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			    // free up i/o streams and http connection
			    try {
			        if( http != null ) http.close();
			        if( is != null ) is.close();
			        if( os != null ) os.close();
			    } catch ( IOException ioe ) {
			        ioe.printStackTrace();
			    }
			}
	}
}
