package eu.scy.agents.conceptmap.model;

import java.util.ArrayList;


public class EscapeUtils {

    public static final String SEPARATOR = ",";

    public static final String ESCAPE_CHAR = "\\";

    public static String escape(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
        	String escapedString = "";
        	if(s != null) {
        		escapedString = s.replace(ESCAPE_CHAR, ESCAPE_CHAR + ESCAPE_CHAR).replace(SEPARATOR, ESCAPE_CHAR + SEPARATOR);
        	}
        	sb.append(escapedString + SEPARATOR);
        }
        sb.delete(sb.length() - SEPARATOR.length(), sb.length());
        return sb.toString();
    }

    public static String[] deEscape(String string) {
        ArrayList<String> results = new ArrayList<String>();
        ArrayList<Integer> markedPositions = new ArrayList<Integer>();
        for (int i = 0; string.indexOf(ESCAPE_CHAR + ESCAPE_CHAR, i) != -1;) {
            int index = string.indexOf(ESCAPE_CHAR + ESCAPE_CHAR, i);
            markedPositions.add(index);
            string = string.substring(0, index) + ESCAPE_CHAR + string.substring(index + 2);
            i = index + 1;
        }
        for (int i = 0; string.indexOf(ESCAPE_CHAR + SEPARATOR, i) != -1;) {
            int index = string.indexOf(ESCAPE_CHAR + SEPARATOR, i);
            if (!markedPositions.contains(index)) {
                string = string.substring(0, index) + SEPARATOR + string.substring(index + 2);
            }
            i = index + 1;
        }
        for (int i = 0; string.indexOf(SEPARATOR, i) != -1;) {
            int index = string.indexOf(SEPARATOR, i);
            if (!markedPositions.contains(index - 1)) {
                results.add(string.substring(0, index));
                string = string.substring(index + 1);
            }
            i = index + 1;
        }
        results.add(string);
        return (String[]) results.toArray(new String[results.size()]);
    }

// Only for testing purposes
//    public static void main(String[] args) {
//        String test = "abc\\\\,def\\,ghj";
//        System.out.println(Arrays.toString(deEscape(test)));
//    }

}
