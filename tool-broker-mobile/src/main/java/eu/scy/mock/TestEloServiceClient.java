package eu.scy.mock;

import eu.scy.mock.datamodel.ELO;
import eu.scy.mock.datamodel.GeoImageCollector;
import eu.scy.mock.service.EloServiceClient;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import java.util.Vector;

/**
 * Created: 11.feb.2009 12:23:07
 *
 * @author Bjørge Næss
 */
public class TestEloServiceClient extends MIDlet implements CommandListener {
	private Form loginForm;
	private EloServiceClient eloServiceClient;
	private TextField eloIdField;

	public Display getDisplay() {
        return Display.getDisplay(this);
    }
	protected void startApp() throws MIDletStateChangeException {
		eloServiceClient = new EloServiceClient("http://scyzophrenia.ath.cx:9998/elos");
		eloIdField = new TextField("Enter ELO Id", null, 2, TextField.NUMERIC);
		getDisplay().setCurrent(getActionsForm());
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
	}

	public Form getActionsForm() {
        if (loginForm == null) {
	        loginForm = new Form("Welcome to the test!", null);
	        loginForm.append(eloIdField);
	        loginForm.addCommand(new Command("Get ELO!", Command.ITEM, 1));
            loginForm.setCommandListener(this);
        }
        return loginForm;
    }
	public void commandAction(Command command, Displayable displayable) {
		if (command.getLabel().equals("Get ELO!")) {
			// Retrieve the first ELO
			ELO elo = eloServiceClient.retrieveELO(Integer.parseInt(eloIdField.getString()));
			loginForm.setTicker(new Ticker("Congratulations! You just retrieved an ELO!"));
			loginForm.deleteAll();
			loginForm.append(eloIdField);

			if (elo.getContent() instanceof GeoImageCollector) {
				loginForm.append("The content of this ELO is a image collector. Displaying images:");
				GeoImageCollector content = (GeoImageCollector) elo.getContent();
				Vector images = content.getImages();
				for (int i = 0; i < images.size(); i++) {
					Image img = (Image) images.elementAt(i);
					ImageItem imgItem = new ImageItem(null, img, ImageItem.LAYOUT_DEFAULT, "Thumbnail", ImageItem.BUTTON);
					loginForm.append(imgItem);
				}
			}
			else {
				loginForm.append(elo.toString());
			}
		}
	}
}
