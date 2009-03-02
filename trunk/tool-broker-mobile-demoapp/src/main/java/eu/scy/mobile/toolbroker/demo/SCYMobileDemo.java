package eu.scy.mobile.toolbroker.demo;

import eu.scy.mobile.toolbroker.model.*;
import eu.scy.mobile.toolbroker.model.impl.MobileToolBrokerImpl;
import eu.scy.mobile.toolbroker.IELOService;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.Connector;
import java.util.Vector;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.sun.me.web.request.Arg;
import com.sun.me.web.request.ProgressInputStream;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

/**
 * Created: 11.feb.2009 12:23:07
 *
 * @author Bjørge Næss
 */
public class SCYMobileDemo extends MIDlet implements CommandListener, RequestListener {
	private Form mainForm;
	private IELOService toolBroker;
	private TextField eloIdField;
	private Form editELOForm;
	private TextField eloName;
	private IELO currentELO;
	private TextField eloContent;

	public Display getDisplay() {
        return Display.getDisplay(this);
    }
	protected void startApp() throws MIDletStateChangeException {

                
        toolBroker = new MobileToolBrokerImpl().getELOService();
        System.out.println("toolBroker = " + toolBroker);
		eloIdField = new TextField("Enter ELO Id", null, 2, TextField.NUMERIC);
		getDisplay().setCurrent(getMainForm());
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
	}

	private void updateELOForm(IELO elo) {
        if (editELOForm == null) {
	        editELOForm = new Form("Edit ELO", null);
			eloName = new TextField("ELO name", null, 100, TextField.PLAIN);
			eloContent = new TextField("ELO content", null, 100, TextField.PLAIN);
	        editELOForm.addCommand(new Command("Save", Command.ITEM, 1));
            editELOForm.setCommandListener(this);
        }
		eloName.setString(elo.getTitle());
		editELOForm.append(eloName);
        Object content = currentELO.getContent();
		if (content != null) {
            if (content instanceof IGeoImageList) {
                editELOForm.append("The content of this ELO is a image collector. Displaying images:");
                IGeoImageList imageList = (IGeoImageList) content;
                Vector images = imageList.getImages();
                for (int i = 0; i < images.size(); i++) {
                    String location = (String) images.elementAt(i);
                    Image img = loadImage(location);
                    ImageItem imgItem = new ImageItem(null, img, ImageItem.LAYOUT_DEFAULT, "Thumbnail", ImageItem.BUTTON);
                    editELOForm.append(imgItem);
                }
            }
            else {
                eloContent.setString(content.toString());
                editELOForm.append(eloContent);
            }
        }
        else {
            editELOForm.append("<no content>");
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

            Object content = currentELO.getContent();
			if (content instanceof ITextContent)
				((ITextContent)content).setContent(eloContent.getString());
			else
				System.out.println("Unsupported content type: "+ content.getClass());

			System.out.println("Current ELO is "+currentELO.getContent().getClass());

			toolBroker.updateELO(currentELO);
		}
		if (command.getLabel().equals("Get ELO!")) {
			// Retrieve the first ELO
			System.out.println("ELO??");
			currentELO = toolBroker.getELO(Integer.parseInt(eloIdField.getString()));
			mainForm.setTicker(new Ticker("Congratulations! You just retrieved an ELO!"));
			System.out.println("elo = " + currentELO);
			updateELOForm(currentELO);
			getDisplay().setCurrent(editELOForm);
		}
	}

    public Image loadImage(final String location) {
        HttpConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpConnection) Connector.open(location);
                        conn.setRequestProperty("accept", "image/*");

            final int responseCode = conn.getResponseCode();
            if (responseCode != HttpConnection.HTTP_OK) return null;

            final int totalToReceive = conn.getHeaderFieldInt(Arg.CONTENT_LENGTH, 0);
            is = new ProgressInputStream(conn.openInputStream(), totalToReceive, this, null, 1024);

            final ByteArrayOutputStream bos = new ByteArrayOutputStream(Math.max(totalToReceive, 8192));
            final byte[] buffer = new byte[4096];
            for (int nread = is.read(buffer); nread >= 0; nread = is.read(buffer)) {
                bos.write(buffer, 0, nread);
            }
            return Image.createImage(new ByteArrayInputStream(bos.toByteArray()));
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
             try {
                if (is != null)is.close();
                if (conn != null) conn.close();

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }

    public void done(Object o, Response response) throws Exception {
        
    }

    public void readProgress(Object o, int i, int i1) {
        
    }

    public void writeProgress(Object o, int i, int i1) {

    }
}
