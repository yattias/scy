package eu.scy.server.controllers.json;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.ScenarioService;
import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.model.pedagogicalplan.Scenario;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mai.2010
 * Time: 05:59:07
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioDiagramJSON extends AbstractController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private ScenarioService scenarioService;

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

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        String scenarioId = httpServletRequest.getParameter("model");
        Scenario scenario = null;
        List list = null;

        if(scenarioId != null) {
            scenario = getScenarioService().getScenario(scenarioId);
            list = getScenarioService().getLearningActivitySpaces(scenario);
            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            //xstream.setMode(XStream.NO_REFERENCES);
            xstream.alias("model", LinkedList.class);
            xstream.toXML(list, httpServletResponse.getWriter());

        }






        return null;  
    }
}
