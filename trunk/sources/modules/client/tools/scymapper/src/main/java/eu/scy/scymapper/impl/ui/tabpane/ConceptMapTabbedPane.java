package eu.scy.scymapper.impl.ui.tabpane;

import javax.swing.*;

/**
 * User: Bjoerge Naess
 * Date: 30.sep.2009
 * Time: 13:38:31
 */
public class ConceptMapTabbedPane extends JTabbedPane {

	public void add(ConceptMapEditor cmapEditor) {
		super.add(cmapEditor.getConceptMap().getName(), cmapEditor);
		setTabComponentAt(indexOfComponent(cmapEditor), new ConceptMapTabButton(cmapEditor.getConceptMap().getName(), this));
	}
}
