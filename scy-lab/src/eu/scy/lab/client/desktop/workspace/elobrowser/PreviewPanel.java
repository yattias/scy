package eu.scy.lab.client.desktop.workspace.elobrowser;

import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class PreviewPanel extends Panel {
    
    public static final String DEFAULT_IMAGE_URL = "res/icons/Document.png";
    
    private Label nameLabel;
    
    private String name;
    
    private Label authorLabel;
    
    private String author;
    
    private Label dateLabel;
    
    private String date;
    
    private Button workOnElo;
    
    private String iconUrl;
    
    private Panel subpanel;
    
    //    private Image img;
    
    private Label imageLabel;
    
    public PreviewPanel() {
        this("", "", "", DEFAULT_IMAGE_URL);
    }
    
    public PreviewPanel(String name, String author, String date) {
        this(name, author, date, DEFAULT_IMAGE_URL);
    }
    
    public PreviewPanel(String name, String author, String date, String imageUrl) {
        super("Preview");
        setFrame(true);
        // setLayout (new FitLayout());
        setLayout(new HorizontalLayout(15));
        setBorder(true);
        this.name = name;
        this.author = author;
        this.date = date;
        this.iconUrl = imageUrl;
        
        // scaling the image
        Label imageLabel = new Label();
        imageLabel.setId("imagelabel");
        imageLabel.setHtml("<p><center><img src=\"" + DEFAULT_IMAGE_URL + "\"></p>");
        
        subpanel = new Panel();
        subpanel.setId("subpanel");
        subpanel.setBorder(false);
        subpanel.setLayout(new VerticalLayout());
        
        nameLabel = new Label("Author: " + author);
        subpanel.add(nameLabel);
        authorLabel = new Label("Created by: " + name);
        subpanel.add(authorLabel);
        dateLabel = new Label("Date: " + date);
        subpanel.add(dateLabel);
        
        workOnElo = new Button("Work on ELO");
        
        //        add(img);
        add(imageLabel);
        add(subpanel);
        addButton(workOnElo);
        
    }
    
    public Panel getPreviewPanel() {
        
        return this;
    }
    
    public void update(String title, String author, String date, String version, String id) {
        
        // scaling the updated image proportional
        //        img.setUrl(imageUrl);
        //        this.remove("imagelabel");
        //        imageLabel.destroy();
        this.remove("subpanel");
        imageLabel = new Label();
        imageLabel.setId("imagelabel");
        imageLabel.setHtml("<p><center><img src=\"" + DEFAULT_IMAGE_URL + "\"></p>");
        this.add(imageLabel);
        this.add(subpanel);
        
        // creating a temporary new Instance, because when setting a new ImageURL, the measurements aren't updated
        //        Image tempImg = new Image(imageUrl);
        //
        //        double oldWidth = tempImg.getWidth();
        //        double oldHeight = tempImg.getHeight();
        //        int width = (int) ((double) (oldWidth * 50.0) / ((double) oldHeight));
        //        img.setSize(Integer.toString(width) + "px", "50px");
        
        // update labels
        nameLabel.setText("Title: " + title);
        authorLabel.setText("Created by: " + author);
        dateLabel.setText("Date: " + date);
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * @param author
     *            the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    
    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }
    
    /**
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }
    
    /**
     * @return the iconUrl
     */
    public String getIconUrl() {
        return iconUrl;
    }
    
    /**
     * @param iconUrl
     *            the iconUrl to set
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    
}