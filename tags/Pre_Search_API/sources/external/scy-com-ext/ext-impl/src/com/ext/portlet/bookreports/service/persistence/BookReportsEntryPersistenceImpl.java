package com.ext.portlet.bookreports.service.persistence;

import com.ext.portlet.bookreports.NoSuchEntryException;
import com.ext.portlet.bookreports.model.BookReportsEntry;
import com.ext.portlet.bookreports.model.impl.BookReportsEntryImpl;
import com.ext.portlet.bookreports.model.impl.BookReportsEntryModelImpl;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.kernel.cache.CacheRegistry;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BookReportsEntryPersistenceImpl extends BasePersistenceImpl
    implements BookReportsEntryPersistence {
    public static final String FINDER_CLASS_NAME_ENTITY = BookReportsEntryImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST = FINDER_CLASS_NAME_ENTITY +
        ".List";
    public static final FinderPath FINDER_PATH_FIND_BY_UUID = new FinderPath(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
            BookReportsEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByUuid",
            new String[] { String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_UUID = new FinderPath(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
            BookReportsEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByUuid",
            new String[] {
                String.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_UUID = new FinderPath(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
            BookReportsEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByUuid",
            new String[] { String.class.getName() });
    public static final FinderPath FINDER_PATH_FETCH_BY_UUID_G = new FinderPath(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
            BookReportsEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G",
            new String[] { String.class.getName(), Long.class.getName() });
    public static final FinderPath FINDER_PATH_COUNT_BY_UUID_G = new FinderPath(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
            BookReportsEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByUUID_G",
            new String[] { String.class.getName(), Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_ALL = new FinderPath(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
            BookReportsEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
            BookReportsEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countAll", new String[0]);
    private static Log _log = LogFactoryUtil.getLog(BookReportsEntryPersistenceImpl.class);
    @BeanReference(name = "com.ext.portlet.bookreports.service.persistence.BookReportsEntryPersistence.impl")
    protected com.ext.portlet.bookreports.service.persistence.BookReportsEntryPersistence bookReportsEntryPersistence;

    public void cacheResult(BookReportsEntry bookReportsEntry) {
        EntityCacheUtil.putResult(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
            BookReportsEntryImpl.class, bookReportsEntry.getPrimaryKey(),
            bookReportsEntry);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
            new Object[] {
                bookReportsEntry.getUuid(),
                
            bookReportsEntry.getGroupId()
            }, bookReportsEntry);
    }

    public void cacheResult(List<BookReportsEntry> bookReportsEntries) {
        for (BookReportsEntry bookReportsEntry : bookReportsEntries) {
            if (EntityCacheUtil.getResult(
                        BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
                        BookReportsEntryImpl.class,
                        bookReportsEntry.getPrimaryKey(), this) == null) {
                cacheResult(bookReportsEntry);
            }
        }
    }

    public void clearCache() {
        CacheRegistry.clear(BookReportsEntryImpl.class.getName());
        EntityCacheUtil.clearCache(BookReportsEntryImpl.class.getName());
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);
    }

    public BookReportsEntry create(Long entryId) {
        BookReportsEntry bookReportsEntry = new BookReportsEntryImpl();

        bookReportsEntry.setNew(true);
        bookReportsEntry.setPrimaryKey(entryId);

        String uuid = PortalUUIDUtil.generate();

        bookReportsEntry.setUuid(uuid);

        return bookReportsEntry;
    }

    public BookReportsEntry remove(Long entryId)
        throws NoSuchEntryException, SystemException {
        Session session = null;

        try {
            session = openSession();

            BookReportsEntry bookReportsEntry = (BookReportsEntry) session.get(BookReportsEntryImpl.class,
                    entryId);

            if (bookReportsEntry == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn(
                        "No BookReportsEntry exists with the primary key " +
                        entryId);
                }

                throw new NoSuchEntryException(
                    "No BookReportsEntry exists with the primary key " +
                    entryId);
            }

            return remove(bookReportsEntry);
        } catch (NoSuchEntryException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public BookReportsEntry remove(BookReportsEntry bookReportsEntry)
        throws SystemException {
        for (ModelListener<BookReportsEntry> listener : listeners) {
            listener.onBeforeRemove(bookReportsEntry);
        }

        bookReportsEntry = removeImpl(bookReportsEntry);

        for (ModelListener<BookReportsEntry> listener : listeners) {
            listener.onAfterRemove(bookReportsEntry);
        }

        return bookReportsEntry;
    }

    protected BookReportsEntry removeImpl(BookReportsEntry bookReportsEntry)
        throws SystemException {
        Session session = null;

        try {
            session = openSession();

            if (bookReportsEntry.isCachedModel() ||
                    BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(BookReportsEntryImpl.class,
                        bookReportsEntry.getPrimaryKeyObj());

                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }

            session.delete(bookReportsEntry);

            session.flush();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        BookReportsEntryModelImpl bookReportsEntryModelImpl = (BookReportsEntryModelImpl) bookReportsEntry;

        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G,
            new Object[] {
                bookReportsEntryModelImpl.getOriginalUuid(),
                
            bookReportsEntryModelImpl.getOriginalGroupId()
            });

        EntityCacheUtil.removeResult(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
            BookReportsEntryImpl.class, bookReportsEntry.getPrimaryKey());

        return bookReportsEntry;
    }

    /**
     * @deprecated Use <code>update(BookReportsEntry bookReportsEntry, boolean merge)</code>.
     */
    public BookReportsEntry update(BookReportsEntry bookReportsEntry)
        throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn(
                "Using the deprecated update(BookReportsEntry bookReportsEntry) method. Use update(BookReportsEntry bookReportsEntry, boolean merge) instead.");
        }

        return update(bookReportsEntry, false);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                bookReportsEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when bookReportsEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public BookReportsEntry update(BookReportsEntry bookReportsEntry,
        boolean merge) throws SystemException {
        boolean isNew = bookReportsEntry.isNew();

        for (ModelListener<BookReportsEntry> listener : listeners) {
            if (isNew) {
                listener.onBeforeCreate(bookReportsEntry);
            } else {
                listener.onBeforeUpdate(bookReportsEntry);
            }
        }

        bookReportsEntry = updateImpl(bookReportsEntry, merge);

        for (ModelListener<BookReportsEntry> listener : listeners) {
            if (isNew) {
                listener.onAfterCreate(bookReportsEntry);
            } else {
                listener.onAfterUpdate(bookReportsEntry);
            }
        }

        return bookReportsEntry;
    }

    public BookReportsEntry updateImpl(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry,
        boolean merge) throws SystemException {
        boolean isNew = bookReportsEntry.isNew();

        BookReportsEntryModelImpl bookReportsEntryModelImpl = (BookReportsEntryModelImpl) bookReportsEntry;

        if (Validator.isNull(bookReportsEntry.getUuid())) {
            String uuid = PortalUUIDUtil.generate();

            bookReportsEntry.setUuid(uuid);
        }

        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.update(session, bookReportsEntry, merge);

            bookReportsEntry.setNew(false);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.putResult(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
            BookReportsEntryImpl.class, bookReportsEntry.getPrimaryKey(),
            bookReportsEntry);

        if (!isNew &&
                (!Validator.equals(bookReportsEntry.getUuid(),
                    bookReportsEntryModelImpl.getOriginalUuid()) ||
                !Validator.equals(bookReportsEntry.getGroupId(),
                    bookReportsEntryModelImpl.getOriginalGroupId()))) {
            FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G,
                new Object[] {
                    bookReportsEntryModelImpl.getOriginalUuid(),
                    
                bookReportsEntryModelImpl.getOriginalGroupId()
                });
        }

        if (isNew ||
                (!Validator.equals(bookReportsEntry.getUuid(),
                    bookReportsEntryModelImpl.getOriginalUuid()) ||
                !Validator.equals(bookReportsEntry.getGroupId(),
                    bookReportsEntryModelImpl.getOriginalGroupId()))) {
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
                new Object[] {
                    bookReportsEntry.getUuid(),
                    
                bookReportsEntry.getGroupId()
                }, bookReportsEntry);
        }

        return bookReportsEntry;
    }

    public BookReportsEntry findByPrimaryKey(Long entryId)
        throws NoSuchEntryException, SystemException {
        BookReportsEntry bookReportsEntry = fetchByPrimaryKey(entryId);

        if (bookReportsEntry == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No BookReportsEntry exists with the primary key " +
                    entryId);
            }

            throw new NoSuchEntryException(
                "No BookReportsEntry exists with the primary key " + entryId);
        }

        return bookReportsEntry;
    }

    public BookReportsEntry fetchByPrimaryKey(Long entryId)
        throws SystemException {
        BookReportsEntry bookReportsEntry = (BookReportsEntry) EntityCacheUtil.getResult(BookReportsEntryModelImpl.ENTITY_CACHE_ENABLED,
                BookReportsEntryImpl.class, entryId, this);

        if (bookReportsEntry == null) {
            Session session = null;

            try {
                session = openSession();

                bookReportsEntry = (BookReportsEntry) session.get(BookReportsEntryImpl.class,
                        entryId);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (bookReportsEntry != null) {
                    cacheResult(bookReportsEntry);
                }

                closeSession(session);
            }
        }

        return bookReportsEntry;
    }

    public List<BookReportsEntry> findByUuid(String uuid)
        throws SystemException {
        Object[] finderArgs = new Object[] { uuid };

        List<BookReportsEntry> list = (List<BookReportsEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_UUID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.bookreports.model.BookReportsEntry WHERE ");

                if (uuid == null) {
                    query.append("uuid_ IS NULL");
                } else {
                    query.append("uuid_ = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("title ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (uuid != null) {
                    qPos.add(uuid);
                }

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<BookReportsEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_UUID, finderArgs,
                    list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<BookReportsEntry> findByUuid(String uuid, int start, int end)
        throws SystemException {
        return findByUuid(uuid, start, end, null);
    }

    public List<BookReportsEntry> findByUuid(String uuid, int start, int end,
        OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                uuid,
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<BookReportsEntry> list = (List<BookReportsEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_UUID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.bookreports.model.BookReportsEntry WHERE ");

                if (uuid == null) {
                    query.append("uuid_ IS NULL");
                } else {
                    query.append("uuid_ = ?");
                }

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("title ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (uuid != null) {
                    qPos.add(uuid);
                }

                list = (List<BookReportsEntry>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<BookReportsEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_UUID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public BookReportsEntry findByUuid_First(String uuid, OrderByComparator obc)
        throws NoSuchEntryException, SystemException {
        List<BookReportsEntry> list = findByUuid(uuid, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No BookReportsEntry exists with the key {");

            msg.append("uuid=" + uuid);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public BookReportsEntry findByUuid_Last(String uuid, OrderByComparator obc)
        throws NoSuchEntryException, SystemException {
        int count = countByUuid(uuid);

        List<BookReportsEntry> list = findByUuid(uuid, count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No BookReportsEntry exists with the key {");

            msg.append("uuid=" + uuid);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public BookReportsEntry[] findByUuid_PrevAndNext(Long entryId, String uuid,
        OrderByComparator obc) throws NoSuchEntryException, SystemException {
        BookReportsEntry bookReportsEntry = findByPrimaryKey(entryId);

        int count = countByUuid(uuid);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.bookreports.model.BookReportsEntry WHERE ");

            if (uuid == null) {
                query.append("uuid_ IS NULL");
            } else {
                query.append("uuid_ = ?");
            }

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("title ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            if (uuid != null) {
                qPos.add(uuid);
            }

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    bookReportsEntry);

            BookReportsEntry[] array = new BookReportsEntryImpl[3];

            array[0] = (BookReportsEntry) objArray[0];
            array[1] = (BookReportsEntry) objArray[1];
            array[2] = (BookReportsEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public BookReportsEntry findByUUID_G(String uuid, Long groupId)
        throws NoSuchEntryException, SystemException {
        BookReportsEntry bookReportsEntry = fetchByUUID_G(uuid, groupId);

        if (bookReportsEntry == null) {
            StringBuilder msg = new StringBuilder();

            msg.append("No BookReportsEntry exists with the key {");

            msg.append("uuid=" + uuid);

            msg.append(", ");
            msg.append("groupId=" + groupId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchEntryException(msg.toString());
        }

        return bookReportsEntry;
    }

    public BookReportsEntry fetchByUUID_G(String uuid, Long groupId)
        throws SystemException {
        return fetchByUUID_G(uuid, groupId, true);
    }

    public BookReportsEntry fetchByUUID_G(String uuid, Long groupId,
        boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] { uuid, groupId };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_UUID_G,
                    finderArgs, this);
        }

        if (result == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.bookreports.model.BookReportsEntry WHERE ");

                if (uuid == null) {
                    query.append("uuid_ IS NULL");
                } else {
                    query.append("uuid_ = ?");
                }

                query.append(" AND ");

                if (groupId == null) {
                    query.append("groupId IS NULL");
                } else {
                    query.append("groupId = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("title ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (uuid != null) {
                    qPos.add(uuid);
                }

                if (groupId != null) {
                    qPos.add(groupId.longValue());
                }

                List<BookReportsEntry> list = q.list();

                result = list;

                BookReportsEntry bookReportsEntry = null;

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
                        finderArgs, list);
                } else {
                    bookReportsEntry = list.get(0);

                    cacheResult(bookReportsEntry);

                    if ((bookReportsEntry.getUuid() == null) ||
                            !bookReportsEntry.getUuid().equals(uuid) ||
                            (bookReportsEntry.getGroupId() == null) ||
                            !bookReportsEntry.getGroupId().equals(groupId)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
                            finderArgs, bookReportsEntry);
                    }
                }

                return bookReportsEntry;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (result == null) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
                        finderArgs, new ArrayList<BookReportsEntry>());
                }

                closeSession(session);
            }
        } else {
            if (result instanceof List) {
                return null;
            } else {
                return (BookReportsEntry) result;
            }
        }
    }

    public List<Object> findWithDynamicQuery(DynamicQuery dynamicQuery)
        throws SystemException {
        Session session = null;

        try {
            session = openSession();

            dynamicQuery.compile(session);

            return dynamicQuery.list();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<Object> findWithDynamicQuery(DynamicQuery dynamicQuery,
        int start, int end) throws SystemException {
        Session session = null;

        try {
            session = openSession();

            dynamicQuery.setLimit(start, end);

            dynamicQuery.compile(session);

            return dynamicQuery.list();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<BookReportsEntry> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<BookReportsEntry> findAll(int start, int end)
        throws SystemException {
        return findAll(start, end, null);
    }

    public List<BookReportsEntry> findAll(int start, int end,
        OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<BookReportsEntry> list = (List<BookReportsEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_ALL,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.bookreports.model.BookReportsEntry ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("title ASC");
                }

                Query q = session.createQuery(query.toString());

                if (obc == null) {
                    list = (List<BookReportsEntry>) QueryUtil.list(q,
                            getDialect(), start, end, false);

                    Collections.sort(list);
                } else {
                    list = (List<BookReportsEntry>) QueryUtil.list(q,
                            getDialect(), start, end);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<BookReportsEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_ALL, finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public void removeByUuid(String uuid) throws SystemException {
        for (BookReportsEntry bookReportsEntry : findByUuid(uuid)) {
            remove(bookReportsEntry);
        }
    }

    public void removeByUUID_G(String uuid, Long groupId)
        throws NoSuchEntryException, SystemException {
        BookReportsEntry bookReportsEntry = findByUUID_G(uuid, groupId);

        remove(bookReportsEntry);
    }

    public void removeAll() throws SystemException {
        for (BookReportsEntry bookReportsEntry : findAll()) {
            remove(bookReportsEntry);
        }
    }

    public int countByUuid(String uuid) throws SystemException {
        Object[] finderArgs = new Object[] { uuid };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_UUID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.bookreports.model.BookReportsEntry WHERE ");

                if (uuid == null) {
                    query.append("uuid_ IS NULL");
                } else {
                    query.append("uuid_ = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (uuid != null) {
                    qPos.add(uuid);
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_UUID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByUUID_G(String uuid, Long groupId)
        throws SystemException {
        Object[] finderArgs = new Object[] { uuid, groupId };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_UUID_G,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.bookreports.model.BookReportsEntry WHERE ");

                if (uuid == null) {
                    query.append("uuid_ IS NULL");
                } else {
                    query.append("uuid_ = ?");
                }

                query.append(" AND ");

                if (groupId == null) {
                    query.append("groupId IS NULL");
                } else {
                    query.append("groupId = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (uuid != null) {
                    qPos.add(uuid);
                }

                if (groupId != null) {
                    qPos.add(groupId.longValue());
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_UUID_G,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countAll() throws SystemException {
        Object[] finderArgs = new Object[0];

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(
                        "SELECT COUNT(*) FROM com.ext.portlet.bookreports.model.BookReportsEntry");

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL, finderArgs,
                    count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public void afterPropertiesSet() {
        String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
                    com.liferay.portal.util.PropsUtil.get(
                        "value.object.listener.com.ext.portlet.bookreports.model.BookReportsEntry")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<BookReportsEntry>> listenersList = new ArrayList<ModelListener<BookReportsEntry>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<BookReportsEntry>) Class.forName(
                            listenerClassName).newInstance());
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }
}
