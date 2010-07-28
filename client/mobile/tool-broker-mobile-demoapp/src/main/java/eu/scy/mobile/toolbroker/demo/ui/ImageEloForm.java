package eu.scy.mobile.toolbroker.demo.ui;

import eu.scy.mobile.toolbroker.demo.model.ImageELO;

import javax.microedition.lcdui.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 20.mar.2009
 * Time: 17:56:19
 * To change this template use File | Settings | File Templates.
 */
public class ImageEloForm extends Form {

	private boolean isInitialized = false;

	private ImageItem imageField;
	private TextField titleField;
	private TextField commentField;
	private ImageELO elo;
	public static final Command CMD_SAVE = new Command("Save", Command.OK, 1);

	public ImageEloForm(String s) {
		super(s);
		setElo(new ImageELO());
	}
	public ImageEloForm(String s, ImageELO elo) {
		super(s);
		setElo(elo);
	}

	public ImageELO getElo() {
		return elo;
	}

	public void setElo(ImageELO elo) {
		this.elo = elo;
		initialize();
		update();
	}

	public ImageItem getImageField() {
		return imageField;
	}

	public TextField getTitleField() {
		return titleField;
	}

	public TextField getCommentField() {
		return commentField;
	}

	private void update() {
		try {
			imageField.setImage(Image.createImage(new ByteArrayInputStream(elo.getImage())));
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		titleField.setString(elo.getTitle());
		commentField.setString(elo.getComment());
	}

	private void initialize() {
		if (isInitialized) return;
		imageField = new ImageItem("Image", null, ImageItem.LAYOUT_LEFT, "");
		imageField.setPreferredSize(-1, 100);
		titleField = new TextField("Title", "", 128, TextField.ANY);
		commentField = new TextField("Comment", "", 200, TextField.ANY);
		commentField.setPreferredSize(-1, 100);
		append(imageField);
		append(titleField);
		append(commentField);
		isInitialized = true;
	}
}
