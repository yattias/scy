package eu.scy.webapp.components.tools.appletwrapper;

import eu.scy.webapp.pages.TapestryContextAware;
import org.apache.tapestry5.annotations.Parameter;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2009
 * Time: 21:06:18
 * To change this template use File | Settings | File Templates.
 */
public class AppletWrapper extends TapestryContextAware {

    @Parameter
    private String codeBase;

    @Parameter
    private String code;

    @Parameter
    private String archive;

    @Parameter
    private String width;

    @Parameter
    private String height;

    public String getCodeBase() {
        return codeBase;
    }

    public String getCode() {
        return code;
    }

    public String getArchive() {
        return archive;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }
}
