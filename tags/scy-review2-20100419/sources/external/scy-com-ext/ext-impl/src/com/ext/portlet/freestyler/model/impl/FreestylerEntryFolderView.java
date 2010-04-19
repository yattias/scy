package com.ext.portlet.freestyler.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.ActionRequest;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.model.FreestylerFolder;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.permission.FreestylerImagePermission;
import com.ext.portlet.freestyler.service.FreestylerFolderLocalServiceUtil;
import com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil;
import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.LinkToolUtil;

/**
 * Get all needed resources to show freestyler entries. The parent folder are all folder from freestylerFolder with parentFolder id = 0.  
 * Child entries are all files from this folder and all child folder with parentFolder id equals given folder.
 * 
 * @author Daniel
 *
 */
public class FreestylerEntryFolderView implements FolderView {

	private ActionRequest req;

	public FreestylerEntryFolderView(ActionRequest req) throws SystemException, PortalException {
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

			FreestylerFolder rootFolder = FreestylerFolderLocalServiceUtil.getFreestylerFolder(Long.valueOf(rootFolderId));
			List<FreestylerFolder> rootFolderList = getRootFolderListFreestyler(req);
			List<FreestylerFolder> hList = new ArrayList<FreestylerFolder>();

			List<FreestylerFolder> folderList;
			List<FreestylerImage> freestylerList;
			FreestylerFolder actualfolder;
			FreestylerFolder parentfolder;

			if (cmd.equals("choseFolder") || cmd.equals("changePreview")) {
				actualfolder = FreestylerFolderLocalServiceUtil.getFreestylerFolder(Long.valueOf(folderId));
				hList.add(actualfolder);
				hList.addAll(getBreadcrumbList(actualfolder));
				folderList = getFreestylerFolderList(req, actualfolder);
				freestylerList = getFreestylerImageList(actualfolder);
				req.setAttribute("parentFolder", actualfolder);
			} else if (cmd.equals("backFolder")) {
				actualfolder = FreestylerFolderLocalServiceUtil.getFreestylerFolder(Long.valueOf(folderId));
				parentfolder = getParentFolder(rootFolder, actualfolder);
				folderList = getFreestylerFolderList(req, parentfolder);
				freestylerList = getFreestylerImageList(parentfolder);
				hList.addAll(getBreadcrumbList(actualfolder));
				req.setAttribute("parentFolder", parentfolder);
			} else {
				folderList = FreestylerFolderLocalServiceUtil.getFolders(rootFolder.getGroupId(), rootFolder.getFolderId());
				freestylerList = getFreestylerImageList(rootFolder);
				hList.add(rootFolder);
				req.setAttribute("parentFolder", rootFolder);

			}
			Collections.reverse(hList);

			req.setAttribute("rootFolderList", rootFolderList);
			req.setAttribute("rootFolder", rootFolder);
			req.setAttribute("folderList", folderList);
			req.setAttribute("freestylerList", freestylerList);
			req.setAttribute("hList", hList);
		}
		return null;
	}

	@Override
	public FolderView getParent() throws SystemException, PortalException {
		List<FreestylerFolder> rootFolderList = getRootFolderListFreestyler(req);
		req.setAttribute("rootFolderList", rootFolderList);
		return null;
	}

	/**
	 * Get all child folders to show hierarchy breadcrumb list.
	 * 
	 * @param actualfolder
	 *            actual chose folder.
	 * @return freestyler folder list.
	 * @throws PortalException
	 * @throws SystemException
	 */
	private List<FreestylerFolder> getBreadcrumbList(FreestylerFolder actualfolder) throws PortalException, SystemException {
		List<FreestylerFolder> hList = new ArrayList<FreestylerFolder>();
		FreestylerFolder tempFolder = actualfolder;
		while (tempFolder.getParentFolderId() != 0) {
			tempFolder = FreestylerFolderLocalServiceUtil.getFreestylerFolder(tempFolder.getParentFolderId());
			hList.add(tempFolder);
		}
		return hList;
	}

	/**
	 * Get list of all freestyler parent folders which have permission to view
	 * and where freestyler image exists..
	 * 
	 * @return list of all parent freestyler folder where images exists.
	 * @throws SystemException
	 * @throws PortalException
	 */
	private List<FreestylerFolder> getRootFolderListFreestyler(ActionRequest req) throws SystemException, PortalException {
		Long entryId = Long.valueOf(req.getParameter("entryId"));
		Boolean isFreestylerImage = false;
		long freestylerEntryId = 0l;

		List<FreestylerFolder> freeFolderList = new ArrayList<FreestylerFolder>();
		List<FreestylerFolder> freeFolderListComp = FreestylerFolderLocalServiceUtil.getFreestylerFolders(0, FreestylerFolderLocalServiceUtil
				.getFreestylerFoldersCount());
		for (FreestylerFolder freeFolderEntry : freeFolderListComp) {
			if (freeFolderEntry.getParentFolderId() == 0) {
				List<FreestylerImage> freeImageList = FreestylerImageLocalServiceUtil.getImages(freeFolderEntry.getFolderId());
				for (FreestylerImage freestylerImage : freeImageList) {
					freestylerEntryId = freestylerImage.getFreestylerId();
					if (freestylerImage.getImageId() == entryId) {
						isFreestylerImage = true;
					}
				}
				if (!freeImageList.isEmpty()
						&& freestylerEntryId > 0
						&& LinkToolUtil.getPermissionChecker().hasPermission(freeFolderEntry.getGroupId(), FreestylerEntry.class.getName(), freestylerEntryId,
								ActionKeys.VIEW) && !isFreestylerImage) {
					freeFolderList.add(freeFolderEntry);
				}
			}
		}
		return freeFolderList;
	}

	/**
	 * Get all child folder from actual freestyler folder where user has
	 * permission for.
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
	private List<FreestylerFolder> getFreestylerFolderList(ActionRequest req, FreestylerFolder actualfolder) throws SystemException, PortalException,
			PrincipalException {
		List<FreestylerFolder> folderList = new ArrayList<FreestylerFolder>();
		List<FreestylerFolder> tempFolderList = FreestylerFolderLocalServiceUtil.getFolders(actualfolder.getGroupId(), actualfolder.getFolderId());
		for (FreestylerFolder dlFolder : tempFolderList) {
			List<FreestylerImage> freeImageList = FreestylerImageLocalServiceUtil.getImages(dlFolder.getFolderId());
			if (!freeImageList.isEmpty() && FreestylerImagePermission.contains(LinkToolUtil.getPermissionChecker(), freeImageList.get(0), ActionKeys.VIEW))
				;
			{
				folderList.add(dlFolder);
			}
		}
		return folderList;
	}

	/**
	 * Get all resource from this freestyler folder where user has permission
	 * for and is also in table tagsAsset.
	 * 
	 * @param actualfolder
	 *            actual chosen folder from user.
	 * @return
	 * @throws SystemException
	 * @throws PrincipalException
	 */
	private List<FreestylerImage> getFreestylerImageList(FreestylerFolder actualfolder) throws SystemException, PrincipalException {
		Boolean isTagsAsset;
		List<FreestylerImage> freeImageList = new ArrayList<FreestylerImage>();
		List<FreestylerImage> tempDocumentList = FreestylerImageLocalServiceUtil.getImages(actualfolder.getFolderId());
		for (FreestylerImage imageEntry : tempDocumentList) {
			try {
				TagsAssetLocalServiceUtil.getAsset(FreestylerImage.class.getName(), imageEntry.getImageId());
				isTagsAsset = true;
			} catch (Exception e) {
				isTagsAsset = false;
			}
			if (isTagsAsset
					&& LinkToolUtil.getPermissionChecker().hasPermission(imageEntry.getGroupId(), FreestylerEntry.class.getName(),
							imageEntry.getFreestylerId(), ActionKeys.VIEW)) {
				freeImageList.add(imageEntry);
			}
		}
		return freeImageList;
	}

	private FreestylerFolder getParentFolder(FreestylerFolder rootFolder, FreestylerFolder actualfolder) throws PortalException, SystemException {
		FreestylerFolder parentfolder;
		if (actualfolder.getParentFolderId() > 0) {
			parentfolder = FreestylerFolderLocalServiceUtil.getFreestylerFolder(actualfolder.getParentFolderId());
		} else {
			parentfolder = rootFolder;
		}
		return parentfolder;
	}

}
