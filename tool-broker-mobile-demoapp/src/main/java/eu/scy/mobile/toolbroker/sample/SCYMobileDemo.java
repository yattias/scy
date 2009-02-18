package eu.scy.mobile.toolbroker.sample;

import eu.scy.mobile.toolbroker.ToolBrokerMobileAPI;
import eu.scy.mobile.toolbroker.ToolBrokerMobileAPIImpl;
import eu.scy.mobile.toolbroker.serializers.Serializers;
import eu.scy.mobile.toolbroker.model.ELO;
import eu.scy.mobile.toolbroker.sample.localmodels.GeoImageCollector;
import eu.scy.mobile.toolbroker.sample.localmodels.serializers.GeoImageJSONSerializer;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import java.util.Vector;

/**
 * Created: 11.feb.2009 12:23:07
 *
 * @author Bjørge Næss
 */
public class SCYMobileDemo extends MIDlet implements CommandListener {
	private Form mainForm;
	private ToolBrokerMobileAPI toolBroker;
	private TextField eloIdField;
	private Form editELOForm;
	private TextField eloName;
	private ELO currentELO;
	private TextField eloContent;

	public Display getDisplay() {
        return Display.getDisplay(this);
    }
	protected void startApp() throws MIDletStateChangeException {
		// Register serializer for GeoImageContent
		Serializers.add("geoImageCollector", new GeoImageJSONSerializer());

		toolBroker = new ToolBrokerMobileAPIImpl();
		eloIdField = new TextField("Enter ELO Id", null, 2, TextField.NUMERIC);
		getDisplay().setCurrent(getMainForm());
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
	}

	private void updateELOForm(ELO elo) {
        if (editELOForm == null) {
	        editELOForm = new Form("Edit ELO", null);
			eloName = new TextField("ELO name", null, 100, TextField.PLAIN);
			eloContent = new TextField("ELO content", null, 100, TextField.PLAIN);
	        editELOForm.addCommand(new Command("Save", Command.ITEM, 1));
            editELOForm.setCommandListener(this);
        }
		eloName.setString(elo.getTitle());
		editELOForm.append(eloName);
		if (currentELO.getContent() instanceof GeoImageCollector) {
			editELOForm.append("The content of this ELO is a image collector. Displaying images:");
			GeoImageCollector content = (GeoImageCollector) currentELO.getContent();
			Vector images = content.getImages();
			for (int i = 0; i < images.size(); i++) {
				Image img = (Image) images.elementAt(i);
				ImageItem imgItem = new ImageItem(null, img, ImageItem.LAYOUT_DEFAULT, "Thumbnail", ImageItem.BUTTON);
				editELOForm.append(imgItem);
			}
		}
		else {
			eloContent.setString(elo.getContent().toString());
			editELOForm.append(eloContent);
		}
    }
	private Form getMainForm() {
        if (mainForm == null) {
	        mainForm = new Form("Welcome to the test!", null);
	        mainForm.append(eloIdField);
	        mainForm.addCommand(new Command("Get ELO!", Command.ITEM, 1));
            mainForm.setCommandListener(this);
        }
        return mainForm;
    }
	public void commandAction(Command command, Displayable displayable) {
		if (command.getLabel().equals("Save")) {
			currentELO.setTitle(eloName.getString());

			if (currentELO.getContent() instanceof String)
				currentELO.setContent(eloName.getString());
			else
				System.out.println("Unsupported content type");

			System.out.println("Current ELO is"+currentELO);

			toolBroker.updateELO(currentELO);
		}
		if (command.getLabel().equals("Get ELO!")) {
			// Retrieve the first ELO
			System.out.println("ELO??");
			currentELO = toolBroker.getELO(Integer.parseInt(eloIdField.getString()));
			mainForm.setTicker(new Ticker("Congratulations! You just retrieved an ELO!"));
			System.out.println("elo = " + currentELO.getContent());
			updateELOForm(currentELO);
			getDisplay().setCurrent(editELOForm);
		}
	}
}
