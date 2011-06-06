package eu.scy.server.taglibs.components.breadcrumbs;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.apr.2010
 * Time: 05:42:36
 * To change this template use File | Settings | File Templates.
 */
public class BreadCrumbs extends TagSupport {

    List breadCrumbs = null;

    public int doEndTag() throws JspException {

        if (breadCrumbs != null) {
            Collections.reverse(breadCrumbs);
            try {
                pageContext.getOut().write(createHomeLink());
                for (int i = 0; i < breadCrumbs.size(); i++) {
                    ScyBase scyBase = (ScyBase) breadCrumbs.get(i);
                    pageContext.getOut().write(createLinkForObject(scyBase));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return EVAL_PAGE;
    }


    protected void instpectRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        String modelString = request.getParameter("model");
        ScyBase modelObject = (ScyBase) request.getAttribute("modelObject");
        if (modelObject != null) {
            breadCrumbs.add(modelObject);
        }


    }


    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        breadCrumbs = new LinkedList();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        instpectRequest(req, (HttpServletResponse) pageContext.getResponse());
        Enumeration enumeration = pageContext.getRequest().getParameterNames();
        // System.out.println("ENCODING: " + pageContext.getRequest().getCharacterEncoding());
        while (enumeration.hasMoreElements()) {
            String param = (String) enumeration.nextElement();
            // System.out.println("       " + param + " ::: " + pageContext.getRequest().getParameter(param));
        }


    }

    private String createHomeLink() {
        String link = createLink("AppIndex.html", "home");
        return link;
    }

    private String createLinkForObject(ScyBase scyBase) {
        if(scyBase instanceof PedagogicalPlan) return createLink("scyauthor/viewPedagogicalPlan.html?" + getModelLinkInfo(scyBase), scyBase.getName());
        return "---->";
    }

    private String getModelLinkInfo(ScyBase scyBase) {
        return "model=" + scyBase.getClass().getName() + "_" + scyBase.getId();
    }



    private String createLink(String page, String pageName) {
        String link = "<a href=\"/webapp/app/" + page + "\">" + pageName + "</a>";
        return link;
    }
}
