package eu.scy.mobile.toolbroker.demo;

import eu.scy.mobile.toolbroker.demo.client.ELOUploader;
import eu.scy.mobile.toolbroker.demo.client.SCYWebserviceClientResponse;
import eu.scy.mobile.toolbroker.demo.model.ImageELO;
import eu.scy.mobile.toolbroker.demo.ui.CameraCanvas;
import eu.scy.mobile.toolbroker.demo.ui.GlobalCommands;
import eu.scy.mobile.toolbroker.demo.ui.ImageEloForm;
import eu.scy.mobile.toolbroker.demo.ui.MainForm;
import eu.scy.mobile.toolbroker.demo.ui.ProgressBar;

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
	private final Command takePictureCommand = new Command("Take a picture!", Command.ITEM, 1);
	private final Command exitCommand = new Command("Exit", Command.EXIT, 1);
	private ProgressBar progressBar;

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
		mainForm = new MainForm("Welcome to Mobile SCY!", null);
		mainForm.addCommand(takePictureCommand);
		mainForm.addCommand(GlobalCommands.CMD_EXIT);
		mainForm.setCommandListener(this);
		return mainForm;
	}

	private Canvas createCameraCanvas() {
		cameraCanvas = new CameraCanvas();
		cameraCanvas.addCommand(CameraCanvas.CMD_CAPTURE);
		cameraCanvas.addCommand(GlobalCommands.CMD_CANCEL);
		cameraCanvas.setCommandListener(this);
		return cameraCanvas;
	}

	private Form createEloForm(ImageELO elo) {
		eloForm = new ImageEloForm("Edit image", elo);
		eloForm.addCommand(ImageEloForm.CMD_SAVE);
		eloForm.addCommand(GlobalCommands.CMD_CANCEL);
		eloForm.setCommandListener(this);
		return eloForm;
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command.equals(takePictureCommand)) {
			getDisplay().setCurrent(createCameraCanvas());
		} else if (command.equals(CameraCanvas.CMD_CAPTURE)) {

			byte[] img = cameraCanvas.capture();

			ImageELO elo = new ImageELO();
			elo.setImage(img);

			getDisplay().setCurrent(createEloForm(elo));
		} else if (command.equals(ImageEloForm.CMD_SAVE)) {

			ImageELO elo = eloForm.getElo();
			elo.setTitle(eloForm.getTitleField().getString());
			elo.setComment(eloForm.getCommentField().getString());

			/**/

			progressBar = new ProgressBar("Please wait", "Uploading...");
			getDisplay().setCurrent(progressBar);
			progressBar.start();

			ELOUploader eloUploader = new ELOUploader(this, elo);
			eloUploader.start();

		} else if (command.equals(GlobalCommands.CMD_CANCEL)) {
			getDisplay().setCurrent(createMainForm());
		} else if (command.equals(GlobalCommands.CMD_EXIT)) {
			try {
				destroyApp(false);
				notifyDestroyed();
			} catch (MIDletStateChangeException e) {
				e.printStackTrace();
			}
		}
	}
	public void responseRecieved(SCYWebserviceClientResponse response) {
		Alert a;
		if (response.getCode() == SCYWebserviceClientResponse.RESPONSE_FAULT) {
			a = new Alert("Upload error");
			a.setString("Sorry, but an error occurred while uploading image: "+response.getMessage());
		}
		else  {
			a = new Alert("Image uploaded");
			a.setString("The image was successfully uploaded!\n\n" +
					"Response from server was: " + response.getMessage());
		}
		a.setTimeout(Alert.FOREVER);
		getDisplay().setCurrent(a, createMainForm());
	}
}
