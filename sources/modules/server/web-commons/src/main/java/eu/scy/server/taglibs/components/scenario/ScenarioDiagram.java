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
                pageContext.getOut().write("LOADING JSON SHIT!");
                pageContext.getOut().write("<a href=\"/webapp/components/json/ScenarioDiagramJSON.html?model=" + getScenario().getId() +"\">LOAD</a>");
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
                                "alert(responseObject.model.LearningActivitySpace[i].name);\n" +
                                "          }"  +

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
                    "            var s1 = createLas(10, 60, 80, 50, 'Orientation');\n" +
                    "\n" +
                    "            var s2 = createLas(200, 30, 120, 50, 'Conceptualization');\n" +
                    "            var s3 = createLas(450, 30,  80, 50,'Design');\n" +
                    "            var s4 = createLas(450, 120, 80, 50, 'Build');\n" +
                    "            var s5 = createLas(120, 150, 80, 50, 'Evaluation');\n" +
                    "            var s6 = createLas(400, 200,  80, 50,'Experiment');\n" +
                    "            var s7 = createLas(100, 250, 80, 50, 'Reflection');\n" +
                    "            var s8 = createLas(50, 390, 80, 50, 'Reporting');\n" +
                    "\n" +
                    "\n" +
                    "           \n" +
                    "            var se = uml.EndState.create({\n" +
                    "              position: {x: 100, y: 480}\n" +
                    "            }).toggleGhosting();\n" +
                    "\n" +
                    "            var all = [se, s1, s2, s3, s4, s5, s6, s7, s8];\n" +
                    "            \n" +
                    "            var elo1 = createElo(150, 40);\n" +
                    "            var elo2 = createElo(360, 40);\n" +
                    "            var elo3 = createElo(600, 70);\n" +
                    "            var elo4 = createElo(600, 150);\n" +
                    "            var elo5 = createElo(300, 190);\n" +
                    "            var elo6 = createElo(320, 100);\n" +
                    "            var elo7 = createElo(220, 100);\n" +
                    "            var elo8 = createElo(160, 210);\n" +
                    "            var elo9 = createElo(75, 330);\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "            function createElo(eloX, eloY){\n" +
                    "               return  uml.Class.create({\n" +
                    "                  rect: {x: eloX, y: eloY, width: 20, height: 20, rotate: 45},\n" +
                    "                  //label: \"Client\",\n" +
                    "                  attrs: {\n" +
                    "                    fill: \"135-#000-#0a0:1-#fff\"\n" +
                    "                  }\n" +
                    "                });\n" +
                    "            }\n" +
                    "\n" +
                    "            function createLas(lasX, lasY,lasW, lasH,  lasTitle){\n" +
                    "                return uml.State.create({\n" +
                    "                  rect: {x: lasX, y: lasY, width: lasW, height: lasH},\n" +
                    "                  label: lasTitle,\n" +
                    "                  attrs: {\n" +
                    "                    fill: \"90-#000-#0af:1-#fff\"\n" +
                    "                  },\n" +
                    "                  actions: {\n" +
                    "\n" +
                    "                   // entry: \"create()\"\n" +
                    "                  }\n" +
                    "                }).toggleGhosting();\n" +
                    "            }\n" +
                    "            //s2.scale(2);\n" +
                    "            //s2.addInner(s4);\n" +
                    "\n" +
                    "            //s0.joint(s1, uml.arrow).register(all);\n" +
                    "            s1.joint(elo1, uml.arrow).register(all);\n" +
                    "            elo1.joint(s2, uml.arrow).register(all);\n" +
                    "            //s2.joint(s3, uml.arrow).register(all);\n" +
                    "            elo2.joint(s2, uml.arrow).register(all);\n" +
                    "            elo2.joint(s3, uml.arrow).register(all);\n" +
                    "            //s2.joint(s6, uml.arrow).register(all);\n" +
                    "            elo7.joint(s2, uml.arrow).register(all);\n" +
                    "            elo7.joint(s5, uml.arrow).register(all);\n" +
                    "            s5.joint(elo8, uml.arrow).register(all);\n" +
                    "            elo8.joint(s7, uml.arrow).register(all);\n" +
                    "            //s2.joint(s5, uml.arrow).register(all);\n" +
                    "            s3.joint(elo3, uml.arrow).register(all);\n" +
                    "            elo3.joint(s4, uml.arrow).register(all);\n" +
                    "            //s3.joint(s5, uml.arrow).register(all);\n" +
                    "            s4.joint(elo4, uml.arrow).register(all);\n" +
                    "            elo5.joint(s5, uml.arrow).register(all);\n" +
                    "            elo4.joint(s6, uml.arrow).register(all);\n" +
                    "            elo5.joint(s5, uml.arrow).register(all);\n" +
                    "            elo5.joint(s6, uml.arrow).register(all);\n" +
                    "            elo6.joint(s6, uml.arrow).register(all);\n" +
                    "            elo6.joint(s2, uml.arrow).register(all);\n" +
                    "            //s5.joint(s6, uml.arrow).register(all);\n" +
                    "            s5.joint(s3, uml.arrow).register(all);\n" +
                    "            //s5.joint(s7, uml.arrow).register(all);\n" +
                    "            s7.joint(elo9, uml.arrow).register(all);\n" +
                    "            elo9.joint(s8, uml.arrow).register(all);\n" +
                    "            s8.joint(se, uml.arrow).register(all);\n" +
                    "\n" +
                    "            //setTimeout(\"updates2(s2)\", 3000);\n" +
                    "            for(var i = 0;i<all.length;i++){\n" +
                    "                if(i > 0){\n" +
                    "                    createLasContentBox(all[i]);\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            function createLasContentBox(lasObj){\n" +
                    "\n" +
                    "                var worldLeftOffset = document.getElementById(\"world\").offsetLeft;\n" +
                    "                var worldTopOffset = document.getElementById(\"world\").offsetTop;\n" +
                    "\n" +
                    "                var s2w = lasObj.rect.width;\n" +
                    "                var s2h = lasObj.rect.height;\n" +
                    "                var s2x = lasObj.rect.x;\n" +
                    "                var s2y = lasObj.rect.y;\n" +
                    "                var s2Div = document.createElement(\"div\");\n" +
                    "                s2Div.setAttribute(\"id\", lasObj.label)\n" +
                    "                s2Div.style.width = s2w + \"px\";\n" +
                    "                s2Div.style.height = \"40px\";\n" +
                    "                s2Div.style.backgroundColor = \"transparent\";\n" +
                    "\n" +
                    "                s2Div.style.cursor = \"pointer\";\n" +
                    "                //s2Div.style.border = \"1px solid #000000\";\n" +
                    "                s2Div.style.position = \"absolute\"\n" +
                    "                s2Div.style.left = (worldLeftOffset + s2x) + \"px\";\n" +
                    "                //s2Div.style.left = s2x + \"px\";\n" +
                    "                s2Div.style.top = (worldTopOffset + s2y) -10 + \"px\";\n" +
                    "                s2Div.style.paddingTop = \"30px\";\n" +
                    "                s2Div.onclick = function(){\n" +
                    "                    var theDialog =  new dijit.Dialog({\n" +
                    "                        title: lasObj.label,\n" +
                    "                        style: \"width:500px;height:300px;\",\n" +
                    "                        id: \"dialog_\" + lasObj.label,\n" +
                    "                        content: \"<div id='dialogContents_\" + lasObj.label + \"'>Contents here</div>\"\n" +
                    "                    });\n" +
                    "\n" +
                    "                    theDialog.show();\n" +
                    "                }\n" +
                    "                document.getElementById(\"world\").appendChild(s2Div);\n" +
                    "                //s2Div.innerHTML = \"<img src=\\\"http://localhost:8080/webapp/themes/scy/default/images/brown_man_icon.png\\\" />\";\n" +
                    "            }\n" +
                    "            setInterval(\"updateLasContentBox()\", 3500);\n" +
                    "            function updateLasContentBox(){\n" +
                    "                var maxNumberOfParticipants = 6;\n" +
                    "                var rndNumber = 1;\n" +
                    "                var rndIconNumber = 0;\n" +
                    "                var userIconArray = new Array();\n" +
                    "                userIconArray[0] = \"<img src=\\\"${baseUrl}/themes/scy/default/images/green_man_icon.png\\\" />\";\n" +
                    "                userIconArray[1] = \"<img src=\\\"${baseUrl}/themes/scy/default/images/brown_man_icon.png\\\" />\";\n" +
                    "\n" +
                    "                for(var i = 0; i<all.length;i++){\n" +
                    "                    if(i > 0){\n" +
                    "\n" +
                    "                        rndNumber =Math.floor(Math.random()*maxNumberOfParticipants);\n" +
                    "                        var imgStr = \"\";\n" +
                    "                        for(j = 0;j<rndNumber;j++){\n" +
                    "                             rndIconNumber = Math.floor(Math.random()*2);\n" +
                    "                             imgStr += userIconArray[rndIconNumber];\n" +
                    "                        }\n" +
                    "\n" +
                    "                        var elementId = all[i].label;\n" +
                    "                        document.getElementById(elementId).innerHTML = imgStr;\n" +
                    "\n" +
                    "                    }\n" +
                    "                }\n" +
                    "\n" +
                    "            }\n" +
                    "            \n" +
                    "        </script>");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return EVAL_PAGE;

    }

}
