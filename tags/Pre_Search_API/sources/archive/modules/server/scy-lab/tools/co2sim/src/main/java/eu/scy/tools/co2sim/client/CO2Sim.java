package eu.scy.tools.co2sim.client;

import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

import com.gwtext.client.widgets.Panel;


public class CO2Sim extends Panel {

    public static final String TOOL_ID = "CO2Simulation";
    private static final String SWF_PATH = "flash/ScyVerticalDemo.swf";

    public CO2Sim() {
        super("CO2-Simulation");
        setClosable(true);
        setId(TOOL_ID);
        SWFWidget swfWidget = new SWFWidget(SWF_PATH, 800, 500);
        add(swfWidget);
    }
}
