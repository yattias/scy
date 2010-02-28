package eu.scy.scymapper.api.diagram.model;

/**
 * /**
 *
 * @author bjoerge
 * @created 17.nov.2009
 * Time: 15:08:27
 * To change this template use File | Settings | File Templates.
 */
public interface INodeModelConstraints {
	/**
	 * Indicates whether the node can be moved or not
	 *
	 * @return true if the node can be moved by user
	 */
	boolean getCanMove();

	void setCanMove(boolean b);

	/**
	 * Indicates whether the node can be deleted or not
	 *
	 * @return true if the node can be deleted by user
	 */
	boolean getCanDelete();

	void setCanDelete(boolean b);

	/**
	 * Indicates whether the node can be resized or not
	 *
	 * @return true if the node can be resized by user
	 */
	boolean getCanResize();

	void setCanResize(boolean b);

	/**
	 * Indicates whether the node label can be edited or not
	 *
	 * @return true if the node label can be edited by user
	 */
	boolean getCanEditLabel();

	void setCanEditLabel(boolean b);

	/**
	 * Indicates whether links can connect to this or not
	 *
	 * @return true if links are allowed to connect to this node
	 */
	boolean getCanConnect();

	void setCanConnect(boolean b);

	void setCanSelect(boolean canSelect);

	boolean getCanSelect();

	void setCanChangeStyle(boolean b);

	boolean getCanChangeStyle();

	void setAlwaysOnTop(boolean alwaysOnTop);

	boolean getAlwaysOnTop();
}
