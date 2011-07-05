package eu.scy.client.tools.scydynamics.model;

import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

public class SignFunction extends PostfixMathCommand {

	public SignFunction() {
		numberOfParameters = 1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		   // check the stack
		   checkStack(inStack);
		   // get the parameter from the stack
		   Object param1 = inStack.pop();
		   // check whether the argument is of the right type
		   if ((param1 instanceof Double)) {
		      // calculate the result
		      double result = java.lang.Math.signum((Double)param1);
		      // push the result on the inStack
		      inStack.push(new Double(result)); 
		   } else {
		      throw new ParseException("Invalid parameter type");
		   }
	}

}
