package eu.scy.server.controllers.json;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.mai.2010
 * Time: 12:06:56
 * To change this template use File | Settings | File Templates.
 */
public class AbstractJSonView implements View{

    @Override
    public String getContentType() {
        return "text/json";
    }

    @Override
    public void render(Map map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        PrintWriter out = httpServletResponse.getWriter();
        out.write("HEnrik");
    }
}
