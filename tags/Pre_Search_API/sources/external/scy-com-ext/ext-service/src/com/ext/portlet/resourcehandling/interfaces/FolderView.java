package com.ext.portlet.resourcehandling.interfaces;

/**
 * The classes which implement this interface must granted to get all needed informations for the view to show browse intern resources.
 */
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;

public interface FolderView {
	public FolderView getParent() throws SystemException, PortalException;
	public FolderView getChildren() throws SystemException, PortalException;
}
