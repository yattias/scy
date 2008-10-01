package eu.scy.lab.client.util;


public class Gears {

    public static native boolean checkForGears() /*-{
        //FIXME: Normally should also check for google.gears, but I have no idea on how to get that object from here
        if ($wnd.google) {
            return true;
        } else {
            return false;
        }
    }-*/;
}
