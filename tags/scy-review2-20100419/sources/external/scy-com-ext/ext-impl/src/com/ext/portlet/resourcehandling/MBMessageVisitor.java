package com.ext.portlet.resourcehandling;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.ext.portlet.resourcehandling.interfaces.ResourceViewInterface;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

/**
 * Resource visitor for message board entries. Get all needed resources for
 * browse intern resources.
 * 
 * @see MBMessageFolderView
 * @see ResourceAdapterBuilder
 * @author Daniel
 * 
 */
public class MBMessageVisitor implements ResourceViewInterface {

	public MBMessageVisitor(ActionRequest req) throws PortalException, SystemException {
		getFolderView(req);
		getPreview(req);
	}

	@Override
	public FolderView getFolderView(ActionRequest req) throws SystemException, PortalException {
		FolderView folderView = new MBMessageFolderView(req);
		return folderView;
	}

	@Override
	public Long getPreview(ActionRequest req) throws PortalException, SystemException {

		if (req.getParameter("previewEntryId") != null) {
			String previewEntryId = req.getParameter("previewEntryId");
			MBMessage messagePreviewEntry = MBMessageLocalServiceUtil.getMBMessage(Long.valueOf(previewEntryId));
			req.setAttribute("messagePreviewEntry", messagePreviewEntry);
			return messagePreviewEntry.getMessageId();
		}
		return null;
	}
}
