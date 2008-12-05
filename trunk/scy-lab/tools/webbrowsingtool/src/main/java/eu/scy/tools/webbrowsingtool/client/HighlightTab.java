package eu.scy.tools.webbrowsingtool.client;

import com.google.gwt.user.client.ui.RichTextArea;
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
    
    private RichTextArea textArea;
    
    private HTMLWindow htmlWindow;
    
    public HighlightTab(RichTextArea textArea){
        super("Highlight");
        this.textArea = textArea;
        
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
                if (textArea.getBasicFormatter()!=null){
                    textArea.getExtendedFormatter().setBackColor("yellow");
                }
             }
        });
        
        final Button underlineButton = new Button("Underline!",new ButtonListenerAdapter(){

            public void onClick(Button button, EventObject e) {
                if (textArea.getBasicFormatter()!=null)
                {
                        textArea.getBasicFormatter().toggleUnderline();
                }
             }
        });
 
        
        Button toggleBold = new Button("Bold",new ButtonListenerAdapter(){
            public void onClick(Button button, EventObject e) {
               if (textArea.getBasicFormatter()!=null)
               {
                       textArea.getBasicFormatter().toggleBold();
               }
            }
            });

        Button showHTML = new Button("Show HTML File",new ButtonListenerAdapter(){
            public void onClick(Button button, EventObject e) {
                   htmlWindow = new HTMLWindow(textArea.getHTML());
                   htmlWindow.show();
            }
            });
        
        //Testing:
        Button testExternalFrame = new Button("testExternalFrame",new ButtonListenerAdapter(){
            public void onClick(Button button, EventObject e) {
               WindowIFrame windowIFrame = new WindowIFrame("http://www.google.de");
               windowIFrame.show();
            }
            });
        
        
        panel.add(toggleBold);
        panel.add(highlightButton);
        panel.add(underlineButton);
        panel.add(showHTML);
        panel.add(testExternalFrame);
        return panel;
    }

    private Panel buildCenterPanel() {
        Panel center = new Panel();
        center.setLayout(new FitLayout());
        center.add(textArea);
        return center;
    }

    
    /**
     * @return the textArea
     */
    public RichTextArea getTextArea() {
        return textArea;
    }

    
    /**
     * @param textArea the textArea to set
     */
    public void setTextArea(RichTextArea textArea) {
        this.textArea = textArea;
    }

    

    
    
    
    
}
