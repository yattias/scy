package eu.scy.webapp.pages.projectmanagement;

import eu.scy.webapp.pages.TapestryContextAware;
import eu.scy.webapp.pages.Index;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.impl.SCYProjectImpl;
import eu.scy.core.model.impl.SCYGroupImpl;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.InjectPage;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.nov.2008
 * Time: 20:33:02
 * To change this template use File | Settings | File Templates.
 */
public class ProjectEditor extends TapestryContextAware {
    private static Logger log = Logger.getLogger("Index.class");

    private SCYProjectImpl _project;


    public SCYProjectImpl getProject() {
        return _project;
    }

    public void setProject(SCYProjectImpl project) {
        this._project = project;
    }

    @SetupRender
    private void init() {
        if (_project == null) {
            _project = new SCYProjectImpl();
            setProject(_project);
        }
    }


    @InjectPage
    private Index indexPage;

    public Object onActionFromProjectEditorForm() {
        log.info("Action from project editor form!");
        if(getProject().getId() == null) {
            //create new SCYProject
            //getProjectDAO().save(getProject());
            SCYGroup g = new SCYGroupImpl();
            g.setProject(getProject());
            g.setName(getProject().getName());
            //getUserDAO().save(g);
            getProject().addGroup(g);
            getProjectDAO().save(getProject());
        } else {
            getProjectDAO().save(getProject());
        }
        setCurrentProject(getProject());
        return indexPage; 
    }


}
