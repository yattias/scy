package eu.scy.colemo.clientframework;


import eu.scy.colemo.client.ApplicationController;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2009
 * Time: 13:05:24
 * To change this template use File | Settings | File Templates.
 */
public class ClientFrameworkHandler {

    private final static String SCY_INFOBUS = "SCYInfobus";
    private final static String POST_EVENT = "postEvent";

    private static ClientFrameworkHandler defaultInstance;

    private ClientFrameworkHandler() {

    }

    public static ClientFrameworkHandler getClientFrameworkHandler() {
        if (defaultInstance == null) defaultInstance = new ClientFrameworkHandler();
        return defaultInstance;
    }

    public void postEvent(String event) {
        call(event);
    }

    private void call(String command) {
        try {
            Method getWindow = null, callMethod = null;
            Object jswin = null;
            Class jsObjectClass = Class.forName("netscape.javascript.JSObject");
            Method ms[] = jsObjectClass.getMethods();
            for (int i = 0; i < ms.length; i++) {
                if (ms[i].getName().compareTo("getWindow") == 0) getWindow = ms[i];
                else if (ms[i].getName().compareTo("call") == 0) callMethod = ms[i];
            }

            Object a[] = new Object[1];
            a[0] = ApplicationController.getDefaultInstance().getApplet();

            jswin = getWindow.invoke(jsObjectClass, a);
            Object[] args = new Object[2];
            args[0] = "postEvent";
            args[1] = new String[]{command};
            Object result = callMethod.invoke(jswin, args);


        } catch (Exception e) {
            e.printStackTrace();

        }


    }

}
