/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.wol.service.persistence;

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

import com.liferay.wol.NoSuchSVNRepositoryException;
import com.liferay.wol.model.SVNRepository;
import com.liferay.wol.model.impl.SVNRepositoryImpl;
import com.liferay.wol.model.impl.SVNRepositoryModelImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <a href="SVNRepositoryPersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SVNRepositoryPersistenceImpl extends BasePersistenceImpl
	implements SVNRepositoryPersistence {
	public static final String FINDER_CLASS_NAME_ENTITY = SVNRepositoryImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST = FINDER_CLASS_NAME_ENTITY +
		".List";
	public static final FinderPath FINDER_PATH_FETCH_BY_URL = new FinderPath(SVNRepositoryModelImpl.ENTITY_CACHE_ENABLED,
			SVNRepositoryModelImpl.FINDER_CACHE_ENABLED,
			FINDER_CLASS_NAME_ENTITY, "fetchByUrl",
			new String[] { String.class.getName() });
	public static final FinderPath FINDER_PATH_COUNT_BY_URL = new FinderPath(SVNRepositoryModelImpl.ENTITY_CACHE_ENABLED,
			SVNRepositoryModelImpl.FINDER_CACHE_ENABLED,
			FINDER_CLASS_NAME_LIST, "countByUrl",
			new String[] { String.class.getName() });
	public static final FinderPath FINDER_PATH_FIND_ALL = new FinderPath(SVNRepositoryModelImpl.ENTITY_CACHE_ENABLED,
			SVNRepositoryModelImpl.FINDER_CACHE_ENABLED,
			FINDER_CLASS_NAME_LIST, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(SVNRepositoryModelImpl.ENTITY_CACHE_ENABLED,
			SVNRepositoryModelImpl.FINDER_CACHE_ENABLED,
			FINDER_CLASS_NAME_LIST, "countAll", new String[0]);

	public void cacheResult(SVNRepository svnRepository) {
		EntityCacheUtil.putResult(SVNRepositoryModelImpl.ENTITY_CACHE_ENABLED,
			SVNRepositoryImpl.class, svnRepository.getPrimaryKey(),
			svnRepository);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_URL,
			new Object[] { svnRepository.getUrl() }, svnRepository);
	}

	public void cacheResult(List<SVNRepository> svnRepositories) {
		for (SVNRepository svnRepository : svnRepositories) {
			if (EntityCacheUtil.getResult(
						SVNRepositoryModelImpl.ENTITY_CACHE_ENABLED,
						SVNRepositoryImpl.class, svnRepository.getPrimaryKey(),
						this) == null) {
				cacheResult(svnRepository);
			}
		}
	}

	public void clearCache() {
		CacheRegistry.clear(SVNRepositoryImpl.class.getName());
		EntityCacheUtil.clearCache(SVNRepositoryImpl.class.getName());
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);
	}

	public SVNRepository create(long svnRepositoryId) {
		SVNRepository svnRepository = new SVNRepositoryImpl();

		svnRepository.setNew(true);
		svnRepository.setPrimaryKey(svnRepositoryId);

		return svnRepository;
	}

	public SVNRepository remove(long svnRepositoryId)
		throws NoSuchSVNRepositoryException, SystemException {
		Session session = null;

		try {
			session = openSession();

			SVNRepository svnRepository = (SVNRepository)session.get(SVNRepositoryImpl.class,
					new Long(svnRepositoryId));

			if (svnRepository == null) {
				if (_log.isWarnEnabled()) {
					_log.warn("No SVNRepository exists with the primary key " +
						svnRepositoryId);
				}

				throw new NoSuchSVNRepositoryException(
					"No SVNRepository exists with the primary key " +
					svnRepositoryId);
			}

			return remove(svnRepository);
		}
		catch (NoSuchSVNRepositoryException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public SVNRepository remove(SVNRepository svnRepository)
		throws SystemException {
		for (ModelListener<SVNRepository> listener : listeners) {
			listener.onBeforeRemove(svnRepository);
		}

		svnRepository = removeImpl(svnRepository);

		for (ModelListener<SVNRepository> listener : listeners) {
			listener.onAfterRemove(svnRepository);
		}

		return svnRepository;
	}

	protected SVNRepository removeImpl(SVNRepository svnRepository)
		throws SystemException {
		Session session = null;

		try {
			session = openSession();

			if (svnRepository.isCachedModel() || BatchSessionUtil.isEnabled()) {
				Object staleObject = session.get(SVNRepositoryImpl.class,
						svnRepository.getPrimaryKeyObj());

				if (staleObject != null) {
					session.evict(staleObject);
				}
			}

			session.delete(svnRepository);

			session.flush();
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

		SVNRepositoryModelImpl svnRepositoryModelImpl = (SVNRepositoryModelImpl)svnRepository;

		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_URL,
			new Object[] { svnRepositoryModelImpl.getOriginalUrl() });

		EntityCacheUtil.removeResult(SVNRepositoryModelImpl.ENTITY_CACHE_ENABLED,
			SVNRepositoryImpl.class, svnRepository.getPrimaryKey());

		return svnRepository;
	}

	public SVNRepository update(SVNRepository svnRepository)
		throws SystemException {
		if (_log.isWarnEnabled()) {
			_log.warn(
				"Using the deprecated update(SVNRepository svnRepository) method. Use update(SVNRepository svnRepository, boolean merge) instead.");
		}

		return update(svnRepository, false);
	}

	public SVNRepository update(SVNRepository svnRepository, boolean merge)
		throws SystemException {
		boolean isNew = svnRepository.isNew();

		for (ModelListener<SVNRepository> listener : listeners) {
			if (isNew) {
				listener.onBeforeCreate(svnRepository);
			}
			else {
				listener.onBeforeUpdate(svnRepository);
			}
		}

		svnRepository = updateImpl(svnRepository, merge);

		for (ModelListener<SVNRepository> listener : listeners) {
			if (isNew) {
				listener.onAfterCreate(svnRepository);
			}
			else {
				listener.onAfterUpdate(svnRepository);
			}
		}

		return svnRepository;
	}

	public SVNRepository updateImpl(
		com.liferay.wol.model.SVNRepository svnRepository, boolean merge)
		throws SystemException {
		boolean isNew = svnRepository.isNew();

		SVNRepositoryModelImpl svnRepositoryModelImpl = (SVNRepositoryModelImpl)svnRepository;

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.update(session, svnRepository, merge);

			svnRepository.setNew(false);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST);

		EntityCacheUtil.putResult(SVNRepositoryModelImpl.ENTITY_CACHE_ENABLED,
			SVNRepositoryImpl.class, svnRepository.getPrimaryKey(),
			svnRepository);

		if (!isNew &&
				(!svnRepository.getUrl()
								   .equals(svnRepositoryModelImpl.getOriginalUrl()))) {
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_URL,
				new Object[] { svnRepositoryModelImpl.getOriginalUrl() });
		}

		if (isNew ||
				(!svnRepository.getUrl()
								   .equals(svnRepositoryModelImpl.getOriginalUrl()))) {
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_URL,
				new Object[] { svnRepository.getUrl() }, svnRepository);
		}

		return svnRepository;
	}

	public SVNRepository findByPrimaryKey(long svnRepositoryId)
		throws NoSuchSVNRepositoryException, SystemException {
		SVNRepository svnRepository = fetchByPrimaryKey(svnRepositoryId);

		if (svnRepository == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("No SVNRepository exists with the primary key " +
					svnRepositoryId);
			}

			throw new NoSuchSVNRepositoryException(
				"No SVNRepository exists with the primary key " +
				svnRepositoryId);
		}

		return svnRepository;
	}

	public SVNRepository fetchByPrimaryKey(long svnRepositoryId)
		throws SystemException {
		SVNRepository svnRepository = (SVNRepository)EntityCacheUtil.getResult(SVNRepositoryModelImpl.ENTITY_CACHE_ENABLED,
				SVNRepositoryImpl.class, svnRepositoryId, this);

		if (svnRepository == null) {
			Session session = null;

			try {
				session = openSession();

				svnRepository = (SVNRepository)session.get(SVNRepositoryImpl.class,
						new Long(svnRepositoryId));
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (svnRepository != null) {
					cacheResult(svnRepository);
				}

				closeSession(session);
			}
		}

		return svnRepository;
	}

	public SVNRepository findByUrl(String url)
		throws NoSuchSVNRepositoryException, SystemException {
		SVNRepository svnRepository = fetchByUrl(url);

		if (svnRepository == null) {
			StringBuilder msg = new StringBuilder();

			msg.append("No SVNRepository exists with the key {");

			msg.append("url=" + url);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchSVNRepositoryException(msg.toString());
		}

		return svnRepository;
	}

	public SVNRepository fetchByUrl(String url) throws SystemException {
		return fetchByUrl(url, true);
	}

	public SVNRepository fetchByUrl(String url, boolean retrieveFromCache)
		throws SystemException {
		Object[] finderArgs = new Object[] { url };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_URL,
					finderArgs, this);
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringBuilder query = new StringBuilder();

				query.append("FROM com.liferay.wol.model.SVNRepository WHERE ");

				if (url == null) {
					query.append("url IS NULL");
				}
				else {
					query.append("url = ?");
				}

				query.append(" ");

				query.append("ORDER BY ");

				query.append("url ASC");

				Query q = session.createQuery(query.toString());

				QueryPos qPos = QueryPos.getInstance(q);

				if (url != null) {
					qPos.add(url);
				}

				List<SVNRepository> list = q.list();

				result = list;

				SVNRepository svnRepository = null;

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_URL,
						finderArgs, list);
				}
				else {
					svnRepository = list.get(0);

					cacheResult(svnRepository);

					if ((svnRepository.getUrl() == null) ||
							!svnRepository.getUrl().equals(url)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_URL,
							finderArgs, list);
					}
				}

				return svnRepository;
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (result == null) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_URL,
						finderArgs, new ArrayList<SVNRepository>());
				}

				closeSession(session);
			}
		}
		else {
			if (result instanceof List) {
				return null;
			}
			else {
				return (SVNRepository)result;
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
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
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
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<SVNRepository> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	public List<SVNRepository> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	public List<SVNRepository> findAll(int start, int end, OrderByComparator obc)
		throws SystemException {
		Object[] finderArgs = new Object[] {
				String.valueOf(start), String.valueOf(end), String.valueOf(obc)
			};

		List<SVNRepository> list = (List<SVNRepository>)FinderCacheUtil.getResult(FINDER_PATH_FIND_ALL,
				finderArgs, this);

		if (list == null) {
			Session session = null;

			try {
				session = openSession();

				StringBuilder query = new StringBuilder();

				query.append("FROM com.liferay.wol.model.SVNRepository ");

				if (obc != null) {
					query.append("ORDER BY ");
					query.append(obc.getOrderBy());
				}

				else {
					query.append("ORDER BY ");

					query.append("url ASC");
				}

				Query q = session.createQuery(query.toString());

				if (obc == null) {
					list = (List<SVNRepository>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);
				}
				else {
					list = (List<SVNRepository>)QueryUtil.list(q, getDialect(),
							start, end);
				}
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					list = new ArrayList<SVNRepository>();
				}

				cacheResult(list);

				FinderCacheUtil.putResult(FINDER_PATH_FIND_ALL, finderArgs, list);

				closeSession(session);
			}
		}

		return list;
	}

	public void removeByUrl(String url)
		throws NoSuchSVNRepositoryException, SystemException {
		SVNRepository svnRepository = findByUrl(url);

		remove(svnRepository);
	}

	public void removeAll() throws SystemException {
		for (SVNRepository svnRepository : findAll()) {
			remove(svnRepository);
		}
	}

	public int countByUrl(String url) throws SystemException {
		Object[] finderArgs = new Object[] { url };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_URL,
				finderArgs, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				StringBuilder query = new StringBuilder();

				query.append("SELECT COUNT(*) ");
				query.append("FROM com.liferay.wol.model.SVNRepository WHERE ");

				if (url == null) {
					query.append("url IS NULL");
				}
				else {
					query.append("url = ?");
				}

				query.append(" ");

				Query q = session.createQuery(query.toString());

				QueryPos qPos = QueryPos.getInstance(q);

				if (url != null) {
					qPos.add(url);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_URL, finderArgs,
					count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	public int countAll() throws SystemException {
		Object[] finderArgs = new Object[0];

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				finderArgs, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(
						"SELECT COUNT(*) FROM com.liferay.wol.model.SVNRepository");

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
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
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.liferay.wol.model.SVNRepository")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<SVNRepository>> listenersList = new ArrayList<ModelListener<SVNRepository>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<SVNRepository>)Class.forName(
							listenerClassName).newInstance());
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	@BeanReference(name = "com.liferay.wol.service.persistence.JIRAActionPersistence.impl")
	protected com.liferay.wol.service.persistence.JIRAActionPersistence jiraActionPersistence;
	@BeanReference(name = "com.liferay.wol.service.persistence.JIRAChangeGroupPersistence.impl")
	protected com.liferay.wol.service.persistence.JIRAChangeGroupPersistence jiraChangeGroupPersistence;
	@BeanReference(name = "com.liferay.wol.service.persistence.JIRAChangeItemPersistence.impl")
	protected com.liferay.wol.service.persistence.JIRAChangeItemPersistence jiraChangeItemPersistence;
	@BeanReference(name = "com.liferay.wol.service.persistence.JIRAIssuePersistence.impl")
	protected com.liferay.wol.service.persistence.JIRAIssuePersistence jiraIssuePersistence;
	@BeanReference(name = "com.liferay.wol.service.persistence.MeetupsEntryPersistence.impl")
	protected com.liferay.wol.service.persistence.MeetupsEntryPersistence meetupsEntryPersistence;
	@BeanReference(name = "com.liferay.wol.service.persistence.MeetupsRegistrationPersistence.impl")
	protected com.liferay.wol.service.persistence.MeetupsRegistrationPersistence meetupsRegistrationPersistence;
	@BeanReference(name = "com.liferay.wol.service.persistence.SVNRepositoryPersistence.impl")
	protected com.liferay.wol.service.persistence.SVNRepositoryPersistence svnRepositoryPersistence;
	@BeanReference(name = "com.liferay.wol.service.persistence.SVNRevisionPersistence.impl")
	protected com.liferay.wol.service.persistence.SVNRevisionPersistence svnRevisionPersistence;
	@BeanReference(name = "com.liferay.wol.service.persistence.WallEntryPersistence.impl")
	protected com.liferay.wol.service.persistence.WallEntryPersistence wallEntryPersistence;
	private static Log _log = LogFactoryUtil.getLog(SVNRepositoryPersistenceImpl.class);
}