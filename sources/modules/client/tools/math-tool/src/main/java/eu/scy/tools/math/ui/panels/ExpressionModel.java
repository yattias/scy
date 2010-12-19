package eu.scy.tools.math.ui.panels;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

public class ExpressionModel {

	private static Logger log = Logger.getLogger("ExpressionModel.class");
	
//	List<String> expressionToEval = new ArrayList<String>();
//	List<String> expressionToDisplay = new ArrayList<String>();
//	
	StringBuilder expressionToDisplay = new StringBuilder();
	
	static final String PI = "pi";
	static final String cubedsym = "^2";
	static final String SQRT = "sqrt";
	static final String CBRT = "cbrt";
	static String cubeRoot = "&#179 &#8730";
	static String sqRoot = "&#8730";
	static String cubed = "<sup>2</sup>";
	boolean needsReplacement = false;
	
	public void addExpression(String exp) {
		expressionToDisplay.append(exp);
	}
	
	public void replaceExpression(String exp) {
		this.clear();
		this.addExpression(exp);
	}
	
	public String getExpressionEval() {
		
		//strip spaces
		
		String stripToNull = StringUtils.stripToNull(expressionToDisplay.toString());
		
		
		return stripToNull;
	}
	
	public String getExpressionDisplay() {
		return expressionToDisplay.toString();
	}
	
	public void clear() {
		
		if( expressionToDisplay.length() < 1)
			return;
		
		expressionToDisplay.delete(0, expressionToDisplay.length());
	}
}
