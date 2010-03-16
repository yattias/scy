package com.ext.portlet.bookreports.service;


/**
 * <a href="MetadataEntryLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * {@link MetadataEntryLocalService} bean. The static methods of
 * this class calls the same methods of the bean instance. It's convenient to be
 * able to just write one line to call a method on a bean instead of writing a
 * lookup call and a method call.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       MetadataEntryLocalService
 * @generated
 */
public class MetadataEntryLocalServiceUtil {
    private static MetadataEntryLocalService _service;

    public static com.ext.portlet.bookreports.model.MetadataEntry addMetadataEntry(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException {
        return getService().addMetadataEntry(metadataEntry);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry createMetadataEntry(
        java.lang.Long entryId) {
        return getService().createMetadataEntry(entryId);
    }

    public static void deleteMetadataEntry(java.lang.Long entryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService().deleteMetadataEntry(entryId);
    }

    public static void deleteMetadataEntry(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException {
        getService().deleteMetadataEntry(metadataEntry);
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

    public static com.ext.portlet.bookreports.model.MetadataEntry getMetadataEntry(
        java.lang.Long entryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService().getMetadataEntry(entryId);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> getMetadataEntries(
        int start, int end) throws com.liferay.portal.SystemException {
        return getService().getMetadataEntries(start, end);
    }

    public static int getMetadataEntriesCount()
        throws com.liferay.portal.SystemException {
        return getService().getMetadataEntriesCount();
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry updateMetadataEntry(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException {
        return getService().updateMetadataEntry(metadataEntry);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry updateMetadataEntry(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getService().updateMetadataEntry(metadataEntry, merge);
    }

    public static MetadataEntryLocalService getService() {
        if (_service == null) {
            throw new RuntimeException("MetadataEntryLocalService is not set");
        }

        return _service;
    }

    public void setService(MetadataEntryLocalService service) {
        _service = service;
    }
}
