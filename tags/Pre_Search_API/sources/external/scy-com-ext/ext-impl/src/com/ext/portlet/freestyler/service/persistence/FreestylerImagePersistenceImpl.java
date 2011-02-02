package com.ext.portlet.freestyler.service.persistence;

import com.ext.portlet.freestyler.NoSuchImageException;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.model.impl.FreestylerImageImpl;
import com.ext.portlet.freestyler.model.impl.FreestylerImageModelImpl;

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


public class FreestylerImagePersistenceImpl extends BasePersistenceImpl
    implements FreestylerImagePersistence {
    public static final String FINDER_CLASS_NAME_ENTITY = FreestylerImageImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST = FINDER_CLASS_NAME_ENTITY +
        ".List";
    public static final FinderPath FINDER_PATH_FIND_BY_GROUPID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByGroupId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_GROUPID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByGroupId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByGroupId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_FOLDERID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByFolderId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_FOLDERID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByFolderId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_FOLDERID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByFolderId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_FREESTYLERID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByFreestylerId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_FREESTYLERID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByFreestylerId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_FREESTYLERID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByFreestylerId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FETCH_BY_SMALLIMAGEID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_ENTITY, "fetchBySmallImageId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_COUNT_BY_SMALLIMAGEID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countBySmallImageId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FETCH_BY_LARGEIMAGEID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_ENTITY, "fetchByLargeImageId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_COUNT_BY_LARGEIMAGEID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByLargeImageId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FETCH_BY_CUSTOM1IMAGEID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_ENTITY, "fetchByCustom1ImageId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_COUNT_BY_CUSTOM1IMAGEID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByCustom1ImageId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FETCH_BY_CUSTOM2IMAGEID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_ENTITY, "fetchByCustom2ImageId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_COUNT_BY_CUSTOM2IMAGEID = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByCustom2ImageId",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_G_U = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByG_U",
            new String[] { Long.class.getName(), Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_G_U = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByG_U",
            new String[] {
                Long.class.getName(), Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_G_U = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByG_U",
            new String[] { Long.class.getName(), Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_F_N = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByF_N",
            new String[] { Long.class.getName(), String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_F_N = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByF_N",
            new String[] {
                Long.class.getName(), String.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_F_N = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByF_N",
            new String[] { Long.class.getName(), String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_F_F = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByF_F",
            new String[] { Long.class.getName(), Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_F_F = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findByF_F",
            new String[] {
                Long.class.getName(), Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_F_F = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countByF_F",
            new String[] { Long.class.getName(), Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_ALL = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageModelImpl.FINDER_CACHE_ENABLED,
            FINDER_CLASS_NAME_LIST, "countAll", new String[0]);
    private static Log _log = LogFactoryUtil.getLog(FreestylerImagePersistenceImpl.class);
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerFolderPersistence.impl")
    protected com.ext.portlet.freestyler.service.persistence.FreestylerFolderPersistence freestylerFolderPersistence;
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerImagePersistence.impl")
    protected com.ext.portlet.freestyler.service.persistence.FreestylerImagePersistence freestylerImagePersistence;
    @BeanReference(name = "com.ext.portlet.freestyler.service.persistence.FreestylerEntryPersistence.impl")
    protected com.ext.portlet.freestyler.service.persistence.FreestylerEntryPersistence freestylerEntryPersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.ImagePersistence.impl")
    protected com.liferay.portal.service.persistence.ImagePersistence imagePersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.ResourcePersistence.impl")
    protected com.liferay.portal.service.persistence.ResourcePersistence resourcePersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected com.liferay.portal.service.persistence.UserPersistence userPersistence;
    @BeanReference(name = "com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence.impl")
    protected com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence expandoValuePersistence;
    @BeanReference(name = "com.liferay.portlet.tags.service.persistence.TagsAssetPersistence.impl")
    protected com.liferay.portlet.tags.service.persistence.TagsAssetPersistence tagsAssetPersistence;
    @BeanReference(name = "com.liferay.portlet.tags.service.persistence.TagsEntryPersistence.impl")
    protected com.liferay.portlet.tags.service.persistence.TagsEntryPersistence tagsEntryPersistence;

    public void cacheResult(FreestylerImage freestylerImage) {
        EntityCacheUtil.putResult(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageImpl.class, freestylerImage.getPrimaryKey(),
            freestylerImage);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_SMALLIMAGEID,
            new Object[] { new Long(freestylerImage.getSmallImageId()) },
            freestylerImage);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_LARGEIMAGEID,
            new Object[] { new Long(freestylerImage.getLargeImageId()) },
            freestylerImage);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_CUSTOM1IMAGEID,
            new Object[] { new Long(freestylerImage.getCustom1ImageId()) },
            freestylerImage);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_CUSTOM2IMAGEID,
            new Object[] { new Long(freestylerImage.getCustom2ImageId()) },
            freestylerImage);
    }

    public void cacheResult(List<FreestylerImage> freestylerImages) {
        for (FreestylerImage freestylerImage : freestylerImages) {
            if (EntityCacheUtil.getResult(
                        FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
                        FreestylerImageImpl.class,
                        freestylerImage.getPrimaryKey(), this) == null) {
                cacheResult(freestylerImage);
            }
        }
    }

    public void clearCache() {
        CacheRegistry.clear(FreestylerImageImpl.class.getName());
        EntityCacheUtil.clearCache(FreestylerImageImpl.class.getName());
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);
    }

    public FreestylerImage create(long imageId) {
        FreestylerImage freestylerImage = new FreestylerImageImpl();

        freestylerImage.setNew(true);
        freestylerImage.setPrimaryKey(imageId);

        return freestylerImage;
    }

    public FreestylerImage remove(long imageId)
        throws NoSuchImageException, SystemException {
        Session session = null;

        try {
            session = openSession();

            FreestylerImage freestylerImage = (FreestylerImage) session.get(FreestylerImageImpl.class,
                    new Long(imageId));

            if (freestylerImage == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No FreestylerImage exists with the primary key " +
                        imageId);
                }

                throw new NoSuchImageException(
                    "No FreestylerImage exists with the primary key " +
                    imageId);
            }

            return remove(freestylerImage);
        } catch (NoSuchImageException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public FreestylerImage remove(FreestylerImage freestylerImage)
        throws SystemException {
        for (ModelListener<FreestylerImage> listener : listeners) {
            listener.onBeforeRemove(freestylerImage);
        }

        freestylerImage = removeImpl(freestylerImage);

        for (ModelListener<FreestylerImage> listener : listeners) {
            listener.onAfterRemove(freestylerImage);
        }

        return freestylerImage;
    }

    protected FreestylerImage removeImpl(FreestylerImage freestylerImage)
        throws SystemException {
        Session session = null;

        try {
            session = openSession();

            if (freestylerImage.isCachedModel() ||
                    BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(FreestylerImageImpl.class,
                        freestylerImage.getPrimaryKeyObj());

                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }

            session.delete(freestylerImage);

            session.flush();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        FreestylerImageModelImpl freestylerImageModelImpl = (FreestylerImageModelImpl) freestylerImage;

        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_SMALLIMAGEID,
            new Object[] {
                new Long(freestylerImageModelImpl.getOriginalSmallImageId())
            });

        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_LARGEIMAGEID,
            new Object[] {
                new Long(freestylerImageModelImpl.getOriginalLargeImageId())
            });

        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_CUSTOM1IMAGEID,
            new Object[] {
                new Long(freestylerImageModelImpl.getOriginalCustom1ImageId())
            });

        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_CUSTOM2IMAGEID,
            new Object[] {
                new Long(freestylerImageModelImpl.getOriginalCustom2ImageId())
            });

        EntityCacheUtil.removeResult(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageImpl.class, freestylerImage.getPrimaryKey());

        return freestylerImage;
    }

    /**
     * @deprecated Use <code>update(FreestylerImage freestylerImage, boolean merge)</code>.
     */
    public FreestylerImage update(FreestylerImage freestylerImage)
        throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn(
                "Using the deprecated update(FreestylerImage freestylerImage) method. Use update(FreestylerImage freestylerImage, boolean merge) instead.");
        }

        return update(freestylerImage, false);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                freestylerImage the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when freestylerImage is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public FreestylerImage update(FreestylerImage freestylerImage, boolean merge)
        throws SystemException {
        boolean isNew = freestylerImage.isNew();

        for (ModelListener<FreestylerImage> listener : listeners) {
            if (isNew) {
                listener.onBeforeCreate(freestylerImage);
            } else {
                listener.onBeforeUpdate(freestylerImage);
            }
        }

        freestylerImage = updateImpl(freestylerImage, merge);

        for (ModelListener<FreestylerImage> listener : listeners) {
            if (isNew) {
                listener.onAfterCreate(freestylerImage);
            } else {
                listener.onAfterUpdate(freestylerImage);
            }
        }

        return freestylerImage;
    }

    public FreestylerImage updateImpl(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage,
        boolean merge) throws SystemException {
        boolean isNew = freestylerImage.isNew();

        FreestylerImageModelImpl freestylerImageModelImpl = (FreestylerImageModelImpl) freestylerImage;

        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.update(session, freestylerImage, merge);

            freestylerImage.setNew(false);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.putResult(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
            FreestylerImageImpl.class, freestylerImage.getPrimaryKey(),
            freestylerImage);

        if (!isNew &&
                (freestylerImage.getSmallImageId() != freestylerImageModelImpl.getOriginalSmallImageId())) {
            FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_SMALLIMAGEID,
                new Object[] {
                    new Long(freestylerImageModelImpl.getOriginalSmallImageId())
                });
        }

        if (isNew ||
                (freestylerImage.getSmallImageId() != freestylerImageModelImpl.getOriginalSmallImageId())) {
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_SMALLIMAGEID,
                new Object[] { new Long(freestylerImage.getSmallImageId()) },
                freestylerImage);
        }

        if (!isNew &&
                (freestylerImage.getLargeImageId() != freestylerImageModelImpl.getOriginalLargeImageId())) {
            FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_LARGEIMAGEID,
                new Object[] {
                    new Long(freestylerImageModelImpl.getOriginalLargeImageId())
                });
        }

        if (isNew ||
                (freestylerImage.getLargeImageId() != freestylerImageModelImpl.getOriginalLargeImageId())) {
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_LARGEIMAGEID,
                new Object[] { new Long(freestylerImage.getLargeImageId()) },
                freestylerImage);
        }

        if (!isNew &&
                (freestylerImage.getCustom1ImageId() != freestylerImageModelImpl.getOriginalCustom1ImageId())) {
            FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_CUSTOM1IMAGEID,
                new Object[] {
                    new Long(freestylerImageModelImpl.getOriginalCustom1ImageId())
                });
        }

        if (isNew ||
                (freestylerImage.getCustom1ImageId() != freestylerImageModelImpl.getOriginalCustom1ImageId())) {
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_CUSTOM1IMAGEID,
                new Object[] { new Long(freestylerImage.getCustom1ImageId()) },
                freestylerImage);
        }

        if (!isNew &&
                (freestylerImage.getCustom2ImageId() != freestylerImageModelImpl.getOriginalCustom2ImageId())) {
            FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_CUSTOM2IMAGEID,
                new Object[] {
                    new Long(freestylerImageModelImpl.getOriginalCustom2ImageId())
                });
        }

        if (isNew ||
                (freestylerImage.getCustom2ImageId() != freestylerImageModelImpl.getOriginalCustom2ImageId())) {
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_CUSTOM2IMAGEID,
                new Object[] { new Long(freestylerImage.getCustom2ImageId()) },
                freestylerImage);
        }

        return freestylerImage;
    }

    public FreestylerImage findByPrimaryKey(long imageId)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = fetchByPrimaryKey(imageId);

        if (freestylerImage == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No FreestylerImage exists with the primary key " +
                    imageId);
            }

            throw new NoSuchImageException(
                "No FreestylerImage exists with the primary key " + imageId);
        }

        return freestylerImage;
    }

    public FreestylerImage fetchByPrimaryKey(long imageId)
        throws SystemException {
        FreestylerImage freestylerImage = (FreestylerImage) EntityCacheUtil.getResult(FreestylerImageModelImpl.ENTITY_CACHE_ENABLED,
                FreestylerImageImpl.class, imageId, this);

        if (freestylerImage == null) {
            Session session = null;

            try {
                session = openSession();

                freestylerImage = (FreestylerImage) session.get(FreestylerImageImpl.class,
                        new Long(imageId));
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (freestylerImage != null) {
                    cacheResult(freestylerImage);
                }

                closeSession(session);
            }
        }

        return freestylerImage;
    }

    public List<FreestylerImage> findByGroupId(long groupId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(groupId) };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_GROUPID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("groupId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("imageId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_GROUPID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerImage> findByGroupId(long groupId, int start, int end)
        throws SystemException {
        return findByGroupId(groupId, start, end, null);
    }

    public List<FreestylerImage> findByGroupId(long groupId, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(groupId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_GROUPID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("groupId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("imageId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                list = (List<FreestylerImage>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_GROUPID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerImage findByGroupId_First(long groupId,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        List<FreestylerImage> list = findByGroupId(groupId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage findByGroupId_Last(long groupId,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        int count = countByGroupId(groupId);

        List<FreestylerImage> list = findByGroupId(groupId, count - 1, count,
                obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage[] findByGroupId_PrevAndNext(long imageId,
        long groupId, OrderByComparator obc)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = findByPrimaryKey(imageId);

        int count = countByGroupId(groupId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

            query.append("groupId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("imageId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(groupId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerImage);

            FreestylerImage[] array = new FreestylerImageImpl[3];

            array[0] = (FreestylerImage) objArray[0];
            array[1] = (FreestylerImage) objArray[1];
            array[2] = (FreestylerImage) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<FreestylerImage> findByFolderId(long folderId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(folderId) };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_FOLDERID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("folderId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("imageId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(folderId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_FOLDERID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerImage> findByFolderId(long folderId, int start,
        int end) throws SystemException {
        return findByFolderId(folderId, start, end, null);
    }

    public List<FreestylerImage> findByFolderId(long folderId, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(folderId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_FOLDERID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("folderId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("imageId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(folderId);

                list = (List<FreestylerImage>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_FOLDERID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerImage findByFolderId_First(long folderId,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        List<FreestylerImage> list = findByFolderId(folderId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("folderId=" + folderId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage findByFolderId_Last(long folderId,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        int count = countByFolderId(folderId);

        List<FreestylerImage> list = findByFolderId(folderId, count - 1, count,
                obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("folderId=" + folderId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage[] findByFolderId_PrevAndNext(long imageId,
        long folderId, OrderByComparator obc)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = findByPrimaryKey(imageId);

        int count = countByFolderId(folderId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

            query.append("folderId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("imageId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(folderId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerImage);

            FreestylerImage[] array = new FreestylerImageImpl[3];

            array[0] = (FreestylerImage) objArray[0];
            array[1] = (FreestylerImage) objArray[1];
            array[2] = (FreestylerImage) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<FreestylerImage> findByFreestylerId(long freestylerId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(freestylerId) };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_FREESTYLERID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("freestylerId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("imageId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(freestylerId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_FREESTYLERID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerImage> findByFreestylerId(long freestylerId,
        int start, int end) throws SystemException {
        return findByFreestylerId(freestylerId, start, end, null);
    }

    public List<FreestylerImage> findByFreestylerId(long freestylerId,
        int start, int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(freestylerId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_FREESTYLERID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("freestylerId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("imageId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(freestylerId);

                list = (List<FreestylerImage>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_FREESTYLERID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerImage findByFreestylerId_First(long freestylerId,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        List<FreestylerImage> list = findByFreestylerId(freestylerId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("freestylerId=" + freestylerId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage findByFreestylerId_Last(long freestylerId,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        int count = countByFreestylerId(freestylerId);

        List<FreestylerImage> list = findByFreestylerId(freestylerId,
                count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("freestylerId=" + freestylerId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage[] findByFreestylerId_PrevAndNext(long imageId,
        long freestylerId, OrderByComparator obc)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = findByPrimaryKey(imageId);

        int count = countByFreestylerId(freestylerId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

            query.append("freestylerId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("imageId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(freestylerId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerImage);

            FreestylerImage[] array = new FreestylerImageImpl[3];

            array[0] = (FreestylerImage) objArray[0];
            array[1] = (FreestylerImage) objArray[1];
            array[2] = (FreestylerImage) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public FreestylerImage findBySmallImageId(long smallImageId)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = fetchBySmallImageId(smallImageId);

        if (freestylerImage == null) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("smallImageId=" + smallImageId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchImageException(msg.toString());
        }

        return freestylerImage;
    }

    public FreestylerImage fetchBySmallImageId(long smallImageId)
        throws SystemException {
        return fetchBySmallImageId(smallImageId, true);
    }

    public FreestylerImage fetchBySmallImageId(long smallImageId,
        boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(smallImageId) };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_SMALLIMAGEID,
                    finderArgs, this);
        }

        if (result == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("smallImageId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("imageId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(smallImageId);

                List<FreestylerImage> list = q.list();

                result = list;

                FreestylerImage freestylerImage = null;

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_SMALLIMAGEID,
                        finderArgs, list);
                } else {
                    freestylerImage = list.get(0);

                    cacheResult(freestylerImage);

                    if ((freestylerImage.getSmallImageId() != smallImageId)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_SMALLIMAGEID,
                            finderArgs, freestylerImage);
                    }
                }

                return freestylerImage;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (result == null) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_SMALLIMAGEID,
                        finderArgs, new ArrayList<FreestylerImage>());
                }

                closeSession(session);
            }
        } else {
            if (result instanceof List) {
                return null;
            } else {
                return (FreestylerImage) result;
            }
        }
    }

    public FreestylerImage findByLargeImageId(long largeImageId)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = fetchByLargeImageId(largeImageId);

        if (freestylerImage == null) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("largeImageId=" + largeImageId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchImageException(msg.toString());
        }

        return freestylerImage;
    }

    public FreestylerImage fetchByLargeImageId(long largeImageId)
        throws SystemException {
        return fetchByLargeImageId(largeImageId, true);
    }

    public FreestylerImage fetchByLargeImageId(long largeImageId,
        boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(largeImageId) };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_LARGEIMAGEID,
                    finderArgs, this);
        }

        if (result == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("largeImageId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("imageId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(largeImageId);

                List<FreestylerImage> list = q.list();

                result = list;

                FreestylerImage freestylerImage = null;

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_LARGEIMAGEID,
                        finderArgs, list);
                } else {
                    freestylerImage = list.get(0);

                    cacheResult(freestylerImage);

                    if ((freestylerImage.getLargeImageId() != largeImageId)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_LARGEIMAGEID,
                            finderArgs, freestylerImage);
                    }
                }

                return freestylerImage;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (result == null) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_LARGEIMAGEID,
                        finderArgs, new ArrayList<FreestylerImage>());
                }

                closeSession(session);
            }
        } else {
            if (result instanceof List) {
                return null;
            } else {
                return (FreestylerImage) result;
            }
        }
    }

    public FreestylerImage findByCustom1ImageId(long custom1ImageId)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = fetchByCustom1ImageId(custom1ImageId);

        if (freestylerImage == null) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("custom1ImageId=" + custom1ImageId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchImageException(msg.toString());
        }

        return freestylerImage;
    }

    public FreestylerImage fetchByCustom1ImageId(long custom1ImageId)
        throws SystemException {
        return fetchByCustom1ImageId(custom1ImageId, true);
    }

    public FreestylerImage fetchByCustom1ImageId(long custom1ImageId,
        boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(custom1ImageId) };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_CUSTOM1IMAGEID,
                    finderArgs, this);
        }

        if (result == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("custom1ImageId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("imageId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(custom1ImageId);

                List<FreestylerImage> list = q.list();

                result = list;

                FreestylerImage freestylerImage = null;

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_CUSTOM1IMAGEID,
                        finderArgs, list);
                } else {
                    freestylerImage = list.get(0);

                    cacheResult(freestylerImage);

                    if ((freestylerImage.getCustom1ImageId() != custom1ImageId)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_CUSTOM1IMAGEID,
                            finderArgs, freestylerImage);
                    }
                }

                return freestylerImage;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (result == null) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_CUSTOM1IMAGEID,
                        finderArgs, new ArrayList<FreestylerImage>());
                }

                closeSession(session);
            }
        } else {
            if (result instanceof List) {
                return null;
            } else {
                return (FreestylerImage) result;
            }
        }
    }

    public FreestylerImage findByCustom2ImageId(long custom2ImageId)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = fetchByCustom2ImageId(custom2ImageId);

        if (freestylerImage == null) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("custom2ImageId=" + custom2ImageId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchImageException(msg.toString());
        }

        return freestylerImage;
    }

    public FreestylerImage fetchByCustom2ImageId(long custom2ImageId)
        throws SystemException {
        return fetchByCustom2ImageId(custom2ImageId, true);
    }

    public FreestylerImage fetchByCustom2ImageId(long custom2ImageId,
        boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(custom2ImageId) };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_CUSTOM2IMAGEID,
                    finderArgs, this);
        }

        if (result == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("custom2ImageId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("imageId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(custom2ImageId);

                List<FreestylerImage> list = q.list();

                result = list;

                FreestylerImage freestylerImage = null;

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_CUSTOM2IMAGEID,
                        finderArgs, list);
                } else {
                    freestylerImage = list.get(0);

                    cacheResult(freestylerImage);

                    if ((freestylerImage.getCustom2ImageId() != custom2ImageId)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_CUSTOM2IMAGEID,
                            finderArgs, freestylerImage);
                    }
                }

                return freestylerImage;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (result == null) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_CUSTOM2IMAGEID,
                        finderArgs, new ArrayList<FreestylerImage>());
                }

                closeSession(session);
            }
        } else {
            if (result instanceof List) {
                return null;
            } else {
                return (FreestylerImage) result;
            }
        }
    }

    public List<FreestylerImage> findByG_U(long groupId, long userId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(groupId), new Long(userId) };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_G_U,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("groupId = ?");

                query.append(" AND ");

                query.append("userId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("imageId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                qPos.add(userId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_G_U, finderArgs,
                    list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerImage> findByG_U(long groupId, long userId,
        int start, int end) throws SystemException {
        return findByG_U(groupId, userId, start, end, null);
    }

    public List<FreestylerImage> findByG_U(long groupId, long userId,
        int start, int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(groupId), new Long(userId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_G_U,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("groupId = ?");

                query.append(" AND ");

                query.append("userId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("imageId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                qPos.add(userId);

                list = (List<FreestylerImage>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_G_U,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerImage findByG_U_First(long groupId, long userId,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        List<FreestylerImage> list = findByG_U(groupId, userId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(", ");
            msg.append("userId=" + userId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage findByG_U_Last(long groupId, long userId,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        int count = countByG_U(groupId, userId);

        List<FreestylerImage> list = findByG_U(groupId, userId, count - 1,
                count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("groupId=" + groupId);

            msg.append(", ");
            msg.append("userId=" + userId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage[] findByG_U_PrevAndNext(long imageId, long groupId,
        long userId, OrderByComparator obc)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = findByPrimaryKey(imageId);

        int count = countByG_U(groupId, userId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

            query.append("groupId = ?");

            query.append(" AND ");

            query.append("userId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("imageId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(groupId);

            qPos.add(userId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerImage);

            FreestylerImage[] array = new FreestylerImageImpl[3];

            array[0] = (FreestylerImage) objArray[0];
            array[1] = (FreestylerImage) objArray[1];
            array[2] = (FreestylerImage) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<FreestylerImage> findByF_N(long folderId, String name)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(folderId), name };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_F_N,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("folderId = ?");

                query.append(" AND ");

                if (name == null) {
                    query.append("name IS NULL");
                } else {
                    query.append("name = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("imageId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(folderId);

                if (name != null) {
                    qPos.add(name);
                }

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_F_N, finderArgs,
                    list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerImage> findByF_N(long folderId, String name,
        int start, int end) throws SystemException {
        return findByF_N(folderId, name, start, end, null);
    }

    public List<FreestylerImage> findByF_N(long folderId, String name,
        int start, int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(folderId),
                
                name,
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_F_N,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("folderId = ?");

                query.append(" AND ");

                if (name == null) {
                    query.append("name IS NULL");
                } else {
                    query.append("name = ?");
                }

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("imageId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(folderId);

                if (name != null) {
                    qPos.add(name);
                }

                list = (List<FreestylerImage>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_F_N,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerImage findByF_N_First(long folderId, String name,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        List<FreestylerImage> list = findByF_N(folderId, name, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("folderId=" + folderId);

            msg.append(", ");
            msg.append("name=" + name);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage findByF_N_Last(long folderId, String name,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        int count = countByF_N(folderId, name);

        List<FreestylerImage> list = findByF_N(folderId, name, count - 1,
                count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("folderId=" + folderId);

            msg.append(", ");
            msg.append("name=" + name);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage[] findByF_N_PrevAndNext(long imageId, long folderId,
        String name, OrderByComparator obc)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = findByPrimaryKey(imageId);

        int count = countByF_N(folderId, name);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

            query.append("folderId = ?");

            query.append(" AND ");

            if (name == null) {
                query.append("name IS NULL");
            } else {
                query.append("name = ?");
            }

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("imageId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(folderId);

            if (name != null) {
                qPos.add(name);
            }

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerImage);

            FreestylerImage[] array = new FreestylerImageImpl[3];

            array[0] = (FreestylerImage) objArray[0];
            array[1] = (FreestylerImage) objArray[1];
            array[2] = (FreestylerImage) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<FreestylerImage> findByF_F(long folderId, long freestylerId)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(folderId), new Long(freestylerId)
            };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_F_F,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("folderId = ?");

                query.append(" AND ");

                query.append("freestylerId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("imageId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(folderId);

                qPos.add(freestylerId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_F_F, finderArgs,
                    list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<FreestylerImage> findByF_F(long folderId, long freestylerId,
        int start, int end) throws SystemException {
        return findByF_F(folderId, freestylerId, start, end, null);
    }

    public List<FreestylerImage> findByF_F(long folderId, long freestylerId,
        int start, int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(folderId), new Long(freestylerId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_F_F,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("folderId = ?");

                query.append(" AND ");

                query.append("freestylerId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("imageId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(folderId);

                qPos.add(freestylerId);

                list = (List<FreestylerImage>) QueryUtil.list(q, getDialect(),
                        start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_F_F,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public FreestylerImage findByF_F_First(long folderId, long freestylerId,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        List<FreestylerImage> list = findByF_F(folderId, freestylerId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("folderId=" + folderId);

            msg.append(", ");
            msg.append("freestylerId=" + freestylerId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage findByF_F_Last(long folderId, long freestylerId,
        OrderByComparator obc) throws NoSuchImageException, SystemException {
        int count = countByF_F(folderId, freestylerId);

        List<FreestylerImage> list = findByF_F(folderId, freestylerId,
                count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No FreestylerImage exists with the key {");

            msg.append("folderId=" + folderId);

            msg.append(", ");
            msg.append("freestylerId=" + freestylerId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchImageException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public FreestylerImage[] findByF_F_PrevAndNext(long imageId, long folderId,
        long freestylerId, OrderByComparator obc)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = findByPrimaryKey(imageId);

        int count = countByF_F(folderId, freestylerId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append(
                "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

            query.append("folderId = ?");

            query.append(" AND ");

            query.append("freestylerId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("imageId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(folderId);

            qPos.add(freestylerId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    freestylerImage);

            FreestylerImage[] array = new FreestylerImageImpl[3];

            array[0] = (FreestylerImage) objArray[0];
            array[1] = (FreestylerImage) objArray[1];
            array[2] = (FreestylerImage) objArray[2];

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

    public List<FreestylerImage> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<FreestylerImage> findAll(int start, int end)
        throws SystemException {
        return findAll(start, end, null);
    }

    public List<FreestylerImage> findAll(int start, int end,
        OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<FreestylerImage> list = (List<FreestylerImage>) FinderCacheUtil.getResult(FINDER_PATH_FIND_ALL,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("imageId ASC");
                }

                Query q = session.createQuery(query.toString());

                if (obc == null) {
                    list = (List<FreestylerImage>) QueryUtil.list(q,
                            getDialect(), start, end, false);

                    Collections.sort(list);
                } else {
                    list = (List<FreestylerImage>) QueryUtil.list(q,
                            getDialect(), start, end);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<FreestylerImage>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_ALL, finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public void removeByGroupId(long groupId) throws SystemException {
        for (FreestylerImage freestylerImage : findByGroupId(groupId)) {
            remove(freestylerImage);
        }
    }

    public void removeByFolderId(long folderId) throws SystemException {
        for (FreestylerImage freestylerImage : findByFolderId(folderId)) {
            remove(freestylerImage);
        }
    }

    public void removeByFreestylerId(long freestylerId)
        throws SystemException {
        for (FreestylerImage freestylerImage : findByFreestylerId(freestylerId)) {
            remove(freestylerImage);
        }
    }

    public void removeBySmallImageId(long smallImageId)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = findBySmallImageId(smallImageId);

        remove(freestylerImage);
    }

    public void removeByLargeImageId(long largeImageId)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = findByLargeImageId(largeImageId);

        remove(freestylerImage);
    }

    public void removeByCustom1ImageId(long custom1ImageId)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = findByCustom1ImageId(custom1ImageId);

        remove(freestylerImage);
    }

    public void removeByCustom2ImageId(long custom2ImageId)
        throws NoSuchImageException, SystemException {
        FreestylerImage freestylerImage = findByCustom2ImageId(custom2ImageId);

        remove(freestylerImage);
    }

    public void removeByG_U(long groupId, long userId)
        throws SystemException {
        for (FreestylerImage freestylerImage : findByG_U(groupId, userId)) {
            remove(freestylerImage);
        }
    }

    public void removeByF_N(long folderId, String name)
        throws SystemException {
        for (FreestylerImage freestylerImage : findByF_N(folderId, name)) {
            remove(freestylerImage);
        }
    }

    public void removeByF_F(long folderId, long freestylerId)
        throws SystemException {
        for (FreestylerImage freestylerImage : findByF_F(folderId, freestylerId)) {
            remove(freestylerImage);
        }
    }

    public void removeAll() throws SystemException {
        for (FreestylerImage freestylerImage : findAll()) {
            remove(freestylerImage);
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
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

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

    public int countByFolderId(long folderId) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(folderId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_FOLDERID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("folderId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(folderId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_FOLDERID,
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
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

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

    public int countBySmallImageId(long smallImageId) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(smallImageId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_SMALLIMAGEID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("smallImageId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(smallImageId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_SMALLIMAGEID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByLargeImageId(long largeImageId) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(largeImageId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_LARGEIMAGEID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("largeImageId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(largeImageId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_LARGEIMAGEID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByCustom1ImageId(long custom1ImageId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(custom1ImageId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_CUSTOM1IMAGEID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("custom1ImageId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(custom1ImageId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_CUSTOM1IMAGEID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByCustom2ImageId(long custom2ImageId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(custom2ImageId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_CUSTOM2IMAGEID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("custom2ImageId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(custom2ImageId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_CUSTOM2IMAGEID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByG_U(long groupId, long userId) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(groupId), new Long(userId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_G_U,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("groupId = ?");

                query.append(" AND ");

                query.append("userId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(groupId);

                qPos.add(userId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_G_U, finderArgs,
                    count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByF_N(long folderId, String name) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(folderId), name };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_F_N,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("folderId = ?");

                query.append(" AND ");

                if (name == null) {
                    query.append("name IS NULL");
                } else {
                    query.append("name = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(folderId);

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

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_F_N, finderArgs,
                    count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByF_F(long folderId, long freestylerId)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(folderId), new Long(freestylerId)
            };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_F_F,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append(
                    "FROM com.ext.portlet.freestyler.model.FreestylerImage WHERE ");

                query.append("folderId = ?");

                query.append(" AND ");

                query.append("freestylerId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(folderId);

                qPos.add(freestylerId);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_F_F, finderArgs,
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
                        "SELECT COUNT(*) FROM com.ext.portlet.freestyler.model.FreestylerImage");

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
                        "value.object.listener.com.ext.portlet.freestyler.model.FreestylerImage")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<FreestylerImage>> listenersList = new ArrayList<ModelListener<FreestylerImage>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<FreestylerImage>) Class.forName(
                            listenerClassName).newInstance());
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }
}
