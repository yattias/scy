package eu.scy.colemo.client;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.nov.2008
 * Time: 20:05:54
 * To change this template use File | Settings | File Templates.
 */
public class ColemoApplet extends JApplet {

    public ColemoApplet() throws HeadlessException {
        //throw new NullPointerException("Heisann!");
    }

    @Override
    public void init() {
        super.init();
        MainFrame frame = new MainFrame();

        String userName = getParameter("username");
        String password = getParameter("password");
        String url = getDocumentBase().getHost();

    }

    @Override
    public void start() {
        super.start();    //To change body of overridden methods use File | Settings | File Templates.
        //throw new NullPointerException("start");
    }
}
