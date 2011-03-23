package eu.scy.agents.conceptmap.proposer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.germanStemmer;

import edu.emory.mathcs.backport.java.util.Arrays;

public class Stemmer {

    private static SnowballStemmer stemmer;
    
    private static Set<String> stopwords;
    
    static {
        stemmer = new englishStemmer();
        readStopwords("en");
    }

    public synchronized static String stem(String string) {
        stemmer.setCurrent(string.toLowerCase());
        stemmer.stem();
        return stemmer.getCurrent();
    }

    private static void readStopwords(String lang) {
        stopwords = new HashSet<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Stemmer.class.getResourceAsStream("/" + lang + "_stopwords.txt")));
            String buffer = null;
            while ((buffer = br.readLine()) != null) {
                try {
                    stopwords.add(Stemmer.stem(buffer));
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }

    public synchronized static boolean equalStem(String s1, String s2) {
        return stemWordWise(s1).equals(stemWordWise(s2));
    }
    
    public synchronized static boolean equalStemUnordered(String s1, String s2) {
        return stemWordWise(s1, true, true).equals(stemWordWise(s2, true, true));
    }

    @SuppressWarnings("unchecked")
    public synchronized static boolean equalStemUnorderedPartially(String s1, String s2) {
        String stemmedS1 = stemWordWise(s1, true, true);
        String stemmedS2 = stemWordWise(s2, true, true);
        HashSet<String> setS1 = new HashSet<String>(Arrays.asList(stemmedS1.split(" ")));
        HashSet<String> setS2 = new HashSet<String>(Arrays.asList(stemmedS2.split(" ")));
        return !Collections.disjoint(setS1, setS2);
    }
    
    public synchronized static String stemWordWise(String string, boolean order, boolean removeStopwords) {
        ArrayList<String> words = new ArrayList<String>();
        for (String l : string.toLowerCase().split(" ")) {
            if (!removeStopwords || !stopwords.contains(l)) {
                words.add(Stemmer.stem(l));
            }
        }
        if (order) {
            Collections.sort(words);
        }
        String temp = "";
        for (String w : words) {
            temp += w + " ";
        }
        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }
    
    public synchronized static String stemWordWise(String string) {
        return stemWordWise(string, false, false);
    }

    public static void setLanguage(String language) {
        if (language.equals("en")) {
            stemmer = new englishStemmer();
        } else if (language.equals("de")){
            stemmer = new germanStemmer();
        }
        readStopwords(language);
    }

}
