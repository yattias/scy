package eu.scy.mobile.toolbroker.demo.ui;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 20.mar.2009
 * Time: 17:55:55
 * To change this template use File | Settings | File Templates.
 */
public class MainForm extends Form {

	private Image bg;

	public MainForm(String s) {
		this(s, null);
	}

	public MainForm(String s, Item[] items) {
		super(s, items);
		initialize();
	}
	private void initialize() {
		InputStream is = getClass().getResourceAsStream("logo_scy_mobile.png");
		try {
			bg = Image.createImage(is);
			append(bg);
		} catch (IOException e) {
			System.err.println("Unable to create background image");
			e.printStackTrace();
		}
		append("Welcome to the SCY mobile data collector");
	}
}
