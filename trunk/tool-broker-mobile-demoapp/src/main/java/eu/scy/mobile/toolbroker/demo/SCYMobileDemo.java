package eu.scy.mobile.toolbroker.demo;

import eu.scy.mobile.toolbroker.demo.model.ImageELO;
import eu.scy.mobile.toolbroker.demo.ui.CameraCanvas;
import eu.scy.mobile.toolbroker.demo.ui.ImageEloForm;
import eu.scy.mobile.toolbroker.demo.ui.MainForm;
import eu.scy.mobile.toolbroker.demo.ui.GlobalCommands;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Created: 11.feb.2009 12:23:07
 *
 * @author Bjørge Næss
 */
public class SCYMobileDemo extends MIDlet implements CommandListener {
	private MainForm mainForm;
	private ImageEloForm eloForm;
	private CameraCanvas cameraCanvas;

	// Commands
	private final Command takePictureCommand = new Command("Take a picture!",Command.ITEM, 1);
	private final Command exitCommand = new Command("Exit",Command.EXIT, 1);

	public Display getDisplay() {
		return Display.getDisplay(this);
	}

	protected void startApp() throws MIDletStateChangeException {
		getDisplay().setCurrent(createMainForm());
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
	}

	private Form createMainForm() {
		if (mainForm == null) {
			mainForm = new MainForm("Welcome to Mobile SCY!", null);
			mainForm.addCommand(takePictureCommand);
			mainForm.setCommandListener(this);
		}
		return mainForm;
	}

	private Canvas createCameraCanvas() {
		if (cameraCanvas == null) {
			cameraCanvas = new CameraCanvas();
			cameraCanvas.addCommand(CameraCanvas.CMD_CAPTURE);
			cameraCanvas.addCommand(GlobalCommands.CMD_CANCEL);
			cameraCanvas.setCommandListener(this);
		}
		return cameraCanvas;
	}

	private Form createEloForm(ImageELO elo) {
		if (eloForm == null) {
			eloForm = new ImageEloForm("Edit image", elo);
			eloForm.addCommand(ImageEloForm.CMD_SAVE);
			eloForm.addCommand(GlobalCommands.CMD_CANCEL);
			eloForm.setCommandListener(this);
		}
		return eloForm;
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command.equals(takePictureCommand)) {
			getDisplay().setCurrent(createCameraCanvas());
		}
		else if (command.equals(CameraCanvas.CMD_CAPTURE)) {

			Image i = cameraCanvas.capture();

			ImageELO elo = new ImageELO();
			elo.setImage(i);

			getDisplay().setCurrent(createEloForm(elo));
		}
		else if (command.equals(ImageEloForm.CMD_SAVE)) {
			Alert a = new Alert("Image saved", "The image was sendt to your desktop! Please go there for more happy times!", null, null);
			a.setTimeout(Alert.FOREVER);
			getDisplay().setCurrent(a);
		}
		else if (command.equals(GlobalCommands.CMD_CANCEL)) {
			getDisplay().setCurrent(createMainForm());
		}
	}
}
