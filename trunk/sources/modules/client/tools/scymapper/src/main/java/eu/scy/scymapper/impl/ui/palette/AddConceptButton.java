package eu.scy.scymapper.impl.ui.palette;

import eu.scy.scymapper.api.diagram.model.INodeModel;

import javax.swing.*;


/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 18.jun.2009
 * Time: 16:33:18
 * To change this template use File | Settings | File Templates.
 */
public class AddConceptButton extends JToggleButton {
	private INodeModel concept;

	public AddConceptButton(INodeModel concept) {
		super();
		setIcon(new ConceptShapedIcon(concept, 20, 20));
		this.concept = concept;
	}
}
