package com.ext.portlet.freestyler.model.impl;

import javax.portlet.ActionRequest;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil;
import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;

public class FreestylerEntryImpl extends FreestylerEntryModelImpl implements FreestylerEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1521887593380057372L;

	@Override
	public FolderView getFolderView(ActionRequest req) throws SystemException, PortalException {
		FolderView folderView = new FreestylerEntryFolderView(req);
		return folderView;
	}

	@Override
	public Long getPreview(ActionRequest req) throws SystemException, PortalException {
		if (req.getParameter("previewEntryId") != null) {
			String previewEntryId = req.getParameter("previewEntryId");
			try {
				FreestylerImage freestylerPreviewEntry = FreestylerImageLocalServiceUtil.getFreestylerImage(Long.valueOf(previewEntryId));
				req.setAttribute("freestylerPreviewEntry", freestylerPreviewEntry);
				return freestylerPreviewEntry.getFreestylerId();

			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

}
