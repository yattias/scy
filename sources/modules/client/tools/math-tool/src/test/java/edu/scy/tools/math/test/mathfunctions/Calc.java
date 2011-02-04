package edu.scy.tools.math.test.mathfunctions;

import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import edu.scy.tools.math.test.ui.CalculatorMain;

public class Calc {
	private String subString[],postFixForm = "";
	private int tokenMax = 0;
	private int currentToken = 0;	
	private ExpressionTree myTree;
	private MyStack myStack;
	//Input/Tokenizer(via import)
	public Calc() {
		String stringInput;
		mainLoop:
		do {
			stringInput=JOptionPane.showInputDialog("Please Enter a Mathematical Expression:");
			if ("".equalsIgnoreCase(stringInput)||null==stringInput) {
				//System.out.println("Terminating Program!");
				stringInput = "0";
				break mainLoop;
			} else {
				stringInput = FormatReviser.autoCorrect(stringInput);
				if (ErrorHandler.syntaxCheck(stringInput)) {
					break;
				} else {
					JOptionPane.showMessageDialog(null,"Error: Syntax/Format");
					continue mainLoop;
				}
			}
			
		} while (true);
		
		StringTokenizer token=new StringTokenizer(stringInput,"%^+()-*/",true);
		subString=new String[token.countTokens()];
		tokenMax=token.countTokens();		
		for(int i=0; token.hasMoreTokens(); i++) {
			subString[i]=token.nextToken();
			//EXPERIMENTAL SECTION1: See Bottom for Details and Explanation.
			//NO Time to finish this part ^_^ HEROES MODE!!!!
			//if(subString[i].charAt(0)=='.' && Character.isDigit(subString[i].charAt(1))) {
			//	subString[i] = FormatReviser.prefZero(subString[i]);
			//}
		}
		myStack=new MyStack();
	}
	//Output Method
	public void showOutput()throws Exception {
		//System.out.println("Extracting Substrings:");
		for(int j=0;j<tokenMax;j++) {
			//System.out.print("Substring #" + (j+1) + ":\t");
			//System.out.println(subString[j]);
		}
		//System.out.println("Building Postfix Form:");
		myTree=getTree();
		postOrder(myTree);
		//System.out.println("Final Postfix Form:\t" + postFixForm);
		//System.out.println("Evaluation: \t" + myStack.pop());
	}
	//Tree Creator	
	public ExpressionTree getTree() {
		ExpressionTree node[],nd;
		node=new ExpressionTree[tokenMax-currentToken];
		int i=0;
		while(currentToken<tokenMax) {
			if (isOperand(subString[currentToken])) {
				nd=new ExpressionTree(null,null,subString[currentToken],true);
				node[i]=nd;
				i++;
				currentToken++;
			} else if(isOperator(subString[currentToken])) {
				nd=new ExpressionTree(null,null,subString[currentToken],false);
				node[i]=nd;							
				i++;
				currentToken++;
			} else if (subString[currentToken].equals("(")) {
				currentToken++;
				node[i]=getTree();			
				i++;
			} else if (subString[currentToken].equals(")")) {
				currentToken++;
				break;
			}
		}
		
		while(true) {
			int priority=0;
			int index=0;
			for(int j=0;j<i;j++) {
				if ((!node[j].isOperand()&&(!node[j].isVisited()))&&node[j].getPriority()>priority) {
					index=j;
					priority=node[j].getPriority();
				}
			}
			if (index>0) {
				node[index].setLeft(node[index-1]);
				node[index].setRight(node[index+1]);
				node[index].setVisited(true);
				node[index-1]=node[index];
				for(int j=index;j<i-2;j++) {
					node[j]=node[j+2];
				}
				i=i-2;
				
			}		
			if(i==0||i==1) {
				break;
			}
		}		
		return(node[0]);
	}
	//Node Evaluator	
	public void postOrder(ExpressionTree node) {
		String test = " ";
		if(node.getLeft()!=null) {
			postOrder(node.getLeft());
		}
		if(node.getRight()!=null)	{
			postOrder(node.getRight());
		}
		if (node.isOperand()) {
			myStack.push(node.getData());
			postFixForm+=node.getData();
			test = test + postFixForm;
			//System.out.println(test);
		} else {		
			postFixForm+=node.getOperator();		
			double op2=myStack.pop();
			double op1=myStack.pop();			
			if("+".equals(node.getOperator())) {
				op2=op2+op1;
			} else if("-".equals(node.getOperator())) {
				op2=op1-op2;
			} else if("*".equals(node.getOperator())) {
				op2=op2*op1;
			} else if("/".equals(node.getOperator())) {
				op2=op1/op2;
			} else if("^".equals(node.getOperator())) {
				op2=Math.pow(op1,op2);
			} else if("%".equals(node.getOperator())) {
				op2=op1%op2;
			}
			myStack.push(op2);
		}
	}
	//Main Method
	public static void main(String args[])throws Exception {
		boolean doMore = true;
	    
		JOptionPane.showMessageDialog(null,"^_^ Welcome to Praetor Epsilon's(a.k.a. L.M. Bibera) Calculator! ^_^");
		Instruct.instructions();
		
		do {
			Calc myMain=new Calc();
			myMain.showOutput();
			doMore = "yes".equalsIgnoreCase(JOptionPane.showInputDialog("Do you want to CONTINUE?Just Enter 'YES'"))?true:false;
		} while (doMore);
		
		JOptionPane.showMessageDialog(null,"^_^ Terminating Program: ByeBye! ^_^");
	}
	
	private boolean isOperator(String in) {
		if("+".equals(in)||"^".equals(in)||"/".equals(in)||"*".equals(in)||"-".equals(in)||"%".equals(in)) {
			return(true);
		} else {
			return(false);
		}
	}

	private boolean isOperand(String in)	{
		if(Character.isDigit(in.charAt(0))) {
			return(true);	
		} else {
			return(false);
		}
	}
}

class ErrorHandler {
	public static boolean syntaxCheck(String args) {
		//System.out.println("Initiating Syntax Check!");
		if (checkWild(args)) {
			if (checkPrecedence(args)) {
				if (checkLocation(args)) {
					if (checkPair(args)) {
						if (isBinary(args)) {
							//System.out.println("NO ERRORS DETECTED!");
							return(true);
						} else {
							return(false);
						}
					} else {
						return(false);
					}
				} else {
					return(false);
				}
			} else {
				return(false);
			}
		} else {
			return(false);
		}
	}
	
	private static boolean checkPrecedence(String input) {
		boolean properPrecedence = false;
		char charArray[] = input.toCharArray();
		char digitArray[] = {'0','1','2','3','4','5','6','7','8','9'};
		char operatorArray[] = {'*','/','+','-','%','^'};
		int firstDigit = 0;
		int firstOperator = 9999;
		int lastDigit = 9999;
		int lastOperator = 0;
		mainLoop1:
		for (int i=0; i<charArray.length; i++) {
			for (int j=0; j<digitArray.length; j++) {
				if (charArray[i] == digitArray[j]) {
					firstDigit = i;
					break mainLoop1;
				}
			}
		}	
		mainLoop2:
		for (int i=0; i<charArray.length; i++) {
			for (int j=0; j<operatorArray.length; j++) {
				if (charArray[i] == operatorArray[j]) {
					firstOperator = i;
					break mainLoop2;
				}
			}
		}
		
		for (int i=0; i<charArray.length; i++) {
			for (int j=0; j<digitArray.length; j++) {
				if (charArray[i] == digitArray[j]) {
					lastDigit = i;
				}
			}
		}
		
		for (int i=0; i<charArray.length; i++) {
			for (int j=0; j<operatorArray.length; j++) {
				if (charArray[i] == operatorArray[j]) {
					lastOperator = i;
				}
			}
		}
		properPrecedence = ((firstOperator>firstDigit) && ((lastOperator<lastDigit)||(lastDigit==0)))?true:false;
		if (!properPrecedence) {
			//System.out.println("ERROR:Invalid Operator Location!");
		}
		return(properPrecedence);
	}
	
	private static boolean checkEmptyPar(char charArray[]) {
		boolean clearedEmpty = true;
		String q = "\"";
		for (int i=0; i<(charArray.length-1); i++) {
			if ((charArray[i] == '(' )&& (charArray[i+1] == ')')) {
				//System.out.println("ERROR:Empty Arguments Inside " + q + "(" + q + " and " + q + ")" + q);
				clearedEmpty = false;
				break;
			}
		}
		return(clearedEmpty);
	}

	private static boolean checkWild(String input) {
		char inputArray[] = input.toCharArray();
		char allowedSymbols[] = {'1','2','3','4','5','6','7','8','9','0','*','/','+','-','%','^','(',')','.'};
		boolean output = true;
		mainLoop:
		for (int i=0; i<inputArray.length; i++) {
			for (int j=0; j<allowedSymbols.length; j++) {
				if (inputArray[i] == allowedSymbols[j]) {
					output = true;
					break; 
				}
				else {
					output = false;
					continue;
				}
			}
			if (output == false) {
				//System.out.println("Character \"" + inputArray[i] + "\" is not allowed!" );
				break mainLoop;
			}
		}
		if (!output) {
			//System.out.println("ERROR:Invalid Characters Included!");
		}
		return(output);
		
	}
	
	private static boolean checkLocation(String input) {
		char inputArray[] = input.toCharArray();
		int openingBrace = 0;
		int closingBrace = 100;
		
		
		for (int i=0; i<inputArray.length; i++ ) {
			if (inputArray[i] == '(') {
				openingBrace = i;
				break;
			}
		}
		for (int j=0; j<inputArray.length; j++ ) {
			if (inputArray[j] == ')') {
				closingBrace = j;
				break;
			}
		}
		boolean output = (openingBrace<closingBrace)?true:false;
		output = checkEmptyPar(inputArray);
		if (!output) {
			//System.out.println("ERROR:Invalid Brace Position!");
		}
		return(output);
	}
	
	private static boolean checkPair(String input) {
		char charArray[] = input.toCharArray();
		int countOpen = 0;
		int countClose = 0;
		
		for (int i=0; i<charArray.length; i++) {
			if (charArray[i] == ('(')) {
				countOpen++;
			}
			else if (charArray[i] == (')')) {
				countClose++;
			}
		}
		
		boolean output = (countOpen == countClose)?true:false;
		if (!output) {
			//System.out.println("ERROR:Unpaired Braces");
		}
		return(output);
	}
	
	private static boolean isBinary(String input) {
		boolean isBinary = false;
		char charArray[] = input.toCharArray();
		for(int i=0; i<charArray.length; i++) {
			if (isOperator(charArray[i]) && isAllowed(charArray[i+1])) {
				isBinary = true;
				continue;
			} else if (!isOperator(charArray[i])) {
				isBinary = true;
				continue;
			} else if (isOperator(charArray[i]) && !isAllowed(charArray[i+1])) {
				isBinary = false;
				break;
			} else if (isOperator(charArray[i]) && ((i+1)==charArray.length)){
				isBinary = false;
				break;
			} else {
				isBinary = true;
			}
		}
		if (!isBinary) {
			//System.out.println("ERROR: All Operations MUST be Binary!");
		}
		return(isBinary);
	}
	
	private static boolean isOperator(char c) {
		boolean isOp = false;
		char opList[] = {'-','+','/','*','%','^'};
		for(int i=0; i<opList.length; i++) {
			if(opList[i] == c) {
				isOp = true;
				break;
			} else {
				isOp = false;
			}
		}
		return(isOp);
	}
	
	private static boolean isAllowed(char c) {
		boolean isExp = false;
		char expList[] = {'1','2','3','4','5','6','7','8','9','0','(','.'};
		for(int i=0; i<expList.length; i++) {
			if (expList[i] == c) {
				isExp = true;
				break;
			} else {
				isExp = false;
			}
		}
		return(isExp);
	}
}

class ExpressionTree {
	ExpressionTree leftNode,rightNode;
	boolean isOperand,wasVisited;
	int priorityLevel;
	double data = 0;
	String operator = "";
	public ExpressionTree() {
		leftNode=null;
		rightNode=null;	
		data=0;
		priorityLevel=0;
		isOperand=true;	
		wasVisited=false;	
	}

	public ExpressionTree(ExpressionTree left,ExpressionTree right, String operandData,boolean operand){
		if(operand) {
			data=Double.parseDouble(operandData);
		} else {
			operator=operandData;	
			char s=operandData.charAt(0);
			switch (s)
			{
				case('+'):
					priorityLevel=1;
					break;
				case('-'):	
					priorityLevel=2;
					break;
				case('/'):	
					priorityLevel=3;
					break;
				case('*'):	
					priorityLevel=4;
					break;
				case('^'):	
					priorityLevel=5;
					break;
				case('%'):
					priorityLevel=6;
					break;
				default:		
					priorityLevel=0;
			}
		}
		leftNode=left;
		rightNode=right;
		this.isOperand=operand;
		wasVisited=false;	
	}

	public ExpressionTree getLeft() {
		return(leftNode);
	}
	
	public ExpressionTree getRight() {
		return(rightNode);
	}

	public void setLeft(ExpressionTree node) {
		leftNode=node;
	}
	
	public void setRight(ExpressionTree node) {
		rightNode=node;
	}

	public double getData() {
		return(data);
	}

	public String getOperator() {
		return(operator);
	}

	public void  setData(double x) {
		data=x;
	}
	
	public int getPriority() {
		return(priorityLevel);
	}

	public void  setPriority(int x) {
		priorityLevel=x;
	}
	
	public boolean isOperand() {
		return(isOperand);
	}
	
	public boolean isOperator() {
		return(!isOperand);
	}
	
	public void setOperand(boolean done) {
		isOperand=done;
	}

	public boolean isVisited() {
		return(wasVisited);
	}
	public void setVisited(boolean done) {
		wasVisited=done;
	}
	
}

class FormatReviser {
	public static String autoCorrect(String input) {
		String output = input;
		output = removeSpaces(output);
		output = removeEmpty(output);
		output = addAsterisk(output);
		output = addPostAsterisk(output);
		output = addPreAsterisk(output);
		//EXPERIMENTAL SECTION1: See Bottom for Details and Explanation.
		//output = appZero(output);
		//System.out.println("AUTOCORRECTION:" + input + " ==> " + output);
		return(output);
	}
	private static String addAsterisk(String input) {
		String oldS = "\\)\\(";
		String newS = ")*("; 
		String output = input.replaceAll(oldS,newS);
		if(input.equals(output)) {
			return(input);
		} else {
			return(output);
		}
	}
	
	private static String removeEmpty(String input) {
		String oldS = "\\(\\)";
		String newS = ""; 
		String output = input.replaceAll(oldS,newS);
		if(input.equals(output)) {
			return(input);
		} else {
			return(output);
		}
	}
	
	private static String removeSpaces(String input) {
		String oldS = " ";
		String newS = ""; 
		String output = input.replaceAll(oldS,newS);
		if(input.equals(output)) {
			return(input);
		} else {
			return(output);
		}
	}
	
	private static String addPreAsterisk(String input) {
		char charArray[] = input.toCharArray();
		for(int i=0; i<(charArray.length-1); i++) {
			if(isDigit(charArray[i]) && isOpBrace(charArray[i+1])) {
				String oldS = String.valueOf(charArray[i]) + "\\(" ;
				String newS = String.valueOf(charArray[i]) +  "*\\(";
				input = input.replaceAll(oldS, newS);
			} else {
				continue;
			}
		}
		return(input);
	}
	
	private static String addPostAsterisk(String input) {
		char charArray[] = input.toCharArray();
		for(int i=1; i<(charArray.length); i++) {
			if(isDigit(charArray[i]) && isClBrace(charArray[i-1])) {
				String oldS = "\\)" + String.valueOf(charArray[i]);
				String newS =  "\\)*" + String.valueOf(charArray[i]);
				input = input.replaceAll(oldS, newS);
			} else {
				continue;
			}
		}
		return(input);
	}
	
	private static boolean isDigit(char in) {
		boolean isDigit = false;
		char charArray[] = {'0','1','2','3','4','5','6','7','8','9'};
		for(int i=0; i<charArray.length; i++) {
			if(charArray[i]==in) {
				isDigit = true;
				break;
			} else {
				isDigit = false;
			}
		}
		return(isDigit);
	}
	
	private static boolean isClBrace(char in) {
		boolean isClBrace = false;
		if(in==')') {
			isClBrace = true;
		} else {
			isClBrace = false;
		}
		return(isClBrace);
	}
	
	private static boolean isOpBrace(char in) {
		boolean isOpBrace = false;
		if(in=='(') {
			isOpBrace = true;
		} else {
			isOpBrace = false;
		}
		return(isOpBrace);
	}
// ============= DO NOT DELETE ==========================================	
	//EXPERIMENTAL SECTION3: See Bottom for Details and Explanation.
	//private static String appZero(String in) {
	//	String out = in;
	//    String before[] = {"\\*.","\\/.","\\+.","\\-.","\\%.","\\^.","\\(\\.","\\)\\.","\\.\\(","\\.\\)"};
	//    String after[] = {"*0.","/0.","+0.","-0.","%0.","^0.","(0.",")0.","(",")"};
	//	for(int i=0; i<before.length; i++) {
	//		out = out.replaceAll(before[i],after[i]);
	//	}
	//	return(out);
	//}
	//EXPERIMENTAL SECTION4: See Bottom for Details and Explanation.
	//public static String prefZero(String in) {
	//	String out = in;
	//	out = out.replaceAll(".","0.");
	//	return(out);
	//}
// ============= DO NOT DELETE ==========================================
} 

class Instruct {
	public static void instructions() {
		rules();
		allowed();
	}
	
	private final static void rules() {
		//System.out.println("RULES: To Avoid Errors");
		//System.out.println("1. Use only the six binary mathematical operators:");
		//System.out.println(" % -> Remainder Extractor");
		//System.out.println(" ^ -> Exponent Operator");
		//System.out.println(" * -> Multiplication");
		//System.out.println(" / -> Division");
		//System.out.println(" + -> Addition");
		//System.out.println(" - -> Subtraction");
		//System.out.println("2. Position your parenthesis in the correct order.");
		//System.out.println("3. Use single points for every decimal numbers.");
		//System.out.println("4. Don't MESS UP by tring to use letter characters.");
		//System.out.println("5. Don't even dare to use unary operators like ++ and -- 'coz it aint gonna work.");
	}
	
	private final static void allowed() {
		//System.out.println("SPECIAL OPERATIONS:This makes this calcu better than my older versions");
		//System.out.println("1. Multiplying without the use of '*' sign: ");
		//System.out.println("  Example: (2)(2)");
		//System.out.println("          = 4");
		//System.out.println("2. Using decimal points:");
		//System.out.println("  Example: 2.3454 - 4234.56");
		//System.out.println("          = -4232.2146");
	}
}

class MyStack {
	private int capacity;
    private double object[];
    private int top;
    
    public MyStack(int cap) {
        capacity = cap;
		object=new double[capacity];		
        top=-1;
    }
    public MyStack() {
        capacity =10000;
		object=new double[capacity];
        top=-1;
    }

    public void push(double ob) {
		top++;
		object[top]=ob;
    }

    public double pop() {
		double t=object[top];
		object[top]=0;
		top--;
		return(t);        
    }
    
    public boolean isEmpty() {
        return(top == -1);
    }

    public double top() {
          return(object[top]);
    }
}

