package com.ext.portlet.cart.service.persistence;

import com.ext.portlet.cart.NoSuchCartException;
import com.ext.portlet.cart.model.Cart;
import com.ext.portlet.cart.model.impl.CartImpl;
import com.ext.portlet.cart.model.impl.CartModelImpl;

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


public class CartPersistenceImpl extends BasePersistenceImpl
    implements CartPersistence {
    public static final String FINDER_CLASS_NAME_ENTITY = CartImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST = FINDER_CLASS_NAME_ENTITY +
        ".List";
    public static final FinderPath FINDER_PATH_FIND_BY_COMPANYID = new FinderPath(CartModelImpl.ENTITY_CACHE_ENABLED,
            CartModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByCompanyId", new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_BY_OBC_COMPANYID = new FinderPath(CartModelImpl.ENTITY_CACHE_ENABLED,
            CartModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findByCompanyId",
            new String[] {
                Long.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_COUNT_BY_COMPANYID = new FinderPath(CartModelImpl.ENTITY_CACHE_ENABLED,
            CartModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countByCompanyId", new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_FIND_ALL = new FinderPath(CartModelImpl.ENTITY_CACHE_ENABLED,
            CartModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(CartModelImpl.ENTITY_CACHE_ENABLED,
            CartModelImpl.FINDER_CACHE_ENABLED, FINDER_CLASS_NAME_LIST,
            "countAll", new String[0]);
    public static final FinderPath FINDER_PATH_GET_CARTENTRIES = new FinderPath(com.ext.portlet.cart.model.impl.CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartModelImpl.FINDER_CACHE_ENABLED_CART_CARTENTRIES,
            "Cart_CartEntries", "getCartEntries",
            new String[] {
                Long.class.getName(), "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_GET_CARTENTRIES_SIZE = new FinderPath(com.ext.portlet.cart.model.impl.CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartModelImpl.FINDER_CACHE_ENABLED_CART_CARTENTRIES,
            "Cart_CartEntries", "getCartEntriesSize",
            new String[] { Long.class.getName() });
    public static final FinderPath FINDER_PATH_CONTAINS_CARTENTRY = new FinderPath(com.ext.portlet.cart.model.impl.CartEntryModelImpl.ENTITY_CACHE_ENABLED,
            CartModelImpl.FINDER_CACHE_ENABLED_CART_CARTENTRIES,
            "Cart_CartEntries", "containsCartEntry",
            new String[] { Long.class.getName(), Long.class.getName() });
    private static final String _SQL_GETCARTENTRIES = "SELECT {CartEntry.*} FROM CartEntry INNER JOIN Cart_CartEntries ON (Cart_CartEntries.cartEntryId = CartEntry.cartEntryId) WHERE (Cart_CartEntries.cartId = ?)";
    private static final String _SQL_GETCARTENTRIESSIZE = "SELECT COUNT(*) AS COUNT_VALUE FROM Cart_CartEntries WHERE cartId = ?";
    private static final String _SQL_CONTAINSCARTENTRY = "SELECT COUNT(*) AS COUNT_VALUE FROM Cart_CartEntries WHERE cartId = ? AND cartEntryId = ?";
    private static Log _log = LogFactoryUtil.getLog(CartPersistenceImpl.class);
    @BeanReference(name = "com.ext.portlet.cart.service.persistence.CartEntryPersistence.impl")
    protected com.ext.portlet.cart.service.persistence.CartEntryPersistence cartEntryPersistence;
    @BeanReference(name = "com.ext.portlet.cart.service.persistence.CartPersistence.impl")
    protected com.ext.portlet.cart.service.persistence.CartPersistence cartPersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.GroupPersistence.impl")
    protected com.liferay.portal.service.persistence.GroupPersistence groupPersistence;
    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected com.liferay.portal.service.persistence.UserPersistence userPersistence;
    protected ContainsCartEntry containsCartEntry;
    protected AddCartEntry addCartEntry;
    protected ClearCartEntries clearCartEntries;
    protected RemoveCartEntry removeCartEntry;

    public void cacheResult(Cart cart) {
        EntityCacheUtil.putResult(CartModelImpl.ENTITY_CACHE_ENABLED,
            CartImpl.class, cart.getPrimaryKey(), cart);
    }

    public void cacheResult(List<Cart> carts) {
        for (Cart cart : carts) {
            if (EntityCacheUtil.getResult(CartModelImpl.ENTITY_CACHE_ENABLED,
                        CartImpl.class, cart.getPrimaryKey(), this) == null) {
                cacheResult(cart);
            }
        }
    }

    public void clearCache() {
        CacheRegistry.clear(CartImpl.class.getName());
        EntityCacheUtil.clearCache(CartImpl.class.getName());
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);
    }

    public Cart create(long cartId) {
        Cart cart = new CartImpl();

        cart.setNew(true);
        cart.setPrimaryKey(cartId);

        return cart;
    }

    public Cart remove(long cartId) throws NoSuchCartException, SystemException {
        Session session = null;

        try {
            session = openSession();

            Cart cart = (Cart) session.get(CartImpl.class, new Long(cartId));

            if (cart == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No Cart exists with the primary key " + cartId);
                }

                throw new NoSuchCartException(
                    "No Cart exists with the primary key " + cartId);
            }

            return remove(cart);
        } catch (NoSuchCartException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public Cart remove(Cart cart) throws SystemException {
        for (ModelListener<Cart> listener : listeners) {
            listener.onBeforeRemove(cart);
        }

        cart = removeImpl(cart);

        for (ModelListener<Cart> listener : listeners) {
            listener.onAfterRemove(cart);
        }

        return cart;
    }

    protected Cart removeImpl(Cart cart) throws SystemException {
        try {
            clearCartEntries.clear(cart.getPrimaryKey());
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }

        Session session = null;

        try {
            session = openSession();

            if (cart.isCachedModel() || BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(CartImpl.class,
                        cart.getPrimaryKeyObj());

                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }

            session.delete(cart);

            session.flush();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.removeResult(CartModelImpl.ENTITY_CACHE_ENABLED,
            CartImpl.class, cart.getPrimaryKey());

        return cart;
    }

    /**
     * @deprecated Use <code>update(Cart cart, boolean merge)</code>.
     */
    public Cart update(Cart cart) throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn(
                "Using the deprecated update(Cart cart) method. Use update(Cart cart, boolean merge) instead.");
        }

        return update(cart, false);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                cart the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when cart is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public Cart update(Cart cart, boolean merge) throws SystemException {
        boolean isNew = cart.isNew();

        for (ModelListener<Cart> listener : listeners) {
            if (isNew) {
                listener.onBeforeCreate(cart);
            } else {
                listener.onBeforeUpdate(cart);
            }
        }

        cart = updateImpl(cart, merge);

        for (ModelListener<Cart> listener : listeners) {
            if (isNew) {
                listener.onAfterCreate(cart);
            } else {
                listener.onAfterUpdate(cart);
            }
        }

        return cart;
    }

    public Cart updateImpl(com.ext.portlet.cart.model.Cart cart, boolean merge)
        throws SystemException {
        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.update(session, cart, merge);

            cart.setNew(false);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

        EntityCacheUtil.putResult(CartModelImpl.ENTITY_CACHE_ENABLED,
            CartImpl.class, cart.getPrimaryKey(), cart);

        return cart;
    }

    public Cart findByPrimaryKey(long cartId)
        throws NoSuchCartException, SystemException {
        Cart cart = fetchByPrimaryKey(cartId);

        if (cart == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No Cart exists with the primary key " + cartId);
            }

            throw new NoSuchCartException(
                "No Cart exists with the primary key " + cartId);
        }

        return cart;
    }

    public Cart fetchByPrimaryKey(long cartId) throws SystemException {
        Cart cart = (Cart) EntityCacheUtil.getResult(CartModelImpl.ENTITY_CACHE_ENABLED,
                CartImpl.class, cartId, this);

        if (cart == null) {
            Session session = null;

            try {
                session = openSession();

                cart = (Cart) session.get(CartImpl.class, new Long(cartId));
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (cart != null) {
                    cacheResult(cart);
                }

                closeSession(session);
            }
        }

        return cart;
    }

    public List<Cart> findByCompanyId(long companyId) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(companyId) };

        List<Cart> list = (List<Cart>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_COMPANYID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.cart.model.Cart WHERE ");

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
                    list = new ArrayList<Cart>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_COMPANYID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public List<Cart> findByCompanyId(long companyId, int start, int end)
        throws SystemException {
        return findByCompanyId(companyId, start, end, null);
    }

    public List<Cart> findByCompanyId(long companyId, int start, int end,
        OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(companyId),
                
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<Cart> list = (List<Cart>) FinderCacheUtil.getResult(FINDER_PATH_FIND_BY_OBC_COMPANYID,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.cart.model.Cart WHERE ");

                query.append("companyId = ?");

                query.append(" ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }

                Query q = session.createQuery(query.toString());

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(companyId);

                list = (List<Cart>) QueryUtil.list(q, getDialect(), start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<Cart>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_BY_OBC_COMPANYID,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public Cart findByCompanyId_First(long companyId, OrderByComparator obc)
        throws NoSuchCartException, SystemException {
        List<Cart> list = findByCompanyId(companyId, 0, 1, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No Cart exists with the key {");

            msg.append("companyId=" + companyId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchCartException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public Cart findByCompanyId_Last(long companyId, OrderByComparator obc)
        throws NoSuchCartException, SystemException {
        int count = countByCompanyId(companyId);

        List<Cart> list = findByCompanyId(companyId, count - 1, count, obc);

        if (list.isEmpty()) {
            StringBuilder msg = new StringBuilder();

            msg.append("No Cart exists with the key {");

            msg.append("companyId=" + companyId);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            throw new NoSuchCartException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public Cart[] findByCompanyId_PrevAndNext(long cartId, long companyId,
        OrderByComparator obc) throws NoSuchCartException, SystemException {
        Cart cart = findByPrimaryKey(cartId);

        int count = countByCompanyId(companyId);

        Session session = null;

        try {
            session = openSession();

            StringBuilder query = new StringBuilder();

            query.append("FROM com.ext.portlet.cart.model.Cart WHERE ");

            query.append("companyId = ?");

            query.append(" ");

            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }

            Query q = session.createQuery(query.toString());

            QueryPos qPos = QueryPos.getInstance(q);

            qPos.add(companyId);

            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, cart);

            Cart[] array = new CartImpl[3];

            array[0] = (Cart) objArray[0];
            array[1] = (Cart) objArray[1];
            array[2] = (Cart) objArray[2];

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

    public List<Cart> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<Cart> findAll(int start, int end) throws SystemException {
        return findAll(start, end, null);
    }

    public List<Cart> findAll(int start, int end, OrderByComparator obc)
        throws SystemException {
        Object[] finderArgs = new Object[] {
                String.valueOf(start), String.valueOf(end), String.valueOf(obc)
            };

        List<Cart> list = (List<Cart>) FinderCacheUtil.getResult(FINDER_PATH_FIND_ALL,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder query = new StringBuilder();

                query.append("FROM com.ext.portlet.cart.model.Cart ");

                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }

                Query q = session.createQuery(query.toString());

                if (obc == null) {
                    list = (List<Cart>) QueryUtil.list(q, getDialect(), start,
                            end, false);

                    Collections.sort(list);
                } else {
                    list = (List<Cart>) QueryUtil.list(q, getDialect(), start,
                            end);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<Cart>();
                }

                cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_FIND_ALL, finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public void removeByCompanyId(long companyId) throws SystemException {
        for (Cart cart : findByCompanyId(companyId)) {
            remove(cart);
        }
    }

    public void removeAll() throws SystemException {
        for (Cart cart : findAll()) {
            remove(cart);
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
                query.append("FROM com.ext.portlet.cart.model.Cart WHERE ");

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
                        "SELECT COUNT(*) FROM com.ext.portlet.cart.model.Cart");

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

    public List<com.ext.portlet.cart.model.CartEntry> getCartEntries(long pk)
        throws SystemException {
        return getCartEntries(pk, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
    }

    public List<com.ext.portlet.cart.model.CartEntry> getCartEntries(long pk,
        int start, int end) throws SystemException {
        return getCartEntries(pk, start, end, null);
    }

    public List<com.ext.portlet.cart.model.CartEntry> getCartEntries(long pk,
        int start, int end, OrderByComparator obc) throws SystemException {
        Object[] finderArgs = new Object[] {
                new Long(pk), String.valueOf(start), String.valueOf(end),
                String.valueOf(obc)
            };

        List<com.ext.portlet.cart.model.CartEntry> list = (List<com.ext.portlet.cart.model.CartEntry>) FinderCacheUtil.getResult(FINDER_PATH_GET_CARTENTRIES,
                finderArgs, this);

        if (list == null) {
            Session session = null;

            try {
                session = openSession();

                StringBuilder sb = new StringBuilder();

                sb.append(_SQL_GETCARTENTRIES);

                if (obc != null) {
                    sb.append("ORDER BY ");
                    sb.append(obc.getOrderBy());
                }
                else {
                    sb.append("ORDER BY ");

                    sb.append("CartEntry.cartEntryId ASC");
                }

                String sql = sb.toString();

                SQLQuery q = session.createSQLQuery(sql);

                q.addEntity("CartEntry",
                    com.ext.portlet.cart.model.impl.CartEntryImpl.class);

                QueryPos qPos = QueryPos.getInstance(q);

                qPos.add(pk);

                list = (List<com.ext.portlet.cart.model.CartEntry>) QueryUtil.list(q,
                        getDialect(), start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    list = new ArrayList<com.ext.portlet.cart.model.CartEntry>();
                }

                cartEntryPersistence.cacheResult(list);

                FinderCacheUtil.putResult(FINDER_PATH_GET_CARTENTRIES,
                    finderArgs, list);

                closeSession(session);
            }
        }

        return list;
    }

    public int getCartEntriesSize(long pk) throws SystemException {
        Object[] finderArgs = new Object[] { new Long(pk) };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_GET_CARTENTRIES_SIZE,
                finderArgs, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                SQLQuery q = session.createSQLQuery(_SQL_GETCARTENTRIESSIZE);

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

                FinderCacheUtil.putResult(FINDER_PATH_GET_CARTENTRIES_SIZE,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    public boolean containsCartEntry(long pk, long cartEntryPK)
        throws SystemException {
        Object[] finderArgs = new Object[] { new Long(pk), new Long(cartEntryPK) };

        Boolean value = (Boolean) FinderCacheUtil.getResult(FINDER_PATH_CONTAINS_CARTENTRY,
                finderArgs, this);

        if (value == null) {
            try {
                value = Boolean.valueOf(containsCartEntry.contains(pk,
                            cartEntryPK));
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (value == null) {
                    value = Boolean.FALSE;
                }

                FinderCacheUtil.putResult(FINDER_PATH_CONTAINS_CARTENTRY,
                    finderArgs, value);
            }
        }

        return value.booleanValue();
    }

    public boolean containsCartEntries(long pk) throws SystemException {
        if (getCartEntriesSize(pk) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void addCartEntry(long pk, long cartEntryPK)
        throws SystemException {
        try {
            addCartEntry.add(pk, cartEntryPK);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void addCartEntry(long pk,
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws SystemException {
        try {
            addCartEntry.add(pk, cartEntry.getPrimaryKey());
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void addCartEntries(long pk, long[] cartEntryPKs)
        throws SystemException {
        try {
            for (long cartEntryPK : cartEntryPKs) {
                addCartEntry.add(pk, cartEntryPK);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void addCartEntries(long pk,
        List<com.ext.portlet.cart.model.CartEntry> cartEntries)
        throws SystemException {
        try {
            for (com.ext.portlet.cart.model.CartEntry cartEntry : cartEntries) {
                addCartEntry.add(pk, cartEntry.getPrimaryKey());
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void clearCartEntries(long pk) throws SystemException {
        try {
            clearCartEntries.clear(pk);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void removeCartEntry(long pk, long cartEntryPK)
        throws SystemException {
        try {
            removeCartEntry.remove(pk, cartEntryPK);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void removeCartEntry(long pk,
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws SystemException {
        try {
            removeCartEntry.remove(pk, cartEntry.getPrimaryKey());
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void removeCartEntries(long pk, long[] cartEntryPKs)
        throws SystemException {
        try {
            for (long cartEntryPK : cartEntryPKs) {
                removeCartEntry.remove(pk, cartEntryPK);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void removeCartEntries(long pk,
        List<com.ext.portlet.cart.model.CartEntry> cartEntries)
        throws SystemException {
        try {
            for (com.ext.portlet.cart.model.CartEntry cartEntry : cartEntries) {
                removeCartEntry.remove(pk, cartEntry.getPrimaryKey());
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void setCartEntries(long pk, long[] cartEntryPKs)
        throws SystemException {
        try {
            clearCartEntries.clear(pk);

            for (long cartEntryPK : cartEntryPKs) {
                addCartEntry.add(pk, cartEntryPK);
            }
        } catch (Exception e) {
            throw processException(e);
        } finally {
            FinderCacheUtil.clearCache("Cart_CartEntries");
        }
    }

    public void setCartEntries(long pk,
        List<com.ext.portlet.cart.model.CartEntry> cartEntries)
        throws SystemException {
        try {
            clearCartEntries.clear(pk);

            for (com.ext.portlet.cart.model.CartEntry cartEntry : cartEntries) {
                addCartEntry.add(pk, cartEntry.getPrimaryKey());
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
                        "value.object.listener.com.ext.portlet.cart.model.Cart")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<Cart>> listenersList = new ArrayList<ModelListener<Cart>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<Cart>) Class.forName(
                            listenerClassName).newInstance());
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }

        containsCartEntry = new ContainsCartEntry(this);

        addCartEntry = new AddCartEntry(this);
        clearCartEntries = new ClearCartEntries(this);
        removeCartEntry = new RemoveCartEntry(this);
    }

    protected class ContainsCartEntry {
        private MappingSqlQuery _mappingSqlQuery;

        protected ContainsCartEntry(CartPersistenceImpl persistenceImpl) {
            super();

            _mappingSqlQuery = MappingSqlQueryFactoryUtil.getMappingSqlQuery(getDataSource(),
                    _SQL_CONTAINSCARTENTRY,
                    new int[] { Types.BIGINT, Types.BIGINT }, RowMapper.COUNT);
        }

        protected boolean contains(long cartId, long cartEntryId) {
            List<Integer> results = _mappingSqlQuery.execute(new Object[] {
                        new Long(cartId), new Long(cartEntryId)
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

    protected class AddCartEntry {
        private SqlUpdate _sqlUpdate;
        private CartPersistenceImpl _persistenceImpl;

        protected AddCartEntry(CartPersistenceImpl persistenceImpl) {
            _sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(getDataSource(),
                    "INSERT INTO Cart_CartEntries (cartId, cartEntryId) VALUES (?, ?)",
                    new int[] { Types.BIGINT, Types.BIGINT });
            _persistenceImpl = persistenceImpl;
        }

        protected void add(long cartId, long cartEntryId)
            throws SystemException {
            if (!_persistenceImpl.containsCartEntry.contains(cartId, cartEntryId)) {
                ModelListener<com.ext.portlet.cart.model.CartEntry>[] cartEntryListeners =
                    cartEntryPersistence.getListeners();

                for (ModelListener<Cart> listener : listeners) {
                    listener.onBeforeAddAssociation(cartId,
                        com.ext.portlet.cart.model.CartEntry.class.getName(),
                        cartEntryId);
                }

                for (ModelListener<com.ext.portlet.cart.model.CartEntry> listener : cartEntryListeners) {
                    listener.onBeforeAddAssociation(cartEntryId,
                        Cart.class.getName(), cartId);
                }

                _sqlUpdate.update(new Object[] {
                        new Long(cartId), new Long(cartEntryId)
                    });

                for (ModelListener<Cart> listener : listeners) {
                    listener.onAfterAddAssociation(cartId,
                        com.ext.portlet.cart.model.CartEntry.class.getName(),
                        cartEntryId);
                }

                for (ModelListener<com.ext.portlet.cart.model.CartEntry> listener : cartEntryListeners) {
                    listener.onAfterAddAssociation(cartEntryId,
                        Cart.class.getName(), cartId);
                }
            }
        }
    }

    protected class ClearCartEntries {
        private SqlUpdate _sqlUpdate;

        protected ClearCartEntries(CartPersistenceImpl persistenceImpl) {
            _sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(getDataSource(),
                    "DELETE FROM Cart_CartEntries WHERE cartId = ?",
                    new int[] { Types.BIGINT });
        }

        protected void clear(long cartId) throws SystemException {
            ModelListener<com.ext.portlet.cart.model.CartEntry>[] cartEntryListeners =
                cartEntryPersistence.getListeners();

            List<com.ext.portlet.cart.model.CartEntry> cartEntries = null;

            if ((listeners.length > 0) || (cartEntryListeners.length > 0)) {
                cartEntries = getCartEntries(cartId);

                for (com.ext.portlet.cart.model.CartEntry cartEntry : cartEntries) {
                    for (ModelListener<Cart> listener : listeners) {
                        listener.onBeforeRemoveAssociation(cartId,
                            com.ext.portlet.cart.model.CartEntry.class.getName(),
                            cartEntry.getPrimaryKey());
                    }

                    for (ModelListener<com.ext.portlet.cart.model.CartEntry> listener : cartEntryListeners) {
                        listener.onBeforeRemoveAssociation(cartEntry.getPrimaryKey(),
                            Cart.class.getName(), cartId);
                    }
                }
            }

            _sqlUpdate.update(new Object[] { new Long(cartId) });

            if ((listeners.length > 0) || (cartEntryListeners.length > 0)) {
                for (com.ext.portlet.cart.model.CartEntry cartEntry : cartEntries) {
                    for (ModelListener<Cart> listener : listeners) {
                        listener.onAfterRemoveAssociation(cartId,
                            com.ext.portlet.cart.model.CartEntry.class.getName(),
                            cartEntry.getPrimaryKey());
                    }

                    for (ModelListener<com.ext.portlet.cart.model.CartEntry> listener : cartEntryListeners) {
                        listener.onBeforeRemoveAssociation(cartEntry.getPrimaryKey(),
                            Cart.class.getName(), cartId);
                    }
                }
            }
        }
    }

    protected class RemoveCartEntry {
        private SqlUpdate _sqlUpdate;
        private CartPersistenceImpl _persistenceImpl;

        protected RemoveCartEntry(CartPersistenceImpl persistenceImpl) {
            _sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(getDataSource(),
                    "DELETE FROM Cart_CartEntries WHERE cartId = ? AND cartEntryId = ?",
                    new int[] { Types.BIGINT, Types.BIGINT });
            _persistenceImpl = persistenceImpl;
        }

        protected void remove(long cartId, long cartEntryId)
            throws SystemException {
            if (_persistenceImpl.containsCartEntry.contains(cartId, cartEntryId)) {
                ModelListener<com.ext.portlet.cart.model.CartEntry>[] cartEntryListeners =
                    cartEntryPersistence.getListeners();

                for (ModelListener<Cart> listener : listeners) {
                    listener.onBeforeRemoveAssociation(cartId,
                        com.ext.portlet.cart.model.CartEntry.class.getName(),
                        cartEntryId);
                }

                for (ModelListener<com.ext.portlet.cart.model.CartEntry> listener : cartEntryListeners) {
                    listener.onBeforeRemoveAssociation(cartEntryId,
                        Cart.class.getName(), cartId);
                }

                _sqlUpdate.update(new Object[] {
                        new Long(cartId), new Long(cartEntryId)
                    });

                for (ModelListener<Cart> listener : listeners) {
                    listener.onAfterRemoveAssociation(cartId,
                        com.ext.portlet.cart.model.CartEntry.class.getName(),
                        cartEntryId);
                }

                for (ModelListener<com.ext.portlet.cart.model.CartEntry> listener : cartEntryListeners) {
                    listener.onAfterRemoveAssociation(cartEntryId,
                        Cart.class.getName(), cartId);
                }
            }
        }
    }
}
