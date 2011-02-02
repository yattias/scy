package eu.scy.server.eportfolio.xml;

import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.eportfolio.xml.utilclasses.CurrentMissionRuntimeInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 09:30:30
 * To change this template use File | Settings | File Templates.
 */
public class CurrentMissionRuntimeInfoController extends XMLStreamerController {
    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        return new CurrentMissionRuntimeInfo();
    }
}
