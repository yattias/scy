package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.diagram.model.INodeModelConstraints;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge Naess
 * Date: 17.nov.2009
 * Time: 15:24:22
 */
public class NodeModelConstraints implements INodeModelConstraints {
	private boolean canMove = true;
	private boolean canDelete = true;
	private boolean canResize = true;
	private boolean canEditLabel = true;
	private boolean canConnect = true;
	private boolean canSelect = true;
	private boolean canChangeStyle = true;
	private boolean alwaysOnTop;

	@Override
	public boolean getCanMove() {
		return canMove;
	}

	@Override
	public void setCanMove(boolean b) {
		canMove = b;
	}

	@Override
	public boolean getCanDelete() {
		return canDelete;
	}

	@Override
	public void setCanDelete(boolean b) {
		canDelete = b;
	}

	@Override
	public boolean getCanResize() {
		return canResize;
	}

	@Override
	public void setCanResize(boolean b) {
		canResize = b;
	}

	@Override
	public boolean getCanEditLabel() {
		return canEditLabel;
	}

	@Override
	public void setCanEditLabel(boolean b) {
		canEditLabel = b;
	}

	@Override
	public boolean getCanConnect() {
		return canConnect;
	}

	@Override
	public void setCanConnect(boolean b) {
		canConnect = b;
	}

	@Override
	public void setCanSelect(boolean canSelect) {
		this.canSelect = canSelect;
	}

	@Override
	public boolean getCanSelect() {
		return canSelect;
	}

	@Override
	public void setCanChangeStyle(boolean b) {
		canChangeStyle = b;
	}

	@Override
	public boolean getCanChangeStyle() {
		return canChangeStyle;
	}

	@Override
	public void setAlwaysOnTop(boolean alwaysOnTop) {
		this.alwaysOnTop = alwaysOnTop;
	}

	@Override
	public boolean getAlwaysOnTop() {
		return alwaysOnTop;
	}
}
