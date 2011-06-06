package eu.scy.server.controllers.json;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.mai.2010
 * Time: 12:26:21
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioJSONView extends AbstractController {

    private static final String QUOTATION_MARK = "\"";
    

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        LearningActivitySpaceImpl las = new LearningActivitySpaceImpl();
        las.setName("Conceptualizationssssss");
        las.setDescription("Hee haa");
        las.setXPos(200);
        las.setYPos(400);

        LearningActivitySpaceImpl las2 = new LearningActivitySpaceImpl();
        las2.setName("LArS 2");
        las2.setDescription("Hirr hirr");
        las2.setXPos(600);
        las2.setYPos(800);
        
        List list = new LinkedList();
        list.add(las);
        list.add(las2);


        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("model", LinkedList.class);
        xstream.toXML(list, httpServletResponse.getWriter());
        


        return null;
    }

    private String getName(String name) {
        return QUOTATION_MARK + name + QUOTATION_MARK;
    }

    private String getVariableName(String name, String value) {
        return getName(name) + ": " + QUOTATION_MARK + value + QUOTATION_MARK;
    }



}
