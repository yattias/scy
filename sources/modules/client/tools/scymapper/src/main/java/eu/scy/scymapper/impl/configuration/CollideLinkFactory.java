package eu.scy.scymapper.impl.configuration;

import eu.scy.scymapper.api.ILinkFactory;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.impl.model.SimpleLink;

public class CollideLinkFactory implements ILinkFactory {
	private ILinkShape shape;
	private String label;
	private String description;
	private Type type;

	public void setLinkShape(ILinkShape shape) {
		this.shape = shape;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public ILinkModel create() {
		ILinkModel linkModel = new SimpleLink();
		linkModel.setShape(shape);
		type=Type.COLLIDE;
		return linkModel;
	}

	@Override
	public Type getType() {
		return type;
	}
}
