package eu.scy.scymapper.impl.configuration;

import eu.scy.scymapper.api.ILinkFactory;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.impl.model.SimpleLink;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 30.okt.2009
 * Time: 13:25:41
 */
public class DefaultLinkFactory implements ILinkFactory {
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

		this.type=Type.SCY;
		return linkModel;
	}

	@Override
	public Type getType() {
		return this.type;
	}
}
