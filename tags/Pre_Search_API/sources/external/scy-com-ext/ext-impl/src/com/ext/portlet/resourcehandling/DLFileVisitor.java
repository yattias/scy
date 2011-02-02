package com.ext.portlet.resourcehandling;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.ext.portlet.resourcehandling.interfaces.ResourceViewInterface;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;

/**
 * Resource visitor for file entries. Get all needed resources for browse intern
 * resources.
 * 
 * @see DLFileFolderView
 * @see ResourceAdapterBuilder
 * @author Daniel
 * 
 */
public class DLFileVisitor implements ResourceViewInterface {

	public DLFileVisitor(ActionRequest req) throws PortalException, SystemException {
		getFolderView(req);
		getPreview(req);
	}

	@Override
	public FolderView getFolderView(ActionRequest req) throws SystemException, PortalException {
		FolderView folderView = new DLFileFolderView(req);
		return folderView;
	}

	@Override
	public Long getPreview(ActionRequest req) throws PortalException, SystemException {
		if (req.getParameter("previewEntryId") != null) {
			String previewEntryId = req.getParameter("previewEntryId");
			DLFileEntry documentPreviewEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(Long.valueOf(previewEntryId));
			req.setAttribute("documentPreviewEntry", documentPreviewEntry);
			return documentPreviewEntry.getFileEntryId();
		}
		return null;
	}
}
