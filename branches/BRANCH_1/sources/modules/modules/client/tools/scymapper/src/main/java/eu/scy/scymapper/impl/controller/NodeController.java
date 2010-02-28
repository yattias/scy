package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.diagram.controller.INodeController;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.styling.INodeStyle;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 20:03:51
 */
public class NodeController implements INodeController {
	protected INodeModel model;

	public NodeController(INodeModel node) {
		this.model = node;
	}

	@Override
	public void setSize(Dimension dimension) {
		if (!model.getConstraints().getCanResize()) return;

		int minHeight = model.getStyle().getMinHeight();
		int minWidth = model.getStyle().getMinWidth();

		if (dimension.height < minHeight) dimension.height = minHeight;
		if (dimension.width < minWidth) dimension.width = minWidth;

		model.setSize(dimension);
	}

	@Override
	public void setLocation(Point p) {
		if (p.getX() < 0) p.x = 0;
		if (p.y < 0) p.y = 0;

		if (model.getConstraints().getCanMove()) model.setLocation(p);
	}

	@Override
	public void setLabel(String text) {
		if (model.getConstraints().getCanEditLabel()) model.setLabel(text);
	}

	@Override
	public void setSelected(boolean b) {
		if (model.getConstraints().getCanSelect()) model.setSelected(b);
	}

	@Override
	public void setDeleted(boolean b) {
		if (model.getConstraints().getCanDelete()) model.setDeleted(true);
	}

	public void setStyle(INodeStyle style) {
		if (model.getConstraints().getCanChangeStyle()) model.setStyle(style);
	}
}
