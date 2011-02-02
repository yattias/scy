package com.ext.portlet.bookreports.service.base;

import com.ext.portlet.bookreports.model.BookReportsEntry;
import com.ext.portlet.bookreports.service.BookReportsEntryLocalService;
import com.ext.portlet.bookreports.service.BookReportsEntryService;
import com.ext.portlet.bookreports.service.persistence.BookReportsEntryPersistence;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.util.PortalUtil;

import java.util.List;


public abstract class BookReportsEntryLocalServiceBaseImpl
    implements BookReportsEntryLocalService {
    @BeanReference(name = "com.ext.portlet.bookreports.service.BookReportsEntryLocalService.impl")
    protected BookReportsEntryLocalService bookReportsEntryLocalService;
    @BeanReference(name = "com.ext.portlet.bookreports.service.BookReportsEntryService.impl")
    protected BookReportsEntryService bookReportsEntryService;
    @BeanReference(name = "com.ext.portlet.bookreports.service.persistence.BookReportsEntryPersistence.impl")
    protected BookReportsEntryPersistence bookReportsEntryPersistence;

    public BookReportsEntry addBookReportsEntry(
        BookReportsEntry bookReportsEntry) throws SystemException {
        bookReportsEntry.setNew(true);

        return bookReportsEntryPersistence.update(bookReportsEntry, false);
    }

    public BookReportsEntry createBookReportsEntry(Long entryId) {
        return bookReportsEntryPersistence.create(entryId);
    }

    public void deleteBookReportsEntry(Long entryId)
        throws PortalException, SystemException {
        bookReportsEntryPersistence.remove(entryId);
    }

    public void deleteBookReportsEntry(BookReportsEntry bookReportsEntry)
        throws SystemException {
        bookReportsEntryPersistence.remove(bookReportsEntry);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery)
        throws SystemException {
        return bookReportsEntryPersistence.findWithDynamicQuery(dynamicQuery);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery, int start,
        int end) throws SystemException {
        return bookReportsEntryPersistence.findWithDynamicQuery(dynamicQuery,
            start, end);
    }

    public BookReportsEntry getBookReportsEntry(Long entryId)
        throws PortalException, SystemException {
        return bookReportsEntryPersistence.findByPrimaryKey(entryId);
    }

    public List<BookReportsEntry> getBookReportsEntries(int start, int end)
        throws SystemException {
        return bookReportsEntryPersistence.findAll(start, end);
    }

    public int getBookReportsEntriesCount() throws SystemException {
        return bookReportsEntryPersistence.countAll();
    }

    public BookReportsEntry updateBookReportsEntry(
        BookReportsEntry bookReportsEntry) throws SystemException {
        bookReportsEntry.setNew(false);

        return bookReportsEntryPersistence.update(bookReportsEntry, true);
    }

    public BookReportsEntry updateBookReportsEntry(
        BookReportsEntry bookReportsEntry, boolean merge)
        throws SystemException {
        bookReportsEntry.setNew(false);

        return bookReportsEntryPersistence.update(bookReportsEntry, merge);
    }

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
