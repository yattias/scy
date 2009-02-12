package eu.scy.test;

import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created: 28.jan.2009 10:57:37
 *
 * @author Bjørge
 */
public class TestUserService extends MIDlet implements CommandListener, RequestListener {

	private final String USERSERVICE_URL = "http://mockuserservice.lo/service/login";
	private final String AUTH_REQ_URL = "http://mockuserservice.lo/service/other";
	private Form loginForm;
	private TextField usernameField;
	private TextField passwordField;
	private Command loginCommand;
	private String errorCode;
	private String errorMessage;
	private Command authCommand;
	private HttpConnection http;
	private String server_cookie;

	public TestUserService() {

	}
	public Display getDisplay() {
        return Display.getDisplay(this);
    }
	protected void startApp() throws MIDletStateChangeException {
		getDisplay().setCurrent(getLoginForm());
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
	}

	public Form getLoginForm() {
        if (loginForm == null) {
			usernameField = new TextField("Username", null, 25, TextField.ANY);
			passwordField = new TextField("Password", null, 25, TextField.PASSWORD);

	        loginForm = new Form("Please log in", new Item[] {
					usernameField,
					passwordField
			});
	        loginForm.addCommand(getLoginCommand());
            loginForm.setCommandListener(this);
        }
        return loginForm;
    }
	public Form getAuthForm() {
		StringItem strItem = new StringItem("Result", "");
		Form authForm = new Form("Please log in", new Item[] {strItem});
        authForm.addCommand(getAuthCommand());
        authForm.setCommandListener(this);
		return authForm;
    }
	public void commandAction(Command command, Displayable displayable) {
		if (command.getLabel().equals("Login")) {
			try {
				http = (HttpConnection) Connector.open(USERSERVICE_URL+"?username="+usernameField.getString()+"&password="+passwordField.getString());
//				http.setRequestProperty("username", );

				//http.setRequestProperty("sessionId", sessionId);
				http.setRequestMethod(HttpConnection.GET);
				System.out.println("command = " + http.getURL());

				if (server_cookie == null) server_cookie = http.getHeaderField("set-cookie");
				else http.setRequestProperty("cookie", server_cookie);

				System.out.println("server_cookie'' = " + server_cookie);

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

				// Append data to the form
				//getAuthForm().append(str + "\n");
				//System.out.println("str = " + str);
				getDisplay().setCurrent(getAuthForm());
				http.close();

			} catch (IOException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}

			/*
			Arg[] args = {
				new Arg("username", usernameField.getString()),
				new Arg("password", passwordField.getString())
			};
			Request.get(USERSERVICE_URL, args, null, this, null);
			System.out.println("You tried to log in with username \""+usernameField.getString()+"\" and password \""+passwordField.getString()+"\", right?");
			*/
		}
		if (command.equals(authCommand)) {
			//Request.get(AUTH_REQ_URL, null, null, this, null);
			try {
				http = (HttpConnection) Connector.open(AUTH_REQ_URL);
				http.setRequestMethod(HttpConnection.GET);

				if (server_cookie == null) server_cookie = http.getHeaderField("set-cookie");
				else http.setRequestProperty("cookie", server_cookie);
				System.out.println("server_cookie'' = " + server_cookie);

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

				// Append data to the form
				getAuthForm().append(str + "\n");
				System.out.println("str = " + str);
				getDisplay().setCurrent(getAuthForm());
				http.close();
			} catch (IOException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
	}

	private Command getLoginCommand() {
		loginCommand = new Command("Login", Command.ITEM, 1);
		return loginCommand;
	}
	private Command getAuthCommand() {
		authCommand = new Command("Check auth", Command.ITEM, 1);
		return authCommand;
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
	private void alert(String title, String msg) {
		Alert alert = new Alert(title);
		alert.setString(msg);
        getDisplay().setCurrent(alert);
    }

	public void done(Object o, Response response) throws Exception {
		if (isError(response)) {
			System.out.println("Error: response = " + response.getResult());
            alert("Error "+errorCode, errorMessage);
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
		getDisplay().setCurrent(getAuthForm());
	}

	public void readProgress(Object o, int i, int i1) {
	}

	public void writeProgress(Object o, int i, int i1) {
	}
}
