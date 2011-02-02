package com.ext.portlet.cart.service.persistence;

import com.ext.portlet.cart.NoSuchCartEntryException;
import com.ext.portlet.cart.model.CartEntry;
import com.ext.portlet.cart.model.impl.CartEntryImpl;
import com.ext.portlet.cart.model.impl.CartEntryModelImpl;

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


public class CartEntryPersistenceImpl extends BasePersistenceImpl
    implements CartEntryPersistence {
    public static final String FINDER_CLASS_NAME_ENTITY = CartEntryImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST = FINDER_CLASS_NAME_ENTITY +
        ".List";
    public static final FinderPath FINDER_PATH_FIND_BY_TAGNAMES = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByTagNames", new String[] { String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_TAGNAMES = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByTagNames",
            new String[] {
                String.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_TAGNAMES = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByTagNames", new String[] { String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_RESOURCETYPE = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByResourceType", new String[] { String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_RESOURCETYPE = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByResourceType",
            new String[] {
                String.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_RESOURCETYPE = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByResourceType", new String[] { String.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_RESOURCEID = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByResourceId", new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_RESOURCEID = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByResourceId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_RESOURCEID = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByResourceId", new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_ALL = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countAll", new String[0]);
    public static final FinderPath FINDER_PATH_GET_CARTS = new FinderPath(com.ext.portlet.cart.model.impl.CartModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED_CART_CARTENTRIES,
            "Cart_CartEntries", "getCarts",
            new String[] {
                Long.class.getName(), "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_GET_CARTS_SIZE = new FinderPath(com.ext.portlet.cart.model.impl.CartModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED_CART_CARTENTRIES,
            "Cart_CartEntries", "getCartsSize",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_CONTAINS_CART = new FinderPath(com.ext.portlet.cart.model.impl.CartModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryModelImpl.FINDER_CACHE_ENABLED_CART_CARTENTRIES,
            "Cart_CartEntries", "containsCart",
            new String[] { Long.class.getName(), Long.class.getName() });
    private static final String _SQL_GETCARTS = "SELECT {Cart.*} FROM Cart INNER JOIN Cart_CartEntries ON (Cart_CartEntries.cartId = Cart.cartId) WHERE (Cart_CartEntries.cartEntryId = ?)";
    private static final String _SQL_GETCARTSSIZE = "SELECT COUNT(*) AS COUNT_VALUE FROM Cart_CartEntries WHERE cartEntryId = ?";
    private static final String _SQL_CONTAINSCART = "SELECT COUNT(*) AS COUNT_VALUE FROM Cart_CartEntries WHERE cartEntryId = ? AND cartId = ?";
    private static Log _log = LogFactoryUtil.getLog(CartEntryPersistenceImpl.class);
    @BeanReference(name = "com.ext.portlet.cart.service.persistence.CartEntryPersistence.impl")
    protected com.ext.portlet.cart.service.persistence.CartEntryPersistence cartEntryPersistence;
    @BeanReference(name = "com.ext.portlet.cart.service.persistence.CartPersistence.impl")
    protected com.ext.portlet.cart.service.persistence.CartPersistence cartPersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected com.liferay.portal.service.persistence.UserPersistence userPersistence;
    protected ContainsCart containsCart;
    protected AddCart addCart;
    protected ClearCarts clearCarts;
    protected RemoveCart removeCart;

    public void cacheResult(CartEntry cartEntry) {
        EntityCacheUtil.putResult(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryImpl.class, cartEntry.getPrimaryKey(), cartEntry);
    }

    public void cacheResult(List<CartEntry> cartEntries) {
        for (CartEntry cartEntry : cartEntries) {
            if (EntityCacheUtil.getResult(
                        CartEntryModelImpl.ENTITY_CACHE_ENABLED,
                        CartEntryImpl.class, cartEntry.getPrimaryKey(), this) == null) {
                cacheResult(cartEntry);
            }
        }
    }

    public void clearCache() {
        CacheRegistry.clear(CartEntryImpl.class.getName());
        EntityCacheUtil.clearCache(CartEntryImpl.class.getName());
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);
    }

    public CartEntry create(long cartEntryId) {
        CartEntry cartEntry = new CartEntryImpl();

        cartEntry.setNew(true);
        cartEntry.setPrimaryKey(cartEntryId);

        return cartEntry;
    }

    public CartEntry remove(long cartEntryId)
        throws NoSuchCartEntryException, SystemException {
        Session session = null;

        try {
            session = openSession();

            CartEntry cartEntry = (CartEntry) session.get(CartEntryImpl.class,
                    new Long(cartEntryId));

            if (cartEntry == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No CartEntry exists with the primary key " +
                        cartEntryId);
                }

                throw new NoSuchCartEntryException(
                    "No CartEntry exists with the primary key " + cartEntryId);
            }

            return remove(cartEntry);
        } catch (NoSuchCartEntryException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public CartEntry remove(CartEntry cartEntry) throws SystemException {
        for (ModelListener<CartEntry> listener : listeners) {
            listener.onBeforeRemove(cartEntry);
        }

        cartEntry = removeImpl(cartEntry);

        for (ModelListener<CartEntry> listener : listeners) {
            listener.onAfterRemove(cartEntry);
        }

        return cartEntry;
    }

    protected CartEntry removeImpl(CartEntry cartEntry)
        throws SystemException {
        try {
            clearCarts.clear(cartEntry.getPrimaryKey());
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }

        Session session = null;

        try {
            session = openSession();

            if (cartEntry.isCachedModel() || BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(CartEntryImpl.class,
                        cartEntry.getPrimaryKeyObj());

                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }

            session.delete(cartEntry);

            session.flush();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.removeResult(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryImpl.class, cartEntry.getPrimaryKey());

        return cartEntry;
    }

    /**
     * @deprecated Use <code>update(CartEntry cartEntry, boolean merge)</code>.
     */
    public CartEntry update(CartEntry cartEntry) throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn(
                "Using the deprecated update(CartEntry cartEntry) method. Use update(CartEntry cartEntry, boolean merge) instead.");
        }

        return update(cartEntry, false);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                cartEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when cartEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public CartEntry update(CartEntry cartEntry, boolean merge)
        throws SystemException {
        boolean isNew = cartEntry.isNew();

        for (ModelListener<CartEntry> listener : listeners) {
            if (isNew) {
                listener.onBeforeCreate(cartEntry);
            } else {
                listener.onBeforeUpdate(cartEntry);
            }
        }

        cartEntry = updateImpl(cartEntry, merge);

        for (ModelListener<CartEntry> listener : listeners) {
            if (isNew) {
                listener.onAfterCreate(cartEntry);
            } else {
                listener.onAfterUpdate(cartEntry);
            }
        }

        return cartEntry;
    }

    public CartEntry updateImpl(
        com.ext.portlet.cart.model.CartEntry cartEntry, boolean merge)
        throws SystemException {
        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.update(session, cartEntry, merge);

            cartEntry.setNew(false);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.putResult(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartEntryImpl.class, cartEntry.getPrimaryKey(), cartEntry);

        return cartEntry;
    }

    public CartEntry findByPrimaryKey(long cartEntryId)
        throws NoSuchCartEntryException, SystemException {
        CartEntry cartEntry = fetchByPrimaryKey(cartEntryId);

        if (cartEntry == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No CartEntry exists with the primary key " +
                    cartEntryId);
            }

            throw new NoSuchCartEntryException(
                "No CartEntry exists with the primary key " + cartEntryId);
        }

        return cartEntry;
    }

    public CartEntry fetchByPrimaryKey(long cartEntryId)
        throws SystemException {
        CartEntry cartEntry = (CartEntry) EntityCacheUtil.getResult(CartEntryModelImpl.ENTITY_CACHE_ENABLED,
                CartEntryImpl.class, cartEntryId, this);

        if (cartEntry == null) {
            Session session = null;

            try {
                session = openSession();

                cartEntry = (CartEntry) session.get(CartEntryImpl.class,
                        new Long(cartEntryId));
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (cartEntry != null) {
                    cacheResult(cartEntry);
                }

                closeSession(session);
            }
        }

        return cartEntry;
    }

    public List<CartEntry> findByTagNames(String tagNames)
        throws SystemException {
        Object[] finderArgs = new Object[] { tagNames };

        List<CartEntry> list = (List<CartEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_TAGNAMES,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

                if (tagNames == null) {
                    query.append("tagNames IS NULL");
                } else {
                    query.append("tagNames = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("cartEntryId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (tagNames != null) {
                    qPos.add(tagNames);
                }

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<CartEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_TAGNAMES,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<CartEntry> findByTagNames(String tagNames, int start, int end)
        throws SystemException {
        return findByTagNames(tagNames, start, end, null);
    }

    public List<CartEntry> findByTagNames(String tagNames, int start, int end,
        OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                tagNames,
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<CartEntry> list = (List<CartEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_TAGNAMES,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

                if (tagNames == null) {
                    query.append("tagNames IS NULL");
                } else {
                    query.append("tagNames = ?");
                }

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("cartEntryId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (tagNames != null) {
                    qPos.add(tagNames);
                }

                list = (List<CartEntry>) QueryUtil.list(q, getDialect(), start,
                        end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<CartEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_TAGNAMES,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public CartEntry findByTagNames_First(String tagNames, OrderByComparator obc)
        throws NoSuchCartEntryException, SystemException {
        List<CartEntry> list = findByTagNames(tagNames, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No CartEntry exists with the key {");

            msg.append("tagNames=" + tagNames);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchCartEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public CartEntry findByTagNames_Last(String tagNames, OrderByComparator obc)
        throws NoSuchCartEntryException, SystemException {
        int count = countByTagNames(tagNames);

        List<CartEntry> list = findByTagNames(tagNames, count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No CartEntry exists with the key {");

            msg.append("tagNames=" + tagNames);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchCartEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public CartEntry[] findByTagNames_PrevAndNext(long cartEntryId,
        String tagNames, OrderByComparator obc)
        throws NoSuchCartEntryException, SystemException {
        CartEntry cartEntry = findByPrimaryKey(cartEntryId);

        int count = countByTagNames(tagNames);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

            if (tagNames == null) {
                query.append("tagNames IS NULL");
            } else {
                query.append("tagNames = ?");
            }

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("cartEntryId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            if (tagNames != null) {
                qPos.add(tagNames);
            }

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    cartEntry);

            CartEntry[] array = new CartEntryImpl[3];

            array[0] = (CartEntry) objArray[0];
            array[1] = (CartEntry) objArray[1];
            array[2] = (CartEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<CartEntry> findByResourceType(String resourceType)
        throws SystemException {
        Object[] finderArgs = new Object[] { resourceType };

        List<CartEntry> list = (List<CartEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_RESOURCETYPE,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

                if (resourceType == null) {
                    query.append("resourceType IS NULL");
                } else {
                    query.append("resourceType = ?");
                }

                query.append(" ");

                query.append("ORDER BY ");

                query.append("cartEntryId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (resourceType != null) {
                    qPos.add(resourceType);
                }

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<CartEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_RESOURCETYPE,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<CartEntry> findByResourceType(String resourceType, int start,
        int end) throws SystemException {
        return findByResourceType(resourceType, start, end, null);
    }

    public List<CartEntry> findByResourceType(String resourceType, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                resourceType,
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<CartEntry> list = (List<CartEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_RESOURCETYPE,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

                if (resourceType == null) {
                    query.append("resourceType IS NULL");
                } else {
                    query.append("resourceType = ?");
                }

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("cartEntryId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (resourceType != null) {
                    qPos.add(resourceType);
                }

                list = (List<CartEntry>) QueryUtil.list(q, getDialect(), start,
                        end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<CartEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_RESOURCETYPE,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public CartEntry findByResourceType_First(String resourceType,
        OrderByComparator obc) throws NoSuchCartEntryException, SystemException {
        List<CartEntry> list = findByResourceType(resourceType, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No CartEntry exists with the key {");

            msg.append("resourceType=" + resourceType);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchCartEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public CartEntry findByResourceType_Last(String resourceType,
        OrderByComparator obc) throws NoSuchCartEntryException, SystemException {
        int count = countByResourceType(resourceType);

        List<CartEntry> list = findByResourceType(resourceType, count - 1,
                count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No CartEntry exists with the key {");

            msg.append("resourceType=" + resourceType);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchCartEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public CartEntry[] findByResourceType_PrevAndNext(long cartEntryId,
        String resourceType, OrderByComparator obc)
        throws NoSuchCartEntryException, SystemException {
        CartEntry cartEntry = findByPrimaryKey(cartEntryId);

        int count = countByResourceType(resourceType);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

            if (resourceType == null) {
                query.append("resourceType IS NULL");
            } else {
                query.append("resourceType = ?");
            }

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("cartEntryId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            if (resourceType != null) {
                qPos.add(resourceType);
            }

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    cartEntry);

            CartEntry[] array = new CartEntryImpl[3];

            array[0] = (CartEntry) objArray[0];
            array[1] = (CartEntry) objArray[1];
            array[2] = (CartEntry) objArray[2];

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<CartEntry> findByResourceId(long resourceId)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(resourceId) };

        List<CartEntry> list = (List<CartEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_RESOURCEID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

                query.append("resourceId = ?");

                query.append(" ");

                query.append("ORDER BY ");

                query.append("cartEntryId ASC");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(resourceId);

                list = q.list();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<CartEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_RESOURCEID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<CartEntry> findByResourceId(long resourceId, int start, int end)
        throws SystemException {
        return findByResourceId(resourceId, start, end, null);
    }

    public List<CartEntry> findByResourceId(long resourceId, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(resourceId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<CartEntry> list = (List<CartEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_RESOURCEID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

                query.append("resourceId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("cartEntryId ASC");
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(resourceId);

                list = (List<CartEntry>) QueryUtil.list(q, getDialect(), start,
                        end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<CartEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_RESOURCEID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public CartEntry findByResourceId_First(long resourceId,
        OrderByComparator obc) throws NoSuchCartEntryException, SystemException {
        List<CartEntry> list = findByResourceId(resourceId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No CartEntry exists with the key {");

            msg.append("resourceId=" + resourceId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchCartEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public CartEntry findByResourceId_Last(long resourceId,
        OrderByComparator obc) throws NoSuchCartEntryException, SystemException {
        int count = countByResourceId(resourceId);

        List<CartEntry> list = findByResourceId(resourceId, count - 1, count,
                obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No CartEntry exists with the key {");

            msg.append("resourceId=" + resourceId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchCartEntryException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public CartEntry[] findByResourceId_PrevAndNext(long cartEntryId,
        long resourceId, OrderByComparator obc)
        throws NoSuchCartEntryException, SystemException {
        CartEntry cartEntry = findByPrimaryKey(cartEntryId);

        int count = countByResourceId(resourceId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

            query.append("resourceId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            else {
                query.append("ORDER BY ");

                query.append("cartEntryId ASC");
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(resourceId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
                    cartEntry);

            CartEntry[] array = new CartEntryImpl[3];

            array[0] = (CartEntry) objArray[0];
            array[1] = (CartEntry) objArray[1];
            array[2] = (CartEntry) objArray[2];

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

    public List<CartEntry> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<CartEntry> findAll(int start, int end)
        throws SystemException {
        return findAll(start, end, null);
    }

    public List<CartEntry> findAll(int start, int end, OrderByComparator obc)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<CartEntry> list = (List<CartEntry>) FinderCacheUtil.getResult(FINDER_PATH_FIND_ALL,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.cart.model.CartEntry ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                else {
                    query.append("ORDER BY ");

                    query.append("cartEntryId ASC");
                }

                Query q = session.createQuery(query.toString());

                if (obc == null) {
                    list = (List<CartEntry>) QueryUtil.list(q, getDialect(),
                            start, end, false);

                    Collections.sort(list);
                } else {
                    list = (List<CartEntry>) QueryUtil.list(q, getDialect(),
                            start, end);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<CartEntry>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_ALL, finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public void removeByTagNames(String tagNames) throws SystemException {
        for (CartEntry cartEntry : findByTagNames(tagNames)) {
            remove(cartEntry);
        }
    }

    public void removeByResourceType(String resourceType)
        throws SystemException {
        for (CartEntry cartEntry : findByResourceType(resourceType)) {
            remove(cartEntry);
        }
    }

    public void removeByResourceId(long resourceId) throws SystemException {
        for (CartEntry cartEntry : findByResourceId(resourceId)) {
            remove(cartEntry);
        }
    }

    public void removeAll() throws SystemException {
        for (CartEntry cartEntry : findAll()) {
            remove(cartEntry);
        }
    }

    public int countByTagNames(String tagNames) throws SystemException {
        Object[] finderArgs = new Object[] { tagNames };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_TAGNAMES,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

                if (tagNames == null) {
                    query.append("tagNames IS NULL");
                } else {
                    query.append("tagNames = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (tagNames != null) {
                    qPos.add(tagNames);
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_TAGNAMES,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByResourceType(String resourceType)
        throws SystemException {
        Object[] finderArgs = new Object[] { resourceType };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_RESOURCETYPE,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

                if (resourceType == null) {
                    query.append("resourceType IS NULL");
                } else {
                    query.append("resourceType = ?");
                }

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                if (resourceType != null) {
                    qPos.add(resourceType);
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_RESOURCETYPE,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public int countByResourceId(long resourceId) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(resourceId) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_RESOURCEID,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("SELECT COUNT(*) ");
                query.append("FROM com.ext.portlet.cart.model.CartEntry WHERE ");

                query.append("resourceId = ?");

                query.append(" ");

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(resourceId);

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

    public int countAll() throws SystemException {
        Object[] finderArgs = new Object[0];

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(
                        "SELECT COUNT(*) FROM com.ext.portlet.cart.model.CartEntry");

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

    public List<com.ext.portlet.cart.model.Cart> getCarts(long pk)
        throws SystemException {
        return getCarts(pk, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
    }

    public List<com.ext.portlet.cart.model.Cart> getCarts(long pk, int start,
        int end) throws SystemException {
        return getCarts(pk, start, end, null);
    }

    public List<com.ext.portlet.cart.model.Cart> getCarts(long pk, int start,
        int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(pk), String.valueOf(start), String.valueOf(end),
                String.valueOf(obc)
            };

        List<com.ext.portlet.cart.model.Cart> list = (List<com.ext.portlet.cart.model.Cart>) FinderCacheUtil.getResult(FINDER_PATH_GET_CARTS,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder sb = new StringBuilder();

                sb.append(_SQL_GETCARTS);

                if (obc != null) {
                    sb.append("ORDER BY ");
                    sb.append(obc.getOrderBy());
                }

                String sql = sb.toString();

                SQLQuery q = session.createSQLQuery(sql);

                q.addEntity("Cart",
                    com.ext.portlet.cart.model.impl.CartImpl.class);

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(pk);

                list = (List<com.ext.portlet.cart.model.Cart>) QueryUtil.list(q,
                        getDialect(), start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<com.ext.portlet.cart.model.Cart>();
                }

                cartPersistence.cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_GET_CARTS, finderArgs,
                    list);

                closeSession(session);
            }
        }

        return list;
    }

    public int getCartsSize(long pk) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(pk) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_GET_CARTS_SIZE,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                SQLQuery q = session.createSQLQuery(_SQL_GETCARTSSIZE);

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

                FinderCacheUtil.putResult(FINDER_PATH_GET_CARTS_SIZE,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public boolean containsCart(long pk, long cartPK) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(pk), new Long(cartPK) };

        Boolean value = (Boolean) FinderCacheUtil.getResult(FINDER_PATH_CONTAINS_CART,
                finderArgs, this);

        if (value == null) {
            try {
                value = Boolean.valueOf(containsCart.contains(pk, cartPK));
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (value == null) {
                    value = Boolean.FALSE;
                }

                FinderCacheUtil.putResult(FINDER_PATH_CONTAINS_CART,
                    finderArgs, value);
            }
        }

        return value.booleanValue();
    }

    public boolean containsCarts(long pk) throws SystemException {
        if (getCartsSize(pk) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void addCart(long pk, long cartPK) throws SystemException {
        try {
            addCart.add(pk, cartPK);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void addCart(long pk, com.ext.portlet.cart.model.Cart cart)
        throws SystemException {
        try {
            addCart.add(pk, cart.getPrimaryKey());
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void addCarts(long pk, long[] cartPKs) throws SystemException {
        try {
            for (long cartPK : cartPKs) {
                addCart.add(pk, cartPK);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void addCarts(long pk, List<com.ext.portlet.cart.model.Cart> carts)
        throws SystemException {
        try {
            for (com.ext.portlet.cart.model.Cart cart : carts) {
                addCart.add(pk, cart.getPrimaryKey());
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void clearCarts(long pk) throws SystemException {
        try {
            clearCarts.clear(pk);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void removeCart(long pk, long cartPK) throws SystemException {
        try {
            removeCart.remove(pk, cartPK);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void removeCart(long pk, com.ext.portlet.cart.model.Cart cart)
        throws SystemException {
        try {
            removeCart.remove(pk, cart.getPrimaryKey());
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void removeCarts(long pk, long[] cartPKs) throws SystemException {
        try {
            for (long cartPK : cartPKs) {
                removeCart.remove(pk, cartPK);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void removeCarts(long pk, List<com.ext.portlet.cart.model.Cart> carts)
        throws SystemException {
        try {
            for (com.ext.portlet.cart.model.Cart cart : carts) {
                removeCart.remove(pk, cart.getPrimaryKey());
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void setCarts(long pk, long[] cartPKs) throws SystemException {
        try {
            clearCarts.clear(pk);

            for (long cartPK : cartPKs) {
                addCart.add(pk, cartPK);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void setCarts(long pk, List<com.ext.portlet.cart.model.Cart> carts)
        throws SystemException {
        try {
            clearCarts.clear(pk);

            for (com.ext.portlet.cart.model.Cart cart : carts) {
                addCart.add(pk, cart.getPrimaryKey());
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void afterPropertiesSet() {
        String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
                    com.liferay.portal.util.PropsUtil.get(
                        "value.object.listener.com.ext.portlet.cart.model.CartEntry")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<CartEntry>> listenersList = new ArrayList<ModelListener<CartEntry>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<CartEntry>) Class.forName(
                            listenerClassName).newInstance());
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }

        containsCart = new ContainsCart(this);

        addCart = new AddCart(this);
        clearCarts = new ClearCarts(this);
        removeCart = new RemoveCart(this);
    }

    protected class ContainsCart {
        private MappingSqlQuery _mappingSqlQuery;

        protected ContainsCart(CartEntryPersistenceImpl persistenceImpl) {
            super();

            _mappingSqlQuery = MappingSqlQueryFactoryUtil.getMappingSqlQuery(getDataSource(),
                    _SQL_CONTAINSCART,
                    new int[] { Types.BIGINT, Types.BIGINT }, RowMapper.COUNT);
        }

        protected boolean contains(long cartEntryId, long cartId) {
            List<Integer> results = _mappingSqlQuery.execute(new Object[] {
                        new Long(cartEntryId), new Long(cartId)
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

    protected class AddCart {
        private SqlUpdate _sqlUpdate;
        private CartEntryPersistenceImpl _persistenceImpl;

        protected AddCart(CartEntryPersistenceImpl persistenceImpl) {
            _sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(getDataSource(),
                    "INSERT INTO Cart_CartEntries (cartEntryId, cartId) VALUES (?, ?)",
                    new int[] { Types.BIGINT, Types.BIGINT });
            _persistenceImpl = persistenceImpl;
        }

        protected void add(long cartEntryId, long cartId)
            throws SystemException {
            if (!_persistenceImpl.containsCart.contains(cartEntryId, cartId)) {
                ModelListener<com.ext.portlet.cart.model.Cart>[] cartListeners = cartPersistence.getListeners();

                for (ModelListener<CartEntry> listener : listeners) {
                    listener.onBeforeAddAssociation(cartEntryId,
                        com.ext.portlet.cart.model.Cart.class.getName(), cartId);
                }

                for (ModelListener<com.ext.portlet.cart.model.Cart> listener : cartListeners) {
                    listener.onBeforeAddAssociation(cartId,
                        CartEntry.class.getName(), cartEntryId);
                }

                _sqlUpdate.update(new Object[] {
                        new Long(cartEntryId), new Long(cartId)
                    });

                for (ModelListener<CartEntry> listener : listeners) {
                    listener.onAfterAddAssociation(cartEntryId,
                        com.ext.portlet.cart.model.Cart.class.getName(), cartId);
                }

                for (ModelListener<com.ext.portlet.cart.model.Cart> listener : cartListeners) {
                    listener.onAfterAddAssociation(cartId,
                        CartEntry.class.getName(), cartEntryId);
                }
            }
        }
    }

    protected class ClearCarts {
        private SqlUpdate _sqlUpdate;

        protected ClearCarts(CartEntryPersistenceImpl persistenceImpl) {
            _sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(getDataSource(),
                    "DELETE FROM Cart_CartEntries WHERE cartEntryId = ?",
                    new int[] { Types.BIGINT });
        }

        protected void clear(long cartEntryId) throws SystemException {
            ModelListener<com.ext.portlet.cart.model.Cart>[] cartListeners = cartPersistence.getListeners();

            List<com.ext.portlet.cart.model.Cart> carts = null;

            if ((listeners.length > 0) || (cartListeners.length > 0)) {
                carts = getCarts(cartEntryId);

                for (com.ext.portlet.cart.model.Cart cart : carts) {
                    for (ModelListener<CartEntry> listener : listeners) {
                        listener.onBeforeRemoveAssociation(cartEntryId,
                            com.ext.portlet.cart.model.Cart.class.getName(),
                            cart.getPrimaryKey());
                    }

                    for (ModelListener<com.ext.portlet.cart.model.Cart> listener : cartListeners) {
                        listener.onBeforeRemoveAssociation(cart.getPrimaryKey(),
                            CartEntry.class.getName(), cartEntryId);
                    }
                }
            }

            _sqlUpdate.update(new Object[] { new Long(cartEntryId) });

            if ((listeners.length > 0) || (cartListeners.length > 0)) {
                for (com.ext.portlet.cart.model.Cart cart : carts) {
                    for (ModelListener<CartEntry> listener : listeners) {
                        listener.onAfterRemoveAssociation(cartEntryId,
                            com.ext.portlet.cart.model.Cart.class.getName(),
                            cart.getPrimaryKey());
                    }

                    for (ModelListener<com.ext.portlet.cart.model.Cart> listener : cartListeners) {
                        listener.onBeforeRemoveAssociation(cart.getPrimaryKey(),
                            CartEntry.class.getName(), cartEntryId);
                    }
                }
            }
        }
    }

    protected class RemoveCart {
        private SqlUpdate _sqlUpdate;
        private CartEntryPersistenceImpl _persistenceImpl;

        protected RemoveCart(CartEntryPersistenceImpl persistenceImpl) {
            _sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(getDataSource(),
                    "DELETE FROM Cart_CartEntries WHERE cartEntryId = ? AND cartId = ?",
                    new int[] { Types.BIGINT, Types.BIGINT });
            _persistenceImpl = persistenceImpl;
        }

        protected void remove(long cartEntryId, long cartId)
            throws SystemException {
            if (_persistenceImpl.containsCart.contains(cartEntryId, cartId)) {
                ModelListener<com.ext.portlet.cart.model.Cart>[] cartListeners = cartPersistence.getListeners();

                for (ModelListener<CartEntry> listener : listeners) {
                    listener.onBeforeRemoveAssociation(cartEntryId,
                        com.ext.portlet.cart.model.Cart.class.getName(), cartId);
                }

                for (ModelListener<com.ext.portlet.cart.model.Cart> listener : cartListeners) {
                    listener.onBeforeRemoveAssociation(cartId,
                        CartEntry.class.getName(), cartEntryId);
                }

                _sqlUpdate.update(new Object[] {
                        new Long(cartEntryId), new Long(cartId)
                    });

                for (ModelListener<CartEntry> listener : listeners) {
                    listener.onAfterRemoveAssociation(cartEntryId,
                        com.ext.portlet.cart.model.Cart.class.getName(), cartId);
                }

                for (ModelListener<com.ext.portlet.cart.model.Cart> listener : cartListeners) {
                    listener.onAfterRemoveAssociation(cartId,
                        CartEntry.class.getName(), cartEntryId);
                }
            }
        }
    }
}
