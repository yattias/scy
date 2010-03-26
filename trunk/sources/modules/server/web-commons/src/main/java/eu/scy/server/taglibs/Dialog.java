package eu.scy.server.taglibs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 11:40:04
 * To change this template use File | Settings | File Templates.
 */
public class Dialog extends TagSupport {

    private String url;
    private String title;
    private String width;
    private String height;
    private String dialogHeader = "";

    public int doEndTag() throws JspException {
        try {
            double id = Math.random();
            pageContext.getOut().write("<a href=\"javascript:loadDialog('"+getUrl()+"', '" + getDialogHeader() + "');\">" + getTitle() + "</a>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWidth() {
        if(width == null || width.length() == 0) return "400";
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        if(height == null || height.length() == 0) return "400";
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void setDialogHeader(String dialogHeader) {
        this.dialogHeader = dialogHeader;
    }
}
