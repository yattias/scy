package eu.scy.client.tools.scydynamics.domain;

import java.awt.Frame;
import java.util.List;

import javax.swing.JOptionPane;

public class DomainUtils {

	public static int getLevenshteinDistance (String s, String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}

		int n = s.length(); // length of s
		int m = t.length(); // length of t

		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}

		int p[] = new int[n+1]; //'previous' cost array, horizontally
		int d[] = new int[n+1]; // cost array, horizontally
		int _d[]; //placeholder to assist in swapping p and d

		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t

		char t_j; // jth character of t

		int cost; // cost

		for (i = 0; i<=n; i++) {
			p[i] = i;
		}

		for (j = 1; j<=m; j++) {
			t_j = t.charAt(j-1);
			d[0] = j;

			for (i=1; i<=n; i++) {
				cost = s.charAt(i-1)==t_j ? 0 : 1;
				// minimum of cell to the left+1, to the top+1, diagonally left and up +cost				
				d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);  
			}

			// copy current distance counts to 'previous row' distance counts
			_d = p;
			p = d;
			d = _d;
		} 

		// our last action in the above loop was to switch d and p, so p now 
		// actually has the most recent cost counts
		return p[n];
	}

	public static final int chooseDifferentLabelDialog(Frame frameForComponent, List<String> proposedNames) {
		String message = new String();
		int choice;
		if (proposedNames.isEmpty()) {
			message = "The label of this object could not be identified.\n" +
			"Would you like to choose a different one?";
			choice = JOptionPane.showConfirmDialog(frameForComponent, message, "Choose label...", JOptionPane.YES_NO_OPTION);
		} else {
			message = "The label of this object could not be fully identified.\n" +
			"Would you like to enter one of the following ones?\n\n";
			for (String name: proposedNames) {
				message = message + name + "\n";
			}
			choice = JOptionPane.showConfirmDialog(frameForComponent, message, "Choose label...", JOptionPane.YES_NO_OPTION);
		}
		return choice;
	}
}
