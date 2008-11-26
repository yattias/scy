package eu.scy.tools.webbrowsingtool.client;

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

//    private Vector<Note> notes;
    
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
        eastData.setMinSize(200);
        eastData.setMaxSize(200);
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
               highlightTab.getNotesPanel().addNote(textFieldTitle.getText(),textBox.getText(),textField.getText()+"source: "); 
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

//    private void addNote(String title, String text, String annotation){
//        if (highlightTab.getNotes()==null){
//            highlightTab.setNotes(new Vector<Note>());
//        }
//        highlightTab.getNotes().add(new Note(title,text,annotation));
//        updateHighlightTab();
//    }
 
//    private void updateHighlightTab() {
//        if (highlightTab!=null){
//            highlightTab.update();
//        }
//    }

    
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
        
    

}
