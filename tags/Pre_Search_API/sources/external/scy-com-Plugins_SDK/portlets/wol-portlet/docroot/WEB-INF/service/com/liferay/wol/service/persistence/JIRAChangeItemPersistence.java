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

import com.liferay.portal.service.persistence.BasePersistence;

/**
 * <a href="JIRAChangeItemPersistence.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface JIRAChangeItemPersistence extends BasePersistence {
	public void cacheResult(com.liferay.wol.model.JIRAChangeItem jiraChangeItem);

	public void cacheResult(
		java.util.List<com.liferay.wol.model.JIRAChangeItem> jiraChangeItems);

	public void clearCache();

	public com.liferay.wol.model.JIRAChangeItem create(long jiraChangeItemId);

	public com.liferay.wol.model.JIRAChangeItem remove(long jiraChangeItemId)
		throws com.liferay.portal.SystemException,
			com.liferay.wol.NoSuchJIRAChangeItemException;

	public com.liferay.wol.model.JIRAChangeItem remove(
		com.liferay.wol.model.JIRAChangeItem jiraChangeItem)
		throws com.liferay.portal.SystemException;

	public com.liferay.wol.model.JIRAChangeItem update(
		com.liferay.wol.model.JIRAChangeItem jiraChangeItem)
		throws com.liferay.portal.SystemException;

	public com.liferay.wol.model.JIRAChangeItem update(
		com.liferay.wol.model.JIRAChangeItem jiraChangeItem, boolean merge)
		throws com.liferay.portal.SystemException;

	public com.liferay.wol.model.JIRAChangeItem updateImpl(
		com.liferay.wol.model.JIRAChangeItem jiraChangeItem, boolean merge)
		throws com.liferay.portal.SystemException;

	public com.liferay.wol.model.JIRAChangeItem findByPrimaryKey(
		long jiraChangeItemId)
		throws com.liferay.portal.SystemException,
			com.liferay.wol.NoSuchJIRAChangeItemException;

	public com.liferay.wol.model.JIRAChangeItem fetchByPrimaryKey(
		long jiraChangeItemId) throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.wol.model.JIRAChangeItem> findByJiraChangeGroupId(
		long jiraChangeGroupId) throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.wol.model.JIRAChangeItem> findByJiraChangeGroupId(
		long jiraChangeGroupId, int start, int end)
		throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.wol.model.JIRAChangeItem> findByJiraChangeGroupId(
		long jiraChangeGroupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.SystemException;

	public com.liferay.wol.model.JIRAChangeItem findByJiraChangeGroupId_First(
		long jiraChangeGroupId,
		com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.SystemException,
			com.liferay.wol.NoSuchJIRAChangeItemException;

	public com.liferay.wol.model.JIRAChangeItem findByJiraChangeGroupId_Last(
		long jiraChangeGroupId,
		com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.SystemException,
			com.liferay.wol.NoSuchJIRAChangeItemException;

	public com.liferay.wol.model.JIRAChangeItem[] findByJiraChangeGroupId_PrevAndNext(
		long jiraChangeItemId, long jiraChangeGroupId,
		com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.SystemException,
			com.liferay.wol.NoSuchJIRAChangeItemException;

	public java.util.List<Object> findWithDynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.SystemException;

	public java.util.List<Object> findWithDynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.wol.model.JIRAChangeItem> findAll()
		throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.wol.model.JIRAChangeItem> findAll(
		int start, int end) throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.wol.model.JIRAChangeItem> findAll(
		int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.SystemException;

	public void removeByJiraChangeGroupId(long jiraChangeGroupId)
		throws com.liferay.portal.SystemException;

	public void removeAll() throws com.liferay.portal.SystemException;

	public int countByJiraChangeGroupId(long jiraChangeGroupId)
		throws com.liferay.portal.SystemException;

	public int countAll() throws com.liferay.portal.SystemException;
}