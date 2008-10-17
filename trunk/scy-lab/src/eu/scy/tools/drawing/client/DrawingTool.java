package eu.scy.tools.drawing.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gwtai.applet.client.Applet;
import com.google.gwt.gwtai.applet.client.AppletJSUtil;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DrawingTool extends VerticalPanel {

    public DrawingTool() {
        setTitle("Drawing Tool");
        final TestApplet applet = (TestApplet) GWT.create(TestApplet.class);
        Widget widgetApplet = AppletJSUtil.createAppletWidget((Applet) applet);
        add(widgetApplet);
    }
}
