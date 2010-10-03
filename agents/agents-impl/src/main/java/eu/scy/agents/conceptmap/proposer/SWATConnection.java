package eu.scy.agents.conceptmap.proposer;

import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.swat.SWATClient;
import info.collide.swat.beans.DatatypePropertyValueBean;
import info.collide.swat.beans.ObjectPropertyValueBean;
import info.collide.swat.beans.PropertyValueBean;
import info.collide.swat.model.Class;
import info.collide.swat.model.DatatypeAnnotation;
import info.collide.swat.model.DatatypeAnnotation.Type;
import info.collide.swat.model.Entity;
import info.collide.swat.model.Instance;
import info.collide.swat.model.OWLType;
import info.collide.swat.model.SWATException;
import info.collide.swat.model.XSDValue;

import java.util.HashSet;

public class SWATConnection implements OntologyConnection {

    private SWATClient sc;

    private String language;

    private String ontologyNamespace;

    public SWATConnection(String language, String ontologyNamespace) {
        try {
            Stemmer.setLanguage(language);
            this.language = language;
            this.ontologyNamespace = ontologyNamespace;
            this.sc = new SWATClient(ontologyNamespace, "scy.collide.info", 2525, OWLType.OWL_DL, new User("CM-Enricher2SWAT"), false);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getOntologyTerms(String namespace) throws TupleSpaceException {
        try {
            DatatypeAnnotation[] listLabels = sc.getOntology().listLabels(language);
            String[] result = new String[listLabels.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = listLabels[i].getValue();
            }
            // Instance[] instances = sc.getOntology().listInstances();
            // Class[] classes = sc.getOntology().listClasses();
            // String[] result = new String[instances.length + classes.length];
            // int i = 0;
            // for (; i < instances.length; i++) {
            // result[i] = instances[i].getId().getName();
            // }
            // for (; i - instances.length < classes.length; i++) {
            // result[i] = classes[i - instances.length].getId().getName();
            // }
            return result;
        } catch (SWATException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String lookupEntityType(String entity, String namespace) throws TupleSpaceException {
        // Entity e = sc.getOntology().getEntity(namespace + entity);
        DatatypeAnnotation da = new DatatypeAnnotation(Type.LABEL, language, entity);
        try {
            Entity e = sc.getOntology().getEntityForLabel(da);
            if (e instanceof Class) {
                return "class";
            } else if (e instanceof Instance) {
                return "individual";
            }
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        // should never happen ;)
        return null;
    }

    @Override
    public String[] getSubclassesOfClass(String entity, String namespace) throws TupleSpaceException {
        try {
            // Entity e = sc.getOntology().getEntity(namespace + entity);
            Entity e = sc.getOntology().getEntityForLabel(new DatatypeAnnotation(Type.LABEL, language, entity));
            Class c = (Class) e;
            String[] result = new String[c.getDirectSubClasses().length];
            for (int i = 0; i < result.length; i++) {
                result[i] = getLabels(c.getDirectSubClasses()[i]);
            }
            return result;
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String[] getSuperclassesOfClass(String entity, String namespace) throws TupleSpaceException {
        try {
            // Entity e = sc.getOntology().getEntity(namespace + entity);
            Entity e = sc.getOntology().getEntityForLabel(new DatatypeAnnotation(Type.LABEL, language, entity));
            Class c = (Class) e;
            String[] result = new String[c.getDirectSuperClasses().length];
            for (int i = 0; i < result.length; i++) {
                result[i] = getLabels(c.getDirectSuperClasses()[i]);
            }
            return result;
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String[] getInstancesOfClass(String entity, String namespace) throws TupleSpaceException {
        try {
            // Entity e = sc.getOntology().getEntity(namespace + entity);
            Entity e = sc.getOntology().getEntityForLabel(new DatatypeAnnotation(Type.LABEL, language, entity));
            Class c = (Class) e;
            String[] result = new String[c.getInstances().length];
            for (int i = 0; i < result.length; i++) {
                result[i] = getLabels(c.getInstances()[i]);
            }
            return result;
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String[] getClassesOfInstance(String entity, String namespace) throws TupleSpaceException {
        try {
            // Entity e = sc.getOntology().getEntity(namespace + entity);
            Entity e = sc.getOntology().getEntityForLabel(new DatatypeAnnotation(Type.LABEL, language, entity));
            Instance i = (Instance) e;
            String[] result = new String[i.getInstancesOf().length];
            for (int x = 0; x < result.length; x++) {
                result[x] = getLabels(i.getInstancesOf()[x]);
            }
            return result;
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public String[][] getPropValuesOfInstance(String entity, String namespace) throws TupleSpaceException {
        try {
            // Entity e = sc.getOntology().getEntity(namespace + entity);
            Entity e = sc.getOntology().getEntityForLabel(new DatatypeAnnotation(Type.LABEL, language, entity));
            Instance i = (Instance) e;
            HashSet<String[]> result = new HashSet<String[]>();
            for (int x = 0; x < i.getPropertyValues().length; x++) {
                PropertyValueBean<?> pvb = i.getPropertyValues()[x];
                if (pvb instanceof DatatypePropertyValueBean) {
                    DatatypePropertyValueBean b = (DatatypePropertyValueBean) pvb;
                    for (XSDValue v : b.getValues()) {
                        result.add(new String[] { pvb.getProperty().getName(), v.getValue() });
                    }
                } else {
                    ObjectPropertyValueBean b = (ObjectPropertyValueBean) pvb;
                    for (Instance v : b.getValues()) {
                        result.add(new String[] { pvb.getProperty().getName(), getLabels(v) });
                    }
                }
            }
            return (String[][]) result.toArray(new String[result.size()][]);
        } catch (SWATException e1) {
            e1.printStackTrace();
        }
        return new String[0][0];
    }

    private String getLabels(Entity e) throws SWATException {
        DatatypeAnnotation[] das = e.getDatatypeAnnotations();
        String result = ""; 
        for (DatatypeAnnotation da : das) {
            if (da.getType() == Type.LABEL && da.getLang().equals(language)) {
                result+= da.getValue() + ","; 
            }
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public String getLanguage() {
        return language;
    }
    
    public String getOntologyNamespace() {
        return ontologyNamespace;
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
