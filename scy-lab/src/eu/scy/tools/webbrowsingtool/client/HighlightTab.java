package eu.scy.tools.webbrowsingtool.client;

import java.util.Vector;

import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.LayoutData;


public class HighlightTab extends Panel {
    
    Vector<Note> notes;
    
    private Panel center; 
    
    public HighlightTab(Vector<Note> notes){
        super("Highlight");
        this.notes = notes;
        
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
        center.clear();
        center = buildCenterPanel();
        center.enable();
    }

    private Panel buildEastPanel() {
        Panel panel = new Panel();
        panel.setWidth(200);
        panel.setCollapsible(true);
        return panel;
    }

    private Panel buildCenterPanel() {
        Panel center = new Panel();
        center.setLayout(new FitLayout());
        center.add(notesPanel());
        return center;
    }

    private Panel notesPanel() {
        Panel panel = new Panel();
        panel.setLayout(new FitLayout());
        if (notes != null){
        Label[] labels = new Label[notes.size()];
        for (int i = 0; i<notes.size(); i++){
            Note note = notes.get(i);
            labels[i]=new Label(note.getText());
            ToolTip tip = new ToolTip();
            tip.setTitle(note.getTitle());
            tip.setHtml(note.getAnnotation());
            tip.setDismissDelay(15000);
            tip.setWidth(300);
            tip.setTrackMouse(true);
            tip.applyTo(labels[i]);
            panel.add(labels[i]);
        }
        }
        return panel;
    }
}
