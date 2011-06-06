package edu.scy.tools.math.test.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexExample {

	public static void main(String[] args) {
		
		String eq = "r+5+sqrt(6+r)";
		
		String p = "r(?!t)";
		
		eq.replaceAll(p, "3");
 
		System.out.println(eq);
		
        Pattern pattern = Pattern.compile(p);

        Matcher matcher = pattern.matcher(eq);
        String output = matcher.replaceAll("2");
        System.out.println(output);
	}
}
