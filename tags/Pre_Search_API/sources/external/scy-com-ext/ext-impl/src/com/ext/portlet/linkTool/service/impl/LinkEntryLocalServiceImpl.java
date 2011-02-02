package com.ext.portlet.linkTool.service.impl;

import java.util.List;

import com.ext.portlet.linkTool.model.LinkEntry;
import com.ext.portlet.linkTool.service.base.LinkEntryLocalServiceBaseImpl;
import com.ext.portlet.linkTool.service.persistence.LinkEntryPersistence;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;

public class LinkEntryLocalServiceImpl extends LinkEntryLocalServiceBaseImpl {

	@BeanReference(name = "com.ext.portlet.linkTool.service.persistence.LinkEntryPersistence.impl")
	protected LinkEntryPersistence linkEntryPersistence;

	@Override
	public List<LinkEntry> getLinkEntry(String resourceId, String linkedResourceId) throws PortalException, SystemException {

		return linkEntryPersistence.findByR_L(resourceId, linkedResourceId);
	}

	@Override
	public List<LinkEntry> getLinkEntries() throws SystemException {
		return linkEntryPersistence.findAll();
	}

	@Override
	public List<LinkEntry> getLinkEntriesByResourceId(long resourceId) throws SystemException {
		return linkEntryPersistence.findByResourceId(String.valueOf(resourceId));
	}
	
	@Override
	public List<LinkEntry> getLinkEntriesByLinkedResourceId(long linkedResourceId) throws SystemException {
		return linkEntryPersistence.findByLinkedResourceId(String.valueOf(linkedResourceId));
	}

}
