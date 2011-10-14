package eu.scy.client.tools.fxscymapper.registration;

/**
 *
 * @author lars
 */
public class StringWrapper {

	private static final String testString = "Scientists at the University of Manchester have come one step closer to creating the next generation of computer chips using graphene. Scientists at the University of Manchester have come one step closer to creating the next generation of computer chips using graphene.";

	public static String wrapString(String s, int length) {
		int maxLength = length;
		String[] pieces = s.split(" ");
		String result = new String();
		for (String piece: pieces) {
			if (result.concat(piece).length()<=maxLength) {
				result = result.concat(piece).concat(" ");
			} else {
				result = result.concat("\n").concat(piece).concat(" ");
				maxLength = maxLength + length;
			}
		}
		return result;
	}

	public static void main (String[] args) {
		System.out.println(wrapString(testString, 100));
	}
}
