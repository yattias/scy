package com.liferay.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil;
import com.ext.portlet.linkTool.model.LinkEntry;
import com.ext.portlet.linkTool.service.LinkEntryLocalServiceUtil;
import com.liferay.counter.service.persistence.CounterUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.bookmarks.EntryURLException;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksFolderUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleServiceUtil;

/**
 * Util class for all link action.
 * 
 * @author daniel
 * 
 */

public class LinkToolUtil {

	/**
	 * Add new bookmark entry if user add extern link and radio button
	 * callByReference.
	 * 
	 * @param actionRequest
	 *            ActionRequest
	 * @param serviceContext
	 *            user Data from themeDisplay.
	 * @return the Id from new bookmark entry.
	 * @throws Exception
	 */
	public long updateEntry(ActionRequest actionRequest, ServiceContext serviceContext) throws Exception {

		String name = ParamUtil.getString(actionRequest, "name");
		String url = ParamUtil.getString(actionRequest, "url");
		String comments = ParamUtil.getString(actionRequest, "comments");
		Date now = new Date();
		long folderId;

		// allow urls that start with www
		if (url.startsWith("www")) {
			String http = "http://";
			String givenUrl = url;
			url = http + givenUrl;
		}

		serviceContext.setAddCommunityPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		User user = UserLocalServiceUtil.getUser(serviceContext.getUserId());
		Long userId = user.getUserId();

		List<BookmarksFolder> bookfolderList = BookmarksFolderLocalServiceUtil.getBookmarksFolders(0, BookmarksFolderLocalServiceUtil
				.getBookmarksFoldersCount());
		List<BookmarksFolder> userBookfolderList = new ArrayList<BookmarksFolder>();
		for (BookmarksFolder bookmarksFolder : bookfolderList) {
			if (userId.equals(bookmarksFolder.getUserId())) {
				userBookfolderList.add(bookmarksFolder);
			}
		}

		// create new folder if user has no for bookmarks
		if (userBookfolderList.size() < 1) {
			long id = CounterUtil.increment();
			BookmarksFolder bookmarksFolder = BookmarksFolderUtil.create(id);
			bookmarksFolder.setUserId(serviceContext.getUserId());
			bookmarksFolder.setGroupId(serviceContext.getScopeGroupId());
			bookmarksFolder.setCompanyId(serviceContext.getCompanyId());
			bookmarksFolder.setCreateDate(now);
			bookmarksFolder.setModifiedDate(now);
			bookmarksFolder.setName("bookmarksFolder");
			BookmarksFolder addFolder = BookmarksFolderLocalServiceUtil.addBookmarksFolder(bookmarksFolder);
			folderId = addFolder.getFolderId();
		} else {
			folderId = userBookfolderList.get(0).getFolderId();
		}

		// add new bookmark entry
		BookmarksEntry entry = BookmarksEntryLocalServiceUtil.addEntry(userId, folderId, name, url, comments, serviceContext);

		// add metadata
		MetadataActionUtil.addMetadata(BookmarksEntry.class.getName(), entry.getEntryId(), actionRequest);

		// add entry and folder to asset db
		AssetPublisherUtil.addAndStoreSelection(actionRequest, BookmarksEntry.class.getName(), entry.getEntryId(), -1);
		AssetPublisherUtil.addRecentFolderId(actionRequest, BookmarksEntry.class.getName(), folderId);

		return entry.getEntryId();

	}

	/**
	 * Add new link entry for viewed resource and chosen resource to link with.
	 * 
	 * @param resourceId
	 *            id form viewed resource.
	 * @param linkedResourceId
	 *            chosen linked resource.
	 * @param serviceContext
	 *            user infos from themeDisplay.
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void addLink(String resourceId, String linkedResourceId, String classNameId, ServiceContext serviceContext) throws SystemException, PortalException {

		Date now = new Date();
		LinkEntry linkEntry;
		Boolean linkExists = false;
		Long linkId = 0l;

		// check if link already exists

		List<LinkEntry> linkEntries = LinkEntryLocalServiceUtil.getLinkEntries();
		List<LinkEntry> linkExistsList = LinkEntryLocalServiceUtil.getService().getLinkEntry(resourceId, linkedResourceId);
		if (!linkExistsList.isEmpty()) {
			linkId = linkExistsList.get(0).getLinkId();
			linkExists = true;
		} else {
			linkExists = false;
		}

		// set primary key linkId
		if ((!linkEntries.isEmpty()) && (!linkExists)) {
			for (LinkEntry linkEntry2 : linkEntries) {
				if (linkEntry2.getLinkId() > linkId) {
					linkId = linkEntry2.getLinkId();
				}
			}
			linkId++;
		} else if ((linkEntries.isEmpty()) && (!linkExists)) {
			linkId = 1l;
		}

		// get linkEntry or create
		if (linkExists) {
			linkEntry = LinkEntryLocalServiceUtil.getLinkEntry(linkId);
		} else {
			linkEntry = LinkEntryLocalServiceUtil.createLinkEntry(linkId);
		}

		// set new data
		linkEntry.setUserId(serviceContext.getUserId());
		linkEntry.setGroupId(serviceContext.getScopeGroupId());
		linkEntry.setCompanyId(serviceContext.getCompanyId());
		linkEntry.setCreateDate(now);
		linkEntry.setModifiedDate(now);
		linkEntry.setLinkedResourceId(linkedResourceId.toString());
		linkEntry.setLinkedResourceClassNameId(classNameId);
		linkEntry.setResourceId(resourceId.toString());

		// persist to db
		LinkEntryLocalServiceUtil.updateLinkEntry(linkEntry);

	}

	/**
	 * Set checkedResource to empty string if is null.
	 * 
	 * @param checkedResource
	 * @return as empty String
	 */

	public String checkCheckedResource(String checkedResource) {
		if (checkedResource == null) {
			checkedResource = "";
		}
		return checkedResource;
	}

	/**
	 * Initializes new permission checker
	 * 
	 * @return new initialized permission checker.
	 * @throws PrincipalException
	 */
	public static PermissionChecker getPermissionChecker() throws PrincipalException {
		PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker == null) {
			throw new PrincipalException("PermissionChecker not initialized");
		}

		return permissionChecker;
	}

	/**
	 * Remove double insert resources.
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> ArrayList<T> removeDoubleEntries(Collection<T> list) {
		return new ArrayList<T>(new HashSet<T>(list));
	}

	/**
	 * Rename chosen input option type to start resource class name.
	 * 
	 * @param sType
	 *            user chosen resource type form option input
	 * @return
	 */
	public String getStartResourceClassName(String sType, Long linkedResourceId) {
		String className = "";
		if (sType.equals(FreestylerEntry.class.getName())) {
			try {
				FreestylerImageLocalServiceUtil.getFreestylerImage(linkedResourceId);
				className = FreestylerImage.class.getName();
			} catch (Exception e) {
				className = FreestylerEntry.class.getName();
			}

			return className;
		} else {
			return sType;
		}
	}

	/**
	 * Delete links at db from entered classPK id.
	 * 
	 * @param entryId
	 *            the classPK id from tagsasset.
	 * @throws SystemException
	 */
	public static void deleteEntry(long entryId) throws SystemException {
		List<LinkEntry> linkList = LinkEntryLocalServiceUtil.getLinkEntriesByResourceId(entryId);
		List<LinkEntry> linkList2 = LinkEntryLocalServiceUtil.getLinkEntriesByLinkedResourceId(entryId);

		for (LinkEntry linkEntry : linkList) {
			LinkEntryLocalServiceUtil.deleteLinkEntry(linkEntry);
		}
		for (LinkEntry linkEntry2 : linkList2) {
			LinkEntryLocalServiceUtil.deleteLinkEntry(linkEntry2);
		}
	}

	/**
	 * Delete linkEntry
	 * 
	 * @param link
	 * @throws SystemException
	 */
	public static void deleteEntry(LinkEntry link) throws SystemException {
		LinkEntryLocalServiceUtil.deleteLinkEntry(link);
	}

	/**
	 * Extract content as text from user entered url and store new journal
	 * arcticle with this content.
	 * 
	 * @param req
	 *            actionRequest
	 * @param serviceContext
	 * @return new saved journal article with webcontent as content
	 * @throws Exception
	 */
	public JournalArticle saveCopyFromUrl(ActionRequest req, ServiceContext serviceContext) throws Exception {

		String urlEntry = ParamUtil.getString(req, "url");

		String contentFromUrl;
		if (urlEntry.contains("www.youtube.com")) {
			contentFromUrl = YoutubeUtil.getYoutubeContent(urlEntry);
		} else {
			contentFromUrl = getUrlStreamContent(urlEntry);
		}

		JournalArticle article = saveJournalArticle(req, serviceContext, contentFromUrl);

		return article;

	}

	/**
	 * store new journal article to database. Edit content text for article as
	 * static content. The type for article is set to copyFromBookmark.
	 * 
	 * @param req
	 * @param serviceContext
	 * @param contentFromURL
	 *            the extract content from homepage with url from entry mask
	 * @return saved journal article
	 * @throws Exception
	 * @throws NumberFormatException
	 */

	private JournalArticle saveJournalArticle(ActionRequest req, ServiceContext serviceContext, String contentFromURL) throws NumberFormatException, Exception {

		String name = ParamUtil.getString(req, "name");
		String description = ParamUtil.getString(req, "description");

		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);

		// add content as static content quelltext
		String content = LocalizationUtil.updateLocalization(StringPool.BLANK, "static-content", contentFromURL, themeDisplay.getLanguageId(), themeDisplay
				.getLanguageId(), true);

		java.util.TimeZone zone = themeDisplay.getUser().getTimeZone();

		java.util.GregorianCalendar gc = new java.util.GregorianCalendar(zone);

		// data for new article
		long userId = themeDisplay.getUserId();
		long groupId = themeDisplay.getScopeGroupId();
		String articleId = "";
		boolean autoArticleId = true;
		String title = name;
		String type = "general";
		// String type = "copyOfBookmark";
		String structureId = "";
		String templateId = "";
		int displayDateMonth = gc.get(Calendar.MONTH);
		int displayDateDay = gc.get(Calendar.DAY_OF_MONTH);
		int displayDateYear = gc.get(Calendar.YEAR);
		int displayDateHour = gc.get(Calendar.HOUR_OF_DAY);
		int displayDateMinute = gc.get(Calendar.MINUTE);
		int expirationDateMonth = 0;
		int expirationDateDay = 0;
		int expirationDateYear = 0;
		int expirationDateHour = 0;
		int expirationDateMinute = 0;
		boolean neverExpire = true;
		int reviewDateMonth = 0;
		int reviewDateDay = 0;
		int reviewDateYear = 0;
		int reviewDateHour = 0;
		int reviewDateMinute = 0;
		boolean neverReview = true;
		boolean indexable = true;
		String smallImageURL = "";
		File smallFile = null;
		String articleURL = "";
		Map<String, byte[]> images = new HashMap<String, byte[]>();
		boolean smallImage = false;

		// add article to db in all tables that start with journalArticle and
		// new entry in table tagsAsset
		JournalArticle article = JournalArticleLocalServiceUtil.addArticle(userId, groupId, articleId, autoArticleId, title, description, content, type,
				structureId, templateId, displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth,
				expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear,
				reviewDateHour, reviewDateMinute, neverReview, indexable, smallImage, smallImageURL, smallFile, images, articleURL, serviceContext);

		// set visible to true at table tagsAsset for this entry
		article = JournalArticleServiceUtil.approveArticle(article.getGroupId(), article.getArticleId(), article.getVersion(), articleURL, serviceContext);

		// add metadata for article
		MetadataActionUtil.addMetadata(JournalArticle.class.getName(), article.getResourcePrimKey(), req);

		return article;
	}

	/**
	 * Extract the content form given homepage from urlEntry.
	 * 
	 * @param urlEntry
	 *            the url which the user enter at entry mask
	 * @return the content from url as string.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws EntryURLException
	 *             the user entered url that does not exist or does not start
	 *             with http
	 */
	private String getUrlStreamContent(String urlEntry) throws MalformedURLException, IOException, EntryURLException {
		String NL = System.getProperty("line.separator");

		// allow urls that start with www
		if (urlEntry.startsWith("www")) {
			String http = "http://";
			String givenUrl = urlEntry;
			urlEntry = http + givenUrl;
		}

		if (!urlEntry.startsWith("http:")) {
			throw new EntryURLException();
		}
		try {

			URL url = new URL(urlEntry);
			URLConnection uc = url.openConnection();
			StringBuffer buff = new StringBuffer();
			String inputLine;

			if (uc.getContentType().contains("text")) {
				BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

				List<String> textList = new ArrayList<String>();
				while ((inputLine = in.readLine()) != null) {
					textList.add(inputLine);
				}

				int size = textList.size();

				for (int i = 0; i < textList.size(); i++) {
					buff.append(textList.get(i).toString());
					if (i < (size - 2)) {
						buff.append(NL);
					}
				}
				urlEntry = buff.toString();
				in.close();

			}
			return urlEntry;
		} catch (Exception e) {
			throw new EntryURLException();
		}
	}
}
