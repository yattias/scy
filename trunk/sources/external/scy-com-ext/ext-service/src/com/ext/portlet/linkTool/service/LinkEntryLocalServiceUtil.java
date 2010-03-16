package com.ext.portlet.linkTool.service;

/**
 * <a href="LinkEntryLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 * 
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 * 
 * <p>
 * This class provides static methods for the {@link LinkEntryLocalService}
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 * 
 * @author Brian Wing Shun Chan
 * @see LinkEntryLocalService
 * @generated
 */
public class LinkEntryLocalServiceUtil {
	private static LinkEntryLocalService _service;

	public static com.ext.portlet.linkTool.model.LinkEntry addLinkEntry(com.ext.portlet.linkTool.model.LinkEntry linkEntry)
			throws com.liferay.portal.SystemException {
		return getService().addLinkEntry(linkEntry);
	}

	public static com.ext.portlet.linkTool.model.LinkEntry createLinkEntry(long linkId) {
		return getService().createLinkEntry(linkId);
	}

	public static void deleteLinkEntry(long linkId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		getService().deleteLinkEntry(linkId);
	}

	public static void deleteLinkEntry(com.ext.portlet.linkTool.model.LinkEntry linkEntry) throws com.liferay.portal.SystemException {
		getService().deleteLinkEntry(linkEntry);
	}

	public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException {
		return getService().dynamicQuery(dynamicQuery);
	}

	public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end)
			throws com.liferay.portal.SystemException {
		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	public static com.ext.portlet.linkTool.model.LinkEntry getLinkEntry(long linkId) throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		return getService().getLinkEntry(linkId);
	}

	public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> getLinkEntries(int start, int end) throws com.liferay.portal.SystemException {
		return getService().getLinkEntries(start, end);
	}
	
	public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> getLinkEntriesByResourceId(long resourceId) throws com.liferay.portal.SystemException {
		return getService().getLinkEntriesByResourceId(resourceId);
	}
	
	public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> getLinkEntriesByLinkedResourceId(long linkedResourceId) throws com.liferay.portal.SystemException {
		return getService().getLinkEntriesByLinkedResourceId(linkedResourceId);
	}

	public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> getLinkEntries() throws com.liferay.portal.SystemException {
		return getService().getLinkEntries();
	}

	public static int getLinkEntriesCount() throws com.liferay.portal.SystemException {
		return getService().getLinkEntriesCount();
	}

	public static com.ext.portlet.linkTool.model.LinkEntry updateLinkEntry(com.ext.portlet.linkTool.model.LinkEntry linkEntry)
			throws com.liferay.portal.SystemException {
		return getService().updateLinkEntry(linkEntry);
	}

	public static com.ext.portlet.linkTool.model.LinkEntry updateLinkEntry(com.ext.portlet.linkTool.model.LinkEntry linkEntry, boolean merge)
			throws com.liferay.portal.SystemException {
		return getService().updateLinkEntry(linkEntry, merge);
	}

	public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> getLinkEntry(java.lang.String resourceId, java.lang.String linkedResourceId)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
		return getService().getLinkEntry(resourceId, linkedResourceId);
	}

	public static LinkEntryLocalService getService() {
		if (_service == null) {
			throw new RuntimeException("LinkEntryLocalService is not set");
		}

		return _service;
	}

	public void setService(LinkEntryLocalService service) {
		_service = service;
	}
}
