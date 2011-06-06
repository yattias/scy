package eu.scy.server.eportfolio.xml;

import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.eportfolio.xml.utilclasses.EPortfolioSearchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.okt.2010
 * Time: 11:26:13
 * To change this template use File | Settings | File Templates.
 */
public class EPortfolioELOSearch extends XMLStreamerController {
    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        EPortfolioSearchResult result = new EPortfolioSearchResult();
        
        return result;
    }
}
