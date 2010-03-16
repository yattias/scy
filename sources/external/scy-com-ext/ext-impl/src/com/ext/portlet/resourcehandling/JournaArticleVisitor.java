package com.ext.portlet.resourcehandling;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.ext.portlet.resourcehandling.interfaces.ResourceViewInterface;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

/**
 * Resource visitor for journal entries. Get all needed resources for browse
 * intern resources.
 * 
 * @see JournalArticleFolderView
 * @see ResourceAdapterBuilder
 * @author Daniel
 * 
 */
public class JournaArticleVisitor implements ResourceViewInterface {

	public JournaArticleVisitor(ActionRequest req) throws SystemException, PortalException {
		getFolderView(req);
		getPreview(req);
	}

	@Override
	public FolderView getFolderView(ActionRequest req) throws SystemException, PortalException {
		FolderView folderView = new JournalArticleFolderView(req);
		return folderView;
	}

	@Override
	public Long getPreview(ActionRequest req) throws PortalException, SystemException {
		if (req.getParameter("previewEntryId") != null) {
			String previewEntryId = req.getParameter("previewEntryId");
			JournalArticle journalPreviewEntry = JournalArticleLocalServiceUtil.getLatestArticle(Long.valueOf(previewEntryId));
			req.setAttribute("journalPreviewEntry", journalPreviewEntry);
			return journalPreviewEntry.getResourcePrimKey();
		}
		return null;
	}
}
