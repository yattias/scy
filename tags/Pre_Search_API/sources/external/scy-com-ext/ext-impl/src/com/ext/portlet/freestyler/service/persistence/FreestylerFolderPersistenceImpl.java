package com.ext.portlet.freestyler.service.persistence;

import com.ext.portlet.freestyler.NoSuchFolderException;
import com.ext.portlet.freestyler.model.FreestylerFolder;
import com.ext.portlet.freestyler.model.impl.FreestylerFolderImpl;
import com.ext.portlet.freestyler.model.impl.FreestylerFolderModelImpl;

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


public class FreestylerFolderPersistenceImpl extends BasePersistenceImpl
    implements FreestylerFolderPersistence {
    public static final String FINDER_CLASS_NAME_ENTITY = FreestylerFolderImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST = FINDER_CLASS_NAME_ENTITY +
        ".List";
    public static final FinderPath FINDER_PATH_FIND_BY_GROUPID = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByGroupId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_GROUPID = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByGroupId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByGroupId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_COMPANYID = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByCompanyId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_COMPANYID = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByCompanyId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_COMPANYID = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByCompanyId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_G_P = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByG_P",
            new String[] { Long.class.getName(), Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_G_P = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByG_P",
            new String[] {
                Long.class.getName(), Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_G_P = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByG_P",
            new String[] { Long.class.getName(), Long.class.getName() });
    public static final FinderPath FINDER_PATH_FETCH_BY_G_P_N = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_ENTITY, "fetchByG_P_N",
            new String[] {
                Long.class.getName(), Long.class.getName(),
                String.class.getName()
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_G_P_N = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByG_P_N",
            new String[] {
                Long.class.getName(), Long.class.getName(),
                String.class.getName()
            });
    public static final FinderPath FINDER_PATH_FIND_ALL = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countAll", new String[0]);
    private static Log _log = LogFactoryUtil.getLog(FreestylerFolderPersistenceImpl.class);
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerFolderPersistence.impl")
    protected com.ext.portlet.freestyler.service.persistence.FreestylerFolderPersistence freestylerFolderPersistence;
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerImagePersistence.impl")
    protected com.ext.portlet.freestyler.service.persistence.FreestylerImagePersistence freestylerImagePersistence;
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerEntryPersistence.impl")
    protected com.ext.portlet.freestyler.service.persistence.FreestylerEntryPersistence freestylerEntryPersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.ImagePersistence.impl")
    protected com.liferay.portal.service.persistence.ImagePersistence imagePersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.LayoutPersistence.impl")
    protected com.liferay.portal.service.persistence.LayoutPersistence layoutPersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.ResourcePersistence.impl")
    protected com.liferay.portal.service.persistence.ResourcePersistence resourcePersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected com.liferay.portal.service.persistence.UserPersistence userPersistence;
    @BeanReference(name = "com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence.impl")
    protected com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence expandoValuePersistence;

    public void cacheResult(FreestylerFolder freestylerFolder) {
        EntityCacheUtil.putResult(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderImpl.class, freestylerFolder.getPrimaryKey(),
            freestylerFolder);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_P_N,
            new Object[] {
                new Long(freestylerFolder.getGroupId()),
                new Long(freestylerFolder.getParentFolderId()),
                
            freestylerFolder.getName()
            }, freestylerFolder);
    }

    public void cacheResult(List<FreestylerFolder> freestylerFolders) {
        for (FreestylerFolder freestylerFolder : freestylerFolders) {
            if (EntityCacheUtil.getResult(
                        FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
                        FreestylerFolderImpl.class,
                        freestylerFolder.getPrimaryKey(), this) == null) {
                cacheResult(freestylerFolder);
            }
        }
    }

    public void clearCache() {
        CacheRegistry.clear(FreestylerFolderImpl.class.getName());
        EntityCacheUtil.clearCache(FreestylerFolderImpl.class.getName());
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);
    }

    public FreestylerFolder create(long folderId) {
        FreestylerFolder freestylerFolder = new FreestylerFolderImpl();

        freestylerFolder.setNew(true);
        freestylerFolder.setPrimaryKey(folderId);

        return freestylerFolder;
    }

    public FreestylerFolder remove(long folderId)
        throws NoSuchFolderException, SystemException {
        Session session = null;

        try {
            session = openSession();

            FreestylerFolder freestylerFolder = (FreestylerFolder) session.get(FreestylerFolderImpl.class,
                    new Long(folderId));

            if (freestylerFolder == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn(
                        "No FreestylerFolder exists with the primary key " +
                        folderId);
                }

                throw new NoSuchFolderException(
                    "No FreestylerFolder exists with the primary key " +
                    folderId);
            }

            return remove(freestylerFolder);
        } catch (NoSuchFolderException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public FreestylerFolder remove(FreestylerFolder freestylerFolder)
        throws SystemException {
        for (ModelListener<FreestylerFolder> listener : listeners) {
            listener.onBeforeRemove(freestylerFolder);
        }

        freestylerFolder = removeImpl(freestylerFolder);

        for (ModelListener<FreestylerFolder> listener : listeners) {
            listener.onAfterRemove(freestylerFolder);
        }

        return freestylerFolder;
    }

    protected FreestylerFolder removeImpl(FreestylerFolder freestylerFolder)
        throws SystemException {
        Session session = null;

        try {
            session = openSession();

            if (freestylerFolder.isCachedModel() ||
                    BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(FreestylerFolderImpl.class,
                        freestylerFolder.getPrimaryKeyObj());

                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }

            session.delete(freestylerFolder);

            session.flush();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        FreestylerFolderModelImpl freestylerFolderModelImpl = (FreestylerFolderModelImpl) freestylerFolder;

        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_P_N,
            new Object[] {
                new Long(freestylerFolderModelImpl.getOriginalGroupId()),
                new Long(freestylerFolderModelImpl.getOriginalParentFolderId()),
                
            freestylerFolderModelImpl.getOriginalName()
            });

        EntityCacheUtil.removeResult(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderImpl.class, freestylerFolder.getPrimaryKey());

        return freestylerFolder;
    }

    /**
     * @deprecated Use <code>update(FreestylerFolder freestylerFolder, boolean merge)</code>.
     */
    public FreestylerFolder update(FreestylerFolder freestylerFolder)
        throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn(
                "Using the deprecated update(FreestylerFolder freestylerFolder) method. Use update(FreestylerFolder freestylerFolder, boolean merge) instead.");
        }

        return update(freestylerFolder, false);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                freestylerFolder the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when freestylerFolder is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public FreestylerFolder update(FreestylerFolder freestylerFolder,
        boolean merge) throws SystemException {
        boolean isNew = freestylerFolder.isNew();

        for (ModelListener<FreestylerFolder> listener : listeners) {
            if (isNew) {
                listener.onBeforeCreate(freestylerFolder);
            } else {
                listener.onBeforeUpdate(freestylerFolder);
            }
        }

        freestylerFolder = updateImpl(freestylerFolder, merge);

        for (ModelListener<FreestylerFolder> listener : listeners) {
            if (isNew) {
                listener.onAfterCreate(freestylerFolder);
            } else {
                listener.onAfterUpdate(freestylerFolder);
            }
        }

        return freestylerFolder;
    }

    public FreestylerFolder updateImpl(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder,
        boolean merge) throws SystemException {
        boolean isNew = freestylerFolder.isNew();

        FreestylerFolderModelImpl freestylerFolderModelImpl = (FreestylerFolderModelImpl) freestylerFolder;

        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.update(session, freestylerFolder, merge);

            freestylerFolder.setNew(false);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.putResult(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerFolderImpl.class, freestylerFolder.getPrimaryKey(),
            freestylerFolder);

        if (!isNew &&
                ((freestylerFolder.getGroupId() != freestylerFolderModelImpl.getOriginalGroupId()) ||
                (freestylerFolder.getParentFolderId() != freestylerFolderModelImpl.getOriginalParentFolderId()) ||
                !Validator.equals(freestylerFolder.getName(),
                    freestylerFolderModelImpl.getOriginalName()))) {
            FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_G_P_N,
                new Object[] {
                    new Long(freestylerFolderModelImpl.getOriginalGroupId()),
                    new Long(freestylerFolderModelImpl.getOriginalParentFolderId()),
                    
                freestylerFolderModelImpl.getOriginalName()
                });
        }

        if (isNew ||
                ((freestylerFolder.getGroupId() != freestylerFolderModelImpl.getOriginalGroupId()) ||
                (freestylerFolder.getParentFolderId() != freestylerFolderModelImpl.getOriginalParentFolderId()) ||
                !Validator.equals(freestylerFolder.getName(),
                    freestylerFolderModelImpl.getOriginalName()))) {
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_P_N,
                new Object[] {
                    new Long(freestylerFolder.getGroupId()),
                    new Long(freestylerFolder.getParentFolderId()),
                    
                freestylerFolder.getName()
                }, freestylerFolder);
        }

        return freestylerFolder;
    }

    public FreestylerFolder findByPrimaryKey(long folderId)
        throws NoSuchFolderException, SystemException {
        FreestylerFolder freestylerFolder = fetchByPrimaryKey(folderId);

        if (freestylerFolder == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No FreestylerFolder exists with the primary key " +
                    folderId);
            }

            throw new NoSuchFolderException(
                "No FreestylerFolder exists with the primary key " + folderId);
        }

        return freestylerFolder;
    }

    public FreestylerFolder fetchByPrimaryKey(long folderId)
        throws SystemException {
        FreestylerFolder freestylerFolder = (FreestylerFolder) EntityCacheUtil.getResult(FreestylerFolderModelImpl.ENTITY_CACHE_ENABLED,
                FreestylerFolderImpl.class, folderId, this);

        if (freestylerFolder == null) {
            Session session = null;

            try {
                session = openSession();

                freestylerFolder = (FreestylerFolder) session.get(FreestylerFolderImpl.class,
                        new Long(folderId));
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (freestylerFolder != null) {
                    cacheResult(freestylerFolder);
                }

                closeSession(session);
            }
        }

        return freestylerFolder;
    }

    public List<FreestylerFolder> findByGroupId(long groupId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(groupId) };

        List<FreestylerFolder> list = (List<FreestylerFolder>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_GROUPID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

                query.append("groupId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("folderId ASC, ");
                query.append("name ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerFolder>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_GROUPID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerFolder> findByGroupId(long groupId, int start, int end)
        throws SystemException {
        return findByGroupId(groupId, start, end, null);
    }

    public List<FreestylerFolder> findByGroupId(long groupId, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(groupId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerFolder> list = (List<FreestylerFolder>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_GROUPID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

                query.append("groupId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("folderId ASC, ");
                    query.append("name ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                list = (List<FreestylerFolder>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerFolder>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_GROUPID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerFolder findByGroupId_First(long groupId,
        OrderByComparator obc) throws NoSuchFolderException, SystemException {
        List<FreestylerFolder> list = findByGroupId(groupId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerFolder exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchFolderException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerFolder findByGroupId_Last(long groupId,
        OrderByComparator obc) throws NoSuchFolderException, SystemException {
        int count = countByGroupId(groupId);

        List<FreestylerFolder> list = findByGroupId(groupId, count - 1, count,
                obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerFolder exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchFolderException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerFolder[] findByGroupId_PrevAndNext(long folderId,
        long groupId, OrderByComparator obc)
        throws NoSuchFolderException, SystemException {
        FreestylerFolder freestylerFolder = findByPrimaryKey(folderId);

        int count = countByGroupId(groupId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

            query.append("groupId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("folderId ASC, ");
                query.append("name ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(groupId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerFolder);

            FreestylerFolder[] array = new FreestylerFolderImpl[3];

            array[0] = (FreestylerFolder) objArray[0];
            array[1] = (FreestylerFolder) objArray[1];
            array[2] = (FreestylerFolder) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<FreestylerFolder> findByCompanyId(long companyId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(companyId) };

        List<FreestylerFolder> list = (List<FreestylerFolder>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_COMPANYID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

                query.append("companyId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("folderId ASC, ");
                query.append("name ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(companyId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerFolder>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_COMPANYID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerFolder> findByCompanyId(long companyId, int start,
        int end) throws SystemException {
        return findByCompanyId(companyId, start, end, null);
    }

    public List<FreestylerFolder> findByCompanyId(long companyId, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(companyId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerFolder> list = (List<FreestylerFolder>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_COMPANYID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

                query.append("companyId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("folderId ASC, ");
                    query.append("name ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(companyId);

                list = (List<FreestylerFolder>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerFolder>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_COMPANYID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerFolder findByCompanyId_First(long companyId,
        OrderByComparator obc) throws NoSuchFolderException, SystemException {
        List<FreestylerFolder> list = findByCompanyId(companyId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerFolder exists with the key {");

            msg.append("companyId=" + companyId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchFolderException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerFolder findByCompanyId_Last(long companyId,
        OrderByComparator obc) throws NoSuchFolderException, SystemException {
        int count = countByCompanyId(companyId);

        List<FreestylerFolder> list = findByCompanyId(companyId, count - 1,
                count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerFolder exists with the key {");

            msg.append("companyId=" + companyId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchFolderException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerFolder[] findByCompanyId_PrevAndNext(long folderId,
        long companyId, OrderByComparator obc)
        throws NoSuchFolderException, SystemException {
        FreestylerFolder freestylerFolder = findByPrimaryKey(folderId);

        int count = countByCompanyId(companyId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

            query.append("companyId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("folderId ASC, ");
                query.append("name ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(companyId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerFolder);

            FreestylerFolder[] array = new FreestylerFolderImpl[3];

            array[0] = (FreestylerFolder) objArray[0];
            array[1] = (FreestylerFolder) objArray[1];
            array[2] = (FreestylerFolder) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<FreestylerFolder> findByG_P(long groupId, long parentFolderId)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(groupId), new Long(parentFolderId)
            };

        List<FreestylerFolder> list = (List<FreestylerFolder>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_G_P,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

                query.append("groupId = ?");

                query.append(" AND ");

                query.append("parentFolderId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("folderId ASC, ");
                query.append("name ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                qPos.add(parentFolderId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerFolder>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_G_P, finderArgs,
                    list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerFolder> findByG_P(long groupId, long parentFolderId,
        int start, int end) throws SystemException {
        return findByG_P(groupId, parentFolderId, start, end, null);
    }

    public List<FreestylerFolder> findByG_P(long groupId, long parentFolderId,
        int start, int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(groupId), new Long(parentFolderId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerFolder> list = (List<FreestylerFolder>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_G_P,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

                query.append("groupId = ?");

                query.append(" AND ");

                query.append("parentFolderId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("folderId ASC, ");
                    query.append("name ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                qPos.add(parentFolderId);

                list = (List<FreestylerFolder>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerFolder>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_G_P,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerFolder findByG_P_First(long groupId, long parentFolderId,
        OrderByComparator obc) throws NoSuchFolderException, SystemException {
        List<FreestylerFolder> list = findByG_P(groupId, parentFolderId, 0, 1,
                obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerFolder exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(", ");
            msg.append("parentFolderId=" + parentFolderId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchFolderException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerFolder findByG_P_Last(long groupId, long parentFolderId,
        OrderByComparator obc) throws NoSuchFolderException, SystemException {
        int count = countByG_P(groupId, parentFolderId);

        List<FreestylerFolder> list = findByG_P(groupId, parentFolderId,
                count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerFolder exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(", ");
            msg.append("parentFolderId=" + parentFolderId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchFolderException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerFolder[] findByG_P_PrevAndNext(long folderId,
        long groupId, long parentFolderId, OrderByComparator obc)
        throws NoSuchFolderException, SystemException {
        FreestylerFolder freestylerFolder = findByPrimaryKey(folderId);

        int count = countByG_P(groupId, parentFolderId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

            query.append("groupId = ?");

            query.append(" AND ");

            query.append("parentFolderId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("folderId ASC, ");
                query.append("name ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(groupId);

            qPos.add(parentFolderId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerFolder);

            FreestylerFolder[] array = new FreestylerFolderImpl[3];

            array[0] = (FreestylerFolder) objArray[0];
            array[1] = (FreestylerFolder) objArray[1];
            array[2] = (FreestylerFolder) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public FreestylerFolder findByG_P_N(long groupId, long parentFolderId,
        String name) throws NoSuchFolderException, SystemException {
        FreestylerFolder freestylerFolder = fetchByG_P_N(groupId,
                parentFolderId, name);

        if (freestylerFolder == null) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerFolder exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(", ");
            msg.append("parentFolderId=" + parentFolderId);

            msg.append(", ");
            msg.append("name=" + name);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchFolderException(msg.toString());
        }

        return freestylerFolder;
    }

    public FreestylerFolder fetchByG_P_N(long groupId, long parentFolderId,
        String name) throws SystemException {
        return fetchByG_P_N(groupId, parentFolderId, name, true);
    }

    public FreestylerFolder fetchByG_P_N(long groupId, long parentFolderId,
        String name, boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(groupId), new Long(parentFolderId),
                
                name
            };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_G_P_N,
                    finderArgs, this);
        }

        if (result == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

                query.append("groupId = ?");

                query.append(" AND ");

                query.append("parentFolderId = ?");

                query.append(" AND ");

                if (name == null) {
                    query.append("name IS NULL");
                } else {
                    query.append("name = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("folderId ASC, ");
                query.append("name ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                qPos.add(parentFolderId);

                if (name != null) {
                    qPos.add(name);
                }

                List<FreestylerFolder> list = q.list();

                result = list;

                FreestylerFolder freestylerFolder = null;

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_P_N,
                        finderArgs, list);
                } else {
                    freestylerFolder = list.get(0);

                    cacheResult(freestylerFolder);

                    if ((freestylerFolder.getGroupId() != groupId) ||
                            (freestylerFolder.getParentFolderId() != parentFolderId) ||
                            (freestylerFolder.getName() == null) ||
                            !freestylerFolder.getName().equals(name)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_P_N,
                            finderArgs, freestylerFolder);
                    }
                }

                return freestylerFolder;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (result == null) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_G_P_N,
                        finderArgs, new ArrayList<FreestylerFolder>());
                }

                closeSession(session);
            }
        } else {
            if (result instanceof List) {
                return null;
            } else {
                return (FreestylerFolder) result;
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

    public List<FreestylerFolder> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<FreestylerFolder> findAll(int start, int end)
        throws SystemException {
        return findAll(start, end, null);
    }

    public List<FreestylerFolder> findAll(int start, int end,
        OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerFolder> list = (List<FreestylerFolder>) FinderCacheUtil.getResult(FINDER_PATH_FIND_ALL,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("folderId ASC, ");
                    query.append("name ASC");
                }

                Query q = session.createQuery(query.toString());

                if (obc == null) {
                    list = (List<FreestylerFolder>) QueryUtil.list(q,
                            getDialect(), start, end, false);

                    Collections.sort(list);
                } else {
                    list = (List<FreestylerFolder>) QueryUtil.list(q,
                            getDialect(), start, end);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerFolder>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_ALL, finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public void removeByGroupId(long groupId) throws SystemException {
        for (FreestylerFolder freestylerFolder : findByGroupId(groupId)) {
            remove(freestylerFolder);
        }
    }

    public void removeByCompanyId(long companyId) throws SystemException {
        for (FreestylerFolder freestylerFolder : findByCompanyId(companyId)) {
            remove(freestylerFolder);
        }
    }

    public void removeByG_P(long groupId, long parentFolderId)
        throws SystemException {
        for (FreestylerFolder freestylerFolder : findByG_P(groupId,
                parentFolderId)) {
            remove(freestylerFolder);
        }
    }

    public void removeByG_P_N(long groupId, long parentFolderId, String name)
        throws NoSuchFolderException, SystemException {
        FreestylerFolder freestylerFolder = findByG_P_N(groupId,
                parentFolderId, name);

        remove(freestylerFolder);
    }

    public void removeAll() throws SystemException {
        for (FreestylerFolder freestylerFolder : findAll()) {
            remove(freestylerFolder);
        }
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
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

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
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

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

    public int countByG_P(long groupId, long parentFolderId)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(groupId), new Long(parentFolderId)
            };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_G_P,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

                query.append("groupId = ?");

                query.append(" AND ");

                query.append("parentFolderId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                qPos.add(parentFolderId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_P, finderArgs,
                    count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByG_P_N(long groupId, long parentFolderId, String name)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(groupId), new Long(parentFolderId),
                
                name
            };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_G_P_N,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerFolder WHERE ");

                query.append("groupId = ?");

                query.append(" AND ");

                query.append("parentFolderId = ?");

                query.append(" AND ");

                if (name == null) {
                    query.append("name IS NULL");
                } else {
                    query.append("name = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                qPos.add(parentFolderId);

                if (name != null) {
                    qPos.add(name);
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_P_N,
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
                        "SELECT COUNT(*) FROM com.ext.portlet.freestyler.model.FreestylerFolder");

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
                        "value.object.listener.com.ext.portlet.freestyler.model.FreestylerFolder")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<FreestylerFolder>> listenersList = new ArrayList<ModelListener<FreestylerFolder>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<FreestylerFolder>) Class.forName(
                            listenerClassName).newInstance());
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }
}
