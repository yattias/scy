package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.diagram.controller.INodeController;
import eu.scy.scymapper.api.diagram.model.INodeModel;

import javax.swing.*;

/**
 * @author bjoerge
 * @created 16.feb.2010 09:43:17
 */
public class ConnectorView extends RichNodeView {
	public ConnectorView(INodeController controller, INodeModel model) {
		super(controller, model);
	}


	@Override
	protected void layoutComponents() {
		int maxHeight = getHeight(); // 10 px spacing on each side
		int maxWidth = getWidth(); // 10 px spacing on each side

		int height = maxHeight - 16;
		int width = maxWidth - 16;

		//labelScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		labelTextPane.setSize(labelTextPane.getPreferredScrollableViewportSize());
		labelTextPane.setVisible(!getModel().isLabelHidden());

//		labelScroller.setBounds(8, 8, width, height);
//		labelScroller.revalidate();

		if (resizeHandle != null) {
			resizeHandle.setBounds(getWidth() - resizeHandle.getWidth(), getHeight() - resizeHandle.getHeight(), resizeHandle.getWidth(), resizeHandle.getHeight());

			resizeHandle.setForeground(getForeground());
			resizeHandle.setBackground(getBackground());
		}
	}

}
