/**
 * 
 */
package eu.scy.lab.client.desktop.north;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

import eu.scy.lab.client.mission.Mission;
import eu.scy.lab.client.mission.MissionInfoWindow;

/**
 * @author Sven
 */
public class NorthPanel extends Panel {
    
    private Mission mission;
    
    public NorthPanel(Mission mission) {
        super();
        this.mission = mission;
        buildPanel();
    }
    
    private void buildPanel() {
        
        setTitle("Mission: " + mission.getTitle());
        
        setLayout(new FitLayout());
        setIconCls("scylogo16x16");
        setHeight(80);
        setCollapsible(true);
        setBorder(true);
        
        HorizontalPanel mainPanel = new HorizontalPanel();
        
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        Panel iconPanel = new Panel();
        String html = "<p><center><img src=\"res/icons/Spreadsheetx45.png\"></p>";
        iconPanel.setHtml(html);
        iconPanel.setBorder(false);
        
        Panel panel = new Panel();
        panel.setLayout(new VerticalLayout());
        Label title = new Label();
        title.setHtml("<p><b>Mission:</b> " + mission.getTitle()+"</p>");
        Label lastVisited = new Label();
        lastVisited.setHtml("<p><b>Last Visited:</b> " + mission.getLastVisitedDate()+"</p>");
        
        panel.add(title);
        panel.add(lastVisited);
        panel.setBorder(false);
        
        Button showInfo = new Button("Show Info");
        showInfo.setIcon("res/icons/x16/Help2x16.png");
        showInfo.addListener(new ButtonListenerAdapter(){
            
            public void onClick(Button button, EventObject e) {
                MissionInfoWindow info = new MissionInfoWindow(mission);
                info.show();
            }
            
        });
        
        // XXX: ULTRA-dirty hack
        Label spacer = new Label();
        spacer.setHtml("&nbsp;&nbsp;&nbsp;&nbsp;");
        
        mainPanel.add(iconPanel);
        mainPanel.add(panel);
        mainPanel.add(showInfo);
        mainPanel.add(spacer);
        
        mainPanel.setCellHorizontalAlignment(iconPanel, HasHorizontalAlignment.ALIGN_LEFT);
        mainPanel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_LEFT);
        mainPanel.setCellHorizontalAlignment(showInfo, HasHorizontalAlignment.ALIGN_RIGHT);
        mainPanel.setCellHorizontalAlignment(spacer, HasHorizontalAlignment.ALIGN_RIGHT);
        
        add(mainPanel);
        
    }
    
}
