package com.ext.portlet.assetviewbyratio.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portlet.bookmarks.EntryURLException;
import com.liferay.portlet.bookmarks.NoSuchEntryException;
import com.liferay.portlet.bookmarks.NoSuchFolderException;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.tags.TagsEntryException;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.LinkToolUtil;

/**
 * Extern-Link action for similarity blogs portlet. Add extern links from a
 * chosen start resource to an entered url. It is fired after pressing the
 * taglib ui add_link. If the user chose callByRefence or let the radio button
 * empty an new bookmark entry will create. A new link from viewed resource to
 * created where created and a new link from created bookmark. If user chose
 * callByValue the content from entered url will be save from stream as new
 * webcontent and also links between the resource would be create.
 * 
 * @author daniel
 * 
 */
public class LinkAction extends PortletAction {

	public static final String NL = System.getProperty("line.separator");

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		Long entryId = Long.valueOf(req.getParameter("entryId"));
		String className = req.getParameter("className");
		String redirect = req.getParameter("redirect");
		String cmd = req.getParameter(Constants.CMD);
		String radio;
		
		
		TagsAsset asset = null;

		if (req.getParameter("saveAsRadio") == null) {
			radio = "bookmark";
		} else {
			radio = req.getParameter("saveAsRadio");
		}

		LinkToolUtil util = new LinkToolUtil();

		try {
			asset = TagsAssetLocalServiceUtil.getAsset(className, entryId);
			ServiceContext serviceContext = ServiceContextFactory.getInstance(BookmarksEntry.class.getName(), req);
			if (cmd == null) {
				req.setAttribute("className", className);
				req.setAttribute("entryId", entryId);
				req.setAttribute("redirect", redirect);
				// set Forward
				setForward(req, "portlet.ext.asset_by_ratio.view_link");
				return;
			} else if (cmd.equals("extern") && radio.equals("bookmark")) {
				Long linkedResourceId = util.updateEntry(req, serviceContext);
				String classNameIdBookmark = String.valueOf(ClassNameLocalServiceUtil.getClassNameId(BookmarksEntry.class.getName()));
				String classNameId = String.valueOf(asset.getClassNameId());
				// add link for blog
				util.addLink(entryId.toString(), linkedResourceId.toString(), classNameIdBookmark, serviceContext);
				// add link for new bookmark
				util.addLink(linkedResourceId.toString(), entryId.toString(), classNameId, serviceContext);
				res.sendRedirect(redirect);
			} else if (cmd.equals("extern") && radio.equals("copy")) {
				JournalArticle ja = util.saveCopyFromUrl(req, serviceContext);
				Long linkedResourceId = ja.getResourcePrimKey();
				String classNameIdJournalArticle = String.valueOf(ClassNameLocalServiceUtil.getClassNameId(JournalArticle.class.getName()));
				String classNameId = String.valueOf(asset.getClassNameId());
				// add link for blog
				util.addLink(entryId.toString(), linkedResourceId.toString(), classNameIdJournalArticle, serviceContext);
				// add link for new bookmark
				util.addLink(linkedResourceId.toString(), entryId.toString(), classNameId, serviceContext);
				res.sendRedirect(redirect);
			}
			res.sendRedirect(redirect);

		} catch (Exception e) {
			if (e instanceof NoSuchEntryException || e instanceof PrincipalException) {
				SessionErrors.add(req, e.getClass().getName());
				setForward(req, "portlet.bookmarks.error");

			} else if (e instanceof EntryURLException || e instanceof NoSuchFolderException) {
				SessionErrors.add(req, e.getClass().getName());
				setForward(req, "portlet.ext.asset_by_ratio.view_link");

			} else if (e instanceof TagsEntryException) {
				SessionErrors.add(req, e.getClass().getName(), e);

			} else {
				throw e;
			}
		}

	}
}
