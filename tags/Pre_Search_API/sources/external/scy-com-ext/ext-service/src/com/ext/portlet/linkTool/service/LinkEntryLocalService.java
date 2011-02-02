package com.ext.portlet.linkTool.service;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Isolation;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;

/**
 * <a href="LinkEntryLocalService.java.html"><b><i>View Source</i></b></a>
 * 
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 * 
 * <p>
 * This interface defines the service. The default implementation is
 * {@link com.ext.portlet.linkTool.service.impl.LinkEntryLocalServiceImpl} .
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 * 
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 * 
 * @author Brian Wing Shun Chan
 * @see LinkEntryLocalServiceUtil
 * @generated
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor = { PortalException.class, SystemException.class })
public interface LinkEntryLocalService {
	public com.ext.portlet.linkTool.model.LinkEntry addLinkEntry(com.ext.portlet.linkTool.model.LinkEntry linkEntry) throws com.liferay.portal.SystemException;

	public com.ext.portlet.linkTool.model.LinkEntry createLinkEntry(long linkId);

	public void deleteLinkEntry(long linkId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	public void deleteLinkEntry(com.ext.portlet.linkTool.model.LinkEntry linkEntry) throws com.liferay.portal.SystemException;

	public java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException;

	public java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end)
			throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public com.ext.portlet.linkTool.model.LinkEntry getLinkEntry(long linkId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> getLinkEntries(int start, int end) throws com.liferay.portal.SystemException;
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> getLinkEntriesByResourceId(long resourceId) throws com.liferay.portal.SystemException;
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> getLinkEntriesByLinkedResourceId(long linkedResourceId) throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> getLinkEntries() throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getLinkEntriesCount() throws com.liferay.portal.SystemException;

	public com.ext.portlet.linkTool.model.LinkEntry updateLinkEntry(com.ext.portlet.linkTool.model.LinkEntry linkEntry)
			throws com.liferay.portal.SystemException;

	public com.ext.portlet.linkTool.model.LinkEntry updateLinkEntry(com.ext.portlet.linkTool.model.LinkEntry linkEntry, boolean merge)
			throws com.liferay.portal.SystemException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> getLinkEntry(java.lang.String resourceId, java.lang.String linkedResourceId)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;
}
