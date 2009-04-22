package eu.scy.mobile.toolbroker.model;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.mar.2009
 * Time: 11:03:46
 * To change this template use File | Settings | File Templates.
 */
public interface IELO {
    String getTitle();

    void setTitle(String title);

    void setId(int id);

    int getId();

    Object getContent();

    void setContent(Object content);

    Vector getChildren();

    void addChildELO(IELO elo);

    void setChildren(Vector children);
}
