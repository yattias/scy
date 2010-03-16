package com.ext.portlet.resourcehandling;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.ext.portlet.resourcehandling.interfaces.ResourceViewInterface;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;

/**
 * Resource visitor for blog entries. Get all needed resources for browse intern resources. 
 * @see BlogEntryFolderView
 * @see ResourceAdapterBuilder
 * @author Daniel
 *
 */
public class BlogsEntryVisitor implements ResourceViewInterface {

	public BlogsEntryVisitor(ActionRequest req) throws SystemException, PortalException {
		getFolderView(req);
		getPreview(req);
	}

	@Override
	public FolderView getFolderView(ActionRequest req) throws SystemException, PortalException {
		FolderView folderView = (FolderView) new BlogsEntryFolderView(req);

		return folderView;
	}

	@Override
	public Long getPreview(ActionRequest req) throws PortalException, SystemException {

		if (req.getParameter("previewEntryId") != null) {
			String previewEntryId = req.getParameter("previewEntryId");
			BlogsEntry blogPreviewEntry = BlogsEntryLocalServiceUtil.getBlogsEntry(Long.valueOf(previewEntryId));
			req.setAttribute("blogPreviewEntry", blogPreviewEntry);
			return blogPreviewEntry.getEntryId();
		}

		return null;
	}

}
