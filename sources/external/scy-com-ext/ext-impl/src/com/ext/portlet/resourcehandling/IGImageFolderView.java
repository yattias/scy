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
import com.liferay.portlet.imagegallery.model.IGFolder;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGFolderLocalServiceUtil;
import com.liferay.portlet.imagegallery.service.IGImageLocalServiceUtil;
import com.liferay.portlet.imagegallery.service.permission.IGFolderPermission;
import com.liferay.portlet.imagegallery.service.permission.IGImagePermission;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.LinkToolUtil;

/**
 * Get all needed resources to show image entries. The parent folder are all folder from IGFolder with parentFolder id = 0.  
 * Child entries are all files from this folder and all child folder with parentFolder id equals given folder.
 * 
 * @author Daniel
 *
 */
public class IGImageFolderView implements FolderView {

	private ActionRequest req;

	public IGImageFolderView(ActionRequest req) throws SystemException, PortalException {
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

			List<IGFolder> rootFolderList = getRootFolderListIGImage();
			IGFolder rootFolder = IGFolderLocalServiceUtil.getIGFolder(Long.valueOf(rootFolderId));
			List<IGFolder> hList = new ArrayList<IGFolder>();

			List<IGFolder> folderList;
			List<IGImage> imageList;
			IGFolder actualfolder;
			IGFolder parentfolder;

			if (cmd.equals("choseFolder") || cmd.equals("changePreview")) {
				actualfolder = IGFolderLocalServiceUtil.getIGFolder(Long.valueOf(folderId));
				hList.add(actualfolder);
				hList.addAll(getBreadcrumb(actualfolder));
				folderList = getIGFolderList(req, actualfolder);
				imageList = getIGImageList(actualfolder);
				req.setAttribute("parentFolder", actualfolder);
			} else if (cmd.equals("backFolder")) {
				actualfolder = IGFolderLocalServiceUtil.getIGFolder(Long.valueOf(folderId));
				parentfolder = getParentFolder(rootFolder, actualfolder);
				hList.addAll(getBreadcrumb(actualfolder));
				folderList = getIGFolderList(req, parentfolder);
				imageList = getIGImageList(parentfolder);
				req.setAttribute("parentFolder", parentfolder);
			} else {
				hList.add(rootFolder);
				folderList = IGFolderLocalServiceUtil.getFolders(rootFolder.getGroupId(), rootFolder.getFolderId());
				imageList = getIGImageList(rootFolder);
				req.setAttribute("parentFolder", rootFolder);
			}

			Collections.reverse(hList);

			req.setAttribute("rootFolderList", rootFolderList);
			req.setAttribute("rootFolder", rootFolder);
			req.setAttribute("folderList", folderList);
			req.setAttribute("imageList", imageList);
			req.setAttribute("hList", hList);
		}
		return null;
	}

	@Override
	public FolderView getParent() throws SystemException, PortalException {
		List<IGFolder> rootFolderList = getRootFolderListIGImage();
		req.setAttribute("rootFolderList", rootFolderList);
		return null;
	}

	private IGFolder getParentFolder(IGFolder rootFolder, IGFolder actualfolder) throws PortalException, SystemException {
		IGFolder parentfolder;
		if (actualfolder.getParentFolderId() > 0) {
			parentfolder = IGFolderLocalServiceUtil.getIGFolder(actualfolder.getParentFolderId());
		} else {
			parentfolder = rootFolder;
		}
		return parentfolder;
	}


	private List<IGFolder> getBreadcrumb(IGFolder actualfolder) throws PortalException, SystemException {
		List<IGFolder> hList = new ArrayList<IGFolder>();
		IGFolder tempFolder = actualfolder;
		while (tempFolder.getParentFolderId() != 0) {
			tempFolder = IGFolderLocalServiceUtil.getIGFolder(tempFolder.getParentFolderId());
			hList.add(tempFolder);
		}
		return hList;
	}

	/**
	 * Get list of all ig parent folders which have permission to view.
	 * 
	 * @return list of all parent ig folder.
	 * @throws SystemException
	 * @throws PortalException
	 */
	private List<IGFolder> getRootFolderListIGImage() throws SystemException, PortalException {
		List<IGFolder> imageFolderList = new ArrayList<IGFolder>();
		List<IGFolder> imageFolderListComp = IGFolderLocalServiceUtil.getIGFolders(0, IGFolderLocalServiceUtil.getIGFoldersCount());
		for (IGFolder igFolderEntry : imageFolderListComp) {
			if (igFolderEntry.getParentFolderId() == 0) {
				if (IGFolderPermission.contains(LinkToolUtil.getPermissionChecker(), igFolderEntry, ActionKeys.VIEW)) {
					imageFolderList.add(igFolderEntry);
				}
			}
		}
		return imageFolderList;
	}

	/**
	 * Get all resource from this image folder where user has permission for and
	 * is also in table tagsAsset.
	 * 
	 * @param actualfolder
	 *            actual chosen folder from user.
	 * @return
	 * @throws SystemException
	 * @throws PrincipalException
	 */
	private List<IGImage> getIGImageList(IGFolder actualfolder) throws SystemException, PrincipalException {
		Boolean isTagsAsset;
		List<IGImage> imageList = new ArrayList<IGImage>();
		List<IGImage> tempDocumentList = IGImageLocalServiceUtil.getImages(actualfolder.getFolderId());
		for (IGImage imageEntry : tempDocumentList) {
			try {
				TagsAssetLocalServiceUtil.getAsset(IGImage.class.getName(), imageEntry.getImageId());
				isTagsAsset = true;
			} catch (Exception e) {
				isTagsAsset = false;
			}
			if (isTagsAsset && IGImagePermission.contains(LinkToolUtil.getPermissionChecker(), imageEntry, ActionKeys.VIEW)) {
				imageList.add(imageEntry);
			}
		}
		return imageList;
	}
	
	/**
	 * Get all child folder from actual image folder where user has permission
	 * for.
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
	private List<IGFolder> getIGFolderList(ActionRequest req, IGFolder actualfolder) throws SystemException, PortalException, PrincipalException {
		List<IGFolder> folderList = new ArrayList<IGFolder>();
		List<IGFolder> tempFolderList = IGFolderLocalServiceUtil.getFolders(actualfolder.getGroupId(), actualfolder.getFolderId());
		for (IGFolder dlFolder : tempFolderList) {
			if (IGFolderPermission.contains(LinkToolUtil.getPermissionChecker(), dlFolder, ActionKeys.VIEW))
				;
			{
				folderList.add(dlFolder);
			}
		}
		return folderList;
	}

}
