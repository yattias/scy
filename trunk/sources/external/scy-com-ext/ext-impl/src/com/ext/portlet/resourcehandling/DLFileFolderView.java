package com.ext.portlet.resourcehandling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.permission.DLFileEntryPermission;
import com.liferay.portlet.documentlibrary.service.permission.DLFolderPermission;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.LinkToolUtil;

/**
 * Get all needed resources to show file entries. The parent folder are all folder from DLFolder with parentFolder id = 0.  
 * Child entries are all files from this folder and all child folder with parentFolder id equals given folder.
 * 
 * @author Daniel
 *
 */
public class DLFileFolderView implements FolderView {

	private ActionRequest req;

	public DLFileFolderView(ActionRequest req) throws SystemException, PortalException {
		this.req = req;
		getParent();
		getChildren();
	}

	@Override
	public FolderView getChildren() throws SystemException, PortalException {

		if (req.getParameter("rootFolderId") != null) {
			String rootFolderId = req.getParameter("rootFolderId");
			String folderId = req.getParameter("folderId");
			String cmd = req.getParameter(Constants.CMD);

			List<DLFolder> rootFolderList = getRootFolderListDocument();
			DLFolder rootFolder = DLFolderLocalServiceUtil.getDLFolder(Long.valueOf(rootFolderId));
			List<DLFolder> hList = new ArrayList<DLFolder>();

			List<DLFolder> folderList;
			List<DLFileEntry> documentList;
			DLFolder actualfolder;
			DLFolder parentfolder;

			if (cmd.equals("choseFolder") || cmd.equals("changePreview")) {
				actualfolder = DLFolderLocalServiceUtil.getDLFolder(Long.valueOf(folderId));
				hList.add(actualfolder);
				hList.addAll(getBreadcrumb(actualfolder));
				folderList = getDocumentFolderList(req, actualfolder);
				documentList = getDocumentList(actualfolder);
				req.setAttribute("parentFolder", actualfolder);
			} else if (cmd.equals("backFolder")) {
				actualfolder = DLFolderLocalServiceUtil.getDLFolder(Long.valueOf(folderId));
				parentfolder = getParentFolder(rootFolder, actualfolder);
				folderList = getDocumentFolderList(req, parentfolder);
				documentList = getDocumentList(parentfolder);
				hList.addAll(getBreadcrumb(actualfolder));
				req.setAttribute("parentFolder", parentfolder);
			} else {
				folderList = DLFolderLocalServiceUtil.getFolders(rootFolder.getGroupId(), rootFolder.getFolderId());
				documentList = getDocumentList(rootFolder);
				hList.add(rootFolder);
				req.setAttribute("parentFolder", rootFolder);
			}

			Collections.reverse(hList);

			req.setAttribute("rootFolderList", rootFolderList);
			req.setAttribute("rootFolder", rootFolder);
			req.setAttribute("folderList", folderList);
			req.setAttribute("documentList", documentList);
			req.setAttribute("hList", hList);
		}

		return null;
	}

	private DLFolder getParentFolder(DLFolder rootFolder, DLFolder actualfolder) throws PortalException, SystemException {
		DLFolder parentfolder;
		if (actualfolder.getParentFolderId() > 0) {
			parentfolder = DLFolderLocalServiceUtil.getFolder(actualfolder.getParentFolderId());
		} else {
			parentfolder = rootFolder;
		}
		return parentfolder;
	}

	@Override
	public FolderView getParent() throws SystemException, PortalException {
		List<DLFolder> rootFolderList = getRootFolderListDocument();
		req.setAttribute("rootFolderList", rootFolderList);
		return null;
	}

	private List<DLFolder> getBreadcrumb(DLFolder actualfolder) throws PortalException, SystemException {
		List<DLFolder> hList = new ArrayList<DLFolder>();
		DLFolder tempFolder = actualfolder;
		while (tempFolder.getParentFolderId() != 0) {
			tempFolder = DLFolderLocalServiceUtil.getDLFolder(tempFolder.getParentFolderId());
			hList.add(tempFolder);
		}
		return hList;
	}

	/**
	 * Get list of DLFolder.
	 * 
	 * @return list of dlFolder.
	 * @throws SystemException
	 * @throws PortalException
	 */
	private List<DLFolder> getRootFolderListDocument() throws SystemException, PortalException {
		List<DLFolder> dLFolderList = new ArrayList<DLFolder>();
		List<DLFolder> dLFolderListComp = DLFolderLocalServiceUtil.getDLFolders(0, DLFolderLocalServiceUtil.getDLFoldersCount());
		for (DLFolder dlFolderEntry : dLFolderListComp) {
			if (dlFolderEntry.getParentFolderId() == 0) {
				if (DLFolderPermission.contains(LinkToolUtil.getPermissionChecker(), dlFolderEntry, ActionKeys.VIEW)) {
					dLFolderList.add(dlFolderEntry);
				}
			}
		}
		return dLFolderList;
	}

	/**
	 * Get all resource from this folder where user has permission for and is
	 * also in table tagsAsset.
	 * 
	 * @param actualfolder
	 *            actual chosen folder from user.
	 * @return
	 * @throws SystemException
	 * @throws PrincipalException
	 */
	private List<DLFileEntry> getDocumentList(DLFolder actualfolder) throws SystemException, PrincipalException {
		Boolean isTagsAsset;
		List<DLFileEntry> documentList = new ArrayList<DLFileEntry>();
		List<DLFileEntry> tempDocumentList = DLFileEntryLocalServiceUtil.getFileEntries(actualfolder.getFolderId());
		for (DLFileEntry dlFileEntry : tempDocumentList) {
			try {
				TagsAssetLocalServiceUtil.getAsset(DLFileEntry.class.getName(), dlFileEntry.getFileEntryId());
				isTagsAsset = true;
			} catch (Exception e) {
				isTagsAsset = false;
			}
			if (isTagsAsset && DLFileEntryPermission.contains(LinkToolUtil.getPermissionChecker(), dlFileEntry, ActionKeys.VIEW)) {
				documentList.add(dlFileEntry);
			}
		}
		return documentList;
	}

	/**
	 * Get all files from portal where user has permission for.
	 * 
	 * @return fileList from portal
	 * @throws SystemException
	 * @throws PrincipalException
	 */
	protected List<DLFileEntry> getDocumentList() throws SystemException, PortalException {
		boolean isTagsAsset;
		List<DLFileEntry> documentListComplete = DLFileEntryLocalServiceUtil.getDLFileEntries(0, DLFileEntryLocalServiceUtil.getDLFileEntriesCount());
		DLFileEntryLocalServiceUtil.getDLFileEntries(0, DLFileEntryLocalServiceUtil.getDLFileEntriesCount());
		List<DLFileEntry> documentList = new ArrayList<DLFileEntry>();

		// add only blogEntry to list where user has permission to view
		for (DLFileEntry document : documentListComplete) {
			try {
				TagsAssetLocalServiceUtil.getAsset(DLFileEntry.class.getName(), document.getFileEntryId());
				isTagsAsset = true;
			} catch (Exception e) {
				isTagsAsset = false;
			}
			if (isTagsAsset && DLFileEntryPermission.contains(LinkToolUtil.getPermissionChecker(), document, ActionKeys.VIEW)) {
				documentList.add(document);
			}
		}
		return documentList;
	}

	/**
	 * Get all child folder from actual folder where user has permission for.
	 * 
	 * @param req
	 *            actionRequest
	 * @param actualfolder
	 *            the actual chosen folder
	 * @return folder List with child folder from parent folder.
	 * @throws SystemException
	 * @throws PortalException
	 * @throws PrincipalException
	 */
	private List<DLFolder> getDocumentFolderList(ActionRequest req, DLFolder actualfolder) throws SystemException, PortalException, PrincipalException {
		List<DLFolder> folderList = new ArrayList<DLFolder>();
		List<DLFolder> tempFolderList = DLFolderLocalServiceUtil.getFolders(actualfolder.getGroupId(), actualfolder.getFolderId());
		for (DLFolder dlFolder : tempFolderList) {
			if (DLFolderPermission.contains(LinkToolUtil.getPermissionChecker(), dlFolder, ActionKeys.VIEW)) {
				folderList.add(dlFolder);
			}
		}

		return folderList;
	}

}
