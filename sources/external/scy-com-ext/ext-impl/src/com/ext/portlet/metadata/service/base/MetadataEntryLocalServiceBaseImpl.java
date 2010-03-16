package com.ext.portlet.metadata.service.base;

import com.ext.portlet.metadata.model.MetadataEntry;
import com.ext.portlet.metadata.service.MetadataEntryLocalService;
import com.ext.portlet.metadata.service.MetadataEntryService;
import com.ext.portlet.metadata.service.persistence.MetadataEntryPersistence;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.util.PortalUtil;

import java.util.List;


public abstract class MetadataEntryLocalServiceBaseImpl
    implements MetadataEntryLocalService {
    @BeanReference(name = "com.ext.portlet.metadata.service.MetadataEntryLocalService.impl")
    protected MetadataEntryLocalService metadataEntryLocalService;
    @BeanReference(name = "com.ext.portlet.metadata.service.MetadataEntryService.impl")
    protected MetadataEntryService metadataEntryService;
    @BeanReference(name = "com.ext.portlet.metadata.service.persistence.MetadataEntryPersistence.impl")
    protected MetadataEntryPersistence metadataEntryPersistence;

    public MetadataEntry addMetadataEntry(MetadataEntry metadataEntry)
        throws SystemException {
        metadataEntry.setNew(true);

        return metadataEntryPersistence.update(metadataEntry, false);
    }

    public MetadataEntry createMetadataEntry(Long entryId) {
        return metadataEntryPersistence.create(entryId);
    }

    public void deleteMetadataEntry(Long entryId)
        throws PortalException, SystemException {
        metadataEntryPersistence.remove(entryId);
    }

    public void deleteMetadataEntry(MetadataEntry metadataEntry)
        throws SystemException {
        metadataEntryPersistence.remove(metadataEntry);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery)
        throws SystemException {
        return metadataEntryPersistence.findWithDynamicQuery(dynamicQuery);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery, int start,
        int end) throws SystemException {
        return metadataEntryPersistence.findWithDynamicQuery(dynamicQuery,
            start, end);
    }

    public MetadataEntry getMetadataEntry(Long entryId)
        throws PortalException, SystemException {
        return metadataEntryPersistence.findByPrimaryKey(entryId);
    }

    public List<MetadataEntry> getMetadataEntries(int start, int end)
        throws SystemException {
        return metadataEntryPersistence.findAll(start, end);
    }

    public int getMetadataEntriesCount() throws SystemException {
        return metadataEntryPersistence.countAll();
    }

    public MetadataEntry updateMetadataEntry(MetadataEntry metadataEntry)
        throws SystemException {
        metadataEntry.setNew(false);

        return metadataEntryPersistence.update(metadataEntry, true);
    }

    public MetadataEntry updateMetadataEntry(MetadataEntry metadataEntry,
        boolean merge) throws SystemException {
        metadataEntry.setNew(false);

        return metadataEntryPersistence.update(metadataEntry, merge);
    }

    public MetadataEntryLocalService getMetadataEntryLocalService() {
        return metadataEntryLocalService;
    }

    public void setMetadataEntryLocalService(
        MetadataEntryLocalService metadataEntryLocalService) {
        this.metadataEntryLocalService = metadataEntryLocalService;
    }

    public MetadataEntryService getMetadataEntryService() {
        return metadataEntryService;
    }

    public void setMetadataEntryService(
        MetadataEntryService metadataEntryService) {
        this.metadataEntryService = metadataEntryService;
    }

    public MetadataEntryPersistence getMetadataEntryPersistence() {
        return metadataEntryPersistence;
    }

    public void setMetadataEntryPersistence(
        MetadataEntryPersistence metadataEntryPersistence) {
        this.metadataEntryPersistence = metadataEntryPersistence;
    }

    protected void runSQL(String sql) throws SystemException {
        try {
            PortalUtil.runSQL(sql);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}
