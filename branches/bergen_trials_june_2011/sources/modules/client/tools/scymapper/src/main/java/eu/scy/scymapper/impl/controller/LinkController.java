package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.diagram.controller.ILinkController;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.styling.ILinkStyle;

import java.awt.*;

/**
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 20:00:05
 */
public class LinkController implements ILinkController {
	protected ILinkModel model;

	public LinkController(ILinkModel link) {
		this.model = link;
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
