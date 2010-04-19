package eu.scy.global.client;

import com.gwtext.client.core.Ext;
import com.gwtext.client.core.ExtElement;

public class LoadIndicator {

    public static void start() {
        start("loading...");
    }

    public static void start(String message) {
        ExtElement element = Ext.getBody();
        element.mask(message);
    }

    public static void stop() {
        ExtElement element = Ext.getBody();
        if (element.isMasked()) {
            element.unmask();
        }
    }

}
