package com.ext.portlet.bookreports.action;

import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;

import com.ext.portlet.bookreports.model.BookReportsEntry;
import com.ext.portlet.bookreports.service.BookReportsEntryLocalServiceUtil;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;

public class BookLocalServiceUtil {
	
	private static String BOOK="entryId";
	private static Long staticEntryId = 0l;

	public static BookReportsEntry addBook(ActionRequest req)
			throws PortalException, SystemException {
		String[] communityPermissions = req.getParameterValues("communityPermissions");
		String[] guestPermissions = req.getParameterValues("guestPermissions");
		ThemeDisplay themeDisplay = (ThemeDisplay) req
				.getAttribute(WebKeys.THEME_DISPLAY);
		long userId = themeDisplay.getUserId();
		String title = req.getParameter("title");
		User user = UserLocalServiceUtil.getUser(userId);
		Date now = new Date();
		long bookId = CounterLocalServiceUtil.increment(BookReportsEntry.class
				.getName());
		BookReportsEntry book = BookReportsEntryLocalServiceUtil
				.createBookReportsEntry(bookId);
		book.setTitle(title);
		book.setGroupId(themeDisplay.getScopeGroupId());
		book.setCompanyId(themeDisplay.getCompanyId());
		book.setUserId(userId);
		book.setUserName(user.getFullName());
		book.setCreateDate(now);
		book.setModifiedDate(now);
		book.setName(title);
		BookReportsEntryLocalServiceUtil.updateBookReportsEntry(book);
		addEntryResources(book, communityPermissions, guestPermissions);
		return book;
	}
	
	public static void addEntryResources(BookReportsEntry book, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException{
		ResourceLocalServiceUtil.addModelResources(book.getCompanyId(), book.getGroupId(), book.getUserId(), book.getUserName(), book.getPrimaryKey(), communityPermissions, guestPermissions);
	}

	public static List<BookReportsEntry> getAll() throws PortalException,
			SystemException {
		int end = BookReportsEntryLocalServiceUtil.getBookReportsEntriesCount();
		return BookReportsEntryLocalServiceUtil.getBookReportsEntries(0, end);
	}

	public static void deleteBook(ActionRequest req) throws PortalException,
			SystemException {
		long entryId = ParamUtil.getLong(req, "bookId");
		BookReportsEntryLocalServiceUtil.deleteBookReportsEntry(entryId);
	}

	public static BookReportsEntry updateBook(ActionRequest req)
			throws PortalException, SystemException {
		String title = ParamUtil.getString(req, "title");	
		BookReportsEntry reportsEntry = BookReportsEntryLocalServiceUtil
				.getBookReportsEntry(staticEntryId);
		reportsEntry.setTitle(title);
		reportsEntry.setModifiedDate(new Date());
		BookReportsEntryLocalServiceUtil.updateBookReportsEntry(reportsEntry);
		return reportsEntry;
	}
	public static void getBook(RenderRequest req) throws Exception{
		HttpServletRequest httpReq = PortalUtil.getHttpServletRequest(req);
		getBook(httpReq);
	}
	
	public static void getBook(HttpServletRequest req) throws Exception{
		long entryId = ParamUtil.getLong(req, "bookId");
		BookReportsEntry book = null;
		if(entryId > 0){
			book = BookReportsEntryLocalServiceUtil.getBookReportsEntry(entryId);
		}
		req.setAttribute(BookLocalServiceUtil.BOOK, book);
		staticEntryId = entryId;
	}
}
