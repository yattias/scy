package eu.scy.eloimporter.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileUtils {
    
    public static String getMimeType(File fileUrl) {
        try {
            String type = null;
            URL u = new URL(fileUrl.toString());
            URLConnection uc = u.openConnection();
            type = uc.getContentType();
            return type;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "text/xml";
    }
}
