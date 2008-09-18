package eu.scy.lab.client.desktop.workspace.elobrowser;

import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class PreviewPanel extends Panel {

	public static final String DEFAULT_IMAGE_URL = "res/images/defaultIcon.png";
	private Label nameLabel;
	private String name;
	private Label authorLabel;
	private String author;
	private Label dateLabel;
	private String date;
	private Button workOnElo;
	private String iconUrl;
	private Image img;

	public PreviewPanel(){
		this("","","",DEFAULT_IMAGE_URL);
	}
	
	public PreviewPanel(String name, String author, String date){
		this(name, author,date,DEFAULT_IMAGE_URL);
	}

	public PreviewPanel(String name, String author, String date, String imageUrl) {
		super("Preview");
		setFrame(true);
//		setLayout (new FitLayout());
		setLayout(new HorizontalLayout(15));
		setBorder(true);
		this.name = name;
		this.author = author;
		this.date = date;
		this.iconUrl = imageUrl;
		
		img = new Image(iconUrl);
		img.setHeight("50px");

		Panel subpanel = new Panel();
		subpanel.setBorder(false);
		subpanel.setLayout(new VerticalLayout());

		nameLabel = new Label("Author: " + author);
		subpanel.add(nameLabel);
		authorLabel = new Label("Created by: " + name);
		subpanel.add(authorLabel);
		dateLabel = new Label("Date: " + date);
		subpanel.add(dateLabel);

		workOnElo = new Button("Work on ELO");

		add(img);
		add(subpanel);
		addButton(workOnElo);

	}

	public Panel getPreviewPanel() {

		return this;
	}

	public void update(String name, String author, String date, String imageUrl){
	img.setUrl(imageUrl);
	img.setHeight("50px");
	nameLabel.setText("Author: " + author);
	authorLabel.setText("Created by: "+ name);
	dateLabel.setText("Date: "+ date);
}
}