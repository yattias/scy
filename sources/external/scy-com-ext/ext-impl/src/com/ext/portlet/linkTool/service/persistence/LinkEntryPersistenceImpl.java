package com.ext.portlet.linkTool.service.persistence;

import com.ext.portlet.linkTool.NoSuchLinkEntryException;
import com.ext.portlet.linkTool.model.LinkEntry;
import com.ext.portlet.linkTool.model.impl.LinkEntryImpl;
import com.ext.portlet.linkTool.model.impl.LinkEntryModelImpl;

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
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LinkEntryPersistenceImpl extends BasePersistenceImpl
    implements LinkEntryPersistence {
    public static final String FINDER_CLASS_NAME_ENTITY = LinkEntryImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST = FINDER_CLASS_NAME_ENTITY +
        ".List";
    public static final FinderPath FINDER_PATH_FIND_BY_RESOURCEID = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByResourceId", new String[] { String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_RESOURCEID = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByResourceId",
            new String[] {
                String.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_RESOURCEID = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByResourceId", new String[] { String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_LINKEDRESOURCEID = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByLinkedResourceId", new String[] { String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_LINKEDRESOURCEID = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByLinkedResourceId",
            new String[] {
                String.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_LINKEDRESOURCEID = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByLinkedResourceId", new String[] { String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_R_L = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByR_L",
            new String[] { String.class.getName(), String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_R_L = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByR_L",
            new String[] {
                String.class.getName(), String.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_R_L = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByR_L",
            new String[] { String.class.getName(), String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_ALL = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countAll", new String[0]);
    private static Log _log = LogFactoryUtil.getLog(LinkEntryPersistenceImpl.class);
    @BeanReference(name = "com.ext.portlet.linkTool.service.persistence.LinkEntryPersistence.impl")
    protected com.ext.portlet.linkTool.service.persistence.LinkEntryPersistence linkEntryPersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.GroupPersistence.impl")
    protected com.liferay.portal.service.persistence.GroupPersistence groupPersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected com.liferay.portal.service.persistence.UserPersistence userPersistence;

    public void cacheResult(LinkEntry linkEntry) {
        EntityCacheUtil.putResult(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryImpl.class, linkEntry.getPrimaryKey(), linkEntry);
    }

    public void cacheResult(List<LinkEntry> linkEntries) {
        for (LinkEntry linkEntry : linkEntries) {
            if (EntityCacheUtil.getResult(
                        LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
                        LinkEntryImpl.class, linkEntry.getPrimaryKey(), this) == null) {
                cacheResult(linkEntry);
            }
        }
    }

    public void clearCache() {
        CacheRegistry.clear(LinkEntryImpl.class.getName());
        EntityCacheUtil.clearCache(LinkEntryImpl.class.getName());
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);
    }

    public LinkEntry create(long linkId) {
        LinkEntry linkEntry = new LinkEntryImpl();

        linkEntry.setNew(true);
        linkEntry.setPrimaryKey(linkId);

        return linkEntry;
    }

    public LinkEntry remove(long linkId)
        throws NoSuchLinkEntryException, SystemException {
        Session session = null;

        try {
            session = openSession();

            LinkEntry linkEntry = (LinkEntry) session.get(LinkEntryImpl.class,
                    new Long(linkId));

            if (linkEntry == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No LinkEntry exists with the primary key " +
                        linkId);
                }

                throw new NoSuchLinkEntryException(
                    "No LinkEntry exists with the primary key " + linkId);
            }

            return remove(linkEntry);
        } catch (NoSuchLinkEntryException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public LinkEntry remove(LinkEntry linkEntry) throws SystemException {
        for (ModelListener<LinkEntry> listener : listeners) {
            listener.onBeforeRemove(linkEntry);
        }

        linkEntry = removeImpl(linkEntry);

        for (ModelListener<LinkEntry> listener : listeners) {
            listener.onAfterRemove(linkEntry);
        }

        return linkEntry;
    }

    protected LinkEntry removeImpl(LinkEntry linkEntry)
        throws SystemException {
        Session session = null;

        try {
            session = openSession();

            if (linkEntry.isCachedModel() || BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(LinkEntryImpl.class,
                        linkEntry.getPrimaryKeyObj());

                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }

            session.delete(linkEntry);

            session.flush();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.removeResult(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryImpl.class, linkEntry.getPrimaryKey());

        return linkEntry;
    }

    /**
     * @deprecated Use <code>update(LinkEntry linkEntry, boolean merge)</code>.
     */
    public LinkEntry update(LinkEntry linkEntry) throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn(
                "Using the deprecated update(LinkEntry linkEntry) method. Use update(LinkEntry linkEntry, boolean merge) instead.");
        }

        return update(linkEntry, false);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                linkEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when linkEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public LinkEntry update(LinkEntry linkEntry, boolean merge)
        throws SystemException {
        boolean isNew = linkEntry.isNew();

        for (ModelListener<LinkEntry> listener : listeners) {
            if (isNew) {
                listener.onBeforeCreate(linkEntry);
            } else {
                listener.onBeforeUpdate(linkEntry);
            }
        }

        linkEntry = updateImpl(linkEntry, merge);

        for (ModelListener<LinkEntry> listener : listeners) {
            if (isNew) {
                listener.onAfterCreate(linkEntry);
            } else {
                listener.onAfterUpdate(linkEntry);
            }
        }

        return linkEntry;
    }

    public LinkEntry updateImpl(
        com.ext.portlet.linkTool.model.LinkEntry linkEntry, boolean merge)
        throws SystemException {
        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.update(session, linkEntry, merge);

            linkEntry.setNew(false);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.putResult(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
            LinkEntryImpl.class, linkEntry.getPrimaryKey(), linkEntry);

        return linkEntry;
    }

    public LinkEntry findByPrimaryKey(long linkId)
        throws NoSuchLinkEntryException, SystemException {
        LinkEntry linkEntry = fetchByPrimaryKey(linkId);

        if (linkEntry == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No LinkEntry exists with the primary key " + linkId);
            }

            throw new NoSuchLinkEntryException(
                "No LinkEntry exists with the primary key " + linkId);
        }

        return linkEntry;
    }

    public LinkEntry fetchByPrimaryKey(long linkId) throws SystemException {
        LinkEntry linkEntry = (LinkEntry) EntityCacheUtil.getResult(LinkEntryModelImpl.ENTITY_CACHE_ENABLED,
                LinkEntryImpl.class, linkId, this);

        if (linkEntry == null) {
            Session session = null;

            try {
                session = openSession();

                linkEntry = (LinkEntry) session.get(LinkEntryImpl.class,
                        new Long(linkId));
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (linkEntry != null) {
                    cacheResult(linkEntry);
                }

                closeSession(session);
            }
        }

        return linkEntry;
    }

    public List<LinkEntry> findByResourceId(String resourceId)
        throws SystemException {
        Object[] finderArgs = new Object[] { resourceId };

        List<LinkEntry> list = (List<LinkEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_RESOURCEID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

                if (resourceId == null) {
                    query.append("resourceId IS NULL");
                } else {
                    query.append("resourceId = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("resourceId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (resourceId != null) {
                    qPos.add(resourceId);
                }

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<LinkEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_RESOURCEID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<LinkEntry> findByResourceId(String resourceId, int start,
        int end) throws SystemException {
        return findByResourceId(resourceId, start, end, null);
    }

    public List<LinkEntry> findByResourceId(String resourceId, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                resourceId,
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<LinkEntry> list = (List<LinkEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_RESOURCEID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

                if (resourceId == null) {
                    query.append("resourceId IS NULL");
                } else {
                    query.append("resourceId = ?");
                }

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("resourceId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (resourceId != null) {
                    qPos.add(resourceId);
                }

                list = (List<LinkEntry>) QueryUtil.list(q, getDialect(), start,
                        end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<LinkEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_RESOURCEID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public LinkEntry findByResourceId_First(String resourceId,
        OrderByComparator obc) throws NoSuchLinkEntryException, SystemException {
        List<LinkEntry> list = findByResourceId(resourceId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No LinkEntry exists with the key {");

            msg.append("resourceId=" + resourceId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchLinkEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public LinkEntry findByResourceId_Last(String resourceId,
        OrderByComparator obc) throws NoSuchLinkEntryException, SystemException {
        int count = countByResourceId(resourceId);

        List<LinkEntry> list = findByResourceId(resourceId, count - 1, count,
                obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No LinkEntry exists with the key {");

            msg.append("resourceId=" + resourceId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchLinkEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public LinkEntry[] findByResourceId_PrevAndNext(long linkId,
        String resourceId, OrderByComparator obc)
        throws NoSuchLinkEntryException, SystemException {
        LinkEntry linkEntry = findByPrimaryKey(linkId);

        int count = countByResourceId(resourceId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append("FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

            if (resourceId == null) {
                query.append("resourceId IS NULL");
            } else {
                query.append("resourceId = ?");
            }

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("resourceId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            if (resourceId != null) {
                qPos.add(resourceId);
            }

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    linkEntry);

            LinkEntry[] array = new LinkEntryImpl[3];

            array[0] = (LinkEntry) objArray[0];
            array[1] = (LinkEntry) objArray[1];
            array[2] = (LinkEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<LinkEntry> findByLinkedResourceId(String linkedResourceId)
        throws SystemException {
        Object[] finderArgs = new Object[] { linkedResourceId };

        List<LinkEntry> list = (List<LinkEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_LINKEDRESOURCEID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

                if (linkedResourceId == null) {
                    query.append("linkedResourceId IS NULL");
                } else {
                    query.append("linkedResourceId = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("resourceId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (linkedResourceId != null) {
                    qPos.add(linkedResourceId);
                }

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<LinkEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_LINKEDRESOURCEID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<LinkEntry> findByLinkedResourceId(String linkedResourceId,
        int start, int end) throws SystemException {
        return findByLinkedResourceId(linkedResourceId, start, end, null);
    }

    public List<LinkEntry> findByLinkedResourceId(String linkedResourceId,
        int start, int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                linkedResourceId,
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<LinkEntry> list = (List<LinkEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_LINKEDRESOURCEID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

                if (linkedResourceId == null) {
                    query.append("linkedResourceId IS NULL");
                } else {
                    query.append("linkedResourceId = ?");
                }

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("resourceId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (linkedResourceId != null) {
                    qPos.add(linkedResourceId);
                }

                list = (List<LinkEntry>) QueryUtil.list(q, getDialect(), start,
                        end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<LinkEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_LINKEDRESOURCEID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public LinkEntry findByLinkedResourceId_First(String linkedResourceId,
        OrderByComparator obc) throws NoSuchLinkEntryException, SystemException {
        List<LinkEntry> list = findByLinkedResourceId(linkedResourceId, 0, 1,
                obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No LinkEntry exists with the key {");

            msg.append("linkedResourceId=" + linkedResourceId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchLinkEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public LinkEntry findByLinkedResourceId_Last(String linkedResourceId,
        OrderByComparator obc) throws NoSuchLinkEntryException, SystemException {
        int count = countByLinkedResourceId(linkedResourceId);

        List<LinkEntry> list = findByLinkedResourceId(linkedResourceId,
                count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No LinkEntry exists with the key {");

            msg.append("linkedResourceId=" + linkedResourceId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchLinkEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public LinkEntry[] findByLinkedResourceId_PrevAndNext(long linkId,
        String linkedResourceId, OrderByComparator obc)
        throws NoSuchLinkEntryException, SystemException {
        LinkEntry linkEntry = findByPrimaryKey(linkId);

        int count = countByLinkedResourceId(linkedResourceId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append("FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

            if (linkedResourceId == null) {
                query.append("linkedResourceId IS NULL");
            } else {
                query.append("linkedResourceId = ?");
            }

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("resourceId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            if (linkedResourceId != null) {
                qPos.add(linkedResourceId);
            }

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    linkEntry);

            LinkEntry[] array = new LinkEntryImpl[3];

            array[0] = (LinkEntry) objArray[0];
            array[1] = (LinkEntry) objArray[1];
            array[2] = (LinkEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<LinkEntry> findByR_L(String resourceId, String linkedResourceId)
        throws SystemException {
        Object[] finderArgs = new Object[] { resourceId, linkedResourceId };

        List<LinkEntry> list = (List<LinkEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_R_L,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

                if (resourceId == null) {
                    query.append("resourceId IS NULL");
                } else {
                    query.append("resourceId = ?");
                }

                query.append(" AND ");

                if (linkedResourceId == null) {
                    query.append("linkedResourceId IS NULL");
                } else {
                    query.append("linkedResourceId = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("resourceId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (resourceId != null) {
                    qPos.add(resourceId);
                }

                if (linkedResourceId != null) {
                    qPos.add(linkedResourceId);
                }

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<LinkEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_R_L, finderArgs,
                    list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<LinkEntry> findByR_L(String resourceId,
        String linkedResourceId, int start, int end) throws SystemException {
        return findByR_L(resourceId, linkedResourceId, start, end, null);
    }

    public List<LinkEntry> findByR_L(String resourceId,
        String linkedResourceId, int start, int end, OrderByComparator obc)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                resourceId,
                
                linkedResourceId,
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<LinkEntry> list = (List<LinkEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_R_L,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

                if (resourceId == null) {
                    query.append("resourceId IS NULL");
                } else {
                    query.append("resourceId = ?");
                }

                query.append(" AND ");

                if (linkedResourceId == null) {
                    query.append("linkedResourceId IS NULL");
                } else {
                    query.append("linkedResourceId = ?");
                }

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("resourceId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (resourceId != null) {
                    qPos.add(resourceId);
                }

                if (linkedResourceId != null) {
                    qPos.add(linkedResourceId);
                }

                list = (List<LinkEntry>) QueryUtil.list(q, getDialect(), start,
                        end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<LinkEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_R_L,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public LinkEntry findByR_L_First(String resourceId,
        String linkedResourceId, OrderByComparator obc)
        throws NoSuchLinkEntryException, SystemException {
        List<LinkEntry> list = findByR_L(resourceId, linkedResourceId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No LinkEntry exists with the key {");

            msg.append("resourceId=" + resourceId);

            msg.append(", ");
            msg.append("linkedResourceId=" + linkedResourceId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchLinkEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public LinkEntry findByR_L_Last(String resourceId, String linkedResourceId,
        OrderByComparator obc) throws NoSuchLinkEntryException, SystemException {
        int count = countByR_L(resourceId, linkedResourceId);

        List<LinkEntry> list = findByR_L(resourceId, linkedResourceId,
                count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No LinkEntry exists with the key {");

            msg.append("resourceId=" + resourceId);

            msg.append(", ");
            msg.append("linkedResourceId=" + linkedResourceId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchLinkEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public LinkEntry[] findByR_L_PrevAndNext(long linkId, String resourceId,
        String linkedResourceId, OrderByComparator obc)
        throws NoSuchLinkEntryException, SystemException {
        LinkEntry linkEntry = findByPrimaryKey(linkId);

        int count = countByR_L(resourceId, linkedResourceId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append("FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

            if (resourceId == null) {
                query.append("resourceId IS NULL");
            } else {
                query.append("resourceId = ?");
            }

            query.append(" AND ");

            if (linkedResourceId == null) {
                query.append("linkedResourceId IS NULL");
            } else {
                query.append("linkedResourceId = ?");
            }

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("resourceId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            if (resourceId != null) {
                qPos.add(resourceId);
            }

            if (linkedResourceId != null) {
                qPos.add(linkedResourceId);
            }

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    linkEntry);

            LinkEntry[] array = new LinkEntryImpl[3];

            array[0] = (LinkEntry) objArray[0];
            array[1] = (LinkEntry) objArray[1];
            array[2] = (LinkEntry) objArray[2];

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

    public List<LinkEntry> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<LinkEntry> findAll(int start, int end)
        throws SystemException {
        return findAll(start, end, null);
    }

    public List<LinkEntry> findAll(int start, int end, OrderByComparator obc)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<LinkEntry> list = (List<LinkEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_ALL,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.linkTool.model.LinkEntry ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("resourceId ASC");
                }

                Query q = session.createQuery(query.toString());

                if (obc == null) {
                    list = (List<LinkEntry>) QueryUtil.list(q, getDialect(),
                            start, end, false);

                    Collections.sort(list);
                } else {
                    list = (List<LinkEntry>) QueryUtil.list(q, getDialect(),
                            start, end);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<LinkEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_ALL, finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public void removeByResourceId(String resourceId) throws SystemException {
        for (LinkEntry linkEntry : findByResourceId(resourceId)) {
            remove(linkEntry);
        }
    }

    public void removeByLinkedResourceId(String linkedResourceId)
        throws SystemException {
        for (LinkEntry linkEntry : findByLinkedResourceId(linkedResourceId)) {
            remove(linkEntry);
        }
    }

    public void removeByR_L(String resourceId, String linkedResourceId)
        throws SystemException {
        for (LinkEntry linkEntry : findByR_L(resourceId, linkedResourceId)) {
            remove(linkEntry);
        }
    }

    public void removeAll() throws SystemException {
        for (LinkEntry linkEntry : findAll()) {
            remove(linkEntry);
        }
    }

    public int countByResourceId(String resourceId) throws SystemException {
        Object[] finderArgs = new Object[] { resourceId };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_RESOURCEID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

                if (resourceId == null) {
                    query.append("resourceId IS NULL");
                } else {
                    query.append("resourceId = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (resourceId != null) {
                    qPos.add(resourceId);
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_RESOURCEID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByLinkedResourceId(String linkedResourceId)
        throws SystemException {
        Object[] finderArgs = new Object[] { linkedResourceId };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_LINKEDRESOURCEID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

                if (linkedResourceId == null) {
                    query.append("linkedResourceId IS NULL");
                } else {
                    query.append("linkedResourceId = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (linkedResourceId != null) {
                    qPos.add(linkedResourceId);
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_LINKEDRESOURCEID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByR_L(String resourceId, String linkedResourceId)
        throws SystemException {
        Object[] finderArgs = new Object[] { resourceId, linkedResourceId };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_R_L,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.linkTool.model.LinkEntry WHERE ");

                if (resourceId == null) {
                    query.append("resourceId IS NULL");
                } else {
                    query.append("resourceId = ?");
                }

                query.append(" AND ");

                if (linkedResourceId == null) {
                    query.append("linkedResourceId IS NULL");
                } else {
                    query.append("linkedResourceId = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (resourceId != null) {
                    qPos.add(resourceId);
                }

                if (linkedResourceId != null) {
                    qPos.add(linkedResourceId);
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_R_L, finderArgs,
                    count);

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
                        "SELECT COUNT(*) FROM com.ext.portlet.linkTool.model.LinkEntry");

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
                        "value.object.listener.com.ext.portlet.linkTool.model.LinkEntry")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<LinkEntry>> listenersList = new ArrayList<ModelListener<LinkEntry>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<LinkEntry>) Class.forName(
                            listenerClassName).newInstance());
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }
}
