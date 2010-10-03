package eu.scy.client.tools.scysimulator;

import javax.swing.JCheckBox;

public class ComparableJCheckBox extends JCheckBox implements Comparable<ComparableJCheckBox> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4694703148254334031L;

	public ComparableJCheckBox(String s) {
		super(s);
	}
	
	@Override
	public int compareTo(ComparableJCheckBox that) {
		return this.getText().toLowerCase().compareTo(that.getText().toLowerCase());
	}

}