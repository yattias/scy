package eu.scy.agents.queryexpansion.importer;

import info.collide.swat.SWATClient;
import info.collide.swat.beans.ClassBean;
import info.collide.swat.beans.DatatypePropertyBean;
import info.collide.swat.beans.DatatypePropertyValueBean;
import info.collide.swat.beans.InstanceBean;
import info.collide.swat.beans.ObjectPropertyBean;
import info.collide.swat.beans.ObjectPropertyValueBean;
import info.collide.swat.beans.OperationBean;
import info.collide.swat.beans.OperationBean.Op;
import info.collide.swat.beans.PropertyValueBean;
import info.collide.swat.model.Class;
import info.collide.swat.model.ClassObjectPropertyRange;
import info.collide.swat.model.DatatypeProperty;
import info.collide.swat.model.Entity;
import info.collide.swat.model.Instance;
import info.collide.swat.model.NamedClass;
import info.collide.swat.model.ObjectProperty;
import info.collide.swat.model.Ontology;
import info.collide.swat.model.Property;
import info.collide.swat.model.SWATException;
import info.collide.swat.model.UnknownClass;
import info.collide.swat.model.XSDType;
import info.collide.swat.model.XSDTypeDatatypePropertyRange;
import info.collide.swat.model.XSDValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JProgressBar;

public class KeywordImporter {

    private static final String KEYWORD_URI = "Keyword";

    private static final String KEYWORD_LIST_URI = "KeywordList";

    private static final String BELONGS_TO_LIST_URI = "belongsToList";

    private static final String IS_CONNECTED_TO_URI = "isConnectedTo";

    private Map<Integer, Set<String>> keywordMap;

    private Map<String, Integer> labelsMap;

    private Class keywordConcept;

    private Class keywordListConcept;

    private ObjectProperty belongsTo;

    private DatatypeProperty isConnectedTo;

    private boolean prepared;

    public static void main(String[] args) throws Exception {
        KeywordImporter ki = new KeywordImporter();
        ki.parseFiles(new File("/home/weinbrenner/Desktop/topWords_10.txt"), new File("/home/weinbrenner/Desktop/labels_10.txt"));

        System.out.println(ki);
    }

    public KeywordImporter() {
        this.prepared = false;
        keywordMap = new HashMap<Integer, Set<String>>();
        labelsMap = new HashMap<String, Integer>();
    }

    public void importIntoOntology(SWATClient sc, String entityName, String cloudName, String[] keywords, JProgressBar progressBar) throws SWATException {
        progressBar.setMaximum(1 + keywords.length);
        Ontology ont = sc.getOntology();

        Entity cloud = sc.getOntology().getEntity(sc.getOntology().getNameSpace() + cloudName);
        if (cloud instanceof UnknownClass) {
            cloud = new Instance(cloudName, ont);
            DatatypePropertyValueBean dpvb = new DatatypePropertyValueBean(isConnectedTo, new XSDValue(entityName, XSDType.STRING, ont));
            OperationBean obCloud = new OperationBean(cloud, new InstanceBean(cloud, null, new Class[] { keywordListConcept }, new DatatypePropertyValueBean[] { dpvb }, null, null), Op.CREATE_INSTANCE);
            progressBar.setValue(progressBar.getValue() + 1);
            sc.doOperations(obCloud);
        } else {
            DatatypePropertyValueBean dpvb = null;
            for (PropertyValueBean<?> pvb : ((Instance) (cloud)).getPropertyValues()) {
                if (pvb.getProperty() == isConnectedTo) {
                    XSDValue[] prevValues = (XSDValue[]) pvb.getValues();
                    XSDValue[] newValues = Arrays.copyOf(prevValues, prevValues.length + 1);
                    newValues[newValues.length - 1] = new XSDValue(entityName, XSDType.STRING, ont);
                    dpvb = new DatatypePropertyValueBean(isConnectedTo, newValues);
                }
            }
            OperationBean obCloud = new OperationBean(cloud, new InstanceBean(cloud, null, null, new DatatypePropertyValueBean[] { dpvb }, null, null), Op.MODIFY_INSTANCE);
            obCloud.setUpdProps(true);
            progressBar.setValue(progressBar.getValue() + 1);
            sc.doOperations(obCloud);
        }

        for (String kw : keywords) {
            // TODO auch hier erstmal testen, ob die schon erzeugt worden sind
            Instance kwInst = new Instance(kw, ont);
            ObjectPropertyValueBean opvb = new ObjectPropertyValueBean(belongsTo, (Instance) cloud);
            OperationBean obKeyword = new OperationBean(kwInst, new InstanceBean(kwInst, null, new Class[] { keywordConcept }, new ObjectPropertyValueBean[] { opvb }, null, null), Op.CREATE_INSTANCE);
            sc.doOperations(obKeyword);
            progressBar.setValue(progressBar.getValue() + 1);
        }
    }

    public void prepareOntology(SWATClient sc) throws SWATException {
        if (prepared) {
            return;
        }
        prepared = true;
        Ontology ont = sc.getOntology();
        String ns = ont.getNameSpace();

        keywordConcept = (Class) ont.getEntity(ns + KEYWORD_URI);
        if (keywordConcept instanceof UnknownClass) {
            keywordConcept = new NamedClass(KEYWORD_URI, ont);
            OperationBean ob = new OperationBean(keywordConcept, new ClassBean(keywordConcept, null, null, null, null), Op.CREATE_CLASS);
            Vector<OperationBean> conflicts = sc.doOperations(ob);
            if (!conflicts.isEmpty()) {
                throw new SWATException("Conflicts while creating class: " + conflicts);
            }
        }

        keywordListConcept = (Class) ont.getEntity(ns + KEYWORD_LIST_URI);
        if (keywordListConcept instanceof UnknownClass) {
            keywordListConcept = new NamedClass(KEYWORD_LIST_URI, ont);
            OperationBean ob = new OperationBean(keywordListConcept, new ClassBean(keywordListConcept, null, null, null, null), Op.CREATE_CLASS);
            Vector<OperationBean> conflicts = sc.doOperations(ob);
            if (!conflicts.isEmpty()) {
                throw new SWATException("Conflicts while creating class: " + conflicts);
            }
        }

        belongsTo = null;
        for (Property p : keywordConcept.getAllProperties()) {
            if (p.getName().endsWith(BELONGS_TO_LIST_URI)) {
                belongsTo = (ObjectProperty) p;
            }
        }
        if (belongsTo == null) {
            belongsTo = new ObjectProperty(BELONGS_TO_LIST_URI, ont);
            OperationBean ob = new OperationBean(belongsTo, new ObjectPropertyBean(belongsTo, null, null, new info.collide.swat.model.Class[] { keywordConcept }, new ClassObjectPropertyRange(keywordListConcept), false, false, false, false, false, null, null), Op.CREATE_OBJECT_PROPERTY);
            Vector<OperationBean> conflicts = sc.doOperations(ob);
            if (!conflicts.isEmpty()) {
                throw new SWATException("Conflicts while creating property: " + conflicts);
            }
        }

        isConnectedTo = null;
        for (Property p : keywordListConcept.getAllProperties()) {
            if (p.getName().endsWith(IS_CONNECTED_TO_URI)) {
                isConnectedTo = (DatatypeProperty) p;
            }
        }
        if (isConnectedTo == null) {
            isConnectedTo = new DatatypeProperty(IS_CONNECTED_TO_URI, ont);
            OperationBean ob = new OperationBean(isConnectedTo, new DatatypePropertyBean(isConnectedTo, null, null, new info.collide.swat.model.Class[] { keywordListConcept }, new XSDTypeDatatypePropertyRange(XSDType.STRING), false, false, null), Op.CREATE_DATATYPE_PROPERTY);
            Vector<OperationBean> conflicts = sc.doOperations(ob);
            if (!conflicts.isEmpty()) {
                throw new SWATException("Conflicts while creating property: " + conflicts);
            }
        }
    }

    public void parseFiles(File topWords, File labels) throws IOException {
        keywordMap.clear();
        labelsMap.clear();
        String buffer = "";
        BufferedReader br = null;
        if (topWords != null && topWords.exists()) {
            br = new BufferedReader(new FileReader(topWords));
            while ((buffer = br.readLine()) != null) {
                String[] line = buffer.split("\t");
                Integer index = Integer.parseInt(line[0].trim());
                String[] words = line[1].split(" ");
                TreeSet<String> wordSet = new TreeSet<String>();
                wordSet.addAll(Arrays.asList(words));
                keywordMap.put(index, wordSet);
            }
            br.close();
        }
        if (labels != null && labels.exists()) {
            br = new BufferedReader(new FileReader(labels));
            while ((buffer = br.readLine()) != null) {
                String[] line = buffer.split("\t");
                line[1] = line[1].replace(' ', '_');
                Integer index = Integer.parseInt(line[0].trim());
                labelsMap.put(line[1], index);
            }
            br.close();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Integer> e : labelsMap.entrySet()) {
            sb.append("Group ");
            sb.append(e.getValue());
            sb.append(": ");
            sb.append(e.getKey());
            sb.append("\n");
            Set<String> words = keywordMap.get(e.getKey());
            for (String word : words) {
                sb.append(word);
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("\n\n");
        }
        return sb.toString();
    }

    public Map<Integer, Set<String>> getKeywordMap() {
        return keywordMap;
    }

    public Map<String, Integer> getLabelsMap() {
        return labelsMap;
    }

    public Entity[] getConnections(SWATClient sc, String entityUri) {
        try {
             Entity e = sc.getOntology().getEntity(entityUri);
//            DatatypeAnnotation label = new DatatypeAnnotation(Type.LABEL, "en", entityUri);
//            Entity e = sc.getOntology().getEntityForLabel(label);
            if (e instanceof UnknownClass) {
                return new Entity[0];
            } else if (e instanceof Instance) {
                Instance i = (Instance) e;
                PropertyValueBean<?>[] propertyValues = i.getPropertyValues();
                for (PropertyValueBean<?> pvb : propertyValues) {
                    if (pvb.getProperty() == isConnectedTo) {
                        XSDValue[] values = (XSDValue[]) pvb.getValues();
                        Entity[] result = new Entity[values.length];
                        for (int j = 0; j < values.length; j++) {
                            result[j] = sc.getOntology().getEntity(values[j].getValue());
                        }
                        return result;
                    }
                }
            }
        } catch (SWATException e) {
            e.printStackTrace();
        }
        return new Entity[0];
    }

    /**
     * @return the keywordConcept
     */
    public Class getKeywordConcept() {
        return keywordConcept;
    }

    /**
     * @return the keywordListConcept
     */
    public Class getKeywordListConcept() {
        return keywordListConcept;
    }

    /**
     * @return the belongsTo
     */
    public ObjectProperty getBelongsTo() {
        return belongsTo;
    }

    /**
     * @return the isConnectedTo
     */
    public DatatypeProperty getIsConnectedTo() {
        return isConnectedTo;
    }

    public void saveFiles(File keywordsFile, File labelsFile) throws IOException {
        TreeMap<Integer, String> m = new TreeMap<Integer, String>();
        for (String s : labelsMap.keySet()) {
            m.put(labelsMap.get(s), s);
        }
        PrintWriter pw = new PrintWriter(labelsFile);
        for (Integer i : m.keySet()) {
            pw.println(i + "\t" + m.get(i));
        }
        pw.close();
        
        pw = new PrintWriter(keywordsFile);
        for (Integer i : keywordMap.keySet()) {
            pw.print(i + "\t");
            Set<String> set = keywordMap.get(i);
            for (String s : set) {
                pw.print(s + " ");
            }
            pw.println();
        }
        pw.close();
        
    }

}
