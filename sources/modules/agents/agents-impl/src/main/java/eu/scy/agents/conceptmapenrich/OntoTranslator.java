package eu.scy.agents.conceptmapenrich;

import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.swat.SWATClient;
import info.collide.swat.beans.AnnotationBean;
import info.collide.swat.beans.ClassBean;
import info.collide.swat.beans.InstanceBean;
import info.collide.swat.beans.OperationBean;
import info.collide.swat.beans.OperationBean.Op;
import info.collide.swat.model.Class;
import info.collide.swat.model.DatatypeAnnotation;
import info.collide.swat.model.Entity;
import info.collide.swat.model.Instance;
import info.collide.swat.model.OWLType;
import info.collide.swat.model.SWATException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OntoTranslator {

    private SWATClient sc;

    private TreeMap<String, String> entities;

    private String ontologyURL;

    private String ontologyName;

    private static final String ONTOLOGY_LANGUAGE = "en";
    
    public OntoTranslator(String ontologyURL) {
        try {
            this.sc = new SWATClient(ontologyURL, "scy.collide.info", 2525, OWLType.OWL_DL, new User("OntoTranslator"), false);
            this.ontologyURL = ontologyURL;
            this.ontologyName = ontologyURL.substring(ontologyURL.lastIndexOf("/") + 1,ontologyURL.indexOf("#"));
            this.entities = new TreeMap<String, String>();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        OntoTranslator ot = new OntoTranslator(ConceptMapEnrichmentAgent.ONTOLOGY_NS);
//        ot.createFirstFile();
//        ot.readFirstFile();
//        ot.createFilesFor("de");
        ot.writeLabels(false, "de", "en");
        ot.finish();
    }

    private void finish() throws TupleSpaceException {
        sc.finishSession();
    }

    public void readFirstFile() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.load(new FileReader(ontologyName + "_" + ONTOLOGY_LANGUAGE + ".properties"));
        for (Entry<Object, Object> e : props.entrySet()) {
            entities.put(e.getKey().toString(), e.getValue().toString());
        }
    }

    public void writeLabels(boolean overwrite, String... countryCodes) throws FileNotFoundException, IOException, SWATException {
        for (String countryCode : countryCodes) {
            Properties props = new Properties();
            props.load(new FileReader(ontologyName + "_" + countryCode + ".properties"));
            System.out.println("Writing " + props.keySet().size() + " labels for locale '" + countryCode + "':");
            for (Object key : props.keySet()) {
                Entity e = sc.getOntology().getEntity(ontologyURL + key);
                DatatypeAnnotation da = new DatatypeAnnotation(DatatypeAnnotation.Type.LABEL, countryCode, props.get(key).toString());
                AnnotationBean ab;
                if (overwrite) {
                    ab = new AnnotationBean(e.getObjectAnnotations(), new DatatypeAnnotation[] { da });
                } else {
                    DatatypeAnnotation[] oldDAs = e.getDatatypeAnnotations();
                    DatatypeAnnotation[] newDAs = Arrays.copyOf(oldDAs, oldDAs.length + 1);
                    newDAs[newDAs.length - 1] = da;
                    ab = new AnnotationBean(e.getObjectAnnotations(), newDAs);
                }
                OperationBean ob;
                if (e instanceof Class) {
                    ob = new OperationBean(e, new ClassBean(e, ab, null, null, (Class[]) null), Op.MODIFY_CLASS);
                } else {
                    ob = new OperationBean(e, new InstanceBean(e, ab, null, null, null, null), Op.MODIFY_INSTANCE);
                }
                ob.setUpdAnnotations(true);
                try {
                    System.out.print("Writing " + key + " ...");
                    Vector<OperationBean> conflicts = sc.doOperations(ob);
                    System.out.println(" Done!");
                    if (!conflicts.isEmpty()) {
                        System.err.println("Conflicts occurred:\n" + conflicts);
                    }
                } catch (SWATException e1) {
                    e1.printStackTrace();
                }
            }
        }
        
    }

    public void createFilesFor(String... countryCodes) throws IOException {
        String nl = System.getProperty("line.separator");
        for (String countryCode : countryCodes) {
            FileWriter fw = new FileWriter(ontologyName + "_" + countryCode + ".properties");
            try {
                Iterator<String> valueIt = entities.values().iterator();
                Iterator<String> keyIt = entities.keySet().iterator();
                StringBuilder sb = new StringBuilder();
                while (valueIt.hasNext()) {
                    for (int i = 0; i < 1 && valueIt.hasNext(); i++) {
                            sb.append(valueIt.next());
                            sb.append("\n");
                    }
                    String[] translations = translate(ONTOLOGY_LANGUAGE, countryCode, sb.toString()).split("\n");
                    for (int i = 0; i < 1; i++) {
                        fw.write(keyIt.next()+ " = " + translations[i] + nl);
                    }
                    sb.delete(0, sb.length());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fw.close();
            }
        }
    }

    public void createFirstFile() throws IOException {
        FileWriter fw = new FileWriter(ontologyName + "_" + ONTOLOGY_LANGUAGE + ".properties");
        try {
            Instance[] instances = sc.getOntology().listInstances();
            Class[] classes = sc.getOntology().listClasses();
            for (int i = 0; i < instances.length; i++) {
                String instance = instances[i].getId().getName();
                if (entities.containsKey(instance)) {
                    System.err.println("Class " + instance + " already known!");
                } else {
                    entities.put(instance, unCamelize(instance, false));
                }
            }
            for (int i = 0; i < classes.length; i++) {
                String className = classes[i].getId().getName();
                if (entities.containsKey(className)) {
                    System.err.println("Instance " + className + " already known!");
                } else {
                    entities.put(className, unCamelize(className, false));
                }
            }
            String nl = System.getProperty("line.separator");
            for (String key : entities.keySet()) {
                fw.write(key + " = " + entities.get(key) + nl);
            }
        } catch (SWATException e) {
            e.printStackTrace();
        } finally {
            fw.close();
        }
    }

    private String unCamelize(String nextToken, boolean stem) {
        Matcher m = Pattern.compile("\\p{Lu}").matcher(nextToken);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, " " + m.group());
        }
        m.appendTail(sb);
        String[] words = sb.toString().split(" ");
        sb.delete(0, sb.length());
        for (String s : words) {
            if (stem) {
                sb.append(Stemmer.stem(s)).append(" ");
            } else {
                sb.append(s).append(" ");
            }
        }
        return sb.toString().trim().toLowerCase();
    }
    
    private String translate(String fromLanguage, String toLanguage, String string) throws MalformedURLException, IOException {
        string = URLEncoder.encode(string, "utf-8");
        String str = "http://ajax.googleapis.com/ajax/services/language/translate?v=1.0&langpair=" + fromLanguage + "|" + toLanguage + "&q=" + string;
        Scanner sc = new Scanner(new URL(str).openStream());
        str = sc.nextLine();
        int index = str.indexOf("\"responseStatus\": ");
        index += "\"responseStatus\": ".length();
        String tmp = str.substring(index, str.length() - 1);
        int status = new Integer(tmp);
        if (status == 200) {
            index = str.indexOf("translatedText\":\"");
            index += "translatedText\":\"".length();
            tmp = str.substring(index, str.indexOf("\"}", index));
            return tmp;
        }
        return str;
    }
    
}
