package com.ext.portlet.freestyler.service.persistence;

import com.ext.portlet.freestyler.NoSuchEntryException;
import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.model.impl.FreestylerEntryImpl;
import com.ext.portlet.freestyler.model.impl.FreestylerEntryModelImpl;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.kernel.cache.CacheRegistry;
import com.liferay.portal.kernel.dao.jdbc.MappingSqlQuery;
import com.liferay.portal.kernel.dao.jdbc.MappingSqlQueryFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.RowMapper;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FreestylerEntryPersistenceImpl extends BasePersistenceImpl
    implements FreestylerEntryPersistence {
    public static final String FINDER_CLASS_NAME_ENTITY = FreestylerEntryImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST = FINDER_CLASS_NAME_ENTITY +
        ".List";
    public static final FinderPath FINDER_PATH_FIND_BY_COMPANYID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByCompanyId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_COMPANYID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByCompanyId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_COMPANYID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByCompanyId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_USERID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByUserId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_USERID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByUserId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_USERID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByUserId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FETCH_BY_FREESTYLERID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_ENTITY, "fetchByFreestylerId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_COUNT_BY_FREESTYLERID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByFreestylerId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_GROUPID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByGroupId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_GROUPID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByGroupId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByGroupId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_ALL = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countAll", new String[0]);
    public static final FinderPath FINDER_PATH_GET_FREESTYLERIMAGES = new FinderPath(com.ext.portlet.freestyler.model.impl.FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED_FREESTYLERENTRY_FREESTYLERIMAGES,
            "FreestylerEntry_FreestylerImages", "getFreestylerImages",
            new String[] {
                Long.class.getName(), "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_GET_FREESTYLERIMAGES_SIZE = new FinderPath(com.ext.portlet.freestyler.model.impl.FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED_FREESTYLERENTRY_FREESTYLERIMAGES,
            "FreestylerEntry_FreestylerImages", "getFreestylerImagesSize",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_CONTAINS_FREESTYLERIMAGE = new FinderPath(com.ext.portlet.freestyler.model.impl.FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryModelImpl.FINDER_CACHE_ENABLED_FREESTYLERENTRY_FREESTYLERIMAGES,
            "FreestylerEntry_FreestylerImages", "containsFreestylerImage",
            new String[] { Long.class.getName(), Long.class.getName() });
    private static final String _SQL_GETFREESTYLERIMAGES = "SELECT {FreestylerImage.*} FROM FreestylerImage INNER JOIN FreestylerEntry_FreestylerImages ON (FreestylerEntry_FreestylerImages.imageId = FreestylerImage.imageId) WHERE (FreestylerEntry_FreestylerImages.freestylerId = ?)";
    private static final String _SQL_GETFREESTYLERIMAGESSIZE = "SELECT COUNT(*) AS COUNT_VALUE FROM FreestylerEntry_FreestylerImages WHERE freestylerId = ?";
    private static final String _SQL_CONTAINSFREESTYLERIMAGE = "SELECT COUNT(*) AS COUNT_VALUE FROM FreestylerEntry_FreestylerImages WHERE freestylerId = ? AND imageId = ?";
    private static Log _log = LogFactoryUtil.getLog(FreestylerEntryPersistenceImpl.class);
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerFolderPersistence.impl")
    protected com.ext.portlet.freestyler.service.persistence.FreestylerFolderPersistence freestylerFolderPersistence;
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerImagePersistence.impl")
    protected com.ext.portlet.freestyler.service.persistence.FreestylerImagePersistence freestylerImagePersistence;
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerEntryPersistence.impl")
    protected com.ext.portlet.freestyler.service.persistence.FreestylerEntryPersistence freestylerEntryPersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.GroupPersistence.impl")
    protected com.liferay.portal.service.persistence.GroupPersistence groupPersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected com.liferay.portal.service.persistence.UserPersistence userPersistence;
    protected ContainsFreestylerImage containsFreestylerImage;
    protected AddFreestylerImage addFreestylerImage;
    protected ClearFreestylerImages clearFreestylerImages;
    protected RemoveFreestylerImage removeFreestylerImage;

    public void cacheResult(FreestylerEntry freestylerEntry) {
        EntityCacheUtil.putResult(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryImpl.class, freestylerEntry.getPrimaryKey(),
            freestylerEntry);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_FREESTYLERID,
            new Object[] { new Long(freestylerEntry.getFreestylerId()) },
            freestylerEntry);
    }

    public void cacheResult(List<FreestylerEntry> freestylerEntries) {
        for (FreestylerEntry freestylerEntry : freestylerEntries) {
            if (EntityCacheUtil.getResult(
                        FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
                        FreestylerEntryImpl.class,
                        freestylerEntry.getPrimaryKey(), this) == null) {
                cacheResult(freestylerEntry);
            }
        }
    }

    public void clearCache() {
        CacheRegistry.clear(FreestylerEntryImpl.class.getName());
        EntityCacheUtil.clearCache(FreestylerEntryImpl.class.getName());
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);
    }

    public FreestylerEntry create(long freestylerId) {
        FreestylerEntry freestylerEntry = new FreestylerEntryImpl();

        freestylerEntry.setNew(true);
        freestylerEntry.setPrimaryKey(freestylerId);

        return freestylerEntry;
    }

    public FreestylerEntry remove(long freestylerId)
        throws NoSuchEntryException, SystemException {
        Session session = null;

        try {
            session = openSession();

            FreestylerEntry freestylerEntry = (FreestylerEntry) session.get(FreestylerEntryImpl.class,
                    new Long(freestylerId));

            if (freestylerEntry == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No FreestylerEntry exists with the primary key " +
                        freestylerId);
                }

                throw new NoSuchEntryException(
                    "No FreestylerEntry exists with the primary key " +
                    freestylerId);
            }

            return remove(freestylerEntry);
        } catch (NoSuchEntryException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public FreestylerEntry remove(FreestylerEntry freestylerEntry)
        throws SystemException {
        for (ModelListener<FreestylerEntry> listener : listeners) {
            listener.onBeforeRemove(freestylerEntry);
        }

        freestylerEntry = removeImpl(freestylerEntry);

        for (ModelListener<FreestylerEntry> listener : listeners) {
            listener.onAfterRemove(freestylerEntry);
        }

        return freestylerEntry;
    }

    protected FreestylerEntry removeImpl(FreestylerEntry freestylerEntry)
        throws SystemException {
        try {
            clearFreestylerImages.clear(freestylerEntry.getPrimaryKey());
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }

        Session session = null;

        try {
            session = openSession();

            if (freestylerEntry.isCachedModel() ||
                    BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(FreestylerEntryImpl.class,
                        freestylerEntry.getPrimaryKeyObj());

                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }

            session.delete(freestylerEntry);

            session.flush();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        FreestylerEntryModelImpl freestylerEntryModelImpl = (FreestylerEntryModelImpl) freestylerEntry;

        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_FREESTYLERID,
            new Object[] {
                new Long(freestylerEntryModelImpl.getOriginalFreestylerId())
            });

        EntityCacheUtil.removeResult(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryImpl.class, freestylerEntry.getPrimaryKey());

        return freestylerEntry;
    }

    /**
     * @deprecated Use <code>update(FreestylerEntry freestylerEntry, boolean merge)</code>.
     */
    public FreestylerEntry update(FreestylerEntry freestylerEntry)
        throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn(
                "Using the deprecated update(FreestylerEntry freestylerEntry) method. Use update(FreestylerEntry freestylerEntry, boolean merge) instead.");
        }

        return update(freestylerEntry, false);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                freestylerEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when freestylerEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public FreestylerEntry update(FreestylerEntry freestylerEntry, boolean merge)
        throws SystemException {
        boolean isNew = freestylerEntry.isNew();

        for (ModelListener<FreestylerEntry> listener : listeners) {
            if (isNew) {
                listener.onBeforeCreate(freestylerEntry);
            } else {
                listener.onBeforeUpdate(freestylerEntry);
            }
        }

        freestylerEntry = updateImpl(freestylerEntry, merge);

        for (ModelListener<FreestylerEntry> listener : listeners) {
            if (isNew) {
                listener.onAfterCreate(freestylerEntry);
            } else {
                listener.onAfterUpdate(freestylerEntry);
            }
        }

        return freestylerEntry;
    }

    public FreestylerEntry updateImpl(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry,
        boolean merge) throws SystemException {
        boolean isNew = freestylerEntry.isNew();

        FreestylerEntryModelImpl freestylerEntryModelImpl = (FreestylerEntryModelImpl) freestylerEntry;

        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.update(session, freestylerEntry, merge);

            freestylerEntry.setNew(false);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.putResult(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerEntryImpl.class, freestylerEntry.getPrimaryKey(),
            freestylerEntry);

        if (!isNew &&
                (freestylerEntry.getFreestylerId() != freestylerEntryModelImpl.getOriginalFreestylerId())) {
            FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_FREESTYLERID,
                new Object[] {
                    new Long(freestylerEntryModelImpl.getOriginalFreestylerId())
                });
        }

        if (isNew ||
                (freestylerEntry.getFreestylerId() != freestylerEntryModelImpl.getOriginalFreestylerId())) {
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_FREESTYLERID,
                new Object[] { new Long(freestylerEntry.getFreestylerId()) },
                freestylerEntry);
        }

        return freestylerEntry;
    }

    public FreestylerEntry findByPrimaryKey(long freestylerId)
        throws NoSuchEntryException, SystemException {
        FreestylerEntry freestylerEntry = fetchByPrimaryKey(freestylerId);

        if (freestylerEntry == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No FreestylerEntry exists with the primary key " +
                    freestylerId);
            }

            throw new NoSuchEntryException(
                "No FreestylerEntry exists with the primary key " +
                freestylerId);
        }

        return freestylerEntry;
    }

    public FreestylerEntry fetchByPrimaryKey(long freestylerId)
        throws SystemException {
        FreestylerEntry freestylerEntry = (FreestylerEntry) EntityCacheUtil.getResult(FreestylerEntryModelImpl.ENTITY_CACHE_ENABLED,
                FreestylerEntryImpl.class, freestylerId, this);

        if (freestylerEntry == null) {
            Session session = null;

            try {
                session = openSession();

                freestylerEntry = (FreestylerEntry) session.get(FreestylerEntryImpl.class,
                        new Long(freestylerId));
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (freestylerEntry != null) {
                    cacheResult(freestylerEntry);
                }

                closeSession(session);
            }
        }

        return freestylerEntry;
    }

    public List<FreestylerEntry> findByCompanyId(long companyId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(companyId) };

        List<FreestylerEntry> list = (List<FreestylerEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_COMPANYID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("companyId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(companyId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_COMPANYID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerEntry> findByCompanyId(long companyId, int start,
        int end) throws SystemException {
        return findByCompanyId(companyId, start, end, null);
    }

    public List<FreestylerEntry> findByCompanyId(long companyId, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(companyId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerEntry> list = (List<FreestylerEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_COMPANYID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("companyId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(companyId);

                list = (List<FreestylerEntry>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_COMPANYID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerEntry findByCompanyId_First(long companyId,
        OrderByComparator obc) throws NoSuchEntryException, SystemException {
        List<FreestylerEntry> list = findByCompanyId(companyId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerEntry exists with the key {");

            msg.append("companyId=" + companyId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerEntry findByCompanyId_Last(long companyId,
        OrderByComparator obc) throws NoSuchEntryException, SystemException {
        int count = countByCompanyId(companyId);

        List<FreestylerEntry> list = findByCompanyId(companyId, count - 1,
                count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerEntry exists with the key {");

            msg.append("companyId=" + companyId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerEntry[] findByCompanyId_PrevAndNext(long freestylerId,
        long companyId, OrderByComparator obc)
        throws NoSuchEntryException, SystemException {
        FreestylerEntry freestylerEntry = findByPrimaryKey(freestylerId);

        int count = countByCompanyId(companyId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

            query.append("companyId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(companyId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerEntry);

            FreestylerEntry[] array = new FreestylerEntryImpl[3];

            array[0] = (FreestylerEntry) objArray[0];
            array[1] = (FreestylerEntry) objArray[1];
            array[2] = (FreestylerEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<FreestylerEntry> findByUserId(long userId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(userId) };

        List<FreestylerEntry> list = (List<FreestylerEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_USERID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("userId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(userId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_USERID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerEntry> findByUserId(long userId, int start, int end)
        throws SystemException {
        return findByUserId(userId, start, end, null);
    }

    public List<FreestylerEntry> findByUserId(long userId, int start, int end,
        OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(userId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerEntry> list = (List<FreestylerEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_USERID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("userId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(userId);

                list = (List<FreestylerEntry>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_USERID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerEntry findByUserId_First(long userId, OrderByComparator obc)
        throws NoSuchEntryException, SystemException {
        List<FreestylerEntry> list = findByUserId(userId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerEntry exists with the key {");

            msg.append("userId=" + userId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerEntry findByUserId_Last(long userId, OrderByComparator obc)
        throws NoSuchEntryException, SystemException {
        int count = countByUserId(userId);

        List<FreestylerEntry> list = findByUserId(userId, count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerEntry exists with the key {");

            msg.append("userId=" + userId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerEntry[] findByUserId_PrevAndNext(long freestylerId,
        long userId, OrderByComparator obc)
        throws NoSuchEntryException, SystemException {
        FreestylerEntry freestylerEntry = findByPrimaryKey(freestylerId);

        int count = countByUserId(userId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

            query.append("userId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(userId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerEntry);

            FreestylerEntry[] array = new FreestylerEntryImpl[3];

            array[0] = (FreestylerEntry) objArray[0];
            array[1] = (FreestylerEntry) objArray[1];
            array[2] = (FreestylerEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public FreestylerEntry findByFreestylerId(long freestylerId)
        throws NoSuchEntryException, SystemException {
        FreestylerEntry freestylerEntry = fetchByFreestylerId(freestylerId);

        if (freestylerEntry == null) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerEntry exists with the key {");

            msg.append("freestylerId=" + freestylerId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchEntryException(msg.toString());
        }

        return freestylerEntry;
    }

    public FreestylerEntry fetchByFreestylerId(long freestylerId)
        throws SystemException {
        return fetchByFreestylerId(freestylerId, true);
    }

    public FreestylerEntry fetchByFreestylerId(long freestylerId,
        boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(freestylerId) };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_FREESTYLERID,
                    finderArgs, this);
        }

        if (result == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("freestylerId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(freestylerId);

                List<FreestylerEntry> list = q.list();

                result = list;

                FreestylerEntry freestylerEntry = null;

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_FREESTYLERID,
                        finderArgs, list);
                } else {
                    freestylerEntry = list.get(0);

                    cacheResult(freestylerEntry);

                    if ((freestylerEntry.getFreestylerId() != freestylerId)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_FREESTYLERID,
                            finderArgs, freestylerEntry);
                    }
                }

                return freestylerEntry;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (result == null) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_FREESTYLERID,
                        finderArgs, new ArrayList<FreestylerEntry>());
                }

                closeSession(session);
            }
        } else {
            if (result instanceof List) {
                return null;
            } else {
                return (FreestylerEntry) result;
            }
        }
    }

    public List<FreestylerEntry> findByGroupId(long groupId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(groupId) };

        List<FreestylerEntry> list = (List<FreestylerEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_GROUPID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("groupId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_GROUPID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerEntry> findByGroupId(long groupId, int start, int end)
        throws SystemException {
        return findByGroupId(groupId, start, end, null);
    }

    public List<FreestylerEntry> findByGroupId(long groupId, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(groupId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerEntry> list = (List<FreestylerEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_GROUPID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("groupId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                list = (List<FreestylerEntry>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_GROUPID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerEntry findByGroupId_First(long groupId,
        OrderByComparator obc) throws NoSuchEntryException, SystemException {
        List<FreestylerEntry> list = findByGroupId(groupId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerEntry exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerEntry findByGroupId_Last(long groupId,
        OrderByComparator obc) throws NoSuchEntryException, SystemException {
        int count = countByGroupId(groupId);

        List<FreestylerEntry> list = findByGroupId(groupId, count - 1, count,
                obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerEntry exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerEntry[] findByGroupId_PrevAndNext(long freestylerId,
        long groupId, OrderByComparator obc)
        throws NoSuchEntryException, SystemException {
        FreestylerEntry freestylerEntry = findByPrimaryKey(freestylerId);

        int count = countByGroupId(groupId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

            query.append("groupId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(groupId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerEntry);

            FreestylerEntry[] array = new FreestylerEntryImpl[3];

            array[0] = (FreestylerEntry) objArray[0];
            array[1] = (FreestylerEntry) objArray[1];
            array[2] = (FreestylerEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
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

    public List<FreestylerEntry> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<FreestylerEntry> findAll(int start, int end)
        throws SystemException {
        return findAll(start, end, null);
    }

    public List<FreestylerEntry> findAll(int start, int end,
        OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerEntry> list = (List<FreestylerEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_ALL,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }

                Query q = session.createQuery(query.toString());

                if (obc == null) {
                    list = (List<FreestylerEntry>) QueryUtil.list(q,
                            getDialect(), start, end, false);

                    Collections.sort(list);
                } else {
                    list = (List<FreestylerEntry>) QueryUtil.list(q,
                            getDialect(), start, end);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_ALL, finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public void removeByCompanyId(long companyId) throws SystemException {
        for (FreestylerEntry freestylerEntry : findByCompanyId(companyId)) {
            remove(freestylerEntry);
        }
    }

    public void removeByUserId(long userId) throws SystemException {
        for (FreestylerEntry freestylerEntry : findByUserId(userId)) {
            remove(freestylerEntry);
        }
    }

    public void removeByFreestylerId(long freestylerId)
        throws NoSuchEntryException, SystemException {
        FreestylerEntry freestylerEntry = findByFreestylerId(freestylerId);

        remove(freestylerEntry);
    }

    public void removeByGroupId(long groupId) throws SystemException {
        for (FreestylerEntry freestylerEntry : findByGroupId(groupId)) {
            remove(freestylerEntry);
        }
    }

    public void removeAll() throws SystemException {
        for (FreestylerEntry freestylerEntry : findAll()) {
            remove(freestylerEntry);
        }
    }

    public int countByCompanyId(long companyId) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(companyId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_COMPANYID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("companyId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(companyId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_COMPANYID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByUserId(long userId) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(userId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_USERID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("userId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(userId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByFreestylerId(long freestylerId) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(freestylerId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_FREESTYLERID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("freestylerId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(freestylerId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_FREESTYLERID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByGroupId(long groupId) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(groupId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_GROUPID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerEntry WHERE ");

                query.append("groupId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_GROUPID,
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
                        "SELECT COUNT(*) FROM com.ext.portlet.freestyler.model.FreestylerEntry");

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

    public List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        long pk) throws SystemException {
        return getFreestylerImages(pk, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
    }

    public List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        long pk, int start, int end) throws SystemException {
        return getFreestylerImages(pk, start, end, null);
    }

    public List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        long pk, int start, int end, OrderByComparator obc)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(pk), String.valueOf(start), String.valueOf(end),
                String.valueOf(obc)
            };

        List<com.ext.portlet.freestyler.model.FreestylerImage> list = (List<com.ext.portlet.freestyler.model.FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_GET_FREESTYLERIMAGES,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder sb = new StringBuilder();

                sb.append(_SQL_GETFREESTYLERIMAGES);

                if (obc != null) {
                    sb.append("ORDER BY ");
                    sb.append(obc.getOrderBy());
                }
                else {
                    sb.append("ORDER BY ");

                    sb.append("FreestylerImage.imageId ASC");
                }

                String sql = sb.toString();

                SQLQuery q = session.createSQLQuery(sql);

                q.addEntity("FreestylerImage",
                    com.ext.portlet.freestyler.model.impl.FreestylerImageImpl.class);

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(pk);

                list = (List<com.ext.portlet.freestyler.model.FreestylerImage>) QueryUtil.list(q,
                        getDialect(), start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<com.ext.portlet.freestyler.model.FreestylerImage>();
                }

                freestylerImagePersistence.cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_GET_FREESTYLERIMAGES,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public int getFreestylerImagesSize(long pk) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(pk) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_GET_FREESTYLERIMAGES_SIZE,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                SQLQuery q = session.createSQLQuery(_SQL_GETFREESTYLERIMAGESSIZE);

                q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(pk);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_GET_FREESTYLERIMAGES_SIZE,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public boolean containsFreestylerImage(long pk, long freestylerImagePK)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(pk),
                
                new Long(freestylerImagePK)
            };

        Boolean value = (Boolean) FinderCacheUtil.getResult(FINDER_PATH_CONTAINS_FREESTYLERIMAGE,
                finderArgs, this);

        if (value == null) {
            try {
                value = Boolean.valueOf(containsFreestylerImage.contains(pk,
                            freestylerImagePK));
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (value == null) {
                    value = Boolean.FALSE;
                }

                FinderCacheUtil.putResult(FINDER_PATH_CONTAINS_FREESTYLERIMAGE,
                    finderArgs, value);
            }
        }

        return value.booleanValue();
    }

    public boolean containsFreestylerImages(long pk) throws SystemException {
        if (getFreestylerImagesSize(pk) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void addFreestylerImage(long pk, long freestylerImagePK)
        throws SystemException {
        try {
            addFreestylerImage.add(pk, freestylerImagePK);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void addFreestylerImage(long pk,
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws SystemException {
        try {
            addFreestylerImage.add(pk, freestylerImage.getPrimaryKey());
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void addFreestylerImages(long pk, long[] freestylerImagePKs)
        throws SystemException {
        try {
            for (long freestylerImagePK : freestylerImagePKs) {
                addFreestylerImage.add(pk, freestylerImagePK);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void addFreestylerImages(long pk,
        List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages)
        throws SystemException {
        try {
            for (com.ext.portlet.freestyler.model.FreestylerImage freestylerImage : freestylerImages) {
                addFreestylerImage.add(pk, freestylerImage.getPrimaryKey());
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void clearFreestylerImages(long pk) throws SystemException {
        try {
            clearFreestylerImages.clear(pk);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void removeFreestylerImage(long pk, long freestylerImagePK)
        throws SystemException {
        try {
            removeFreestylerImage.remove(pk, freestylerImagePK);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void removeFreestylerImage(long pk,
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws SystemException {
        try {
            removeFreestylerImage.remove(pk, freestylerImage.getPrimaryKey());
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void removeFreestylerImages(long pk, long[] freestylerImagePKs)
        throws SystemException {
        try {
            for (long freestylerImagePK : freestylerImagePKs) {
                removeFreestylerImage.remove(pk, freestylerImagePK);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void removeFreestylerImages(long pk,
        List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages)
        throws SystemException {
        try {
            for (com.ext.portlet.freestyler.model.FreestylerImage freestylerImage : freestylerImages) {
                removeFreestylerImage.remove(pk, freestylerImage.getPrimaryKey());
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void setFreestylerImages(long pk, long[] freestylerImagePKs)
        throws SystemException {
        try {
            clearFreestylerImages.clear(pk);

            for (long freestylerImagePK : freestylerImagePKs) {
                addFreestylerImage.add(pk, freestylerImagePK);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void setFreestylerImages(long pk,
        List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages)
        throws SystemException {
        try {
            clearFreestylerImages.clear(pk);

            for (com.ext.portlet.freestyler.model.FreestylerImage freestylerImage : freestylerImages) {
                addFreestylerImage.add(pk, freestylerImage.getPrimaryKey());
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("FreestylerEntry_FreestylerImages");
        }
    }

    public void afterPropertiesSet() {
        String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
                    com.liferay.portal.util.PropsUtil.get(
                        "value.object.listener.com.ext.portlet.freestyler.model.FreestylerEntry")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<FreestylerEntry>> listenersList = new ArrayList<ModelListener<FreestylerEntry>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<FreestylerEntry>) Class.forName(
                            listenerClassName).newInstance());
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }

        containsFreestylerImage = new ContainsFreestylerImage(this);

        addFreestylerImage = new AddFreestylerImage(this);
        clearFreestylerImages = new ClearFreestylerImages(this);
        removeFreestylerImage = new RemoveFreestylerImage(this);
    }

    protected class ContainsFreestylerImage {
        private MappingSqlQuery _mappingSqlQuery;

        protected ContainsFreestylerImage(
            FreestylerEntryPersistenceImpl persistenceImpl) {
            super();

            _mappingSqlQuery = MappingSqlQueryFactoryUtil.getMappingSqlQuery(getDataSource(),
                    _SQL_CONTAINSFREESTYLERIMAGE,
                    new int[] { Types.BIGINT, Types.BIGINT }, RowMapper.COUNT);
        }

        protected boolean contains(long freestylerId, long imageId) {
            List<Integer> results = _mappingSqlQuery.execute(new Object[] {
                        new Long(freestylerId), new Long(imageId)
                    });

            if (results.size() > 0) {
                Integer count = results.get(0);

                if (count.intValue() > 0) {
                    return true;
                }
            }

            return false;
        }
    }

    protected class AddFreestylerImage {
        private SqlUpdate _sqlUpdate;
        private FreestylerEntryPersistenceImpl _persistenceImpl;

        protected AddFreestylerImage(
            FreestylerEntryPersistenceImpl persistenceImpl) {
            _sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(getDataSource(),
                    "INSERT INTO FreestylerEntry_FreestylerImages (freestylerId, imageId) VALUES (?, ?)",
                    new int[] { Types.BIGINT, Types.BIGINT });
            _persistenceImpl = persistenceImpl;
        }

        protected void add(long freestylerId, long imageId)
            throws SystemException {
            if (!_persistenceImpl.containsFreestylerImage.contains(
                        freestylerId, imageId)) {
                ModelListener<com.ext.portlet.freestyler.model.FreestylerImage>[] freestylerImageListeners =
                    freestylerImagePersistence.getListeners();

                for (ModelListener<FreestylerEntry> listener : listeners) {
                    listener.onBeforeAddAssociation(freestylerId,
                        com.ext.portlet.freestyler.model.FreestylerImage.class.getName(),
                        imageId);
                }

                for (ModelListener<com.ext.portlet.freestyler.model.FreestylerImage> listener : freestylerImageListeners) {
                    listener.onBeforeAddAssociation(imageId,
                        FreestylerEntry.class.getName(), freestylerId);
                }

                _sqlUpdate.update(new Object[] {
                        new Long(freestylerId), new Long(imageId)
                    });

                for (ModelListener<FreestylerEntry> listener : listeners) {
                    listener.onAfterAddAssociation(freestylerId,
                        com.ext.portlet.freestyler.model.FreestylerImage.class.getName(),
                        imageId);
                }

                for (ModelListener<com.ext.portlet.freestyler.model.FreestylerImage> listener : freestylerImageListeners) {
                    listener.onAfterAddAssociation(imageId,
                        FreestylerEntry.class.getName(), freestylerId);
                }
            }
        }
    }

    protected class ClearFreestylerImages {
        private SqlUpdate _sqlUpdate;

        protected ClearFreestylerImages(
            FreestylerEntryPersistenceImpl persistenceImpl) {
            _sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(getDataSource(),
                    "DELETE FROM FreestylerEntry_FreestylerImages WHERE freestylerId = ?",
                    new int[] { Types.BIGINT });
        }

        protected void clear(long freestylerId) throws SystemException {
            ModelListener<com.ext.portlet.freestyler.model.FreestylerImage>[] freestylerImageListeners =
                freestylerImagePersistence.getListeners();

            List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages =
                null;

            if ((listeners.length > 0) ||
                    (freestylerImageListeners.length > 0)) {
                freestylerImages = getFreestylerImages(freestylerId);

                for (com.ext.portlet.freestyler.model.FreestylerImage freestylerImage : freestylerImages) {
                    for (ModelListener<FreestylerEntry> listener : listeners) {
                        listener.onBeforeRemoveAssociation(freestylerId,
                            com.ext.portlet.freestyler.model.FreestylerImage.class.getName(),
                            freestylerImage.getPrimaryKey());
                    }

                    for (ModelListener<com.ext.portlet.freestyler.model.FreestylerImage> listener : freestylerImageListeners) {
                        listener.onBeforeRemoveAssociation(freestylerImage.getPrimaryKey(),
                            FreestylerEntry.class.getName(), freestylerId);
                    }
                }
            }

            _sqlUpdate.update(new Object[] { new Long(freestylerId) });

            if ((listeners.length > 0) ||
                    (freestylerImageListeners.length > 0)) {
                for (com.ext.portlet.freestyler.model.FreestylerImage freestylerImage : freestylerImages) {
                    for (ModelListener<FreestylerEntry> listener : listeners) {
                        listener.onAfterRemoveAssociation(freestylerId,
                            com.ext.portlet.freestyler.model.FreestylerImage.class.getName(),
                            freestylerImage.getPrimaryKey());
                    }

                    for (ModelListener<com.ext.portlet.freestyler.model.FreestylerImage> listener : freestylerImageListeners) {
                        listener.onBeforeRemoveAssociation(freestylerImage.getPrimaryKey(),
                            FreestylerEntry.class.getName(), freestylerId);
                    }
                }
            }
        }
    }

    protected class RemoveFreestylerImage {
        private SqlUpdate _sqlUpdate;
        private FreestylerEntryPersistenceImpl _persistenceImpl;

        protected RemoveFreestylerImage(
            FreestylerEntryPersistenceImpl persistenceImpl) {
            _sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(getDataSource(),
                    "DELETE FROM FreestylerEntry_FreestylerImages WHERE freestylerId = ? AND imageId = ?",
                    new int[] { Types.BIGINT, Types.BIGINT });
            _persistenceImpl = persistenceImpl;
        }

        protected void remove(long freestylerId, long imageId)
            throws SystemException {
            if (_persistenceImpl.containsFreestylerImage.contains(
                        freestylerId, imageId)) {
                ModelListener<com.ext.portlet.freestyler.model.FreestylerImage>[] freestylerImageListeners =
                    freestylerImagePersistence.getListeners();

                for (ModelListener<FreestylerEntry> listener : listeners) {
                    listener.onBeforeRemoveAssociation(freestylerId,
                        com.ext.portlet.freestyler.model.FreestylerImage.class.getName(),
                        imageId);
                }

                for (ModelListener<com.ext.portlet.freestyler.model.FreestylerImage> listener : freestylerImageListeners) {
                    listener.onBeforeRemoveAssociation(imageId,
                        FreestylerEntry.class.getName(), freestylerId);
                }

                _sqlUpdate.update(new Object[] {
                        new Long(freestylerId), new Long(imageId)
                    });

                for (ModelListener<FreestylerEntry> listener : listeners) {
                    listener.onAfterRemoveAssociation(freestylerId,
                        com.ext.portlet.freestyler.model.FreestylerImage.class.getName(),
                        imageId);
                }

                for (ModelListener<com.ext.portlet.freestyler.model.FreestylerImage> listener : freestylerImageListeners) {
                    listener.onAfterRemoveAssociation(imageId,
                        FreestylerEntry.class.getName(), freestylerId);
                }
            }
        }
    }
}
