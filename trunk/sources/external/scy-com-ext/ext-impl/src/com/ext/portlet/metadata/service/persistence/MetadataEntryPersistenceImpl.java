package com.ext.portlet.metadata.service.persistence;

import com.ext.portlet.metadata.NoSuchEntryException;
import com.ext.portlet.metadata.model.MetadataEntry;
import com.ext.portlet.metadata.model.impl.MetadataEntryImpl;
import com.ext.portlet.metadata.model.impl.MetadataEntryModelImpl;

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
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MetadataEntryPersistenceImpl extends BasePersistenceImpl
    implements MetadataEntryPersistence {
    public static final String FINDER_CLASS_NAME_ENTITY = MetadataEntryImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST = FINDER_CLASS_NAME_ENTITY +
        ".List";
    public static final FinderPath FINDER_PATH_FETCH_BY_ASSERTENTRYID = new FinderPath(MetadataEntryModelImpl.ENTITY_CACHE_ENABLED,
            MetadataEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_ENTITY, "fetchByAssertEntryId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_COUNT_BY_ASSERTENTRYID = new FinderPath(MetadataEntryModelImpl.ENTITY_CACHE_ENABLED,
            MetadataEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByAssertEntryId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_ALL = new FinderPath(MetadataEntryModelImpl.ENTITY_CACHE_ENABLED,
            MetadataEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(MetadataEntryModelImpl.ENTITY_CACHE_ENABLED,
            MetadataEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countAll", new String[0]);
    private static Log _log = LogFactoryUtil.getLog(MetadataEntryPersistenceImpl.class);
    @BeanReference(name = "com.ext.portlet.metadata.service.persistence.MetadataEntryPersistence.impl")
    protected com.ext.portlet.metadata.service.persistence.MetadataEntryPersistence metadataEntryPersistence;

    public void cacheResult(MetadataEntry metadataEntry) {
        EntityCacheUtil.putResult(MetadataEntryModelImpl.ENTITY_CACHE_ENABLED,
            MetadataEntryImpl.class, metadataEntry.getPrimaryKey(),
            metadataEntry);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_ASSERTENTRYID,
            new Object[] { metadataEntry.getAssertEntryId() }, metadataEntry);
    }

    public void cacheResult(List<MetadataEntry> metadataEntries) {
        for (MetadataEntry metadataEntry : metadataEntries) {
            if (EntityCacheUtil.getResult(
                        MetadataEntryModelImpl.ENTITY_CACHE_ENABLED,
                        MetadataEntryImpl.class, metadataEntry.getPrimaryKey(),
                        this) == null) {
                cacheResult(metadataEntry);
            }
        }
    }

    public void clearCache() {
        CacheRegistry.clear(MetadataEntryImpl.class.getName());
        EntityCacheUtil.clearCache(MetadataEntryImpl.class.getName());
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);
    }

    public MetadataEntry create(Long entryId) {
        MetadataEntry metadataEntry = new MetadataEntryImpl();

        metadataEntry.setNew(true);
        metadataEntry.setPrimaryKey(entryId);

        return metadataEntry;
    }

    public MetadataEntry remove(Long entryId)
        throws NoSuchEntryException, SystemException {
        Session session = null;

        try {
            session = openSession();

            MetadataEntry metadataEntry = (MetadataEntry) session.get(MetadataEntryImpl.class,
                    entryId);

            if (metadataEntry == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No MetadataEntry exists with the primary key " +
                        entryId);
                }

                throw new NoSuchEntryException(
                    "No MetadataEntry exists with the primary key " + entryId);
            }

            return remove(metadataEntry);
        } catch (NoSuchEntryException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public MetadataEntry remove(MetadataEntry metadataEntry)
        throws SystemException {
        for (ModelListener<MetadataEntry> listener : listeners) {
            listener.onBeforeRemove(metadataEntry);
        }

        metadataEntry = removeImpl(metadataEntry);

        for (ModelListener<MetadataEntry> listener : listeners) {
            listener.onAfterRemove(metadataEntry);
        }

        return metadataEntry;
    }

    protected MetadataEntry removeImpl(MetadataEntry metadataEntry)
        throws SystemException {
        Session session = null;

        try {
            session = openSession();

            if (metadataEntry.isCachedModel() || BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(MetadataEntryImpl.class,
                        metadataEntry.getPrimaryKeyObj());

                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }

            session.delete(metadataEntry);

            session.flush();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        MetadataEntryModelImpl metadataEntryModelImpl = (MetadataEntryModelImpl) metadataEntry;

        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_ASSERTENTRYID,
            new Object[] { metadataEntryModelImpl.getOriginalAssertEntryId() });

        EntityCacheUtil.removeResult(MetadataEntryModelImpl.ENTITY_CACHE_ENABLED,
            MetadataEntryImpl.class, metadataEntry.getPrimaryKey());

        return metadataEntry;
    }

    /**
     * @deprecated Use <code>update(MetadataEntry metadataEntry, boolean merge)</code>.
     */
    public MetadataEntry update(MetadataEntry metadataEntry)
        throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn(
                "Using the deprecated update(MetadataEntry metadataEntry) method. Use update(MetadataEntry metadataEntry, boolean merge) instead.");
        }

        return update(metadataEntry, false);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                metadataEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when metadataEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public MetadataEntry update(MetadataEntry metadataEntry, boolean merge)
        throws SystemException {
        boolean isNew = metadataEntry.isNew();

        for (ModelListener<MetadataEntry> listener : listeners) {
            if (isNew) {
                listener.onBeforeCreate(metadataEntry);
            } else {
                listener.onBeforeUpdate(metadataEntry);
            }
        }

        metadataEntry = updateImpl(metadataEntry, merge);

        for (ModelListener<MetadataEntry> listener : listeners) {
            if (isNew) {
                listener.onAfterCreate(metadataEntry);
            } else {
                listener.onAfterUpdate(metadataEntry);
            }
        }

        return metadataEntry;
    }

    public MetadataEntry updateImpl(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry,
        boolean merge) throws SystemException {
        boolean isNew = metadataEntry.isNew();

        MetadataEntryModelImpl metadataEntryModelImpl = (MetadataEntryModelImpl) metadataEntry;

        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.update(session, metadataEntry, merge);

            metadataEntry.setNew(false);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.putResult(MetadataEntryModelImpl.ENTITY_CACHE_ENABLED,
            MetadataEntryImpl.class, metadataEntry.getPrimaryKey(),
            metadataEntry);

        if (!isNew &&
                (!Validator.equals(metadataEntry.getAssertEntryId(),
                    metadataEntryModelImpl.getOriginalAssertEntryId()))) {
            FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_ASSERTENTRYID,
                new Object[] { metadataEntryModelImpl.getOriginalAssertEntryId() });
        }

        if (isNew ||
                (!Validator.equals(metadataEntry.getAssertEntryId(),
                    metadataEntryModelImpl.getOriginalAssertEntryId()))) {
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_ASSERTENTRYID,
                new Object[] { metadataEntry.getAssertEntryId() }, metadataEntry);
        }

        return metadataEntry;
    }

    public MetadataEntry findByPrimaryKey(Long entryId)
        throws NoSuchEntryException, SystemException {
        MetadataEntry metadataEntry = fetchByPrimaryKey(entryId);

        if (metadataEntry == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No MetadataEntry exists with the primary key " +
                    entryId);
            }

            throw new NoSuchEntryException(
                "No MetadataEntry exists with the primary key " + entryId);
        }

        return metadataEntry;
    }

    public MetadataEntry fetchByPrimaryKey(Long entryId)
        throws SystemException {
        MetadataEntry metadataEntry = (MetadataEntry) EntityCacheUtil.getResult(MetadataEntryModelImpl.ENTITY_CACHE_ENABLED,
                MetadataEntryImpl.class, entryId, this);

        if (metadataEntry == null) {
            Session session = null;

            try {
                session = openSession();

                metadataEntry = (MetadataEntry) session.get(MetadataEntryImpl.class,
                        entryId);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (metadataEntry != null) {
                    cacheResult(metadataEntry);
                }

                closeSession(session);
            }
        }

        return metadataEntry;
    }

    public MetadataEntry findByAssertEntryId(Long assertEntryId)
        throws NoSuchEntryException, SystemException {
        MetadataEntry metadataEntry = fetchByAssertEntryId(assertEntryId);

        if (metadataEntry == null) {
            StringBuilder msg = new StringBuilder();

            msg.append("No MetadataEntry exists with the key {");

            msg.append("assertEntryId=" + assertEntryId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchEntryException(msg.toString());
        }

        return metadataEntry;
    }

    public MetadataEntry fetchByAssertEntryId(Long assertEntryId)
        throws SystemException {
        return fetchByAssertEntryId(assertEntryId, true);
    }

    public MetadataEntry fetchByAssertEntryId(Long assertEntryId,
        boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] { assertEntryId };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_ASSERTENTRYID,
                    finderArgs, this);
        }

        if (result == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.metadata.model.MetadataEntry WHERE ");

                if (assertEntryId == null) {
                    query.append("assertEntryId IS NULL");
                } else {
                    query.append("assertEntryId = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("dc_title ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (assertEntryId != null) {
                    qPos.add(assertEntryId.longValue());
                }

                List<MetadataEntry> list = q.list();

                result = list;

                MetadataEntry metadataEntry = null;

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_ASSERTENTRYID,
                        finderArgs, list);
                } else {
                    metadataEntry = list.get(0);

                    cacheResult(metadataEntry);

                    if ((metadataEntry.getAssertEntryId() == null) ||
                            !metadataEntry.getAssertEntryId()
                                              .equals(assertEntryId)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_ASSERTENTRYID,
                            finderArgs, metadataEntry);
                    }
                }

                return metadataEntry;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (result == null) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_ASSERTENTRYID,
                        finderArgs, new ArrayList<MetadataEntry>());
                }

                closeSession(session);
            }
        } else {
            if (result instanceof List) {
                return null;
            } else {
                return (MetadataEntry) result;
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

    public List<MetadataEntry> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<MetadataEntry> findAll(int start, int end)
        throws SystemException {
        return findAll(start, end, null);
    }

    public List<MetadataEntry> findAll(int start, int end, OrderByComparator obc)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<MetadataEntry> list = (List<MetadataEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_ALL,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.metadata.model.MetadataEntry ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("dc_title ASC");
                }

                Query q = session.createQuery(query.toString());

                if (obc == null) {
                    list = (List<MetadataEntry>) QueryUtil.list(q,
                            getDialect(), start, end, false);

                    Collections.sort(list);
                } else {
                    list = (List<MetadataEntry>) QueryUtil.list(q,
                            getDialect(), start, end);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<MetadataEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_ALL, finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public void removeByAssertEntryId(Long assertEntryId)
        throws NoSuchEntryException, SystemException {
        MetadataEntry metadataEntry = findByAssertEntryId(assertEntryId);

        remove(metadataEntry);
    }

    public void removeAll() throws SystemException {
        for (MetadataEntry metadataEntry : findAll()) {
            remove(metadataEntry);
        }
    }

    public int countByAssertEntryId(Long assertEntryId)
        throws SystemException {
        Object[] finderArgs = new Object[] { assertEntryId };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_ASSERTENTRYID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.metadata.model.MetadataEntry WHERE ");

                if (assertEntryId == null) {
                    query.append("assertEntryId IS NULL");
                } else {
                    query.append("assertEntryId = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (assertEntryId != null) {
                    qPos.add(assertEntryId.longValue());
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_ASSERTENTRYID,
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
                        "SELECT COUNT(*) FROM com.ext.portlet.metadata.model.MetadataEntry");

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
                        "value.object.listener.com.ext.portlet.metadata.model.MetadataEntry")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<MetadataEntry>> listenersList = new ArrayList<ModelListener<MetadataEntry>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<MetadataEntry>) Class.forName(
                            listenerClassName).newInstance());
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }
}
