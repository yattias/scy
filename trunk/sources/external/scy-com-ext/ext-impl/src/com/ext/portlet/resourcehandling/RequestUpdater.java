package com.ext.portlet.resourcehandling;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.ResourceViewInterface;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.wiki.model.WikiPage;

/**
 * The requestUpdater get all needed informations to show user entered className type
 * to browse intern resources. The liferay standard resources has visitor classes
 * to get the informations. The own resource types must implement the
 * ResourceViewInterface.
 * 
 * @see ResourceViewInterface
 * 
 * @author Daniel
 * 
 */
public class RequestUpdater {
	private static RequestUpdater SINGLETON = new RequestUpdater();

	public static RequestUpdater getResourceAdapterBuilder() {
		return SINGLETON;
	}

	public void updateResourceView(ActionRequest req) throws SystemException, PortalException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		String redirect = req.getParameter("redirect");
		String className = req.getParameter("sType");

		if (req.getParameter("sType") != null) {

			try {
				if (ResourceViewInterface.class.isAssignableFrom(Class.forName(className))) {
					String implClassName = getImplClass(className);
					ResourceViewInterface resourceViewInterface = (ResourceViewInterface) ((ResourceViewInterface) Class.forName(implClassName).newInstance());
					resourceViewInterface.getFolderView(req);
					resourceViewInterface.getPreview(req);
				} else {

					if (className.equals(BlogsEntry.class.getName())) {
						new BlogsEntryVisitor(req);
					} else if (className.equals(IGImage.class.getName())) {
						new IGImageVisitor((req));
					} else if (className.equals(DLFileEntry.class.getName())) {
						new DLFileVisitor((req));
					} else if (className.equals(MBMessage.class.getName())) {
						new MBMessageVisitor((req));
					} else if (className.equals(WikiPage.class.getName())) {
						new WikiPageVisitor((req));
					} else if (className.equals(JournalArticle.class.getName())) {
						new JournaArticleVisitor(req);
					} else if (className.equals(BookmarksEntry.class.getName())) {
						new BookmarksEntryVisitor(req);
					}
					req.setAttribute("sType", className);

				}
			} catch (ClassNotFoundException e) {
				System.out.println("Maybe the impl from model interface is not like code conention exacts");
				e.printStackTrace();
			}
		}
		req.setAttribute("redirect", redirect);
	}

	/**
	 * Build impl class name from interface for model. Only works if impl class
	 * is build like code convention exacts.
	 * 
	 * @param className
	 *            class name from interface for model
	 * @return impl class for model
	 * @throws ClassNotFoundException
	 */
	private String getImplClass(String className) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(className);
		StringBuilder sb = new StringBuilder();
		sb.append(clazz.getPackage().getName());
		sb.append(".impl.");
		sb.append(clazz.getSimpleName());
		sb.append("Impl");
		return sb.toString();
	}
}
