package eu.scy.tools.webbrowsingtool.client;

import com.gwtext.client.widgets.SyntaxHighlightPanel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.layout.FitLayout;


public class HTMLWindow extends Window {

    public HTMLWindow(String html){
        super("HTML");
        setHeight(400);
        setWidth(400);
//        setAutoScroll(true);
        SyntaxHighlightPanel panel = new SyntaxHighlightPanel(html,SyntaxHighlightPanel.SYNTAX_HTML);
        panel.setLayout(new FitLayout());
//        panel.setAutoScroll(false);
        
        add(panel);
        
    }
    
}
