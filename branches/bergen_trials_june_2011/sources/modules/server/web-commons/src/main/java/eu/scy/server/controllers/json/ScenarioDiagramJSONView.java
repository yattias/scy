package eu.scy.server.controllers.json;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.ScenarioService;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.server.controllers.json.util.LearningActivitySpaceAnchorEloConnectionUtil;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.okt.2010
 * Time: 06:01:25
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioDiagramJSONView extends AbstractView {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private ScenarioService scenarioService;
    private LASService lasService;


    @Override
    protected void renderMergedOutputModel(Map map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String scenarioId = httpServletRequest.getParameter("model");
        Scenario scenario = null;
        List list = null;

        if(scenarioId != null) {
            scenario = getScenarioService().getScenario(scenarioId);
            logger.info("SCENARIO IS : " + scenario.getName());
            list = getScenarioService().getLearningActivitySpaces(scenario);

            List learningActivitySpaces = new LinkedList();
            List anchorElos = new LinkedList();
            List connections = new LinkedList();

            for (int i = 0; i < list.size(); i++) {
                LearningActivitySpace learningActivitySpace = (LearningActivitySpace) list.get(i);



                List producedELOS = getLasService().getAnchorELOsProducedByLAS(learningActivitySpace);
                for (int j = 0; j < producedELOS.size(); j++) {
                    AnchorELO anchorELO = (AnchorELO) producedELOS.get(j);
                    LearningActivitySpaceAnchorEloConnectionUtil learningActivitySpaceAnchorEloConnectionUtil = new LearningActivitySpaceAnchorEloConnectionUtil();
                    learningActivitySpaceAnchorEloConnectionUtil.setFrom(learningActivitySpace.getId());
                    learningActivitySpaceAnchorEloConnectionUtil.setTo(anchorELO.getId());
                    learningActivitySpaceAnchorEloConnectionUtil.setDirection("FROM_LAS_TO_ELO");
                    connections.add(learningActivitySpaceAnchorEloConnectionUtil);

                    AnchorELO copy = new AnchorELOImpl();
                    copy.setId(anchorELO.getId());
                    copy.setName(anchorELO.getName());
                    copy.setDescription(anchorELO.getDescription());
                    copy.setMissionMapId(anchorELO.getMissionMapId());
                    copy.setXPos(anchorELO.getXPos());
                    copy.setYPos(anchorELO.getYPos());
                    copy.setObligatoryInPortfolio(anchorELO.getObligatoryInPortfolio());
                    anchorElos.add(copy);

                    if(anchorELO.getInputTo() != null) {
                        LearningActivitySpaceAnchorEloConnectionUtil connection = new LearningActivitySpaceAnchorEloConnectionUtil();
                        connection.setFrom(anchorELO.getId());
                        connection.setTo(anchorELO.getInputTo().getId());
                        connection.setDirection("FROM_ELO_TO_LAS");
                        connections.add(connection);
                    }
                }

                LearningActivitySpace copy = new LearningActivitySpaceImpl();
                copy.setId(learningActivitySpace.getId());
                copy.setName(learningActivitySpace.getName());
                copy.setDescription(learningActivitySpace.getDescription());
                copy.setXPos(learningActivitySpace.getXPos());
                copy.setYPos(learningActivitySpace.getYPos());
                learningActivitySpaces.add(copy);
            }

            for (int i = 0; i < anchorElos.size(); i++) {
                AnchorELO anchorELO = (AnchorELO) anchorElos.get(i);
                AnchorELO copy = new AnchorELOImpl();
                copy.setName(anchorELO.getName());
                copy.setDescription(anchorELO.getDescription());
                copy.setMissionMapId(anchorELO.getMissionMapId());
                copy.setXPos(anchorELO.getXPos());
                copy.setYPos(anchorELO.getYPos());
                copy.setObligatoryInPortfolio(anchorELO.getObligatoryInPortfolio());
            }

            learningActivitySpaces.addAll(anchorElos);
            learningActivitySpaces.addAll(connections);


            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            //httpServletResponse.setContentType("text/json");
            httpServletResponse.setContentType( "text/json; charset=UTF-8" );

            xstream.setMode(XStream.NO_REFERENCES);
            xstream.alias("model", LinkedList.class);
            //xstream.aliasField("name", LearningActivitySpaceImpl.class, "name");
            xstream.alias("LearningActivitySpace", LearningActivitySpaceImpl.class);
            xstream.alias("AnchorELO", AnchorELOImpl.class);
            xstream.alias("Connection", LearningActivitySpaceAnchorEloConnectionUtil.class);

            logger.info("ABOUT TO STREAM JSON!");
            logger.info(xstream.toString());
            StringWriter stringWriter = new StringWriter();
            xstream.toXML(learningActivitySpaces, stringWriter);
            logger.info("JSON:  " + stringWriter.toString());


            try {

                //ServletOutputStream out = httpServletResponse.getOutputStream();
                //httpServletResponse.getOutputStream().write(stringWriter.toString().getBytes());
                //out.write(stringWriter.getBytes());
                //out.flush();

                xstream.toXML(learningActivitySpaces, httpServletResponse.getWriter());
            } catch (IOException e) {
                e.printStackTrace();
            }

            logger.info("JSON SHOULD HAVE BEEN STREAMED NOW FROM ScenarioDiagramJSONView!");


        } else {
            logger.info("SCENARIO ID IS NULL!");
        }
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public ScenarioService getScenarioService() {
        return scenarioService;
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }
}
