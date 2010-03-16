package com.ext.portlet.metadata.service.base;

import com.ext.portlet.metadata.service.MetadataEntryLocalService;
import com.ext.portlet.metadata.service.MetadataEntryService;
import com.ext.portlet.metadata.service.persistence.MetadataEntryPersistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.service.base.PrincipalBean;
import com.liferay.portal.util.PortalUtil;


public abstract class MetadataEntryServiceBaseImpl extends PrincipalBean
    implements MetadataEntryService {
    @BeanReference(name = "com.ext.portlet.metadata.service.MetadataEntryLocalService.impl")
    protected MetadataEntryLocalService metadataEntryLocalService;
    @BeanReference(name = "com.ext.portlet.metadata.service.MetadataEntryService.impl")
    protected MetadataEntryService metadataEntryService;
    @BeanReference(name = "com.ext.portlet.metadata.service.persistence.MetadataEntryPersistence.impl")
    protected MetadataEntryPersistence metadataEntryPersistence;

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
