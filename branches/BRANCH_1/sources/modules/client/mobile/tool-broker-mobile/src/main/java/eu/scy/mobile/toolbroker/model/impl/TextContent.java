package eu.scy.mobile.toolbroker.model.impl;

import eu.scy.mobile.toolbroker.model.ITextContent;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 25.feb.2009
 * Time: 13:58:14
 * To change this template use File | Settings | File Templates.
 */
public class TextContent implements ITextContent {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        return content;
    }
}
