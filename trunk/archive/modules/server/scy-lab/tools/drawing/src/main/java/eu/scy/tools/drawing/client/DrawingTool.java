package eu.scy.tools.drawing.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gwtai.applet.client.AppletJSUtil;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DrawingTool extends VerticalPanel {

    public static final String DRAWINGTOOL_ID = "drawing-tool";
    
    public DrawingTool() {
        Button b = new Button("Create applet");
        b.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                final TestApplet applet = (TestApplet) GWT.create(TestApplet.class);
                Widget widgetApplet = AppletJSUtil.createAppletWidget(applet);
                add(widgetApplet);
            }
        });
        add(b);
    }
}
