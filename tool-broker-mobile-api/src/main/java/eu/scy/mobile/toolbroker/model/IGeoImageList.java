package eu.scy.mobile.toolbroker.model;

import javax.microedition.lcdui.Image;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.mar.2009
 * Time: 12:26:34
 * To change this template use File | Settings | File Templates.
 */
public interface IGeoImageList {

    String getName();

    void setName(String name);

    Vector getImages();

    void setImages(Vector images);

    void addImage(String image);
}
