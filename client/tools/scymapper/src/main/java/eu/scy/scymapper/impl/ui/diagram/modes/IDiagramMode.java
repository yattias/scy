package eu.scy.scymapper.impl.ui.diagram.modes;

import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 03.nov.2009
 * Time: 15:16:10
 * To change this template use File | Settings | File Templates.
 * TODO: Refactor to API
 */
public interface IDiagramMode {
	public MouseListener getMouseListener();

	public MouseMotionListener getMouseMotionListener();

	public FocusListener getFocusListener();
}
