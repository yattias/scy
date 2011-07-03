package eu.scy.server.eportfolio.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.model.transfer.ELOSearchResult;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;
import eu.scy.server.url.UrlInspector;
import roolo.elo.BasicELO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jan.2011
 * Time: 06:21:45
 * To change this template use File | Settings | File Templates.
 */
public class EloSearchService extends MissionRuntimeEnabledXMLService {

    private MissionELOService missionELOService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private UrlInspector urlInspector;

    @Override
    protected Object getObject(MissionRuntimeElo mre, HttpServletRequest request, HttpServletResponse response) {
        logger.info("*** *** LOADING ELO SEARCH SERVICWE!");
        URI missionURI = null;
        try {
            missionURI = new URI(request.getParameter("missionURI"));
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String searchCategory = request.getParameter("searchCategory");


        if (missionURI != null) {
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionURI, getMissionELOService());
            MissionSpecificationElo missionSpecificationElo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);

            PedagogicalPlanTransfer pedagogicalPlan = getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationElo);

            List anchorElos = getMissionELOService().getAnchorELOs(missionSpecificationElo);
            String technicalFormatFilter = null;

            for (int i = 0; i < anchorElos.size(); i++) {
                ScyElo anchorElo = (ScyElo) anchorElos.get(i);
                if(anchorElo.getTitle().equals(searchCategory)) {
                    technicalFormatFilter = anchorElo.getTechnicalFormat();
                }

            }


            List elos = getMissionELOService().findElosFor(getCurrentUserName(request));
            ELOSearchResult eloSearchResult = new ELOSearchResult();
            for (int i = 0; i < elos.size(); i++) {
                BasicELO basicELO = (BasicELO) elos.get(i);
                ScyElo scyElo = ScyElo.loadLastVersionElo(basicELO.getUri(), getMissionELOService());
                if(technicalFormatFilter != null && pedagogicalPlan.getTrimSearchResultsInEportfolioToContainElosWithEqualTechnicalFormat() ) {
                    if(scyElo.getTechnicalFormat().equals(technicalFormatFilter)) {
                        eloSearchResult.getElos().add(createELOModel(scyElo, anchorElos, searchCategory));
                    }
                } else {
                    eloSearchResult.getElos().add(createELOModel(scyElo, anchorElos, searchCategory));
                }

                logger.info("PART OF SEARCH RESULT: " + scyElo.getTitle());
            }

            return eloSearchResult;
        }


        logger.info("MISSION URI WAS NULL!!");
        return null;

    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);    //To change body of overridden methods use File | Settings | File Templates.
        xstream.addImplicitCollection(ELOSearchResult.class, "elos");
    }

    private Object createELOModel(ScyElo scyElo, List anchorElos, String searchCategory) {
        TransferElo eloModel = new TransferElo(scyElo);
        eloModel.setCatname(searchCategory);
        //String catName = getCategoryName(scyElo, anchorElos);
        //eloModel.setCatname(catName);

        return eloModel;
    }

    private String getCategoryName(ScyElo scyElo, List anchorElos) {
        String catName = "";
        if(scyElo.getTitle().equals("My Health Passport - a new elo")) {
            logger.info("HERE STARTS THE FUN!");
        }
        URI anchorEloUri = scyElo.getIsForkedOfEloUri();
        while(anchorEloUri != null) {
            ScyElo potentialAnchorELo = ScyElo.loadLastVersionElo(anchorEloUri, getMissionELOService());

            System.out.println("POTENTIAL: " + potentialAnchorELo.getTitle() + " " + potentialAnchorELo.getObligatoryInPortfolio());
            System.out.println("URI: " + potentialAnchorELo.getUri().toString());
            if(potentialAnchorELo.getObligatoryInPortfolio() != null && potentialAnchorELo.getObligatoryInPortfolio()) {
                logger.info("WEE - I have found the fucking anchor elo!");
                return potentialAnchorELo.getTitle();
            }

            if(potentialAnchorELo.getUri().equals(potentialAnchorELo.getIsForkedOfEloUri())) return potentialAnchorELo.getTitle();
            anchorEloUri = potentialAnchorELo.getIsForkedOfEloUri();
            if(anchorEloUri == null) {
                anchorEloUri = potentialAnchorELo.getUriFirstVersion();
            }
        }
        return catName;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }

    public UrlInspector getUrlInspector() {
        return urlInspector;
    }

    public void setUrlInspector(UrlInspector urlInspector) {
        this.urlInspector = urlInspector;
    }
}
