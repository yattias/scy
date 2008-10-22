package eu.scy.tools.youtube.client;

import com.google.gwt.widgetideas.client.YouTubeViewer;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;


public class VideoView extends Panel {
    
    private YouTubeViewer video;
    private Label titleLabel;
    private Label descriptionLabel;
    
    // FIXME: beautify
    public VideoView() {
        super("Video Viewer");
        setLayout(new HorizontalLayout(2));

        Panel leftPanel = new Panel();
        leftPanel.setBorder(false);
        leftPanel.setPaddings(10);
        leftPanel.setLayout(new VerticalLayout());
        
        Panel topPanel = new Panel();
        topPanel.setPaddings(10);
        topPanel.setBorder(false);
        titleLabel = new Label();
        topPanel.add(titleLabel);
        leftPanel.add(topPanel);
        
        video = new YouTubeViewer("");
        video.setWidth("425px");
        video.setHeight("344px");
        leftPanel.add(video);
        add(leftPanel);
        
        Panel rightPanel = new Panel();
        rightPanel.setBorder(false);
        rightPanel.setPaddings(10);
        
        descriptionLabel = new Label();
        rightPanel.add(descriptionLabel);
        add(rightPanel);
        
        showVideo(new VideoBean("jb41_lzILxg", "Lorem ipsum!", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."));
    }
    
    public void showVideo(VideoBean videoBean) {
        video.setMovieID(videoBean.getMovieID());
        // FIXME: Externalise style?
        titleLabel.setHtml("<center><h1> " + videoBean.getTite() + "</h1></center>");
        descriptionLabel.setText(videoBean.getDescription());
    }
}
