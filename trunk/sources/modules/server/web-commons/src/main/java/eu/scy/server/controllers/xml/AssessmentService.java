package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.server.controllers.xml.transfer.Portfolio;
import eu.scy.server.controllers.xml.transfer.PortfolioContainer;
import eu.scy.server.controllers.xml.transfer.TransferElo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 19:54:38
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentService extends XMLStreamerController{
    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        PortfolioContainer portfolios = new PortfolioContainer();

        Portfolio portfolio = new Portfolio();
        portfolio.setMissionName("Freaky Styley");
        portfolio.setReflectionCollaboration("Woha wee");
        portfolio.setReflectionEffort("2");
        portfolio.setReflectionInquiry("REF INQ");
        portfolio.setReflectionMission("REFL MIS");
        portfolio.setStudent("digi");

        TransferElo transferElo = new TransferElo();
        transferElo.setAssessed(Boolean.FALSE);
        transferElo.setAssessmentComment("really awesome");
        transferElo.setGrade("NG");
        transferElo.setCatname("a cat on da mat");
        transferElo.setReflectionComment("Reflect me baby - do it hard!");

        portfolio.addElo(transferElo);
        portfolios.addPortfolio(portfolio);

        return portfolios;
    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);    //To change body of overridden methods use File | Settings | File Templates.
        xStream.alias("portfolioContainer", PortfolioContainer.class);
        xStream.alias("elo", TransferElo.class);
        xStream.alias("portfolio", Portfolio.class);
        xStream.useAttributeFor(TransferElo.class, "uri");
        xStream.useAttributeFor(Portfolio.class, "student" );
    }
}
