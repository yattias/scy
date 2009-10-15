package eu.scy.webapp.components.breadcrumbs;

import eu.scy.webapp.pages.TapestryContextAware;
import eu.scy.core.model.SCYProject;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.ScyBaseObject;

import java.util.List;
import java.util.LinkedList;

import org.apache.tapestry5.annotations.Parameter;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.nov.2008
 * Time: 06:23:43
 * To change this template use File | Settings | File Templates.
 */
public class BreadCrumbs extends TapestryContextAware {

    private ScyBaseObject currentObject;

    public ScyBaseObject getCurrentObject() {
        return currentObject;
    }

    @Parameter
    private ScyBaseObject model;

    public void setCurrentObject(ScyBaseObject currentObject) {
        this.currentObject = currentObject;
    }

    public ScyBaseObject getModel() {
        return model;
    }

    public void setModel(ScyBaseObject model) {
        this.model = model;
    }

    public List getObjectList() {

        log.info("__ ___ ___  MODEL : " + getModel());

        List objectList = new LinkedList();

        if(getModel() instanceof SCYProject) {
            addObjectsToSelect(objectList, (SCYProject)getModel());
        } else if (getModel() instanceof SCYGroup) {
            addObjectsToSelect(objectList, (SCYGroup) getModel());
        } else if(getModel() instanceof User) {
            addObjectsToSelect(objectList, (User) getModel());
        }

        return objectList;
    }


    private void addObjectsToSelect(List objectList, SCYProject project) {
        objectList.add(project);
    }

    private void addObjectsToSelect(List objectList, SCYGroup group) {
        addObjectsToSelect(objectList, group.getProject());
        objectList.add(group);
    }

 private void addObjectsToSelect(List objectList, User user) {
     throw new RuntimeException("NOT IMPLEMENTED");
     //addObjectsToSelect(objectList, user.getGroup());
       // objectList.add(user);
    }

    public String getTitle() {
        if(getCurrentObject() == null) return "null";
        if(getCurrentObject() instanceof User) return "HIHI";//((User)currentObject).getUserDetails().getUsername();
        else return getCurrentObject().getName();
    }



}
