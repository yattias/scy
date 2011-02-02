package eu.scy.tools.webbrowsingtool.client;

import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.layout.FitLayout;


public class WindowIFrame extends Window {
    
    public WindowIFrame(String url){
        super(url);
        HTMLPanel panel = new HTMLPanel();
        setHeight(400);
        setWidth(300);
        panel.setAutoLoad(url);
        panel.setLayout(new FitLayout());
        add(panel);
    }

}
