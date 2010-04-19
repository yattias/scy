package com.ext.portlet.linkTool.service.base;

import com.ext.portlet.linkTool.model.LinkEntry;
import com.ext.portlet.linkTool.service.LinkEntryLocalService;
import com.ext.portlet.linkTool.service.LinkEntryService;
import com.ext.portlet.linkTool.service.persistence.LinkEntryPersistence;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.GroupService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.persistence.GroupPersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import java.util.List;


public abstract class LinkEntryLocalServiceBaseImpl
    implements LinkEntryLocalService {
    @BeanReference(name = "com.ext.portlet.linkTool.service.LinkEntryLocalService.impl")
    protected LinkEntryLocalService linkEntryLocalService;
    @BeanReference(name = "com.ext.portlet.linkTool.service.LinkEntryService.impl")
    protected LinkEntryService linkEntryService;
    @BeanReference(name = "com.ext.portlet.linkTool.service.persistence.LinkEntryPersistence.impl")
    protected LinkEntryPersistence linkEntryPersistence;
    @BeanReference(name = "com.liferay.portal.service.GroupLocalService.impl")
    protected GroupLocalService groupLocalService;
    @BeanReference(name = "com.liferay.portal.service.GroupService.impl")
    protected GroupService groupService;
    @BeanReference(name = "com.liferay.portal.service.persistence.GroupPersistence.impl")
    protected GroupPersistence groupPersistence;
    @BeanReference(name = "com.liferay.portal.service.UserLocalService.impl")
    protected UserLocalService userLocalService;
    @BeanReference(name = "com.liferay.portal.service.UserService.impl")
    protected UserService userService;
    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected UserPersistence userPersistence;

    public LinkEntry addLinkEntry(LinkEntry linkEntry)
        throws SystemException {
        linkEntry.setNew(true);

        return linkEntryPersistence.update(linkEntry, false);
    }

    public LinkEntry createLinkEntry(long linkId) {
        return linkEntryPersistence.create(linkId);
    }

    public void deleteLinkEntry(long linkId)
        throws PortalException, SystemException {
        linkEntryPersistence.remove(linkId);
    }

    public void deleteLinkEntry(LinkEntry linkEntry) throws SystemException {
        linkEntryPersistence.remove(linkEntry);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery)
        throws SystemException {
        return linkEntryPersistence.findWithDynamicQuery(dynamicQuery);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery, int start,
        int end) throws SystemException {
        return linkEntryPersistence.findWithDynamicQuery(dynamicQuery, start,
            end);
    }

    public LinkEntry getLinkEntry(long linkId)
        throws PortalException, SystemException {
        return linkEntryPersistence.findByPrimaryKey(linkId);
    }

    public List<LinkEntry> getLinkEntries(int start, int end)
        throws SystemException {
        return linkEntryPersistence.findAll(start, end);
    }

    public int getLinkEntriesCount() throws SystemException {
        return linkEntryPersistence.countAll();
    }

    public LinkEntry updateLinkEntry(LinkEntry linkEntry)
        throws SystemException {
        linkEntry.setNew(false);

        return linkEntryPersistence.update(linkEntry, true);
    }

    public LinkEntry updateLinkEntry(LinkEntry linkEntry, boolean merge)
        throws SystemException {
        linkEntry.setNew(false);

        return linkEntryPersistence.update(linkEntry, merge);
    }

    public LinkEntryLocalService getLinkEntryLocalService() {
        return linkEntryLocalService;
    }

    public void setLinkEntryLocalService(
        LinkEntryLocalService linkEntryLocalService) {
        this.linkEntryLocalService = linkEntryLocalService;
    }

    public LinkEntryService getLinkEntryService() {
        return linkEntryService;
    }

    public void setLinkEntryService(LinkEntryService linkEntryService) {
        this.linkEntryService = linkEntryService;
    }

    public LinkEntryPersistence getLinkEntryPersistence() {
        return linkEntryPersistence;
    }

    public void setLinkEntryPersistence(
        LinkEntryPersistence linkEntryPersistence) {
        this.linkEntryPersistence = linkEntryPersistence;
    }

    public GroupLocalService getGroupLocalService() {
        return groupLocalService;
    }

    public void setGroupLocalService(GroupLocalService groupLocalService) {
        this.groupLocalService = groupLocalService;
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public GroupPersistence getGroupPersistence() {
        return groupPersistence;
    }

    public void setGroupPersistence(GroupPersistence groupPersistence) {
        this.groupPersistence = groupPersistence;
    }

    public UserLocalService getUserLocalService() {
        return userLocalService;
    }

    public void setUserLocalService(UserLocalService userLocalService) {
        this.userLocalService = userLocalService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserPersistence getUserPersistence() {
        return userPersistence;
    }

    public void setUserPersistence(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    protected void runSQL(String sql) throws SystemException {
        try {
            PortalUtil.runSQL(sql);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}
