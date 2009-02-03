package eu.scy.webapp.pages.projectmanagement;

import eu.scy.webapp.pages.TapestryContextAware;
import eu.scy.webapp.pages.Index;
import eu.scy.core.model.Project;
import eu.scy.core.model.Group;
import eu.scy.core.model.impl.ProjectImpl;
import eu.scy.core.model.impl.GroupImpl;
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

    private ProjectImpl _project;


    public ProjectImpl getProject() {
        return _project;
    }

    public void setProject(ProjectImpl project) {
        this._project = project;
    }

    @SetupRender
    private void init() {
        if (_project == null) {
            _project = new ProjectImpl();
            setProject(_project);
        }
    }


    @InjectPage
    private Index indexPage;

    public Object onActionFromProjectEditorForm() {
        log.info("Action from project editor form!");
        if(getProject().getId() == null) {
            //create new Project
            //getProjectDAO().save(getProject());
            Group g = new GroupImpl();
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
