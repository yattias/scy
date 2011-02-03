package eu.scy.core.model.transfer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.feb.2011
 * Time: 12:19:37
 * To change this template use File | Settings | File Templates.
 */
public class RawData extends BaseXMLTransfer {

    private String thumbnail;
    private String fullScreen;
    private String text;
    private String dataSet;

    public String getThumbnail() {
        try {
            return URLEncoder.encode(thumbnail, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            return thumbnail;
        }
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFullScreen() {
        try {
            return URLEncoder.encode(fullScreen, "UTF-8");
        } finally {
            return fullScreen;
        }
    }

    public void setFullScreen(String fullScreen) {
        this.fullScreen = fullScreen;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}
