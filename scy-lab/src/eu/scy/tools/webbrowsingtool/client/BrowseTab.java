package eu.scy.tools.webbrowsingtool.client;

import java.util.Vector;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;


public class BrowseTab extends Panel {

    private Vector<Note> notes;
    
    private HighlightTab highlightTab;
    
    public BrowseTab(HighlightTab highlightTab){
        super("Browse");
        this.highlightTab=highlightTab;
        
        setLayout(new FitLayout());
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
        
        BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);
        eastData.setSplit(true);
        eastData.setMinSize(175);
        eastData.setMaxSize(500);
        eastData.setMargins(new Margins(0, 0, 5, 0));
        
        panel.add(buildCenterPanel(),centerData );
        panel.add(buildEastPanel(),eastData );
        add(panel);
    }
    
    public BrowseTab(){
        this(null);
    }

    private Panel buildEastPanel() {
        Panel panel = new Panel();
        panel.setLayout(new VerticalLayout());
        panel.setWidth(200);
        panel.setCollapsible(true);
        Label labelTitle = new Label("Title");
        final TextField textFieldTitle = new TextField();
        Label labelAddText = new Label("Add Text");
        final TextArea textBox = new TextArea();
        Label labelComment = new Label("Comment:");
        final TextField textField = new TextField();
        Button addButton = new Button("Add!",new ButtonListenerAdapter(){

            public void onClick(Button button, EventObject e) {
               addNote(textFieldTitle.getText(),textBox.getText(),textField.getText()); 
               System.out.println(notes.get(notes.size()-1));
               updateHighlightTab();
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
        Frame iFrame = new Frame("http://www.google.de");
        panel.add(iFrame);
        return panel;
    }

    private void addNote(String title, String text, String annotation){
        if (notes==null){
            notes=new Vector<Note>();
        }
        notes.add(new Note(title,text,annotation));
    }
 
    private void updateHighlightTab() {
        if (highlightTab!=null){
            highlightTab.update();
        }
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
     * @return the notes
     */
    public Vector<Note> getNotes() {
        return notes;
    }

    
    /**
     * @param notes the notes to set
     */
    public void setNotes(Vector<Note> notes) {
        this.notes = notes;
    }
    
    

}
