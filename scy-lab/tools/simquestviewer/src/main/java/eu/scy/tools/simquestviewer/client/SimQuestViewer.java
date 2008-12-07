package eu.scy.tools.simquestviewer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gwtai.applet.client.AppletJSUtil;
import com.google.gwt.user.client.ui.Widget;

public class SimQuestViewer extends com.gwtext.client.widgets.Panel {

    public static final String SIMQUESTVIEWER_ID = "simquestviewer-tool";

    public SimQuestViewer() {
        super("SimQuestViewer");
        setId(SIMQUESTVIEWER_ID);
        setClosable(true);
        final SCYSimQuestViewerAppletIntegration applet = (SCYSimQuestViewerAppletIntegration) GWT.create(SCYSimQuestViewerAppletIntegration.class);
        Widget widgetApplet = AppletJSUtil.createAppletWidget(applet);
        add(widgetApplet);
    }
}
