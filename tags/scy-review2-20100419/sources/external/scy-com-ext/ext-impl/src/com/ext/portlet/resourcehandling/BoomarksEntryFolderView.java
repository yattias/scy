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
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.permission.BookmarksEntryPermission;
import com.liferay.portlet.bookmarks.service.permission.BookmarksFolderPermission;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.LinkToolUtil;

/**
 * Get all needed resources to show bookmark entries. The parent folder are all folder from bookmarksFolders with parentFolder id = 0.  
 * Child entries are all files from this folder and all child folder with parentFolder id equals given folder.
 * 
 * @author Daniel
 *
 */
public class BoomarksEntryFolderView implements FolderView {

	private ActionRequest req;

	public BoomarksEntryFolderView(ActionRequest req) throws SystemException, PortalException {
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

			BookmarksFolder rootFolder = BookmarksFolderLocalServiceUtil.getBookmarksFolder(Long.valueOf(rootFolderId));
			List<BookmarksFolder> rootFolderList = getRootFolderListBookmark();
			List<BookmarksFolder> hList = new ArrayList<BookmarksFolder>();

			List<BookmarksFolder> folderList;
			List<BookmarksEntry> bookmarkList;
			BookmarksFolder actualfolder;
			BookmarksFolder parentfolder;

			if (cmd.equals("choseFolder") || cmd.equals("changePreview") ) {
				actualfolder = BookmarksFolderLocalServiceUtil.getBookmarksFolder(Long.valueOf(folderId));
				hList.add(actualfolder);
				hList.addAll(getBreadcrumbList(actualfolder));
				folderList = getBoomarkFolderList(req, actualfolder);
				bookmarkList = getBookmarkList(actualfolder);
				req.setAttribute("parentFolder", actualfolder);
			} else if (cmd.equals("backFolder")) {
				actualfolder = BookmarksFolderLocalServiceUtil.getBookmarksFolder(Long.valueOf(folderId));
				parentfolder = getParentFolder(rootFolder, actualfolder);
				folderList = getBoomarkFolderList(req, parentfolder);
				bookmarkList = getBookmarkList(parentfolder);
				hList.addAll(getBreadcrumbList(actualfolder));
				req.setAttribute("parentFolder", parentfolder);
			} else {
				folderList = BookmarksFolderLocalServiceUtil.getFolders(rootFolder.getGroupId(), rootFolder.getFolderId(), 0, BookmarksFolderLocalServiceUtil
						.getFoldersCount(rootFolder.getGroupId(), rootFolder.getFolderId()));
				bookmarkList = getBookmarkList(rootFolder);
				hList.add(rootFolder);
				req.setAttribute("parentFolder", rootFolder);
			}
			Collections.reverse(hList);

			req.setAttribute("rootFolderList", rootFolderList);
			req.setAttribute("rootFolder", rootFolder);
			req.setAttribute("folderList", folderList);
			req.setAttribute("bookmarkList", bookmarkList);
			req.setAttribute("hList", hList);
		}
		return null;
	}
	
	@Override
	public FolderView getParent() throws SystemException, PortalException {
		List<BookmarksFolder> rootFolderList = getRootFolderListBookmark();
		req.setAttribute("rootFolderList", rootFolderList);
		return null;
	}

	private List<BookmarksFolder> getBreadcrumbList(BookmarksFolder actualfolder) throws PortalException, SystemException {
		List<BookmarksFolder> hList = new ArrayList<BookmarksFolder>();
		BookmarksFolder tempFolder = actualfolder;
		while (tempFolder.getParentFolderId() != 0) {
			tempFolder = BookmarksFolderLocalServiceUtil.getBookmarksFolder(tempFolder.getParentFolderId());
			hList.add(tempFolder);
		}
		return hList;
	}

	private BookmarksFolder getParentFolder(BookmarksFolder rootFolder, BookmarksFolder actualfolder) throws PortalException, SystemException {
		BookmarksFolder parentfolder;
		if (actualfolder.getParentFolderId() > 0) {
			parentfolder = BookmarksFolderLocalServiceUtil.getBookmarksFolder(actualfolder.getParentFolderId());
		} else {
			parentfolder = rootFolder;
		}
		return parentfolder;
	}

	/**
	 * Get list of all bookmark parent folders which have permission to view.
	 * 
	 * @return list of all parent bookmark folder.
	 * @throws SystemException
	 * @throws PortalException
	 */
	private List<BookmarksFolder> getRootFolderListBookmark() throws SystemException, PortalException {
		List<BookmarksFolder> bookmarksFolderList = new ArrayList<BookmarksFolder>();
		List<BookmarksFolder> bookFolderListComp = BookmarksFolderLocalServiceUtil.getBookmarksFolders(0, BookmarksFolderLocalServiceUtil
				.getBookmarksFoldersCount());
		for (BookmarksFolder bookmarksFolderEntry : bookFolderListComp) {
			if (bookmarksFolderEntry.getParentFolderId() == 0) {
				if (BookmarksFolderPermission.contains(LinkToolUtil.getPermissionChecker(), bookmarksFolderEntry, ActionKeys.VIEW)) {
					bookmarksFolderList.add(bookmarksFolderEntry);
				}
			}
		}
		return bookmarksFolderList;
	}

	/**
	 * Get all resource from this bookmark folder where user has permission for
	 * and is also in table tagsAsset.
	 * 
	 * @param actualfolder
	 *            actual chosen folder from user.
	 * @return
	 * @throws SystemException
	 * @throws PrincipalException
	 */
	private List<BookmarksEntry> getBookmarkList(BookmarksFolder actualfolder) throws SystemException, PrincipalException {
		Boolean isTagsAsset;
		List<BookmarksEntry> bookmarkList = new ArrayList<BookmarksEntry>();
		List<BookmarksEntry> tempDocumentList = BookmarksEntryLocalServiceUtil.getEntries(actualfolder.getFolderId(), 0, BookmarksEntryLocalServiceUtil
				.getEntriesCount(actualfolder.getFolderId()));
		for (BookmarksEntry bookmarkEntry : tempDocumentList) {
			try {
				TagsAssetLocalServiceUtil.getAsset(BookmarksEntry.class.getName(), bookmarkEntry.getEntryId());
				isTagsAsset = true;
			} catch (Exception e) {
				isTagsAsset = false;
			}
			if (isTagsAsset && BookmarksEntryPermission.contains(LinkToolUtil.getPermissionChecker(), bookmarkEntry, ActionKeys.VIEW)) {
				bookmarkList.add(bookmarkEntry);
			}
		}
		return bookmarkList;
	}

	/**
	 * Get all child folder from actual bookmark folder where user has
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
	private List<BookmarksFolder> getBoomarkFolderList(ActionRequest req, BookmarksFolder actualfolder) throws SystemException, PortalException,
			PrincipalException {
		List<BookmarksFolder> folderList = new ArrayList<BookmarksFolder>();
		List<BookmarksFolder> tempFolderList = BookmarksFolderLocalServiceUtil.getFolders(actualfolder.getGroupId(), actualfolder.getFolderId(), 0,
				BookmarksFolderLocalServiceUtil.getFoldersCount(actualfolder.getGroupId(), actualfolder.getFolderId()));
		for (BookmarksFolder dlFolder : tempFolderList) {
			if (BookmarksFolderPermission.contains(LinkToolUtil.getPermissionChecker(), dlFolder, ActionKeys.VIEW))
				;
			{
				folderList.add(dlFolder);
			}
		}
		return folderList;
	}

}
