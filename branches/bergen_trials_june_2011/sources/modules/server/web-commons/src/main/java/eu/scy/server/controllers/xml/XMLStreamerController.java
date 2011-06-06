package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.core.UserService;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.model.transfer.PortfolioContainer;
import eu.scy.core.model.transfer.TransferElo;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.okt.2010
 * Time: 05:18:06
 * To change this template use File | Settings | File Templates.
 */
public abstract class XMLStreamerController extends AbstractController {
    protected UserService userService;

    private XMLTransferObjectService xmlTransferObjectService;

    protected XStream xstream;

    protected int getXStreamMode() {
        return XStream.XPATH_RELATIVE_REFERENCES;
    }

    protected void addAliases(XStream xStream) {
        xStream.alias("pedagogicalplan", PedagogicalPlan.class);
        xStream.alias("pedagogicalplan", PedagogicalPlanImpl.class);
        xStream.alias("las", LearningActivitySpaceImpl.class);
        xStream.alias("las", LearningActivitySpace.class);


        xStream.alias("portfoliocontainer", PortfolioContainer.class);
        xStream.alias("portfolios", LinkedList.class);
        xStream.alias("elo", TransferElo.class);
        xStream.alias("portfolio", Portfolio.class);
        //xStream.useAttributeFor(TransferElo.class, "uri");


    }

    protected void omitFields(XStream xStream) {
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        this.xstream = getXmlTransferObjectService().getXStreamInstance();
        omitFields(xstream);
        addAliases(xstream);

        Object objectToStream = getObjectToStream(request, httpServletResponse);
        if(objectToStream instanceof ModelAndView) {
            return (ModelAndView) objectToStream;
        } else {
            xstream.toXML(objectToStream, httpServletResponse.getWriter());    
        }


        return null;
    }

    protected abstract Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse);

    public User getCurrentUser(HttpServletRequest request) {
        return getUserService().getUser(getCurrentUserName(request));
    }

    public String getCurrentUserName(HttpServletRequest request) {
        org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
        return user.getUsername();
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public XStream getXstream() {
        return xstream;
    }

    public void setXstream(XStream xstream) {
        this.xstream = xstream;
    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }
}
