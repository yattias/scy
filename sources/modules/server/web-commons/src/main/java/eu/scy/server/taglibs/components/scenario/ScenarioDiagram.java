package eu.scy.server.taglibs.components.scenario;

import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.Scenario;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mai.2010
 * Time: 23:18:58
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioDiagram extends TagSupport {

    private Scenario scenario;
    private Boolean loadJSON;
    private PedagogicalPlan pedagogicalPlan;

    private List<LearningActivitySpace> learningActivitySpaces;

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public PedagogicalPlan getPedagogicalPlan() {
        return pedagogicalPlan;
    }

    public void setPedagogicalPlan(PedagogicalPlan pedagogicalPlan) {
        this.pedagogicalPlan = pedagogicalPlan;
    }

    public Boolean getLoadJSON() {
        return loadJSON;
    }

    public void setLoadJSON(Boolean loadJSON) {
        this.loadJSON = loadJSON;
    }

    private List<LearningActivitySpace> getLearningActivitySpaces() {
        List <LearningActivitySpace> returnList = new LinkedList<LearningActivitySpace>();

        LearningActivitySpace space = getScenario().getLearningActivitySpace();
        returnList.add(space);

        return returnList;
    }

    public int doEndTag() throws JspException {
        try {

            getScenario().getLearningActivitySpace();



            pageContext.getOut().write("<h1>" + getScenario().getName()+ " </h1>");
            if(getLoadJSON()) {
                
                pageContext.getOut().write(
                        "<div id=\"world\"></div>\n" +
                        "        <script type=\"text/javascript\">\n" +
                       
                        "dojo.xhrGet( {\n" +
                        "        url: \"/webapp/components/json/ScenarioDiagramJSON.html?model=" + getScenario().getId() + "\",\n" +
                        "        handleAs: \"json\",\n" +
                        "        load: function(responseObject, ioArgs) {\n" +
                        "          // Now you can just use the object\n" +
                        "          console.dir(responseObject);  // Dump it to the console\n" +

                        
                                "for(var i = 0;i<responseObject.model.LearningActivitySpace.length;i++){              " +
                                "lasMap[responseObject.model.LearningActivitySpace[i].id] = createLas(responseObject.model.LearningActivitySpace[i]);\n" +
                                "createLasContentBox(lasMap[responseObject.model.LearningActivitySpace[i].id], responseObject.model.LearningActivitySpace[i].id);" +

                                "          }"  +

                                "for(var i = 0;i<responseObject.model.AnchorELO.length;i++){              " +
                                "eloMap[responseObject.model.AnchorELO[i].id] = createElo(responseObject.model.AnchorELO[i]);\n" +

                                "          }" +
                                "for(var i = 0;i<responseObject.model.Connection.length;i++){" +
                                "if(responseObject.model.Connection[i].direction == \"FROM_ELO_TO_LAS\"){" +
                                "   eloMap[responseObject.model.Connection[i].from].joint(lasMap[responseObject.model.Connection[i].to], uml.arrow).register(lasMap);" +
                                "} else {" +
                                "   lasMap[responseObject.model.Connection[i].from].joint(eloMap[responseObject.model.Connection[i].to], uml.arrow).register(lasMap);" +
                                "}" +
                                "}"  +


                        "        }\n" +
                        "        // More properties for xhrGet...\n" +
                        "});");
            }
            pageContext.getOut().write(

                    "            var uml = Joint.dia.uml;\n" +
                    "            var devs = Joint.dia.devs;\n" +
                    "            \n" +
                    "            Joint.paper(\"world\", 640, 500);\n" +
                    "\n" +
                    "                     \n" +
                    "\n" +

                    "            var se = uml.EndState.create({\n" +
                    "              position: {x: 100, y: 480}\n" +
                    "            }).toggleGhosting();\n" +





                   
                    "        </script>");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return EVAL_PAGE;

    }

}
