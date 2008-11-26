package eu.scy.tools.webbrowsingtool.client;

import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;


public class HighlightTab extends Panel {
    
    private Panel center;
    
    private NotesPanel notesPanel;
    
    public HighlightTab(NotesPanel notesPanel){
        super("Highlight");
        this.notesPanel = notesPanel;
        
        setLayout(new FitLayout());
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
        
        BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);
        eastData.setSplit(true);
        eastData.setMinSize(175);
        eastData.setMaxSize(500);
        eastData.setMargins(new Margins(0, 0, 5, 0));
        
        center = buildCenterPanel();
        panel.add(center,centerData );
        panel.add(buildEastPanel(),eastData );
        add(panel);
    }
    
    public void update(){
    }

    private Panel buildEastPanel() {
        final Panel panel = new Panel();
        panel.setWidth(200);
        panel.setCollapsible(true);
        final Button highlightButton = new Button("Highlight!",new ButtonListenerAdapter(){

            public void onClick(Button button, EventObject e) {
                Window.alert("Feature not implemented yet");
            }

        });
        panel.add(highlightButton);
        return panel;
    }

    private Panel buildCenterPanel() {
        Panel center = new Panel();
        center.setLayout(new FitLayout());
        center.add(notesPanel);
        return center;
    }

    

    
    /**
     * @return the notesPanel
     */
    public NotesPanel getNotesPanel() {
        return notesPanel;
    }

    
    /**
     * @param notesPanel the notesPanel to set
     */
    public void setNotesPanel(NotesPanel notesPanel) {
        this.notesPanel = notesPanel;
    }
    
    
    
    
}
