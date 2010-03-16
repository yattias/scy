package com.ext.portlet.bookreports.service.base;

import com.ext.portlet.bookreports.service.BookReportsEntryLocalService;
import com.ext.portlet.bookreports.service.BookReportsEntryService;
import com.ext.portlet.bookreports.service.persistence.BookReportsEntryPersistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.service.base.PrincipalBean;
import com.liferay.portal.util.PortalUtil;


public abstract class BookReportsEntryServiceBaseImpl extends PrincipalBean
    implements BookReportsEntryService {
    @BeanReference(name = "com.ext.portlet.bookreports.service.BookReportsEntryLocalService.impl")
    protected BookReportsEntryLocalService bookReportsEntryLocalService;
    @BeanReference(name = "com.ext.portlet.bookreports.service.BookReportsEntryService.impl")
    protected BookReportsEntryService bookReportsEntryService;
    @BeanReference(name = "com.ext.portlet.bookreports.service.persistence.BookReportsEntryPersistence.impl")
    protected BookReportsEntryPersistence bookReportsEntryPersistence;

    public BookReportsEntryLocalService getBookReportsEntryLocalService() {
        return bookReportsEntryLocalService;
    }

    public void setBookReportsEntryLocalService(
        BookReportsEntryLocalService bookReportsEntryLocalService) {
        this.bookReportsEntryLocalService = bookReportsEntryLocalService;
    }

    public BookReportsEntryService getBookReportsEntryService() {
        return bookReportsEntryService;
    }

    public void setBookReportsEntryService(
        BookReportsEntryService bookReportsEntryService) {
        this.bookReportsEntryService = bookReportsEntryService;
    }

    public BookReportsEntryPersistence getBookReportsEntryPersistence() {
        return bookReportsEntryPersistence;
    }

    public void setBookReportsEntryPersistence(
        BookReportsEntryPersistence bookReportsEntryPersistence) {
        this.bookReportsEntryPersistence = bookReportsEntryPersistence;
    }

    protected void runSQL(String sql) throws SystemException {
        try {
            PortalUtil.runSQL(sql);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}
