package edu.scy.tools.math.test.ui;


import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

/**
 * Contains a couple of samples for evaluating mathematical operations. There
 * are many more examples in the JUnit tests.
 */
public class MathematicalOperationsSample {

	/**
	 * Run the sample code. No arguments are necessary.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		/*
		 * This sample shows the basic usage of the JEval Evaluator class.
		 * Calling the default contructor will set he quoteCharater to single
		 * quote. This constructor will also load all math variables, math
		 * functions and string variables.
		 */
		Evaluator evaluator = new Evaluator();

		try {
			/*
			 * This sample shows basic addition.
			 * 
			 * Note: The output will contain at least one decimal place, since
			 * the results of math operations are treated like doubles.
			 */
			

			/**
			 * This sample shows basic division
			 */
			

			/**
			 * This sample shows a more complex expression involving
			 * parentheses.
			 */
			

			/**
			 * This sample shows an invalid expression. There is no operand to
			 * the right of the plus addition operator.
			 */
			System.out.println("An exception is expected in the "
					+ "next evaluation.");
			
		} catch (EvaluationException ee) {
			
		}
	}
}
