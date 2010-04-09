package com.ext.portlet.metadata.service.impl;

import com.ext.portlet.metadata.model.MetadataEntry;
import com.ext.portlet.metadata.service.base.MetadataEntryLocalServiceBaseImpl;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;

public class MetadataEntryLocalServiceImpl extends MetadataEntryLocalServiceBaseImpl {

	@Override
	public MetadataEntry getMetadataEntryByAssetId(Long assetId) throws SystemException, PortalException {
		return metadataEntryPersistence.findByAssertEntryId(assetId);
	}

}
