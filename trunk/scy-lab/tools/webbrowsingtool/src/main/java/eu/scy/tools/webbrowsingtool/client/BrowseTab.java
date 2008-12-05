package eu.scy.tools.webbrowsingtool.client;

import java.util.Vector;

import com.google.gwt.user.client.ui.Frame;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;


public class BrowseTab extends Panel {
    
    public static String DEFAULT_URL="http://en.wikipedia.org/wiki/Java_(programming_language)";
    
    private String url;

    private HighlightTab highlightTab;
    
    private Frame iFrame;
    
    public BrowseTab(HighlightTab highlightTab,String url){
        super("Browse");
        this.highlightTab=highlightTab;
        this.url = url;
        
        setLayout(new FitLayout());
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
        
        BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);
        eastData.setSplit(true);
        eastData.setMinSize(200);
        eastData.setMaxSize(200);
        eastData.setMargins(new Margins(0, 0, 5, 0));
        
        panel.add(buildCenterPanel(),centerData );
        panel.add(buildEastPanel(),eastData );
        add(panel);
    }
    
    public BrowseTab(HighlightTab highlightTab){
        this(highlightTab,DEFAULT_URL);
    }
    
    public BrowseTab(){
        this(new HighlightTab(new NotesPanel(new Vector<Note>())));
    }

    private Panel buildEastPanel() {
        Panel panel = new Panel();       
        panel.setLayout(new VerticalLayout());
        panel.setPaddings(5, 4, 4, 5);
        panel.setWidth(200);
        panel.setCollapsible(true);
        Label labelTitle = new Label("Title:");
        final TextField textFieldTitle = new TextField();
        textFieldTitle.setWidth(190);
        Label labelAddText = new Label("Add Text:");
        final TextArea textBox = new TextArea();
        textBox.setWidth(190);
        textBox.setHeight(200);
        Label labelComment = new Label("Comment:");
        final TextField textField = new TextField();
        textField.setWidth(190);
        Button addButton = new Button("Add!",new ButtonListenerAdapter(){
            public void onClick(Button button, EventObject e) {
               highlightTab.getNotesPanel().addNote(textFieldTitle.getValueAsString(),textBox.getValueAsString(),textField.getValueAsString(),iFrame.getUrl()); 
            }
        });
        panel.add(labelTitle);
        panel.add(textFieldTitle);
        panel.add(labelAddText);
        panel.add(textBox);
        panel.add(labelComment);
        panel.add(textField);
        panel.add(addButton);

        return panel;
    }

    private Panel buildCenterPanel() {
        Panel panel = new Panel();
        panel.setLayout(new FitLayout());
        iFrame = new Frame(DEFAULT_URL);
        panel.add(iFrame);
        return panel;
    }
    
    /**
     * @return the highlightTab
     */
    public HighlightTab getHighlightTab() {
        return highlightTab;
    }

    
    /**
     * @param highlightTab the highlightTab to set
     */
    public void setHighlightTab(HighlightTab highlightTab) {
        this.highlightTab = highlightTab;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }


}
