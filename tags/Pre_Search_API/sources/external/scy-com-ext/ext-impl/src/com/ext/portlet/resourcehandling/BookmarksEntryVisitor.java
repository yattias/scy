package com.ext.portlet.resourcehandling;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.ext.portlet.resourcehandling.interfaces.ResourceViewInterface;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;

/**
 * Resource visitor for bookmark entries. Get all needed resources for browse
 * intern resources.
 * 
 * @see BookmarksEntryFolderView
 * @see ResourceAdapterBuilder
 * @author Daniel
 * 
 */
public class BookmarksEntryVisitor implements ResourceViewInterface {

	public BookmarksEntryVisitor(ActionRequest req) throws SystemException, PortalException {
		getFolderView(req);
		getPreview(req);
	}

	@Override
	public FolderView getFolderView(ActionRequest req) throws SystemException, PortalException {
		FolderView folderView = new BoomarksEntryFolderView(req);
		return folderView;
	}

	@Override
	public Long getPreview(ActionRequest req) throws PortalException, SystemException {
		if (req.getParameter("previewEntryId") != null) {
			String previewEntryId = req.getParameter("previewEntryId");
			BookmarksEntry bookmarkPreviewEntry = BookmarksEntryLocalServiceUtil.getBookmarksEntry(Long.valueOf(previewEntryId));
			req.setAttribute("bookmarkPreviewEntry", bookmarkPreviewEntry);
			return bookmarkPreviewEntry.getEntryId();
		}
		return null;
	}
}
