package eu.scy.tools.math.ui.panels;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ExpressionModel {

	List<String> expressionToEval = new ArrayList<String>();
	List<String> expressionToDisplay = new ArrayList<String>();
	
	 static final String PI = "pi";
	 static final String cubedsym = "^2";
	static final String SQRT = "sqrt";
	static final String CBRT = "cbrt";
	static String cubeRoot = "&#179 &#8730";
	static String sqRoot = "&#8730";
	static String cubed = "<sup>2</sup>";
	boolean needsReplacement = false;
	public void addExpression(String exp) {
		//check rational
		//check powers
		//check pi
		
		
		
		if( exp.equals(CBRT) ) {
			expressionToDisplay.add(cubeRoot);
			
			expressionToEval.add("exp(log(k)/3)");
			
			needsReplacement = true;
		} else {
			
			if( needsReplacement == true && StringUtils.isNumeric(exp)) {
				String last = expressionToEval.get(expressionToEval.size()-1);
				String newLast = StringUtils.replace(last, "k", exp);
				
				expressionToEval.remove(expressionToEval.size()-1);
				expressionToEval.add(newLast);
				
				needsReplacement = false;
				
			} else {
				expressionToEval.add(exp);
			}
			
			
			expressionToDisplay.add(exp);
		}
		
		
	}
	
	public void removeLastExpression() {
		//check rational
		//check powers
	}
	
	public String getExpressionEval() {
		StringBuffer buff = new StringBuffer();
		for (String exp : expressionToEval) {
			buff.append(exp);
		}
		return buff.toString();
	}
	
	public String getExpressionHTML() {
		
		StringBuffer buff = new StringBuffer("<html>");
		for (String exp : expressionToDisplay) {
			buff.append(" " +exp);
		}
		buff.append("</html>");
		return buff.toString();
	}
	
}
