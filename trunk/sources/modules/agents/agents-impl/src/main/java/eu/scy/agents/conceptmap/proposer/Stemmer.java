package eu.scy.agents.conceptmap.proposer;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.germanStemmer;

public class Stemmer {

    private static SnowballStemmer stemmer;
    
    static {
        stemmer = new englishStemmer();
    }

    public synchronized static String stem(String string) {
        stemmer.setCurrent(string.toLowerCase());
        stemmer.stem();
        return stemmer.getCurrent();
    }

    public synchronized static boolean equalStem(String s1, String s2) {
        return stemWordWise(s1).equals(stemWordWise(s2));
    }
    
    public synchronized static String stemWordWise(String string) {
        String temp = "";
        // no stringbuilder, because there will be rarely any terms with words > 2
        for (String l : string.toLowerCase().split(" ")) {
            temp += Stemmer.stem(l) + " ";
        }
        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }

    public static void setLanguage(String language) {
        if (language.equals("en")) {
            stemmer = new englishStemmer();
        } else if (language.equals("de")){
            stemmer = new germanStemmer();
        }
    }

}
