package com.ext.portlet.resourcehandling;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.ext.portlet.resourcehandling.interfaces.ResourceViewInterface;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGImageLocalServiceUtil;

/**
 * Resource visitor for image entries. Get all needed resources for browse
 * intern resources.
 * 
 * @see IGImageFolderView
 * @see ResourceAdapterBuilder
 * @author Daniel
 * 
 */
public class IGImageVisitor implements ResourceViewInterface {

	public IGImageVisitor(ActionRequest req) throws SystemException, PortalException {
		getFolderView(req);
		getPreview(req);
	}

	@Override
	public FolderView getFolderView(ActionRequest req) throws SystemException, PortalException {
		FolderView folderView = new IGImageFolderView(req);
		return folderView;
	}

	@Override
	public Long getPreview(ActionRequest req) throws NumberFormatException, PortalException, SystemException {
		if (req.getParameter("previewEntryId") != null) {
			String previewEntryId = req.getParameter("previewEntryId");
			IGImage imagePreviewEntry = IGImageLocalServiceUtil.getIGImage(Long.valueOf(previewEntryId));
			req.setAttribute("imagePreviewEntry", imagePreviewEntry);
			return imagePreviewEntry.getImageId();
		}
		return null;
	}
}
