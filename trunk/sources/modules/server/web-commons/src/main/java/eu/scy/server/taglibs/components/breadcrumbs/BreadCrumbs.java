package eu.scy.server.taglibs.components.breadcrumbs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.apr.2010
 * Time: 05:42:36
 * To change this template use File | Settings | File Templates.
 */
public class BreadCrumbs extends TagSupport {

    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().write(createHomeLink());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);    //To change body of overridden methods use File | Settings | File Templates.
        pageContext.getRequest().getParameter("model");
    }

    private String createHomeLink() {
        String link = createLink("AppIndex.html", "home");
        return link;
    }

    private String createLink(String page, String pageName) {
        String link = "<a href=\"/webapp/app/" + page + "\">" + pageName + "</a>";
        return link;
    }
}
