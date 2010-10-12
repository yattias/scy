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
    private PedagogicalPlan pedagogicalPlan;
    private String lasLink; //the link that will be fired when clicking on a LAS
    private Boolean includeRuntimeInfo;

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

    public String getLasLink() {
        return lasLink;
    }

    public void setLasLink(String lasLink) {
        this.lasLink = lasLink;
    }

    public Boolean getIncludeRuntimeInfo() {
        return includeRuntimeInfo;
    }

    public void setIncludeRuntimeInfo(Boolean includeRuntimeInfo) {
        this.includeRuntimeInfo = includeRuntimeInfo;
    }

    private List<LearningActivitySpace> getLearningActivitySpaces() {
        List <LearningActivitySpace> returnList = new LinkedList<LearningActivitySpace>();

        LearningActivitySpace space = getScenario().getLearningActivitySpace();
        returnList.add(space);

        return returnList;
    }

    public int doEndTag() throws JspException {
        try {

            //getScenario().getLearningActivitySpace();



            //pageContext.getOut().write("<h1>" + getScenario().getName()+ " </h1>");

            //pageContext.getOut().write("<h1>" + getLasLink() + " </h1>");

                
                //pageContext.getOut().write("<a href=\"/webapp/components/json/RuntimeUserInfoJSON.html?model=" + getPedagogicalPlan().getId() +"\">LOAD RUNTIME ACTIVITY</a>");
                pageContext.getOut().write(
                        "<div id=\"world\"></div>\n" +
                        "        <script type=\"text/javascript\">\n" +
                         "var userLasController = new UserLasController();\n\n" +
                        "dojo.xhrGet( {\n" +
                        "        url: \"/webapp/components/json/ScenarioDiagramJSON.json?model=" + getPedagogicalPlan().getScenario().getId() + "\",\n" +
                        "        handleAs: \"json\",\n" +
                        "        load: function(responseObject, ioArgs) {\n" +
                        "          // Now you can just use the object\n" +
                        "          console.dir(responseObject);  // Dump it to the console\n" +
                        "          alert(\"responseobject: \" + responseObject);  // Dump it to the console\n" +
                        "          alert(\"model: \" + responseObject.model[0]);  // Dump it to the console\n" +
                        "          alert(\"learningActivitySpace \" + responseObject.model[0].LearningActivitySpace);  // Dump it to the console\n" +
                        "          alert(\"length: \" + responseObject.model[0].LearningActivitySpace.length);  // Dump it to the console\n" +

                        
                                "lasRuntimeInfoUrl = '" + getLasLink() + "';\n" + 
                                "for(var i = 0;i<responseObject.model[0].LearningActivitySpace.length;i++){              " +
                                "alert(\"hei\" + i);\n" +
                                "lasMap[responseObject.model[0].LearningActivitySpace[i].id] = createLas(responseObject.model[0].LearningActivitySpace[i]);\n" +
                                "createLasContentBox(lasMap[responseObject.model[0].LearningActivitySpace[i].id], responseObject.model[0].LearningActivitySpace[i].id, '" + getLasLink() + "?id=' + responseObject.model[0].LearningActivitySpace[i].id);" +
                                "userLasController.addLas(lasMap[responseObject.model[0].LearningActivitySpace[i]]);" +

                                "          }"  +

                                "for(var i = 0;i<responseObject.model[0].AnchorELO.length;i++){              " +
                                "eloMap[responseObject.model[0].AnchorELO[i].id] = createElo(responseObject.model[0].AnchorELO[i]);\n" +

                                "          }" +
                                "for(var i = 0;i<responseObject.model[0].Connection.length;i++){" +
                                "if(responseObject.model[0].Connection[i].direction == \"FROM_ELO_TO_LAS\"){" +
                                "   eloMap[responseObject.model[0].Connection[i].from].joint(lasMap[responseObject.model[0].Connection[i].to], uml.arrow).register(lasMap);" +
                                "} else {" +
                                "   lasMap[responseObject.model[0].Connection[i].from].joint(eloMap[responseObject.model[0].Connection[i].to], uml.arrow).register(lasMap);" +
                                "}" +
                                "}"  +


                        "        }\n" +
                        "        // More properties for xhrGet...\n" +
                        "});");
            
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



                    "");
        if(getIncludeRuntimeInfo()) {


        pageContext.getOut().write("" +
                


                "userLasController.setRuntimeUserInfoUrl('/webapp/components/json/RuntimeUserInfoJSON.html?model=" + getPedagogicalPlan().getId() + "&scenario="+getPedagogicalPlan().getScenario().getId()+"');\n" +


               
                "userLasController.init();");                    
        }
        pageContext.getOut().write(
                    "        </script>");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return EVAL_PAGE;

    }

}
