package com.ext.portlet.bookreports.service;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Isolation;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;


/**
 * <a href="MetadataEntryLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * {@link
 * com.ext.portlet.bookreports.service.impl.MetadataEntryLocalServiceImpl}}.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       MetadataEntryLocalServiceUtil
 * @generated
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
    PortalException.class, SystemException.class}
)
public interface MetadataEntryLocalService {
    public com.ext.portlet.bookreports.model.MetadataEntry addMetadataEntry(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry createMetadataEntry(
        java.lang.Long entryId);

    public void deleteMetadataEntry(java.lang.Long entryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public void deleteMetadataEntry(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.ext.portlet.bookreports.model.MetadataEntry getMetadataEntry(
        java.lang.Long entryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> getMetadataEntries(
        int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getMetadataEntriesCount()
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry updateMetadataEntry(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry updateMetadataEntry(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry,
        boolean merge) throws com.liferay.portal.SystemException;
}
