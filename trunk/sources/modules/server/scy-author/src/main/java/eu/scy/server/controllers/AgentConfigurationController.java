package eu.scy.server.controllers;

import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterAPI;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.roolo.MissionELOService;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.feb.2011
 * Time: 22:42:25
 * To change this template use File | Settings | File Templates.
 */
public class AgentConfigurationController extends BaseController {

    private AgentParameterAPI agentParameterAPI;
    private MissionELOService missionELOService;
    private TupleSpace portfolioTupleSpace;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        try {
            String uriParam = request.getParameter("eloURI");
            logger.info("*** **** MISSION ELO URI IS : " + uriParam);
            URI uri = new URI(uriParam);

            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());
            modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionSpecificationElo));
            modelAndView.addObject("agentParameterAPI", getAgentParameterAPI());
            //modelAndView.addObject("anchorElos", getMissionELOService().getWebSafeTransporters(getMissionELOService().getAnchorELOs(missionSpecificationElo)));

            missionSpecificationElo.getTypedContent().getMissionMapModelEloUri();

            logger.info("SETTING PARAMETERS FOR VOTAT!");
            AgentParameter agentParameter = new AgentParameter();
            agentParameter.setParameterName("funkyParam");
            agentParameter.setParameterValue("42");
            getAgentParameterAPI().setParameter("votat", agentParameter);
            logger.info("DONE SETTING PARAMETERS FOR VOTAT!");

            Tuple tu = new Tuple(AgentProtocol.COMMAND_LINE, String.class, String.class, String.class, AgentProtocol.ALIVE);

            List agentConfigurationWrappers = new LinkedList();

            try {
                Tuple[] tuples = getPortfolioTupleSpace().readAll(tu);
                for (int i = 0; i < tuples.length; i++) {
                    Tuple tuple = tuples[i];
                    Field[] fields = tuple.getFields();
                    Field name = fields[3];
                    Field staus = fields[4];

                    AgentConfigurationWrapper agentConfigurationWrapper = new AgentConfigurationWrapper();
                    agentConfigurationWrapper.setName((String) name.getValue());
                    agentConfigurationWrapper.setStatus((String) staus.getValue());
                    if (agentConfigurationWrapper.getName().equals("eu.scy.agents.sensors.behaviourclassifier.ScySimBehaviourClassifier")) {
                        logger.info("Will not get parameters for: eu.scy.agents.sensors.behaviourclassifier.ScySimBehaviourClassifier");
                    } else {
                        logger.info("GEtting parameters for agent: " + agentConfigurationWrapper.getName());
                        List<String> agentParameters = getAgentParameterAPI().listAgentParameter(agentConfigurationWrapper.getName());
                        for (int j = 0; j < agentParameters.size(); j++) {
                            String param = agentParameters.get(j);
                            logger.info("PARAM IS: " + param);
                            if (param.equals("sleepMillis")) {
                                logger.info("WILL NOT ADD PARAMETER: sleepMillis");
                            } else {
                                AgentParameterConfigurationWrapper agentParameterConfigurationWrapper = new AgentParameterConfigurationWrapper();
                                agentParameterConfigurationWrapper.setParameterName(param);
                                agentConfigurationWrapper.addAgentParameterConfiguration(agentParameterConfigurationWrapper);
                            }

                        }


                        agentConfigurationWrappers.add(agentConfigurationWrapper);
                    }

                }

            } catch (TupleSpaceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            modelAndView.addObject("agentConfigurationWrappers", agentConfigurationWrappers);


            //AgentParameter aParm = getAgentParameterAPI().getParameter("votat", agentParameter);
            //logger.info(aParm.getParameterName() + " " + aParm.getParameterValue());
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public AgentParameterAPI getAgentParameterAPI() {
        return agentParameterAPI;
    }

    public void setAgentParameterAPI(AgentParameterAPI agentParameterAPI) {
        this.agentParameterAPI = agentParameterAPI;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public TupleSpace getPortfolioTupleSpace() {
        return portfolioTupleSpace;
    }

    public void setPortfolioTupleSpace(TupleSpace portfolioTupleSpace) {
        this.portfolioTupleSpace = portfolioTupleSpace;
    }
}
