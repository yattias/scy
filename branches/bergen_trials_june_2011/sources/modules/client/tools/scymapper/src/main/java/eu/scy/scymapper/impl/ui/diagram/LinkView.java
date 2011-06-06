package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.diagram.controller.ILinkController;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.ILinkModelListener;
import eu.scy.scymapper.api.diagram.view.LinkViewComponent;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 23.jun.2009
 * Time: 16:23:37
 */
public class LinkView extends LinkViewComponent implements ILinkModelListener {

	private Border selectionBorder;

	public LinkView(ILinkController controller, ILinkModel model) {
		super(controller, model);
		model.addListener(this);
	}

	@Override
	public void updated(ILinkModel m) {
		super.updatePosition();
		repaint();
	}

	@Override
	public void selectionChanged(ILinkModel link) {
		if (selectionBorder == null) {
			selectionBorder = new SelectionBorder(new Insets(100, 100, 100, 100));
		}
		setBorder(link.isSelected() ? selectionBorder : BorderFactory.createEmptyBorder());
//		if (link.isSelected()) requestFocus();
	}

	@Override
	public void labelChanged(ILinkModel link) {
	}

    @Override
    public void linkFlipped(ILinkModel simpleLink) {

    }

}