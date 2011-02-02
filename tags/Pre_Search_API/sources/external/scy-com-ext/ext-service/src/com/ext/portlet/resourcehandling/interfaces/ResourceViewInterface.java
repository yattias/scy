package com.ext.portlet.resourcehandling.interfaces;

import javax.portlet.ActionRequest;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;

/**
 * Tagging interface for those classes that shall be included in the browse
 * intern resources.
 * 
 * @author Daniel
 * 
 */
public interface ResourceViewInterface {
	public FolderView getFolderView(ActionRequest req) throws SystemException, PortalException;

	public Long getPreview(ActionRequest req) throws SystemException, PortalException;

}
