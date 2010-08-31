package eu.scy.agents.conceptmapenrich;

import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.swat.SWATClient;
import info.collide.swat.beans.DatatypePropertyValueBean;
import info.collide.swat.beans.ObjectPropertyValueBean;
import info.collide.swat.beans.PropertyValueBean;
import info.collide.swat.model.Class;
import info.collide.swat.model.Entity;
import info.collide.swat.model.Instance;
import info.collide.swat.model.OWLType;
import info.collide.swat.model.SWATException;
import info.collide.swat.model.XSDValue;

import java.util.HashSet;


public class SWATConnection implements OntologyConnection {

    private SWATClient sc;
    
    public SWATConnection() {
        try {
            sc = new SWATClient("http://www.scy.eu/co2house#", "scy.collide.info", 2525, OWLType.OWL_DL, new User("CM-Enricher2SWAT"), false);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String[] getOntologyTerms(String namespace) throws TupleSpaceException {
        try {
            Instance[] instances = sc.getOntology().listInstances();
            Class[] classes = sc.getOntology().listClasses();
            String[] result = new String[instances.length + classes.length];
            int i = 0;
            for (; i < instances.length; i++) {
                result[i] = instances[i].getId().getName();
            }
            for (; i - instances.length < classes.length; i++) {
                result[i] = classes[i - instances.length].getId().getName();
            }
            return result;
        } catch (SWATException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String lookupEntityType(String entity, String namespace) throws TupleSpaceException {
        Entity e = sc.getOntology().getEntity(namespace + entity);
        if (e instanceof Class) {
            return "class";
        } else if (e instanceof Instance) {
            return "individual";
        }
        // should never happen ;)
        return null;
    }

    @Override
    public String[] getSubclassesOfClass(String entity, String namespace) throws TupleSpaceException {
        Entity e = sc.getOntology().getEntity(namespace + entity);
        Class c = (Class) e;
        try {
            String[] result = new String[c.getDirectSubClasses().length];
            for (int i = 0; i < result.length; i++) {
                result[i] = c.getDirectSubClasses()[i].getId().getName();
            }
            return result;
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String[] getSuperclassesOfClass(String entity, String namespace) throws TupleSpaceException {
        Entity e = sc.getOntology().getEntity(namespace + entity);
        Class c = (Class) e;
        try {
            String[] result = new String[c.getDirectSuperClasses().length];
            for (int i = 0; i < result.length; i++) {
                result[i] = c.getDirectSuperClasses()[i].getId().getName();
            }
            return result;
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String[] getInstancesOfClass(String entity, String namespace) throws TupleSpaceException {
        Entity e = sc.getOntology().getEntity(namespace + entity);
        Class c = (Class) e;
        try {
            String[] result = new String[c.getInstances().length];
            for (int i = 0; i < result.length; i++) {
                result[i] = c.getInstances()[i].getId().getName();
            }
            return result;
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String[] getClassesOfInstance(String entity, String namespace) throws TupleSpaceException {
        Entity e = sc.getOntology().getEntity(namespace + entity);
        Instance i = (Instance) e;
        try {
            String[] result = new String[i.getInstancesOf().length];
            for (int x = 0; x < result.length; x++) {
                result[x] = i.getInstancesOf()[x].getId().getName();
            }
            return result;
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String[] getPropValuesOfInstance(String entity, String namespace) throws TupleSpaceException {
        Entity e = sc.getOntology().getEntity(namespace + entity);
        Instance i = (Instance) e;
        try {
            HashSet<String> result = new HashSet<String>();
            for (int x = 0; x < i.getPropertyValues().length; x++) {
                PropertyValueBean<?> pvb = i.getPropertyValues()[x];
                if (pvb instanceof DatatypePropertyValueBean) {
                    DatatypePropertyValueBean b = (DatatypePropertyValueBean) pvb;
                    for (XSDValue v : b.getValues()) {
                        result.add(pvb.getProperty().getName() + " " + v.getValue());
                    }
                } else {
                    ObjectPropertyValueBean b = (ObjectPropertyValueBean) pvb;
                    for (Instance v : b.getValues()) {
                        result.add(pvb.getProperty().getName() + " " + v.getId().getName());
                    }
                }
            }
            return (String[]) result.toArray(new String[result.size()]);
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public void close() {
        try {
            sc.finishSession();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }


}
