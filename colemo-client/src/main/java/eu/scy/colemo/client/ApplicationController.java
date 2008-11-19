package eu.scy.colemo.client;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:29:12
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationController {

    private static ApplicationController defaultInstance;

    private GraphicsDiagram graphicsDiagram;
    private MainFrame mainFrame;

    public static ApplicationController getDefaultInstance() {
        if(defaultInstance == null) defaultInstance = new ApplicationController();
        return defaultInstance;
    }

    private ApplicationController() {

    }

    public GraphicsDiagram getGraphicsDiagram() {
        return graphicsDiagram;
    }

    public void setGraphicsDiagram(GraphicsDiagram graphicsDiagram) {
        this.graphicsDiagram = graphicsDiagram;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}
