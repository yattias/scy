package eu.scy.colemo.client;

import eu.scy.colemo.clientframework.ClientFrameworkHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.nov.2008
 * Time: 20:05:54
*/
public class ColemoApplet extends JApplet {

    public ColemoApplet() throws HeadlessException {
        //throw new NullPointerException("Heisann!");
    }

    @Override
    public void init() {
        super.init();
        ApplicationController.getDefaultInstance().setApplet(this);
        
        String userName = getParameter("username");
        String password = getParameter("password");
        String url = getDocumentBase().getHost();

        MainFrame frame = new MainFrame();
        frame.setUsername(userName);
        frame.setPassword(password);
        frame.setHost(url);

        ClientFrameworkHandler.getClientFrameworkHandler().postEvent("COLEMO -- INITIALIZED!");

    }

    @Override
    public void start() {
        super.start();
    }
}
