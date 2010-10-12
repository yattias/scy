package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.diagram.controller.ILinkController;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.styling.ILinkStyle;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 23.jun.2009
 * Time: 16:24:26
 * To change this template use File | Settings | File Templates.
 */
public class LinkConnectorController implements ILinkController {
	private ILinkModel model;

	public LinkConnectorController(ILinkModel model) {
		this.model = model;
	}

	@Override
	public void setLabel(String text) {
		model.setLabel(text);
	}

	@Override
	public void setTo(Point p) {
		model.setTo(p);
	}

	@Override
	public void setFrom(Point p) {
		model.setFrom(p);
	}

	@Override
	public void setStyle(ILinkStyle style) {
		model.setStyle(style);
	}
}
