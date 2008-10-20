package eu.scy.tools.youtube.client;

import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;


public class VideoInput extends Panel {

    public static final String TOOL_ID = "VideoInput-ID";
    
    private Panel previewPanel;
    private TextField urlField;
    private TextArea descriptionArea;
    private TextField titleField;
    private VideoView videoView;

    public VideoInput() {
        super("Video Input");
        setId(TOOL_ID);
        
        final FormPanel formPanel = new FormPanel();
        formPanel.setFrame(true);  
        formPanel.setLabelWidth(100);  

        urlField = new TextField("Address", "url", 400);
        urlField.setValidator( new Validator() {

            public boolean validate(String value) throws ValidationException {
                return getIDFromURL(value) != null;
            }
        });
        urlField.setAllowBlank(false);  
        formPanel.add(urlField);  

        titleField = new TextField("Title", "title", 400);
        formPanel.add(titleField);

        descriptionArea = new TextArea("Description");
        descriptionArea.setWidth(400);
        formPanel.add(descriptionArea);

        Button previewButton = new Button("Preview");
        previewButton.addListener( new ButtonListenerAdapter() {

            public void onClick(Button button, EventObject e) {
                if (urlField.getText().equals("")) {
                    Window.alert("Please enter a URL.");
                } if (getIDFromURL(urlField.getText()) == null) {
                    Window.alert("Could not get ID from video Address. Please check the URL.");
                } else {
                    showPreview();
                }
            }
        });
        formPanel.addButton(previewButton);

        Button cancelButton = new Button("Cancel");
        cancelButton.addListener( new ButtonListenerAdapter() {
            
            public void onClick(Button button, EventObject e) {
                previewPanel.hide();
                urlField.setValue("");
                titleField.setValue("");
                descriptionArea.setValue("");
            } 
        });
        formPanel.addButton(cancelButton);
        
        Button saveButton = new Button("Save");
        saveButton.addListener( new ButtonListenerAdapter() {
            
            public void onClick(Button button, EventObject e) {
                Window.alert("Saving not implemented, yet");
            } 
        });
        formPanel.addButton(saveButton);
        
        add(formPanel);

        previewPanel = new Panel();
        videoView = new VideoView();
        videoView.setTitle("Preview");
        previewPanel.add(videoView);
        previewPanel.setVisible(false);

        add(previewPanel);
    }

    protected void showPreview() {
        String id = getIDFromURL(urlField.getText());
        if (id == null) {
            Window.alert("ERROR: Could not get ID from YouTube URL");
            previewPanel.hide();
            return;
        }
        videoView.showVideo(new VideoBean(id, titleField.getText(), descriptionArea.getText()));
        previewPanel.show();
    }

    /**
     * Returns the video id from a given youtube url
     * Eg."jb41_lzILxg" for "http://www.youtube.com/watch?v=jb41_lzILxg&feature=related"
     */
    public static native String getIDFromURL(String url) /*-{
        regex = "\\?v=([^&]+)";
        found = url.match(regex);

        if (found != null) {
            return(found[1]);        
        } else {
            return null;
        }
    }-*/;
}
