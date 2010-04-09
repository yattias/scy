package eu.scy.server.taglibs.components.breadcrumbs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Enumeration;

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

     protected void instpectRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) {
         String modelString = request.getParameter("model");
        if(modelString != null) {
            String type = modelString.substring(0, modelString.indexOf("_"));
            String id = modelString.substring(modelString.indexOf("_")+1, modelString.length());
            System.out.println("TYPE: " + type);
            System.out.println("ID: " + id);

        }
    }


    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        instpectRequest(req, (HttpServletResponse) pageContext.getResponse());
        System.out.println("BREAD CRUMBS: QureryStrign: " +req.getQueryString());
        System.out.println("BREAD CRUMBS : URI: " + req.getRequestURI());
        Enumeration enumeration = pageContext.getRequest().getParameterNames();
        while(enumeration.hasMoreElements()) {
            String param = (String) enumeration.nextElement();
            System.out.println("       " + param + " ::: " + pageContext.getRequest().getParameter(param));
        }


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
