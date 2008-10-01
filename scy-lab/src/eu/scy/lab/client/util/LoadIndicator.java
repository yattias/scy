package eu.scy.lab.client.util;

import com.gwtext.client.core.Ext;
import com.gwtext.client.core.ExtElement;


public class LoadIndicator {
    
    public static void start(){
        ExtElement element = Ext.getBody();
        element.mask("loading...");
    }
    
    public static void stop(){
        ExtElement element = Ext.getBody();
        if (element.isMasked()){
            element.unmask();
        }
    }

}
