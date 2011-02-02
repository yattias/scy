package com.ext.portlet.resourcehandling;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.ext.portlet.resourcehandling.interfaces.ResourceViewInterface;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;

/**
 * Resource visitor for wiki page entries. Get all needed resources for browse
 * intern resources.
 * 
 * @see WikiPageFolderView
 * @see ResourceAdapterBuilder
 * @author Daniel
 * 
 */
public class WikiPageVisitor implements ResourceViewInterface {

	public WikiPageVisitor(ActionRequest req) throws SystemException, PortalException {
		getFolderView(req);
		getPreview(req);
	}

	@Override
	public FolderView getFolderView(ActionRequest req) throws SystemException, PortalException {
		FolderView folderView = new WikiPageFolderView(req);
		return folderView;
	}

	@Override
	public Long getPreview(ActionRequest req) throws PortalException, SystemException {
		if (req.getParameter("previewEntryId") != null) {
			String previewEntryId = req.getParameter("previewEntryId");
			WikiPage wikiPreviewEntry = WikiPageLocalServiceUtil.getPage(Long.valueOf(previewEntryId));
			req.setAttribute("wikiPreviewEntry", wikiPreviewEntry);
			return wikiPreviewEntry.getResourcePrimKey();
		}
		return null;
	}
}
