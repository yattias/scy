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

/**
 * <a href="WallEntryFinderUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class WallEntryFinderUtil {
	public static int countByG1_G2_U1_U2(long groupId1, long groupId2,
		long userId1, long userId2) throws com.liferay.portal.SystemException {
		return getFinder()
				   .countByG1_G2_U1_U2(groupId1, groupId2, userId1, userId2);
	}

	public static java.util.List<com.liferay.wol.model.WallEntry> findByG1_G2_U1_U2(
		long groupId1, long groupId2, long userId1, long userId2, int start,
		int end) throws com.liferay.portal.SystemException {
		return getFinder()
				   .findByG1_G2_U1_U2(groupId1, groupId2, userId1, userId2,
			start, end);
	}

	public static WallEntryFinder getFinder() {
		return _finder;
	}

	public void setFinder(WallEntryFinder finder) {
		_finder = finder;
	}

	private static WallEntryFinder _finder;
}