package com.ext.portlet.bookreports.service;


/**
 * <a href="BookReportsEntryLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.ext.portlet.bookreports.service.BookReportsEntryLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.bookreports.service.BookReportsEntryLocalService
 *
 */
public class BookReportsEntryLocalServiceUtil {
    private static BookReportsEntryLocalService _service;

    public static com.ext.portlet.bookreports.model.BookReportsEntry addBookReportsEntry(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry)
        throws com.liferay.portal.SystemException {
        return getService().addBookReportsEntry(bookReportsEntry);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry createBookReportsEntry(
        java.lang.Long entryId) {
        return getService().createBookReportsEntry(entryId);
    }

    public static void deleteBookReportsEntry(java.lang.Long entryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService().deleteBookReportsEntry(entryId);
    }

    public static void deleteBookReportsEntry(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry)
        throws com.liferay.portal.SystemException {
        getService().deleteBookReportsEntry(bookReportsEntry);
    }

    public static java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery, start, end);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry getBookReportsEntry(
        java.lang.Long entryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService().getBookReportsEntry(entryId);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> getBookReportsEntries(
        int start, int end) throws com.liferay.portal.SystemException {
        return getService().getBookReportsEntries(start, end);
    }

    public static int getBookReportsEntriesCount()
        throws com.liferay.portal.SystemException {
        return getService().getBookReportsEntriesCount();
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry updateBookReportsEntry(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry)
        throws com.liferay.portal.SystemException {
        return getService().updateBookReportsEntry(bookReportsEntry);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry updateBookReportsEntry(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getService().updateBookReportsEntry(bookReportsEntry, merge);
    }

    public static BookReportsEntryLocalService getService() {
        if (_service == null) {
            throw new RuntimeException(
                "BookReportsEntryLocalService is not set");
        }

        return _service;
    }

    public void setService(BookReportsEntryLocalService service) {
        _service = service;
    }
}
