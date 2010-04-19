package com.ext.portlet.missionhandling.service.persistence;

import com.ext.portlet.missionhandling.NoSuchMissionEntryException;
import com.ext.portlet.missionhandling.model.MissionEntry;
import com.ext.portlet.missionhandling.model.impl.MissionEntryImpl;
import com.ext.portlet.missionhandling.model.impl.MissionEntryModelImpl;

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
import com.liferay.portal.kernel.util.CalendarUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MissionEntryPersistenceImpl extends BasePersistenceImpl
    implements MissionEntryPersistence {
    public static final String FINDER_CLASS_NAME_ENTITY = MissionEntryImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST = FINDER_CLASS_NAME_ENTITY +
        ".List";
    public static final FinderPath FINDER_PATH_FIND_BY_CREATEDATE = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByCreateDate", new String[] { Date.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_CREATEDATE = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByCreateDate",
            new String[] {
                Date.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_CREATEDATE = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByCreateDate", new String[] { Date.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_ORGANIZATIONID = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByOrganizationId", new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_ORGANIZATIONID = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByOrganizationId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_ORGANIZATIONID = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByOrganizationId", new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_GROUPID = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByGroupId", new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_GROUPID = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByGroupId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByGroupId", new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_COMPANYID = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByCompanyId", new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_COMPANYID = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByCompanyId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_COMPANYID = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByCompanyId", new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_ALL = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countAll", new String[0]);
    private static Log _log = LogFactoryUtil.getLog(MissionEntryPersistenceImpl.class);
    @BeanReference(name = "com.ext.portlet.missionhandling.service.persistence.MissionEntryPersistence.impl")
    protected com.ext.portlet.missionhandling.service.persistence.MissionEntryPersistence missionEntryPersistence;

    public void cacheResult(MissionEntry missionEntry) {
        EntityCacheUtil.putResult(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryImpl.class, missionEntry.getPrimaryKey(), missionEntry);
    }

    public void cacheResult(List<MissionEntry> missionEntries) {
        for (MissionEntry missionEntry : missionEntries) {
            if (EntityCacheUtil.getResult(
                        MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
                        MissionEntryImpl.class, missionEntry.getPrimaryKey(),
                        this) == null) {
                cacheResult(missionEntry);
            }
        }
    }

    public void clearCache() {
        CacheRegistry.clear(MissionEntryImpl.class.getName());
        EntityCacheUtil.clearCache(MissionEntryImpl.class.getName());
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);
    }

    public MissionEntry create(long missionEntryId) {
        MissionEntry missionEntry = new MissionEntryImpl();

        missionEntry.setNew(true);
        missionEntry.setPrimaryKey(missionEntryId);

        return missionEntry;
    }

    public MissionEntry remove(long missionEntryId)
        throws NoSuchMissionEntryException, SystemException {
        Session session = null;

        try {
            session = openSession();

            MissionEntry missionEntry = (MissionEntry) session.get(MissionEntryImpl.class,
                    new Long(missionEntryId));

            if (missionEntry == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No MissionEntry exists with the primary key " +
                        missionEntryId);
                }

                throw new NoSuchMissionEntryException(
                    "No MissionEntry exists with the primary key " +
                    missionEntryId);
            }

            return remove(missionEntry);
        } catch (NoSuchMissionEntryException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public MissionEntry remove(MissionEntry missionEntry)
        throws SystemException {
        for (ModelListener<MissionEntry> listener : listeners) {
            listener.onBeforeRemove(missionEntry);
        }

        missionEntry = removeImpl(missionEntry);

        for (ModelListener<MissionEntry> listener : listeners) {
            listener.onAfterRemove(missionEntry);
        }

        return missionEntry;
    }

    protected MissionEntry removeImpl(MissionEntry missionEntry)
        throws SystemException {
        Session session = null;

        try {
            session = openSession();

            if (missionEntry.isCachedModel() || BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(MissionEntryImpl.class,
                        missionEntry.getPrimaryKeyObj());

                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }

            session.delete(missionEntry);

            session.flush();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.removeResult(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryImpl.class, missionEntry.getPrimaryKey());

        return missionEntry;
    }

    /**
     * @deprecated Use <code>update(MissionEntry missionEntry, boolean merge)</code>.
     */
    public MissionEntry update(MissionEntry missionEntry)
        throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn(
                "Using the deprecated update(MissionEntry missionEntry) method. Use update(MissionEntry missionEntry, boolean merge) instead.");
        }

        return update(missionEntry, false);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                missionEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when missionEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public MissionEntry update(MissionEntry missionEntry, boolean merge)
        throws SystemException {
        boolean isNew = missionEntry.isNew();

        for (ModelListener<MissionEntry> listener : listeners) {
            if (isNew) {
                listener.onBeforeCreate(missionEntry);
            } else {
                listener.onBeforeUpdate(missionEntry);
            }
        }

        missionEntry = updateImpl(missionEntry, merge);

        for (ModelListener<MissionEntry> listener : listeners) {
            if (isNew) {
                listener.onAfterCreate(missionEntry);
            } else {
                listener.onAfterUpdate(missionEntry);
            }
        }

        return missionEntry;
    }

    public MissionEntry updateImpl(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry,
        boolean merge) throws SystemException {
        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.update(session, missionEntry, merge);

            missionEntry.setNew(false);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.putResult(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
            MissionEntryImpl.class, missionEntry.getPrimaryKey(), missionEntry);

        return missionEntry;
    }

    public MissionEntry findByPrimaryKey(long missionEntryId)
        throws NoSuchMissionEntryException, SystemException {
        MissionEntry missionEntry = fetchByPrimaryKey(missionEntryId);

        if (missionEntry == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No MissionEntry exists with the primary key " +
                    missionEntryId);
            }

            throw new NoSuchMissionEntryException(
                "No MissionEntry exists with the primary key " +
                missionEntryId);
        }

        return missionEntry;
    }

    public MissionEntry fetchByPrimaryKey(long missionEntryId)
        throws SystemException {
        MissionEntry missionEntry = (MissionEntry) EntityCacheUtil.getResult(MissionEntryModelImpl.ENTITY_CACHE_ENABLED,
                MissionEntryImpl.class, missionEntryId, this);

        if (missionEntry == null) {
            Session session = null;

            try {
                session = openSession();

                missionEntry = (MissionEntry) session.get(MissionEntryImpl.class,
                        new Long(missionEntryId));
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (missionEntry != null) {
                    cacheResult(missionEntry);
                }

                closeSession(session);
            }
        }

        return missionEntry;
    }

    public List<MissionEntry> findByCreateDate(Date createDate)
        throws SystemException {
        Object[] finderArgs = new Object[] { createDate };

        List<MissionEntry> list = (List<MissionEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_CREATEDATE,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

                if (createDate == null) {
                    query.append("createDate IS NULL");
                } else {
                    query.append("createDate = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("missionEntryId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (createDate != null) {
                    qPos.add(CalendarUtil.getTimestamp(createDate));
                }

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<MissionEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_CREATEDATE,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<MissionEntry> findByCreateDate(Date createDate, int start,
        int end) throws SystemException {
        return findByCreateDate(createDate, start, end, null);
    }

    public List<MissionEntry> findByCreateDate(Date createDate, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                createDate,
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<MissionEntry> list = (List<MissionEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_CREATEDATE,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

                if (createDate == null) {
                    query.append("createDate IS NULL");
                } else {
                    query.append("createDate = ?");
                }

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("missionEntryId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (createDate != null) {
                    qPos.add(CalendarUtil.getTimestamp(createDate));
                }

                list = (List<MissionEntry>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<MissionEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_CREATEDATE,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public MissionEntry findByCreateDate_First(Date createDate,
        OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        List<MissionEntry> list = findByCreateDate(createDate, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No MissionEntry exists with the key {");

            msg.append("createDate=" + createDate);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchMissionEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public MissionEntry findByCreateDate_Last(Date createDate,
        OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        int count = countByCreateDate(createDate);

        List<MissionEntry> list = findByCreateDate(createDate, count - 1,
                count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No MissionEntry exists with the key {");

            msg.append("createDate=" + createDate);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchMissionEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public MissionEntry[] findByCreateDate_PrevAndNext(long missionEntryId,
        Date createDate, OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        MissionEntry missionEntry = findByPrimaryKey(missionEntryId);

        int count = countByCreateDate(createDate);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

            if (createDate == null) {
                query.append("createDate IS NULL");
            } else {
                query.append("createDate = ?");
            }

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("missionEntryId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            if (createDate != null) {
                qPos.add(CalendarUtil.getTimestamp(createDate));
            }

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    missionEntry);

            MissionEntry[] array = new MissionEntryImpl[3];

            array[0] = (MissionEntry) objArray[0];
            array[1] = (MissionEntry) objArray[1];
            array[2] = (MissionEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<MissionEntry> findByOrganizationId(long organizationId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(organizationId) };

        List<MissionEntry> list = (List<MissionEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_ORGANIZATIONID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

                query.append("organizationId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("missionEntryId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(organizationId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<MissionEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_ORGANIZATIONID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<MissionEntry> findByOrganizationId(long organizationId,
        int start, int end) throws SystemException {
        return findByOrganizationId(organizationId, start, end, null);
    }

    public List<MissionEntry> findByOrganizationId(long organizationId,
        int start, int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(organizationId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<MissionEntry> list = (List<MissionEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_ORGANIZATIONID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

                query.append("organizationId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("missionEntryId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(organizationId);

                list = (List<MissionEntry>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<MissionEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_ORGANIZATIONID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public MissionEntry findByOrganizationId_First(long organizationId,
        OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        List<MissionEntry> list = findByOrganizationId(organizationId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No MissionEntry exists with the key {");

            msg.append("organizationId=" + organizationId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchMissionEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public MissionEntry findByOrganizationId_Last(long organizationId,
        OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        int count = countByOrganizationId(organizationId);

        List<MissionEntry> list = findByOrganizationId(organizationId,
                count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No MissionEntry exists with the key {");

            msg.append("organizationId=" + organizationId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchMissionEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public MissionEntry[] findByOrganizationId_PrevAndNext(
        long missionEntryId, long organizationId, OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        MissionEntry missionEntry = findByPrimaryKey(missionEntryId);

        int count = countByOrganizationId(organizationId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

            query.append("organizationId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("missionEntryId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(organizationId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    missionEntry);

            MissionEntry[] array = new MissionEntryImpl[3];

            array[0] = (MissionEntry) objArray[0];
            array[1] = (MissionEntry) objArray[1];
            array[2] = (MissionEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<MissionEntry> findByGroupId(long groupId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(groupId) };

        List<MissionEntry> list = (List<MissionEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_GROUPID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

                query.append("groupId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("missionEntryId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<MissionEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_GROUPID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<MissionEntry> findByGroupId(long groupId, int start, int end)
        throws SystemException {
        return findByGroupId(groupId, start, end, null);
    }

    public List<MissionEntry> findByGroupId(long groupId, int start, int end,
        OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(groupId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<MissionEntry> list = (List<MissionEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_GROUPID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

                query.append("groupId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("missionEntryId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                list = (List<MissionEntry>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<MissionEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_GROUPID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public MissionEntry findByGroupId_First(long groupId, OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        List<MissionEntry> list = findByGroupId(groupId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No MissionEntry exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchMissionEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public MissionEntry findByGroupId_Last(long groupId, OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        int count = countByGroupId(groupId);

        List<MissionEntry> list = findByGroupId(groupId, count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No MissionEntry exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchMissionEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public MissionEntry[] findByGroupId_PrevAndNext(long missionEntryId,
        long groupId, OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        MissionEntry missionEntry = findByPrimaryKey(missionEntryId);

        int count = countByGroupId(groupId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

            query.append("groupId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("missionEntryId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(groupId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    missionEntry);

            MissionEntry[] array = new MissionEntryImpl[3];

            array[0] = (MissionEntry) objArray[0];
            array[1] = (MissionEntry) objArray[1];
            array[2] = (MissionEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<MissionEntry> findByCompanyId(long companyId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(companyId) };

        List<MissionEntry> list = (List<MissionEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_COMPANYID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

                query.append("companyId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("missionEntryId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(companyId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<MissionEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_COMPANYID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<MissionEntry> findByCompanyId(long companyId, int start, int end)
        throws SystemException {
        return findByCompanyId(companyId, start, end, null);
    }

    public List<MissionEntry> findByCompanyId(long companyId, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(companyId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<MissionEntry> list = (List<MissionEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_COMPANYID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

                query.append("companyId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("missionEntryId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(companyId);

                list = (List<MissionEntry>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<MissionEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_COMPANYID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public MissionEntry findByCompanyId_First(long companyId,
        OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        List<MissionEntry> list = findByCompanyId(companyId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No MissionEntry exists with the key {");

            msg.append("companyId=" + companyId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchMissionEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public MissionEntry findByCompanyId_Last(long companyId,
        OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        int count = countByCompanyId(companyId);

        List<MissionEntry> list = findByCompanyId(companyId, count - 1, count,
                obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No MissionEntry exists with the key {");

            msg.append("companyId=" + companyId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchMissionEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public MissionEntry[] findByCompanyId_PrevAndNext(long missionEntryId,
        long companyId, OrderByComparator obc)
        throws NoSuchMissionEntryException, SystemException {
        MissionEntry missionEntry = findByPrimaryKey(missionEntryId);

        int count = countByCompanyId(companyId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

            query.append("companyId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("missionEntryId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(companyId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    missionEntry);

            MissionEntry[] array = new MissionEntryImpl[3];

            array[0] = (MissionEntry) objArray[0];
            array[1] = (MissionEntry) objArray[1];
            array[2] = (MissionEntry) objArray[2];

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

    public List<MissionEntry> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<MissionEntry> findAll(int start, int end)
        throws SystemException {
        return findAll(start, end, null);
    }

    public List<MissionEntry> findAll(int start, int end, OrderByComparator obc)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<MissionEntry> list = (List<MissionEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_ALL,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("missionEntryId ASC");
                }

                Query q = session.createQuery(query.toString());

                if (obc == null) {
                    list = (List<MissionEntry>) QueryUtil.list(q, getDialect(),
                            start, end, false);

                    Collections.sort(list);
                } else {
                    list = (List<MissionEntry>) QueryUtil.list(q, getDialect(),
                            start, end);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<MissionEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_ALL, finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public void removeByCreateDate(Date createDate) throws SystemException {
        for (MissionEntry missionEntry : findByCreateDate(createDate)) {
            remove(missionEntry);
        }
    }

    public void removeByOrganizationId(long organizationId)
        throws SystemException {
        for (MissionEntry missionEntry : findByOrganizationId(organizationId)) {
            remove(missionEntry);
        }
    }

    public void removeByGroupId(long groupId) throws SystemException {
        for (MissionEntry missionEntry : findByGroupId(groupId)) {
            remove(missionEntry);
        }
    }

    public void removeByCompanyId(long companyId) throws SystemException {
        for (MissionEntry missionEntry : findByCompanyId(companyId)) {
            remove(missionEntry);
        }
    }

    public void removeAll() throws SystemException {
        for (MissionEntry missionEntry : findAll()) {
            remove(missionEntry);
        }
    }

    public int countByCreateDate(Date createDate) throws SystemException {
        Object[] finderArgs = new Object[] { createDate };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_CREATEDATE,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

                if (createDate == null) {
                    query.append("createDate IS NULL");
                } else {
                    query.append("createDate = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (createDate != null) {
                    qPos.add(CalendarUtil.getTimestamp(createDate));
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_CREATEDATE,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByOrganizationId(long organizationId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(organizationId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_ORGANIZATIONID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

                query.append("organizationId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(organizationId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_ORGANIZATIONID,
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
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

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
                    "FROM com.ext.portlet.missionhandling.model.MissionEntry WHERE ");

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

    public int countAll() throws SystemException {
        Object[] finderArgs = new Object[0];

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(
                        "SELECT COUNT(*) FROM com.ext.portlet.missionhandling.model.MissionEntry");

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
                        "value.object.listener.com.ext.portlet.missionhandling.model.MissionEntry")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<MissionEntry>> listenersList = new ArrayList<ModelListener<MissionEntry>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<MissionEntry>) Class.forName(
                            listenerClassName).newInstance());
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }
}
