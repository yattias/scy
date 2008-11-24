package eu.scy.framework;

import eu.scy.framework.actions.AddGroupToProjectAction;
import eu.scy.framework.actions.ActionManager;
import eu.scy.framework.actions.AddMemberToGroupAction;

import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.nov.2008
 * Time: 21:17:41
 * To change this template use File | Settings | File Templates.
 */
public class ActionManagerImpl implements ActionManager {

    private static Logger log = Logger.getLogger("ActionManagerImpl.class");

    private List<BaseAction> actions;

    public ActionManagerImpl() {
        if (actions == null) {
            actions = new LinkedList();
            actions.add(new AddGroupToProjectAction());
            actions.add(new AddMemberToGroupAction());
        }

    }

    public List<BaseAction> getActions(Object userObject) {
        log.info("Getting actions for " + userObject);
        if (userObject == null) return Collections.EMPTY_LIST;
        List returnList = new LinkedList();

        List<Class> classes = getClasses(userObject.getClass());
        log.info("found " + classes.size() + " classes");

        for (int i = 0; i < actions.size(); i++) {

            BaseAction baseAction = actions.get(i);
            log.info("i: " + i + " bas" + baseAction.getName());
            for (int j = 0; j < classes.size(); j++) {
                log.info("j:" + j);
                Class aClass = classes.get(j);
                if (baseAction.getOperatesOn().getName().equals(aClass.getName())) returnList.add(baseAction);
            }
        }

        return returnList;
    }

    public BaseAction getActionById(String id) {
        for (int i = 0; i < actions.size(); i++) {
            BaseAction baseAction = actions.get(i);
            if(baseAction.getActionId().equals(id)) return baseAction;
        }

        return null;
    }


    private List<Class> getClasses(Class clazz) {
        Class[] classesToCheck = clazz.getInterfaces();

        List<Class> classes = new LinkedList<Class>(Arrays.asList(classesToCheck));
        getSuperClasses(classes, clazz);
        return classes;
    }


    private List<Class> getSuperClasses(List<Class> superclasses, Class/*<? extends Questionable>*/ clazz) {
        if (clazz != null) {
            superclasses.add(clazz);
            getSuperClasses(superclasses, clazz.getSuperclass());
        }
        return superclasses;
    }


}
