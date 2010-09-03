package eu.scy.agents.conceptmapenrich;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.germanStemmer;

public class Stemmer {

    private static SnowballStemmer stemmer;
    
    static {
        stemmer = new englishStemmer();
    }

    public static String stem(String string) {
        stemmer.setCurrent(string);
        stemmer.stem();
        return stemmer.getCurrent();
    }

    public static String stemWordWise(String string) {
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
