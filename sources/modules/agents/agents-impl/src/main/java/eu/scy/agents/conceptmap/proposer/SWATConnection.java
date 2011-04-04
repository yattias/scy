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
import info.collide.swat.model.NamedClass;
import info.collide.swat.model.OWLType;
import info.collide.swat.model.ObjectProperty;
import info.collide.swat.model.Property;
import info.collide.swat.model.SWATException;
import info.collide.swat.model.XSDValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.scy.agents.conceptmap.proposer.CMProposerAgent.Relation;

public class SWATConnection implements OntologyConnection {

    private SWATClient sc;

    private String language;

    private String ontologyNamespace;

    public SWATConnection(String language, String ontologyNamespace) {
        try {
            Stemmer.setLanguage(language);
            this.language = language;
            this.ontologyNamespace = ontologyNamespace;
            this.sc = new SWATClient(ontologyNamespace, "localhost", 2525, OWLType.OWL_DL, new User("CM-Enricher2SWAT"), false);
             System.out.println("Initializing SWAT connection ...");
            // build up cache
            NamedClass[] classes = sc.getOntology().listNamedClasses();
            for (NamedClass nc : classes) {
                nc.getAllSuperClasses();
                nc.getAllSubClasses();
                nc.getAllProperties();
                nc.getLabels();
            }
            Property[] properties = sc.getOntology().listAllProperties();
            for (Property p : properties) {
                p.getDomain();
                p.getRange();
                p.getLabels();
            }
            Instance[] instances = sc.getOntology().listInstances();
            for (Instance i : instances) {
                i.getLabels();
                i.getPropertyValues();
            }
             System.out.println(" Finished!");
            sc.saveCache();
            
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        } catch (SWATException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getOntologyTerms(String namespace) throws TupleSpaceException {
        try {
            DatatypeAnnotation[] listLabels = sc.getOntology().listLabels(language);
            ArrayList<String> result = new ArrayList<String>();
            for (int i = 0; i < listLabels.length; i++) {
                result.add(listLabels[i].getValue());
            }
            Class keywordClass = (Class) sc.getOntology().getEntity(namespace + "Keyword");
            for (Instance i : keywordClass.getInstances()) {
                 result.remove(i.getLabels(language));
            }
            return (String[]) result.toArray(new String[result.size()]);
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
        } catch (ClassCastException e1) {
            System.err.println("Error while retrieving subclasses of class " + entity);
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
        } catch (ClassCastException e1) {
            System.err.println("Error while retrieving superclasses of class " + entity);
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
        } catch (ClassCastException e1) {
            System.err.println("Error while retrieving instances of class " + entity);
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
            ArrayList<String> result = new ArrayList<String>();
            for (int x = 0; x < i.getInstancesOf().length; x++) {
                String label = getLabels(i.getInstancesOf()[x]);
                if (label.trim().length() != 0) {
                    result.add(label); 
                }
            }
            return (String[]) result.toArray(new String[result.size()]);
        } catch (SWATException e1) {
            e1.printStackTrace();
        } catch (ClassCastException e1) {
            System.err.println("Error while retrieving classes of instance " + entity);
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
        } catch (ClassCastException e1) {
            System.err.println("Error while retrieving property values of instance " + entity);
            e1.printStackTrace();
        }
        return new String[0][0];
    }

    private String getLabels(Entity e) throws SWATException {
        DatatypeAnnotation[] das = e.getDatatypeAnnotations();
        String result = "";
        for (DatatypeAnnotation da : das) {
            if (da.getType() == Type.LABEL && da.getLang().equals(language)) {
                result += da.getValue() + ",";
            }
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
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

    @Override
    public Map<String, Set<String>> getOntologyClouds(String namespace) throws TupleSpaceException {
        Map<String, Set<String>> keyword2Concept = new HashMap<String, Set<String>>();
        Map<String, Set<String>> cloud2Concept = new HashMap<String, Set<String>>();
        try {
            Class keywordListClass = (Class) sc.getOntology().getEntity(namespace + "KeywordList");
            for (Instance i : keywordListClass.getInstances()) {
                PropertyValueBean<?>[] values = i.getPropertyValues();
                for (PropertyValueBean<?> pvb : values) {
                    if (pvb.getProperty().getName().equals("isConnectedTo")) {
                        XSDValue[] xsdValues = (XSDValue[]) pvb.getValues();
                        Set<String> connectionPoints = new HashSet<String>();
                        for (XSDValue xsdValue : xsdValues) {
                             connectionPoints.add(sc.getOntology().getEntity(xsdValue.getValue()).getLabels(language));
                        }
                        cloud2Concept.put(i.getId().getName(), connectionPoints);
                    }
                }
            }
            Class keywordClass = (Class) sc.getOntology().getEntity(namespace + "Keyword");
            for (Instance i : keywordClass.getInstances()) {
                PropertyValueBean<?>[] values = i.getPropertyValues();
                for (PropertyValueBean<?> pvb : values) {
                    if (pvb.getProperty().getName().equals("belongsToList")) {
                        Instance[] listInstances = (Instance[]) pvb.getValues();
                        for (Instance list : listInstances) {
                             keyword2Concept.put(i.getLabels(language),
                             cloud2Concept.get(list.getId().getName()));
                        }
                    }
                }
            }
        } catch (SWATException e) {
            // TODO return something more meaningful
            e.printStackTrace();
        }
        return keyword2Concept;
    }

    @Override
    public Map<String, Set<String>> getRelationHierarchy() throws TupleSpaceException {
        HashMap<String, Set<String>> m = new HashMap<String, Set<String>>();
        try {
            ObjectProperty[] listObjectProperties = sc.getOntology().listObjectProperties();
            for (ObjectProperty op : listObjectProperties) {
                Property[] allSubProperties = op.getAllSubProperties();
                HashSet<String> subProperties = new HashSet<String>();
                for (Property p : allSubProperties) {
                    if (Relation.isKnown(p.getName())) {
                        subProperties.add(Relation.valueOf(p.getName()).getLabel(language));
                    }
                }
                if (op.getDirectSuperProperties().length == 0) {
                    subProperties.add(Relation.isA.getLabel(language));
                }
                if (Relation.isKnown(op.getName())) {
                    m.put(Relation.valueOf(op.getName()).getLabel(language), subProperties);
                }
            }
            m.put(Relation.isA.getLabel(language), new HashSet<String>());
        } catch (SWATException e) {
            e.printStackTrace();
        }
        return m;
    }

}
